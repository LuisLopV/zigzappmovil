package com.adso.splashscreen


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InvitarAmigo : AppCompatActivity() {
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_invitar_amigo)

        val btnAtras = findViewById<ImageButton>(R.id.buttonBack2)

        textView = findViewById(R.id.Texto2)
        registerForContextMenu(textView)



        btnAtras.setOnClickListener {
            val intent = Intent(this, UbiMap::class.java)
            startActivity(intent)
        }

        textView.setOnLongClickListener {
            openContextMenu(textView)
            true
        }
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.copy) {
            copyToClipboard(textView.text.toString())
            return true
        }
        return super.onContextItemSelected(item)
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Texto copiado", Toast.LENGTH_SHORT).show()
    }
}

