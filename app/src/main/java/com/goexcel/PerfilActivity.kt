package com.goexcel

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnConfiguracion = findViewById<ImageView>(R.id.btnSettings)
        val btnEditarAvatar = findViewById<ImageView>(R.id.btnEditAvatar)
        val txtUserName = findViewById<TextView>(R.id.userName)
        val txtUserLevel = findViewById<TextView>(R.id.userLevel)
        val homeIcon = findViewById<ImageView>(R.id.nav_home)
        val profileImage = findViewById<ShapeableImageView>(R.id.profileImage)

        val logro1 = findViewById<ImageView>(R.id.logro1)
        val logro2 = findViewById<ImageView>(R.id.logro2)
        val logro3 = findViewById<ImageView>(R.id.logro3)
        val logro4 = findViewById<ImageView>(R.id.logro4)

        val barraProgreso = findViewById<ProgressBar>(R.id.barraProgreso)
        val tvProgreso = findViewById<TextView>(R.id.tvProgreso)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId)

            userRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val nombre = snapshot.child("nombre").value?.toString() ?: "Nombre de usuario"
                    val nivel = snapshot.child("nivel").value?.toString() ?: "Nivel desconocido"
                    val avatarIndex = snapshot.child("avatar").value?.toString()?.toIntOrNull() ?: 1

                    txtUserName.text = nombre
                    txtUserLevel.text = nivel

                    val avatarResId = resources.getIdentifier("avatar__${avatarIndex}_", "drawable", packageName)
                    profileImage.setImageResource(if (avatarResId != 0) avatarResId else R.drawable.avatar__1_)
                }
            }

            val progresoRef = FirebaseDatabase.getInstance().getReference("progresoUsuarios").child(userId)
            progresoRef.get().addOnSuccessListener { snapshot ->
                val mod1 = snapshot.child("modulo1").children.any { it.child("estado").value == "completado" }
                val mod2 = snapshot.child("modulo2").children.any { it.child("estado").value == "completado" }
                val mod3 = snapshot.child("modulo3").children.any { it.child("estado").value == "completado" }
                val mod4 = snapshot.child("modulo4").children.any { it.child("estado").value == "completado" }

                configurarLogro(logro1, mod1, R.drawable.insignia1)
                configurarLogro(logro2, mod2, R.drawable.insignia2)
                configurarLogro(logro3, mod3, R.drawable.insignia3)
                configurarLogro(logro4, mod4, R.drawable.insignia4)

                val completados = listOf(mod1, mod2, mod3, mod4).count { it }
                val porcentaje = (completados * 100) / 4

                barraProgreso.progress = porcentaje
                tvProgreso.text = if (porcentaje == 100) {
                    "🎉 ¡Felicidades, completaste todos los módulos!"
                } else {
                    "Tu avance general es del $porcentaje%"
                }
            }

        } else {
            txtUserName.text = "Usuario no autenticado"
            txtUserLevel.text = "Sesión inválida"
        }

        // Navegación
        btnConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }

        btnEditarAvatar.setOnClickListener {
            startActivity(Intent(this, EditarAvatarActivity::class.java))
        }

        homeIcon.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_trophy).setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_stats).setOnClickListener {
            startActivity(Intent(this, DesafiosActivity::class.java))
            finish()
        }
    }

    private fun configurarLogro(view: ImageView, completado: Boolean, resId: Int) {
        view.setImageResource(resId)
        if (!completado) {
            view.setColorFilter(
                ContextCompat.getColor(this, android.R.color.darker_gray),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            view.clearColorFilter()
        }
    }
}

