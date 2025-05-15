package com.goexcel

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DesafioGenericoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desafio_generico)

        // Obtener los datos recibidos del intent
        val pregunta = intent.getStringExtra("pregunta") ?: getString(R.string.pregunta_no_disponible)
        val tipo = intent.getStringExtra("tipo") ?: "texto_libre"
        val respuestaCorrecta = intent.getStringExtra("respuesta") ?: ""
        val opciones = intent.getSerializableExtra("opciones") as? HashMap<String, String>

        // Referencias de UI
        val txtPregunta = findViewById<TextView>(R.id.txtPregunta)
        val layoutOpciones = findViewById<RadioGroup>(R.id.layoutOpciones)
        val edtRespuestaLibre = findViewById<EditText>(R.id.edtRespuestaLibre)
        val btnValidar = findViewById<Button>(R.id.btnValidar)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        // Mostrar la pregunta
        txtPregunta.text = pregunta
        txtPregunta.visibility = View.VISIBLE
        Log.d("DESAFIO", "📌 Pregunta recibida: $pregunta")
        Toast.makeText(this, "📌 Pregunta recibida: $pregunta", Toast.LENGTH_SHORT).show()

        // Mostrar vista según tipo de respuesta
        if (tipo == "texto_libre") {
            layoutOpciones.visibility = View.GONE
            edtRespuestaLibre.visibility = View.VISIBLE
        } else {
            layoutOpciones.visibility = View.VISIBLE
            edtRespuestaLibre.visibility = View.GONE
            layoutOpciones.removeAllViews()

            opciones?.forEach { (clave, valor) ->
                val radioButton = RadioButton(this)
                radioButton.text = "$clave. $valor"
                radioButton.tag = clave
                radioButton.textSize = 16f
                layoutOpciones.addView(radioButton)
            }
        }

        // Validar respuesta
        btnValidar.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            val userRespuesta = if (tipo == "texto_libre") {
                edtRespuestaLibre.text.toString().trim().lowercase()
            } else {
                val checkedId = layoutOpciones.checkedRadioButtonId
                val selectedRadio = layoutOpciones.findViewById<RadioButton>(checkedId)
                selectedRadio?.tag?.toString()?.lowercase() ?: ""
            }

            if (userRespuesta == respuestaCorrecta.lowercase()) {
                txtResultado.setTextColor(Color.parseColor("#388E3C"))
                txtResultado.text = getString(R.string.mensaje_correcto)

                // Guardar que fue completado en Firebase
                val clave = pregunta.take(10).replace("[^a-zA-Z0-9]".toRegex(), "").lowercase()
                val baseRef = FirebaseDatabase.getInstance().getReference("DesafiosPorUsuario").child(uid)
                baseRef.child(clave).child("completado").setValue(true)
            } else {
                txtResultado.setTextColor(Color.parseColor("#D32F2F"))
                txtResultado.text = getString(R.string.mensaje_incorrecto)
            }

            txtResultado.visibility = View.VISIBLE
        }

        // Botón regresar
        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
