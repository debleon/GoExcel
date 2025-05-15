package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var loginError: TextView
    private lateinit var backButton: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.btnLogin)
        loginError = findViewById(R.id.loginError)
        backButton = findViewById(R.id.backButton)
        progressBar = findViewById(R.id.progressBar)

        emailInput.requestFocus()

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                loginError.text = "Debes ingresar correo y contraseña"
                loginError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            loginError.visibility = View.GONE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            database.child("users").child(uid)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        progressBar.visibility = View.GONE

                                        val nombre = snapshot.child("nombre").value?.toString() ?: "Usuario"
                                        val nivel = snapshot.child("nivel").value?.toString() ?: "Nivel desconocido"

                                        // Puedes pasar los datos al HomeActivity si quieres
                                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                        intent.putExtra("nombreUsuario", nombre)
                                        intent.putExtra("nivelUsuario", nivel)
                                        startActivity(intent)
                                        finish()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        showError("No se pudo leer la información del usuario.")
                                    }
                                })
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        showError(task.exception?.message ?: "Error al iniciar sesión")
                    }
                }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun showError(message: String) {
        loginError.text = message
        loginError.visibility = View.VISIBLE
    }
}
