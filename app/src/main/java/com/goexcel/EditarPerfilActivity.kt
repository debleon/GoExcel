package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.imageview.ShapeableImageView

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtCorreo: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnSalir: Button
    private lateinit var avatarImage: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        edtNombre = findViewById(R.id.edtNombre)
        edtCorreo = findViewById(R.id.edtCorreo)
        edtPassword = findViewById(R.id.edtPassword)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSalir = findViewById(R.id.btnSalir)
        avatarImage = findViewById(R.id.profileImage)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val userId = user?.uid

        if (user != null && userId != null) {
            val ref = FirebaseDatabase.getInstance().getReference("usuarios").child(userId)

            ref.get().addOnSuccessListener { snapshot ->
                val nombre = snapshot.child("nombre").value?.toString() ?: ""
                val correo = user.email ?: ""
                val avatarIndex = snapshot.child("avatar").value?.toString()?.toIntOrNull() ?: 1

                edtNombre.setText(nombre)
                edtCorreo.setText(correo)
                edtCorreo.isEnabled = false

                // Cargar avatar desde recursos
                val avatarResId = resources.getIdentifier("avatar__${avatarIndex}_", "drawable", packageName)
                if (avatarResId != 0) {
                    avatarImage.setImageResource(avatarResId)
                }
            }
        }

        btnGuardar.setOnClickListener {
            val nuevoNombre = edtNombre.text.toString().trim()
            val nuevaPass = edtPassword.text.toString()

            if (user != null && userId != null) {
                val ref = FirebaseDatabase.getInstance().getReference("usuarios").child(userId)
                val updates = mapOf("nombre" to nuevoNombre)

                ref.updateChildren(updates).addOnSuccessListener {
                    if (nuevaPass.isNotEmpty()) {
                        if (nuevaPass.length >= 6) {
                            user.updatePassword(nuevaPass)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Nombre y contraseña actualizados", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error al cambiar contraseña", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSalir.setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.nav_trophy).setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
