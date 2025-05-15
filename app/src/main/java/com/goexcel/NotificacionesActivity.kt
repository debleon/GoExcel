package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NotificacionesActivity : AppCompatActivity() {

    private lateinit var switchGenerales: SwitchCompat
    private lateinit var switchSugerencias: SwitchCompat
    private lateinit var switchModulos: SwitchCompat
    private lateinit var switchLogros: SwitchCompat
    private lateinit var switchNovedades: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        // Referencias a switches
        switchGenerales = findViewById(R.id.switchNotificacionesGenerales)
        switchSugerencias = findViewById(R.id.switchSugerencias)
        switchModulos = findViewById(R.id.switchNuevosModulos)
        switchLogros = findViewById(R.id.switchLogros)
        switchNovedades = findViewById(R.id.switchNovedades)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val dbRef = FirebaseDatabase.getInstance()
                .getReference("usuarios").child(userId).child("notificaciones")

            // Leer valores existentes
            dbRef.get().addOnSuccessListener { snapshot ->
                switchGenerales.isChecked = snapshot.child("generales").getValue(Boolean::class.java) ?: false
                switchSugerencias.isChecked = snapshot.child("sugerencias").getValue(Boolean::class.java) ?: false
                switchModulos.isChecked = snapshot.child("nuevosModulos").getValue(Boolean::class.java) ?: false
                switchLogros.isChecked = snapshot.child("logros").getValue(Boolean::class.java) ?: false
                switchNovedades.isChecked = snapshot.child("novedades").getValue(Boolean::class.java) ?: false
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar notificaciones", Toast.LENGTH_SHORT).show()
            }

            // Guardar al presionar "Guardar"
            findViewById<Button>(R.id.btnGuardarNotificaciones).setOnClickListener {
                val cambios = mapOf(
                    "generales" to switchGenerales.isChecked,
                    "sugerencias" to switchSugerencias.isChecked,
                    "nuevosModulos" to switchModulos.isChecked,
                    "logros" to switchLogros.isChecked,
                    "novedades" to switchNovedades.isChecked
                )
                dbRef.setValue(cambios).addOnSuccessListener {
                    Toast.makeText(this, "Notificaciones actualizadas", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón Salir (volver al Perfil)
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
