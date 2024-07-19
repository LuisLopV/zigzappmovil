package com.adso.splashscreen
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val backgroundImage: ImageView = findViewById(R.id.SplashScreenImage)
        val spinAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        backgroundImage.startAnimation(spinAnimation)

        Handler().postDelayed({
            val intent = Intent(this, PermisosUbi::class.java)
            startActivity(intent)
            finish()
        }, 2500) // 2500 is the delayed time in milliseconds.
    }
}