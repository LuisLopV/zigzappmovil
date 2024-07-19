package com.adso.splashscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adso.splashscreen.databinding.ActivityVerificacionCodeBinding

class VerificacionCode : AppCompatActivity() {

    private lateinit var binding: ActivityVerificacionCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificacionCodeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            val code = binding.etCode.text.toString()
            if (isValidCode(code)) {
                // Implementar la lógica para verificar el código
                verifyCode(code)
            } else {
                Toast.makeText(this, "Código no válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para verificar el código ingresado
    private fun verifyCode(code: String) {


        // Por ahora, solo mostrar un mensaje de éxito simulado
        if (code == "123456") { // Suponiendo que "123456" es el código esperado para fines de demostración
            Toast.makeText(this, "Código verificado", Toast.LENGTH_SHORT).show()
            // Aquí puedes redirigir al usuario a otra actividad, por ejemplo:
            val intent = Intent(this, SignInActivity::class.java)

            startActivity(intent)
        } else {
            Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para validar el formato del código ingresado
    private fun isValidCode(code: String): Boolean {
        // Por ejemplo, un código válido podría ser de 6 dígitos
        return code.length == 6 && code.all { it.isDigit() }
    }
}