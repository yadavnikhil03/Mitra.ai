package com.mitra.app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.mitra.app.R
import com.mitra.app.databinding.ActivitySplashBinding
import com.mitra.app.ui.chat.ChatActivity
import com.mitra.app.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.particleView.start()

        // Fade & scale-in animation for glowing orb + Mitra brand mark below it
        binding.splashContent.alpha = 0f
        binding.splashContent.scaleX = 0.9f
        binding.splashContent.scaleY = 0.9f
        binding.splashContent.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setInterpolator(DecelerateInterpolator())
            .start()

        lifecycleScope.launch {
            delay(1200)
            val dest = if (auth.currentUser != null) ChatActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this@SplashActivity, dest))
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.particleView.start()
    }

    override fun onPause() {
        super.onPause()
        binding.particleView.stop()
    }
}
