package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ModuloRuta5Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulo_ruta5)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.paso1).setOnClickListener { }
        findViewById<ImageView>(R.id.paso2).setOnClickListener { }
        findViewById<ImageView>(R.id.paso3).setOnClickListener { }
        findViewById<ImageView>(R.id.paso4).setOnClickListener { }
        findViewById<ImageView>(R.id.paso5).setOnClickListener {
        }
        val btnFavoritos = findViewById<ImageView>(R.id.nav_trophy)
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
    }
}
