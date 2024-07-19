package com.adso.splashscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adso.splashscreen.databinding.ActivityContrasenaBinding

class ContrasenaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContrasenaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContrasenaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {
            val email = binding.etEmail.text.toString()

            if (isValidEmail(email)) {
                // Llamar al API para enviar un correo de restablecimiento de contraseña
                sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
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

    // Función para enviar el correo de restablecimiento de contraseña
    private fun sendPasswordResetEmail(email: String) {
        // Aquí deberías implementar la llamada a tu API para enviar el correo de restablecimiento de contraseña
        // Ejemplo:
        // apiService.sendPasswordResetEmail(email).enqueue(object : Callback<Void> {
        //     override fun onResponse(call: Call<Void>, response: Response<Void>) {
        //         if (response.isSuccessful) {
        //             Toast.makeText(this@ForgotPasswordActivity, "Correo enviado", Toast.LENGTH_SHORT).show()
        //         } else {
        //             Toast.makeText(this@ForgotPasswordActivity, "Error al enviar correo", Toast.LENGTH_SHORT).show()
        //         }
        //     }
        //
        //     override fun onFailure(call: Call<Void>, t: Throwable) {
        //         Toast.makeText(this@ForgotPasswordActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        //     }
        // })

        // Por ahora, solo mostrar un mensaje de éxito simulado
        Toast.makeText(this, "Correo de restablecimiento de contraseña enviado", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, VerificacionCode::class.java)
        startActivity(intent)
    }
    }
