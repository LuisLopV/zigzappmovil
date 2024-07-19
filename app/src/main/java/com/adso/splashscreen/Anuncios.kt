package com.adso.splashscreen


import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adso.splashscreen.databinding.ActivityAnunciosBinding
import com.bumptech.glide.Glide

class Anuncios : AppCompatActivity() {

    private lateinit var binding: ActivityAnunciosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnunciosBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val btnAtras = findViewById<ImageButton>(R.id.buttonBack2)

        btnAtras.setOnClickListener {
            val intent = Intent(this, UbiMap::class.java)
            startActivity(intent)
        }

        val imageView: ImageView = findViewById(R.id.backArrow)

// Cargar el GIF utilizando Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.altavoz) // Reemplaza gif_image con el nombre de tu GIF en res/raw
            .into(binding.backArrow)
    }
}