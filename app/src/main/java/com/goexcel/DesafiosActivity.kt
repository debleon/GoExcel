package com.goexcel

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DesafiosActivity : AppCompatActivity() {

    private lateinit var layoutDesafios: LinearLayout
    private val database = FirebaseDatabase.getInstance().getReference("Desafios")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desafios)

        layoutDesafios = findViewById(R.id.layoutContainer)

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val uid = user.uid
        val usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid)

        usuarioRef.get().addOnSuccessListener { userSnapshot ->
            val nivelUsuario = userSnapshot.child("nivel").value?.toString()
                ?.lowercase()
                ?.replace("nivel", "")
                ?.replace("á", "a")
                ?.replace("é", "e")
                ?.replace("í", "i")
                ?.replace("ó", "o")
                ?.replace("ú", "u")
                ?.trim() ?: "basico"

            Toast.makeText(this, "📌 Nivel usuario: $nivelUsuario", Toast.LENGTH_SHORT).show()

            database.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (desafio in snapshot.children) {
                        val nivel = desafio.child("nivel").getValue(String::class.java)?.lowercase()?.trim()

                        if (nivel == nivelUsuario) {
                            val pregunta = desafio.child("opciones/pregunta").getValue(String::class.java) ?: ""
                            val tipo = desafio.child("opciones/tipo").getValue(String::class.java) ?: "texto_libre"
                            val respuesta = desafio.child("opciones/respuesta").getValue(String::class.java) ?: ""

                            val opcionesSnapshot = desafio.child("opciones/respuestas")
                            val opciones = hashMapOf<String, String>()
                            for (opcion in opcionesSnapshot.children) {
                                val clave = opcion.key ?: continue
                                val valor = opcion.getValue(String::class.java) ?: continue
                                opciones[clave] = valor
                            }

                            val contenedor = layoutInflater.inflate(R.layout.item_desafio, null)
                            val txtTitulo = contenedor.findViewById<TextView>(R.id.txtTituloDesafio)
                            val txtDesc = contenedor.findViewById<TextView>(R.id.txtDescripcionDesafio)
                            val btnIniciar = contenedor.findViewById<Button>(R.id.btnIniciar)

                            txtTitulo.text = pregunta.take(40)
                            txtDesc.text = "Nivel: ${nivelUsuario.replaceFirstChar { it.uppercase() }}"

                            // Verificar si el desafío ya fue resuelto
                            val clave = pregunta.take(10).replace("[^a-zA-Z0-9]".toRegex(), "").lowercase()
                            val progresoRef = FirebaseDatabase.getInstance()
                                .getReference("DesafiosPorUsuario").child(uid).child(clave)

                            progresoRef.get().addOnSuccessListener { progresoSnap ->
                                val completado = progresoSnap.child("completado").getValue(Boolean::class.java) ?: false
                                if (completado) {
                                    btnIniciar.text = "✔ Resuelto"
                                    btnIniciar.setBackgroundColor(Color.parseColor("#A5D6A7"))
                                    btnIniciar.isEnabled = false
                                }
                            }

                            btnIniciar.setOnClickListener {
                                val intent = Intent(this, DesafioGenericoActivity::class.java).apply {
                                    putExtra("pregunta", pregunta)
                                    putExtra("tipo", tipo)
                                    putExtra("respuesta", respuesta)
                                    putExtra("opciones", opciones)
                                }
                                startActivity(intent)
                            }

                            layoutDesafios.addView(contenedor)
                        }
                    }
                } else {
                    Toast.makeText(this, "No hay desafíos disponibles", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar desafíos", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_trophy).setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
    }
}



