package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class Paso3PractiquemosActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnValidar: Button
    private lateinit var etRespuesta: EditText
    private val respuestaCorrecta = "=SUMA(B2:B4)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso3_practiquemos)

        firebaseAuth = FirebaseAuth.getInstance()
        btnValidar = findViewById(R.id.btnPracticaCompletada)
        etRespuesta = findViewById(R.id.etRespuesta)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnValidar.setOnClickListener {
            val respuestaUsuario = etRespuesta.text.toString().trim().uppercase()

            if (respuestaUsuario == respuestaCorrecta.uppercase()) {
                guardarProgresoYVolver()
            } else {
                Toast.makeText(
                    this,
                    "❌ Respuesta incorrecta. Revisa la tabla y vuelve a intentarlo.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun guardarProgresoYVolver() {
        val user = firebaseAuth.currentUser ?: return
        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 1",
            "paso" to "Paso 3",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo1").child("paso3").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "✅ ¡Excelente! Has completado la práctica como un experto.",
                    Toast.LENGTH_LONG
                ).show()

                btnValidar.isEnabled = false
                btnValidar.text = "✅ Práctica completada"

                btnValidar.postDelayed({
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
