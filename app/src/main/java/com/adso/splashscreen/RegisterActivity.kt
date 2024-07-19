package com.adso.splashscreen
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private val REQUEST_PERMISSIONS=100
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Inicializar elementos de la interfaz de usuario
        val btnAtras = findViewById<ImageButton>(R.id.buttonBack2)
        val editTextNombre1 = findViewById<EditText>(R.id.primer_nombre)
        val editTextNombre2 = findViewById<EditText>(R.id.segundo_nombre)
        val editTextApellido1 = findViewById<EditText>(R.id.primer_apellido)
        val editTextApellido2 = findViewById<EditText>(R.id.segundo_apellido)
        val rhSpinner: Spinner = findViewById(R.id.rh)
        val editTextCedula = findViewById<EditText>(R.id.cedula)
        val editTextDia = findViewById<EditText>(R.id.dia)
        val editTextMes = findViewById<EditText>(R.id.mes)
        val editTextAnio = findViewById<EditText>(R.id.anio)
        val buttonRegister = findViewById<Button>(R.id.continuar)


        ArrayAdapter.createFromResource(this,R.array.rh_Types,android.R.layout.simple_spinner_item).also {
            adapter ->  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rhSpinner.adapter = adapter
        }

        btnAtras.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Configurar clic del botón de registro
        buttonRegister.setOnClickListener {
            // Obtener los valores ingresados por el usuario
            val nombre1 = editTextNombre1.text.toString().trim()
            val nombre2 = editTextNombre2.text.toString().trim()
            val apellido1 = editTextApellido1.text.toString().trim()
            val apellido2 = editTextApellido2.text.toString().trim()

            val cedula = editTextCedula.text.toString().trim()
            val dia = editTextDia.text.toString().trim()
            val mes = editTextMes.text.toString().trim()
            val año = editTextAnio.text.toString().trim()

            // Validar si todos los campos están completos
            if (nombre1.isEmpty() || apellido1.isEmpty() ||
                 cedula.isEmpty() || dia.isEmpty() ||
                mes.isEmpty() || año.isEmpty()
            ) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


        }
        val uploadPhotoButton: Button = findViewById(R.id.upload_photo)
        uploadPhotoButton.setOnClickListener {
            showPictureDialog()
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Seleccionar Acción")
        val pictureDialogItems =
            arrayOf("Seleccionar foto de galería", "Capturar foto desde cámara")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
    }

    private fun takePhotoFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.adso.splashscreen.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


}

