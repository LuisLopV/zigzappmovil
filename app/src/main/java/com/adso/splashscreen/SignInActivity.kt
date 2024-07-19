package com.adso.splashscreen


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import com.adso.splashscreen.databinding.ActivitySignInBinding
import com.google.android.gms.common.api.Response

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            // Validar que el campo de usuario sea un correo electrónico válido
            if (!isValidEmail(email)) {
                Toast.makeText(this@SignInActivity, "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar las credenciales con un usuario registrado simulado
            if (isValidCredentials(email, password)) {
                Toast.makeText(this@SignInActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignInActivity, UbiMap::class.java)
                intent.putExtra("USERNAME", email)
                startActivity(intent)
                finish() // Finaliza esta actividad para que no se pueda regresar presionando el botón atrás
            } else {
                Toast.makeText(this@SignInActivity, "Usuario no registrado. Por favor, regístrese.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@SignInActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this@SignInActivity, ContrasenaActivity::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this) {
            Toast.makeText(this@SignInActivity, "Inicia sesión o regístrate", Toast.LENGTH_SHORT).show()
        }
    }

    }


    // Función para validar un correo electrónico
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val domainAllowed = listOf("gmail.com", "hotmail.com", "hotmail.es", "outlook.com", "outlook.es", "yahoo.com", "yahoo.es")
        val domain = email.substringAfterLast('@')
        return emailRegex.toRegex().matches(email) && domainAllowed.contains(domain)
    }

    // Función para validar las credenciales
    private fun isValidCredentials(email: String, password: String): Boolean {
        // Credenciales registradas
        val registeredEmail = "usuario@gmail.com"
        val registeredPassword = "123456"

        // Comparar las credenciales ingresadas con las registradas
        return email == registeredEmail && password == registeredPassword
    }






