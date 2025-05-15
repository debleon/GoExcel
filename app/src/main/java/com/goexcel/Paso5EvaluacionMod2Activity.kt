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

class Paso5EvaluacionMod2Activity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnEvaluar: Button

    private val respuestasCorrectas = mapOf(
        1 to R.id.resp1_a,
        2 to R.id.resp2_a,
        3 to R.id.resp3_a,
        4 to R.id.resp4_a,
        5 to R.id.resp5_a,
        6 to R.id.resp6_a,
        7 to R.id.resp7_a,
        8 to R.id.resp8_a,
        9 to R.id.resp9_a,
        10 to R.id.resp10_a
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso5_evaluacion_mod2)

        firebaseAuth = FirebaseAuth.getInstance()
        btnEvaluar = findViewById(R.id.btnEvaluarQuiz)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnEvaluar.setOnClickListener {
            evaluarQuiz()
        }
    }

    private fun evaluarQuiz() {
        var correctas = 0

        for (i in 1..10) {
            val groupId = resources.getIdentifier("respuesta$i", "id", packageName)
            val grupo = findViewById<RadioGroup>(groupId)
            val seleccion = grupo.checkedRadioButtonId

            val iconId = resources.getIdentifier("icono$i", "id", packageName)
            val icono = findViewById<ImageView>(iconId)

            if (seleccion == respuestasCorrectas[i]) {
                correctas++
                icono.setImageResource(R.drawable.ic_check)
            } else {
                icono.setImageResource(R.drawable.ic_error)
            }
            icono.visibility = View.VISIBLE
        }

        if (correctas >= 8) {
            guardarProgreso(correctas)
        } else {
            Toast.makeText(this, "Obtuviste $correctas/10. Necesitas mínimo 8 para aprobar. 📉", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarProgreso(aciertos: Int) {
        val user = firebaseAuth.currentUser
        if (user == null) {
            Toast.makeText(this, "⚠️ Debes iniciar sesión para guardar tu progreso.", Toast.LENGTH_LONG).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 2",
            "paso" to "Paso 5",
            "estado" to "completado",
            "resultado" to "$aciertos/10",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo2").child("paso5").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "🎉 ¡Aprobaste con $aciertos/10! Progreso guardado.", Toast.LENGTH_LONG).show()
                btnEvaluar.isEnabled = false
                btnEvaluar.text = "✅ Evaluación completada"

                btnEvaluar.postDelayed({
                    val intent = Intent(this, ModuloRuta2Activity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }, 2000)
            }
            .addOnFailureListener {
                Toast.makeText(this, "❌ Error al guardar el progreso: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}
