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

class Paso1RefuerzoActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var webView: WebView
    private lateinit var btnLeccion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso1_refuerzo)

        firebaseAuth = FirebaseAuth.getInstance()
        webView = findViewById(R.id.webViewSheet)
        btnLeccion = findViewById(R.id.btnLeccionAprendida)

        // Configurar WebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://docs.google.com/spreadsheets/d/1JitU7LkgE_ZUhekeH7fmVY2VMpwW22g_VnlB2cnA1Vc/edit?usp=sharing")

        // Botón de volver
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Acción al marcar lección como aprendida
        btnLeccion.setOnClickListener {
            guardarProgresoYVolver()
        }
    }

    private fun guardarProgresoYVolver() {
        val user = firebaseAuth.currentUser ?: return
        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 2",
            "paso" to "Paso 1",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo2").child("paso1").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ ¡Lección completada! Sigues reforzando como un crack 💪", Toast.LENGTH_LONG).show()
                btnLeccion.isEnabled = false
                btnLeccion.text = "✅ Completado"

                btnLeccion.postDelayed({
                    val intent = Intent(this, ModuloRuta2Activity::class.java)
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
