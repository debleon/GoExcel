package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Paso3PracticaActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var etRespuesta: EditText
    private lateinit var iconoResultado: ImageView
    private lateinit var btnValidar: Button

    private val respuestaCorrecta = """=SI(B2>10,"Bien","Mejorar")"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso3_practica)

        firebaseAuth = FirebaseAuth.getInstance()
        etRespuesta = findViewById(R.id.etRespuesta)
        iconoResultado = findViewById(R.id.iconoResultado)
        btnValidar = findViewById(R.id.btnValidar)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnValidar.setOnClickListener {
            validarRespuesta()
        }
    }

    private fun validarRespuesta() {
        val input = etRespuesta.text.toString().trim().replace(" ", "")
        val esperada = respuestaCorrecta.replace(" ", "").lowercase()

        if (input.lowercase() == esperada) {
            iconoResultado.setImageResource(R.drawable.ic_check)
            iconoResultado.visibility = View.VISIBLE
            guardarProgreso()
        } else {
            iconoResultado.setImageResource(R.drawable.ic_close)
            iconoResultado.visibility = View.VISIBLE
            Toast.makeText(this, "❌ Intenta de nuevo. Revisa tu fórmula.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarProgreso() {
        val user = firebaseAuth.currentUser ?: return
        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 2",
            "paso" to "Paso 3",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo2").child("paso3").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ ¡Excelente! Dominaste esta práctica 👏", Toast.LENGTH_LONG).show()
                btnValidar.isEnabled = false
                btnValidar.text = "✅ Completado"

                btnValidar.postDelayed({
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
