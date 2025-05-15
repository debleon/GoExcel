package com.goexcel

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var logoutPopup: RelativeLayout
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnCerrarPopup: ImageView
    private lateinit var menuIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔵 Referencias de UI
        logoutPopup = findViewById(R.id.logoutPopup)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnCerrarPopup = findViewById(R.id.btnCerrarPopup)
        menuIcon = findViewById(R.id.menuIcon)

        // 🔵 Menú de cerrar sesión
        menuIcon.setOnClickListener {
            logoutPopup.visibility = View.VISIBLE
        }

        btnCerrarPopup.setOnClickListener {
            logoutPopup.visibility = View.GONE
        }

        btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // 🔵 Rutas por módulos
        // Clic en el texto MODULO 1 (contenedor completo)
        val module1Container = findViewById<LinearLayout>(R.id.module1Container)
        module1Container.setOnClickListener {
            val intent = Intent(this, ModuloRuta1Activity::class.java)
            startActivity(intent)
        }

        // Clic en la lupa de MODULO 1
        val module1Icon = findViewById<ImageView>(R.id.module1Icon)
        module1Icon.setOnClickListener {
            val intent = Intent(this, ModuloVista1Activity::class.java)
            startActivity(intent)

        }
        // Clic en el texto MODULO 2 (contenedor completo)
        val module2Container = findViewById<LinearLayout>(R.id.module2Container)
        module2Container.setOnClickListener {
            val intent = Intent(this, ModuloRuta2Activity::class.java)
            startActivity(intent)
        }

        // Clic en la lupa de MODULO 2
        val module2Icon= findViewById<ImageView>(R.id.module2Icon)
        module2Icon.setOnClickListener {
            val intent = Intent(this, ModuloVista2Activity::class.java)
            startActivity(intent)

        }
        // Clic en el texto MODULO 3 (contenedor completo)
        val module3Container = findViewById<LinearLayout>(R.id.module3Container)
        module3Container.setOnClickListener {
            val intent = Intent(this, ModuloRuta3Activity::class.java)
            startActivity(intent)
        }

        // Clic en la lupa de MODULO 3
        val module3Icon= findViewById<ImageView>(R.id.module3Icon)
        module3Icon.setOnClickListener {
            val intent = Intent(this, ModuloVista3Activity::class.java)
            startActivity(intent)

        }

        // Clic en el texto MODULO 4 (contenedor completo)
        val module4Container = findViewById<LinearLayout>(R.id.module4Container)
        module4Container.setOnClickListener {
            val intent = Intent(this, ModuloRuta4Activity::class.java)
            startActivity(intent)
        }

        // Clic en la lupa de MODULO 4
        val module4Icon= findViewById<ImageView>(R.id.module4Icon)
        module4Icon.setOnClickListener {
            val intent = Intent(this, ModuloVista4Activity::class.java)
            startActivity(intent)

        }

        // Clic en el texto MODULO 5 (contenedor completo)
        val module5Container = findViewById<LinearLayout>(R.id.module5Container)
        module5Container.setOnClickListener {
            val intent = Intent(this, ModuloRuta5Activity::class.java)
            startActivity(intent)
        }

        // Clic en la lupa de MODULO 5
        val module5Icon= findViewById<ImageView>(R.id.module5Icon)
        module5Icon.setOnClickListener {
            val intent = Intent(this, ModuloVista5Activity::class.java)
            startActivity(intent)

        }

        val profileIcon = findViewById<ImageView>(R.id.nav_profile)

        profileIcon.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
        val btnFavoritos = findViewById<ImageView>(R.id.nav_trophy)
        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_stats).setOnClickListener {
           startActivity(Intent(this, DesafiosActivity::class.java))
           finish()
        }
    }}