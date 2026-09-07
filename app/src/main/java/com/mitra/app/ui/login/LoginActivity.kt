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

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setupClickListeners()
        observeState()
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
        binding.etPassword.setOnEditorActionListener { _, _, _ ->
            binding.btnAuthSubmit.performClick(); true
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.uiState.collect { state ->
                        binding.btnAuthSubmit.isEnabled = !state.isLoading
                        binding.btnAuthSubmit.alpha     = if (state.isLoading) 0.55f else 1f

                        binding.btnAuthToggle.text = when (state.mode) {
                            AuthMode.LOGIN  -> getString(R.string.new_here)
                            AuthMode.SIGNUP -> getString(R.string.have_account)
                        }
                        binding.btnAuthSubmit.text = when (state.mode) {
                            AuthMode.LOGIN  -> getString(R.string.sign_in)
                            AuthMode.SIGNUP -> getString(R.string.sign_up)
                        }

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
}
