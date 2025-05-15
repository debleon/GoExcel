package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.imageview.ShapeableImageView

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnConfiguracion = findViewById<ImageView>(R.id.btnSettings)
        val btnEditarAvatar = findViewById<ImageView>(R.id.btnEditAvatar)
        val txtUserName = findViewById<TextView>(R.id.userName)
        val txtUserLevel = findViewById<TextView>(R.id.userLevel)
        val homeIcon = findViewById<ImageView>(R.id.nav_home)
        val profileImage = findViewById<ShapeableImageView>(R.id.profileImage)

        // 🔐 Obtener UID del usuario autenticado
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // 🔄 Leer datos desde Firebase Realtime Database
        if (userId != null) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId)

            databaseRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val nombre = snapshot.child("nombre").value?.toString() ?: "Nombre de usuario"
                    val nivel = snapshot.child("nivel").value?.toString() ?: "Nivel desconocido"
                    val avatarIndex = snapshot.child("avatar").value?.toString()?.toIntOrNull() ?: 1

                    txtUserName.text = nombre
                    txtUserLevel.text = nivel

                    // ✅ Cargar imagen de avatar dinámica
                    val avatarResId = resources.getIdentifier("avatar__${avatarIndex}_", "drawable", packageName)
                    if (avatarResId != 0) {
                        profileImage.setImageResource(avatarResId)
                    } else {
                        profileImage.setImageResource(R.drawable.avatar__1_) // Fallback
                    }

                } else {
                    txtUserName.text = "Nombre no disponible"
                    txtUserLevel.text = "Nivel no disponible"
                }
            }.addOnFailureListener {
                txtUserName.text = "Error al cargar"
                txtUserLevel.text = "Error al cargar"
            }
        } else {
            txtUserName.text = "Usuario no autenticado"
            txtUserLevel.text = "Sesión inválida"
        }

        // ⚙️ Navegar a otras pantallas
        btnConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }

        btnEditarAvatar.setOnClickListener {
            startActivity(Intent(this, EditarAvatarActivity::class.java))
        }

        homeIcon.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        val btnFavoritos = findViewById<ImageView>(R.id.nav_trophy)
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_stats).setOnClickListener {
            startActivity(Intent(this, DesafiosActivity::class.java))
            finish()
        }
    }
}


