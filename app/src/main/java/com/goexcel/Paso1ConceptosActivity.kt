package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Paso1ConceptosActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLeccion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso1_conceptos)

        firebaseAuth = FirebaseAuth.getInstance()

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val titulo = findViewById<TextView>(R.id.tvTituloPaso)
        val introduccion = findViewById<TextView>(R.id.tvIntro)
        val contenido = findViewById<TextView>(R.id.tvContenido)
        btnLeccion = findViewById(R.id.btnLeccionAprendida)

        titulo.text = getString(R.string.paso1_titulo)
        introduccion.text = getString(R.string.paso1_intro)
        contenido.text = getString(R.string.paso1_contenido)

        val webView = findViewById<WebView>(R.id.webViewExcel)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        val sheetUrl = "https://docs.google.com/spreadsheets/d/1H_oi0lQ5XqE3fPRpNXt7HuKCjkxdQJeDHSY60ckq3_g/preview"
        webView.loadUrl(sheetUrl)

        // ✅ Listener que guarda y devuelve
        btnLeccion.setOnClickListener {
            guardarProgresoYVolver()
        }
    }

    private fun guardarProgresoYVolver() {
        val user = firebaseAuth.currentUser ?: return
        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 1",
            "paso" to "Paso 1",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo1").child("paso1").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "🌟 ¡Lección completada! Sigues avanzando como un profesional 💪",
                    Toast.LENGTH_LONG
                ).show()

                btnLeccion.isEnabled = false
                btnLeccion.text = "✅ Completado"

                btnLeccion.postDelayed({
                    val intent = Intent(this, ModuloRuta1Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }, 2000)
            }
            .addOnFailureListener {
                Toast.makeText(this, "❌ Error al guardar el progreso", Toast.LENGTH_SHORT).show()
            }
    }
}


