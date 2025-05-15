package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ConfiguracionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val backButton = findViewById<ImageView>(R.id.backButton)
        val opcionPerfil = findViewById<LinearLayout>(R.id.opcionPerfil)
        val opcionPrivacidad = findViewById<LinearLayout>(R.id.opcionPrivacidad)
        val opcionNotificaciones = findViewById<LinearLayout>(R.id.opcionNotificaciones)
        val opcionAyuda = findViewById<LinearLayout>(R.id.opcionAyuda)
        val homeIcon = findViewById<ImageView>(R.id.nav_home)
        val btnFavoritos = findViewById<ImageView>(R.id.nav_trophy)
        val avatarImage = findViewById<ImageView>(R.id.profileImage)

        // 🔄 Cargar avatar actualizado desde Firebase
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId)
            databaseRef.get().addOnSuccessListener { snapshot ->
                val avatarIndex = snapshot.child("avatar").value?.toString()?.toIntOrNull() ?: 1
                val avatarResId = resources.getIdentifier("avatar__${avatarIndex}_", "drawable", packageName)
                if (avatarResId != 0) {
                    avatarImage.setImageResource(avatarResId)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar el avatar", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔙 Volver al perfil
        backButton.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
            finish()
        }

        // 👉 Ir a editar perfil
        opcionPerfil.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }

        // 👉 Ir a privacidad
        opcionPrivacidad.setOnClickListener {
            startActivity(Intent(this, PrivacidadActivity::class.java))
        }

        // 👉 Ir a notificaciones
        opcionNotificaciones.setOnClickListener {
            startActivity(Intent(this, NotificacionesActivity::class.java))
        }

        // 👉 Ir a ayuda
        opcionAyuda.setOnClickListener {
            startActivity(Intent(this, AyudaActivity::class.java))
        }

        // 🚪 Cerrar sesión
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
    }
}
