package com.mitra.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mitra.app.R
import com.mitra.app.databinding.ActivityLoginBinding
import com.mitra.app.ui.chat.ChatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply insets so card stays above keyboard
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Start particle background
        binding.particleView.start()

        // Animate glow orb (breathe)
        startGlowBreath()

        // 3-D tilt on auth card pointer events
        setupCardTilt()

        setupClickListeners()
        observeState()
    }

    private fun startGlowBreath() {
        binding.glowOrb.animate()
            .scaleX(1.08f).scaleY(1.08f)
            .setDuration(3200)
            .withEndAction {
                binding.glowOrb.animate()
                    .scaleX(1.0f).scaleY(1.0f)
                    .setDuration(3200)
                    .withEndAction { startGlowBreath() }
                    .start()
            }.start()
    }

    private fun setupCardTilt() {
        binding.authCard.setOnTouchListener { v, event ->
            val dx = (event.x - v.width / 2f) / (v.width / 2f)
            val dy = (event.y - v.height / 2f) / (v.height / 2f)
            when (event.action) {
                android.view.MotionEvent.ACTION_MOVE -> {
                    v.rotationY = dx * 4f
                    v.rotationX = -dy * 4f
                }
                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> {
                    v.animate().rotationX(0f).rotationY(0f).setDuration(300).start()
                }
            }
            false
        }
    }

    private fun setupClickListeners() {
        binding.btnAuthSubmit.setOnClickListener {
            val email    = binding.etEmail.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString() ?: ""
            vm.submit(email, password)
        }
        binding.btnAuthToggle.setOnClickListener { vm.toggleMode() }
        binding.btnForgotPassword.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            vm.forgotPassword(email)
        }
        // Pressing Enter on password submits
        binding.etPassword.setOnEditorActionListener { _, _, _ ->
            binding.btnAuthSubmit.performClick(); true
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.uiState.collect { state ->
                        // Loading state
                        binding.btnAuthSubmit.isEnabled = !state.isLoading
                        binding.btnAuthSubmit.alpha     = if (state.isLoading) 0.55f else 1f

                        // Toggle button text
                        binding.btnAuthToggle.text = when (state.mode) {
                            AuthMode.LOGIN  -> getString(R.string.new_here)
                            AuthMode.SIGNUP -> getString(R.string.have_account)
                        }
                        binding.btnAuthSubmit.text = when (state.mode) {
                            AuthMode.LOGIN  -> getString(R.string.sign_in)
                            AuthMode.SIGNUP -> getString(R.string.sign_up)
                        }

                        // Error banner
                        if (state.errorMessage.isNotBlank()) {
                            binding.tvAuthError.text       = state.errorMessage
                            binding.tvAuthError.visibility = View.VISIBLE
                            binding.tvAuthError.startAnimation(
                                AnimationUtils.loadAnimation(this@LoginActivity, R.anim.fade_in)
                            )
                        } else {
                            binding.tvAuthError.visibility = View.GONE
                        }
                    }
                }
                launch {
                    vm.events.collect { event ->
                        when (event) {
                            is LoginEvent.NavigateToChat -> goToChat()
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    private fun goToChat() {
        startActivity(Intent(this, ChatActivity::class.java))
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onResume()  { super.onResume();  binding.particleView.start() }
    override fun onPause()   { super.onPause();   binding.particleView.stop()  }
    override fun onDestroy() { super.onDestroy(); binding.particleView.stop()  }
}
