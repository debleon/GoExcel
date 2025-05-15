package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditarAvatarActivity : AppCompatActivity() {

    private var selectedAvatarId: Int = -1
    private lateinit var auth: FirebaseAuth
    private var selectedImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_avatar)

        auth = FirebaseAuth.getInstance()

        val avatarViews = listOf(
            R.id.avatar1, R.id.avatar2, R.id.avatar3, R.id.avatar4,
            R.id.avatar5, R.id.avatar6, R.id.avatar7, R.id.avatar8,
            R.id.avatar9, R.id.avatar10, R.id.avatar11, R.id.avatar12
        )

        val avatarImageViews = avatarViews.map { findViewById<ImageView>(it) }

        // ✅ Marcar el avatar actual al iniciar
        auth.currentUser?.uid?.let { userId ->
            val db = FirebaseDatabase.getInstance().getReference("usuarios").child(userId)
            db.get().addOnSuccessListener { snapshot ->
                val savedAvatar = snapshot.child("avatar").value?.toString()?.toIntOrNull()
                if (savedAvatar != null && savedAvatar in 1..avatarImageViews.size) {
                    selectedAvatarId = savedAvatar
                    selectedImageView = avatarImageViews[savedAvatar - 1]
                    selectedImageView?.setBackgroundResource(R.drawable.avatar_selected_border)
                }
            }
        }

        // 👆 Selección de avatares
        avatarImageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                selectedImageView?.background = null // Quitar anterior borde
                selectedAvatarId = index + 1
                imageView.setBackgroundResource(R.drawable.avatar_selected_border)
                selectedImageView = imageView
                Toast.makeText(this, "Avatar $selectedAvatarId seleccionado", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔃 Actualizar
        findViewById<Button>(R.id.btnActualizar).setOnClickListener {
            if (selectedAvatarId == -1) {
                Toast.makeText(this, "Selecciona un avatar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.currentUser?.uid?.let { userId ->
                val db = FirebaseDatabase.getInstance().getReference("usuarios").child(userId)
                db.child("avatar").setValue(selectedAvatarId)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Avatar actualizado", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, PerfilActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar avatar", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // ❌ Cancelar
        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            finish()
        }

    }
}



