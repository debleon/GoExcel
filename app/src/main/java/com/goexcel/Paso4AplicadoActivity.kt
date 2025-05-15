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

class Paso4AplicadoActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnValidar: Button

    // Campos y feedback visual
    private lateinit var respuestas: List<EditText>
    private lateinit var iconos: List<ImageView>

    // Respuestas correctas
    private val respuestasCorrectas = listOf("Aprobado", "Sí", "Correcto", "No positivo", "Alto")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso4_aplicado)

        firebaseAuth = FirebaseAuth.getInstance()
        btnValidar = findViewById(R.id.btnValidarTodo)

        respuestas = listOf(
            findViewById(R.id.respuesta1),
            findViewById(R.id.respuesta2),
            findViewById(R.id.respuesta3),
            findViewById(R.id.respuesta4),
            findViewById(R.id.respuesta5)
        )

        iconos = listOf(
            findViewById(R.id.iconoResultado1),
            findViewById(R.id.iconoResultado2),
            findViewById(R.id.iconoResultado3),
            findViewById(R.id.iconoResultado4),
            findViewById(R.id.iconoResultado5)
        )

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnValidar.setOnClickListener {
            validarRespuestas()
        }
    }

    private fun validarRespuestas() {
        var todasCorrectas = true

        for (i in respuestas.indices) {
            val entrada = respuestas[i].text.toString().trim().lowercase()
            val correcta = respuestasCorrectas[i].lowercase()

            if (entrada == correcta) {
                iconos[i].setImageResource(R.drawable.ic_check) // ✅
                iconos[i].visibility = View.VISIBLE
            } else {
                iconos[i].setImageResource(R.drawable.ic_error) // ❌
                iconos[i].visibility = View.VISIBLE
                todasCorrectas = false
            }
        }

        if (todasCorrectas) {
            guardarProgresoYVolver()
        } else {
            Toast.makeText(this, "❌ Hay respuestas incorrectas. Revisa los campos marcados.", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarProgresoYVolver() {
        val user = firebaseAuth.currentUser ?: return
        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 1",
            "paso" to "Paso 4",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo1").child("paso4").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ ¡Todas correctas! Has completado esta lección 👏", Toast.LENGTH_LONG).show()
                btnValidar.isEnabled = false
                btnValidar.text = "✅ Completado"
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
