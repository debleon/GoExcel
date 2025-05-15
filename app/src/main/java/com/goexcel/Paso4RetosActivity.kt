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

class Paso4RetosActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnValidar: Button

    private val respuestasEsperadas = listOf(
        """=SI(A2>=3,"Aprobado","Reprobado")""",
        """=CONTAR.SI(B2:B6,"Sí")""",
        """=BUSCARV("ProductoX",A2:B10,2,FALSO)""",
        """=SI.ERROR(A2/B2,0)""",
        """=SI(Y(A2>5,B2="Sí"),"Correcto","Incorrecto")"""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso4_retos)

        firebaseAuth = FirebaseAuth.getInstance()
        btnValidar = findViewById(R.id.btnValidarRetos)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnValidar.setOnClickListener {
            validarRetos()
        }
    }

    private fun validarRetos() {
        var aciertos = 0

        for (i in 1..5) {
            val inputId = resources.getIdentifier("respuesta$i", "id", packageName)
            val iconId = resources.getIdentifier("icono$i", "id", packageName)

            val input = findViewById<EditText>(inputId).text.toString().trim().replace(" ", "").lowercase()
            val esperado = respuestasEsperadas[i - 1].replace(" ", "").lowercase()
            val icono = findViewById<ImageView>(iconId)

            if (input == esperado) {
                icono.setImageResource(R.drawable.ic_check)
                aciertos++
            } else {
                icono.setImageResource(R.drawable.ic_error)
            }
            icono.visibility = View.VISIBLE
        }

        if (aciertos == 5) {
            guardarProgresoYVolver()
        } else {
            Toast.makeText(this, "🔍 Algunos retos están incorrectos. ¡Revisa y corrige!", Toast.LENGTH_LONG).show()
        }
    }

    private fun guardarProgresoYVolver() {
        val user = firebaseAuth.currentUser
        if (user == null) {
            Toast.makeText(this, "❌ No hay usuario autenticado. Inicia sesión.", Toast.LENGTH_LONG).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("progresoUsuarios")
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val datos = mapOf(
            "modulo" to "Modulo 2",
            "paso" to "Paso 4",
            "estado" to "completado",
            "timestamp" to timestamp
        )

        ref.child(user.uid).child("modulo2").child("paso4").setValue(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "🎉 ¡Completaste todos los retos con éxito!", Toast.LENGTH_LONG).show()
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
                Toast.makeText(this, "❌ Error al guardar el progreso: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}
