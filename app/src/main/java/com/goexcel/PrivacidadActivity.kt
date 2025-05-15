package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PrivacidadActivity : AppCompatActivity() {

    private lateinit var switchAvances: SwitchCompat
    private lateinit var switchAnalisis: SwitchCompat
    private lateinit var switchNotificaciones: SwitchCompat
    private lateinit var switchFavoritos: SwitchCompat
    private lateinit var switchExperiencia: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacidad)

        // Referencias a switches
        switchAvances = findViewById(R.id.switchPrivacidadAvances)
        switchAnalisis = findViewById(R.id.switchAnalisis)
        switchNotificaciones = findViewById(R.id.switchNotificaciones)
        switchFavoritos = findViewById(R.id.switchFavoritos)
        switchExperiencia = findViewById(R.id.switchExperiencia)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val dbRef = FirebaseDatabase.getInstance()
                .getReference("usuarios").child(userId).child("privacidad")

            // Leer los valores guardados
            dbRef.get().addOnSuccessListener { snapshot ->
                switchAvances.isChecked = snapshot.child("avances").getValue(Boolean::class.java) ?: false
                switchAnalisis.isChecked = snapshot.child("analisis").getValue(Boolean::class.java) ?: false
                switchNotificaciones.isChecked = snapshot.child("notificaciones").getValue(Boolean::class.java) ?: false
                switchFavoritos.isChecked = snapshot.child("favoritos").getValue(Boolean::class.java) ?: false
                switchExperiencia.isChecked = snapshot.child("experiencia").getValue(Boolean::class.java) ?: false
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }

            // Guardar al hacer clic
            findViewById<Button>(R.id.btnGuardarPrivacidad).setOnClickListener {
                val cambios = mapOf(
                    "avances" to switchAvances.isChecked,
                    "analisis" to switchAnalisis.isChecked,
                    "notificaciones" to switchNotificaciones.isChecked,
                    "favoritos" to switchFavoritos.isChecked,
                    "experiencia" to switchExperiencia.isChecked
                )
                dbRef.setValue(cambios).addOnSuccessListener {
                    Toast.makeText(this, "Privacidad actualizada", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
        }

        // Botón salir (regresa al perfil)
        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }

        // Navegación inferior
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_logo).setOnClickListener {
            Toast.makeText(this, "GOEXCEL", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageView>(R.id.nav_stats).setOnClickListener {
            Toast.makeText(this, "Módulo de Avances", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageView>(R.id.nav_trophy).setOnClickListener {
            Toast.makeText(this, "Favoritos", Toast.LENGTH_SHORT).show()
        }
        val homeIcon = findViewById<ImageView>(R.id.nav_home)
        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() // Opcional: cierra la vista actual
        }
        val btnFavoritos = findViewById<ImageView>(R.id.nav_trophy)
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
    }
}
