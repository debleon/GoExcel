package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritosActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var notasContainer: LinearLayout
    private lateinit var edtNuevaNota: EditText
    private lateinit var btnAgregarNota: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return
        databaseRef = FirebaseDatabase.getInstance().getReference("usuarios")
            .child(userId).child("favoritos")

        notasContainer = findViewById(R.id.contenedorNotas)
        edtNuevaNota = findViewById(R.id.edtNuevaNota)
        btnAgregarNota = findViewById(R.id.btnAgregarNota)

        btnAgregarNota.setOnClickListener {
            val textoNota = edtNuevaNota.text.toString().trim()
            if (textoNota.isNotEmpty()) {
                val nuevaClave = databaseRef.push().key
                if (nuevaClave != null) {
                    databaseRef.child(nuevaClave).setValue(textoNota)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
                            edtNuevaNota.setText("")
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Escribe algo antes de guardar", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar y mostrar notas con opción de editar/eliminar
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notasContainer.removeAllViews()
                for (notaSnap in snapshot.children) {
                    val notaId = notaSnap.key ?: continue
                    val texto = notaSnap.getValue(String::class.java) ?: continue

                    val card = LinearLayout(this@FavoritosActivity).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(24, 24, 24, 24)
                        background = ContextCompat.getDrawable(this@FavoritosActivity, R.drawable.bg_goexcel_card)
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 0, 0, 24)
                        }
                        elevation = 6f
                    }

                    val textView = TextView(this@FavoritosActivity).apply {
                        text = texto
                        textSize = 16f
                        setTextColor(resources.getColor(android.R.color.black))
                    }

                    val botones = LinearLayout(this@FavoritosActivity).apply {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.END
                        setPadding(0, 16, 0, 0)
                    }

                    val btnEditar = Button(this@FavoritosActivity).apply {
                        text = "Editar"
                        setBackgroundResource(R.drawable.btn_editar_style)
                        setOnClickListener {
                            mostrarDialogoEditarNota(notaId, texto)
                        }
                    }

                    val btnEliminar = Button(this@FavoritosActivity).apply {
                        text = "Eliminar"
                        setBackgroundResource(R.drawable.btn_eliminar_style)
                        setOnClickListener {
                            databaseRef.child(notaId).removeValue()
                        }
                    }

                    botones.addView(btnEditar)
                    botones.addView(btnEliminar)
                    card.addView(textView)
                    card.addView(botones)
                    notasContainer.addView(card)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FavoritosActivity, "Error al cargar notas", Toast.LENGTH_SHORT).show()
            }
        })

        // Navegación inferior
        val homeIcon = findViewById<ImageView>(R.id.nav_home)
        val profileIcon = findViewById<ImageView>(R.id.nav_profile)

        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        profileIcon.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        findViewById<ImageView>(R.id.nav_stats).setOnClickListener {
            startActivity(Intent(this, DesafiosActivity::class.java))
            finish()
        }
    }

    private fun mostrarDialogoEditarNota(notaId: String, textoActual: String) {
        val input = EditText(this)
        input.setText(textoActual)

        AlertDialog.Builder(this)
            .setTitle("Editar Nota")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoTexto = input.text.toString().trim()
                if (nuevoTexto.isNotEmpty()) {
                    databaseRef.child(notaId).setValue(nuevoTexto)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

