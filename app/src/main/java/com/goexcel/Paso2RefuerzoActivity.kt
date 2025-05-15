package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Paso2RefuerzoActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLeccion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso2_refuerzo)

        firebaseAuth = FirebaseAuth.getInstance()
        btnLeccion = findViewById(R.id.btnLeccionAprendida)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

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
            "paso" to "Paso 2",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo2").child("paso2").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ ¡Muy bien! Seguimos avanzando paso a paso 👏", Toast.LENGTH_LONG).show()
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
