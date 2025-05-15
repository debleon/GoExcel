package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ModuloRuta1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modulo_ruta1)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


        // Acciones por paso (preparadas para ser activadas luego)
        findViewById<ImageView>(R.id.paso1).setOnClickListener {
            // Acción futura para lección 1
        }
        findViewById<ImageView>(R.id.paso2).setOnClickListener {
            // Acción futura para lección 2
        }
        findViewById<ImageView>(R.id.paso3).setOnClickListener {
            // Acción futura para lección 3
        }
        findViewById<ImageView>(R.id.paso4).setOnClickListener {
            // Acción futura para lección 4
        }
        findViewById<ImageView>(R.id.paso5).setOnClickListener {
            // Acción futura para lección 5
        }
        val btnFavoritos = findViewById<ImageView>(R.id.nav_trophy)
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
        findViewById<ImageView>(R.id.paso1).setOnClickListener {
            val intent = Intent(this, Paso1ConceptosActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.paso2).setOnClickListener {
            val intent = Intent(this, Paso2CaminoActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.paso3).setOnClickListener {
            val intent = Intent(this, Paso3PractiquemosActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.paso4).setOnClickListener {
            val intent = Intent(this, Paso4AplicadoActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.paso5).setOnClickListener {
            val intent = Intent(this, Paso5EvaluacionActivity::class.java)
            startActivity(intent)
        }

    }
}
