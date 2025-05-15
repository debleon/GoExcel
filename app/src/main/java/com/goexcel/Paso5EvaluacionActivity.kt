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

class Paso5EvaluacionActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnEvaluar: Button

    // Respuestas correctas reales (según strings actualizados)
    private val respuestasCorrectas = listOf(
        R.id.opcionB1,  // =SUMA(A1:A5)
        R.id.opcionC2,  // =MAX()
        R.id.opcionA3,  // =PROMEDIO()
        R.id.opcionA4,  // Evalúa SI
        R.id.opcionB5,  // =MIN()
        R.id.opcionB6,  // CONCATENAR textos
        R.id.opcionA7,  // HOY()
        R.id.opcionB8,  // CONTAR()
        R.id.opcionC9,  // ESTEXTO()
        R.id.opcionA10  // BUSCARV
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso5_evaluacion)

        firebaseAuth = FirebaseAuth.getInstance()
        btnEvaluar = findViewById(R.id.btnEvaluarRespuestas)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnEvaluar.setOnClickListener {
            evaluar()
        }
    }

    private fun evaluar() {
        var correctas = 0

        for (i in 1..10) {
            val grupo = findViewById<RadioGroup>(
                resources.getIdentifier("grupoOpciones$i", "id", packageName)
            )
            val icono = findViewById<ImageView>(
                resources.getIdentifier("iconoResultado$i", "id", packageName)
            )
            val seleccion = grupo.checkedRadioButtonId
            val correcta = respuestasCorrectas[i - 1]

            if (seleccion == correcta) {
                icono.setImageResource(R.drawable.ic_check)
                icono.visibility = View.VISIBLE
                correctas++
            } else {
                icono.setImageResource(R.drawable.ic_error)
                icono.visibility = View.VISIBLE
            }
        }

        if (correctas >= 7) {
            guardarProgresoYFinalizar()
        } else {
            Toast.makeText(this, "Obtuviste $correctas/10. Debes tener mínimo 7 para aprobar.", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarProgresoYFinalizar() {
        val user = firebaseAuth.currentUser ?: return
        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 1",
            "paso" to "Paso 5",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo1").child("paso5").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "🎉 ¡Módulo 1 completado exitosamente!", Toast.LENGTH_LONG).show()
                btnEvaluar.isEnabled = false
                btnEvaluar.text = "✅ Completado"
                btnEvaluar.postDelayed({
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
