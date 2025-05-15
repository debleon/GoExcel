package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var btnRegister: Button
    private lateinit var registerError: TextView
    private lateinit var backButton: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.emailInput)
        nameInput = findViewById(R.id.nameInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        btnRegister = findViewById(R.id.btnRegister)
        registerError = findViewById(R.id.registerError)
        backButton = findViewById(R.id.backButton)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.GONE

        val clearErrorOnChange = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                registerError.visibility = TextView.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        emailInput.addTextChangedListener(clearErrorOnChange)
        nameInput.addTextChangedListener(clearErrorOnChange)
        passwordInput.addTextChangedListener(clearErrorOnChange)
        confirmPasswordInput.addTextChangedListener(clearErrorOnChange)

        btnRegister.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val name = nameInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showError("La contraseña debe tener al menos 6 caracteres.")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showError("Las contraseñas no coinciden.")
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.GONE

                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            val database = FirebaseDatabase.getInstance().reference
                            val userInfo = mapOf(
                                "nombre" to name,
                                "email" to email,
                                "nivel" to "Nivel Básico",
                                "avatar" to 1,

                                    )

                            database.child("usuarios").child(uid).setValue(userInfo)
                        }

                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("registered_email", email)
                        startActivity(intent)
                        finish()
                    } else {
                        showError(task.exception?.message ?: "Error al registrar")
                        Log.e("RegisterError", task.exception?.message ?: "Error")
                    }
                }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        registerError.text = message
        registerError.visibility = View.VISIBLE
    }
}
