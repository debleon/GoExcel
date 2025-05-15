package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AyudaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)

        // 🟤 Botón salir vuelve al perfil
        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }

        // 🔵 Navegación inferior
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_logo).setOnClickListener {
            Toast.makeText(this, "GOEXCEL 💡", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageView>(R.id.nav_stats).setOnClickListener {
            Toast.makeText(this, "Próximamente: Avances 📊", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageView>(R.id.nav_trophy).setOnClickListener {
            Toast.makeText(this, "Próximamente: Favoritos 🌟", Toast.LENGTH_SHORT).show()
        }
        val homeIcon = findViewById<ImageView>(R.id.nav_home)
        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() // Opcional: cierra la vista actual
        }
        val btnFavoritos = findViewById<ImageView>(R.id.nav_trophy)
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))

        homeIcon.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}}
