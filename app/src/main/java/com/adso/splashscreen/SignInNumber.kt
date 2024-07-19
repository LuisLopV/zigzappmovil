package com.adso.splashscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignInNumber : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in_number)

        val btnSiguiente= findViewById<Button>(R.id.continueButton)
        val btnAtras = findViewById<ImageButton>(R.id.buttonBack)


        btnSiguiente.setOnClickListener {
            val intent = Intent(this, Password::class.java)
            startActivity(intent)
        }

        btnAtras.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


    }
}