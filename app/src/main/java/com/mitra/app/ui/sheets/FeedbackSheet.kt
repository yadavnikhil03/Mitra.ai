package com.mitra.app.ui.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mitra.app.R
import com.mitra.app.databinding.BottomSheetFeedbackBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FeedbackSheet(
    private val onSend: suspend (String) -> Boolean
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetFeedbackBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.Theme_Mitra_BottomSheetDialog

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        _binding = BottomSheetFeedbackBinding.inflate(inf, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        super.onViewCreated(view, state)

        binding.btnClose.setOnClickListener { dismiss() }

        binding.btnSendFeedback.setOnClickListener {
            val text = binding.etFeedback.text?.toString()?.trim() ?: ""
            if (text.isEmpty()) { binding.etFeedback.requestFocus(); return@setOnClickListener }

            binding.btnSendFeedback.isEnabled = false
            binding.tvFeedbackStatus.isVisible = false

            lifecycleScope.launch {
                val ok = onSend(text)
                binding.btnSendFeedback.isEnabled = true
                binding.tvFeedbackStatus.isVisible = true

                if (ok) {
                    binding.tvFeedbackStatus.text = getString(R.string.feedback_thanks)
                    binding.etFeedback.text?.clear()
                    delay(1400)
                    dismiss()
                } else {
                    binding.tvFeedbackStatus.text = getString(R.string.feedback_fail)
                }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
