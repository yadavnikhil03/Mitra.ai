package com.mitra.app.ui.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mitra.app.R
import com.mitra.app.databinding.BottomSheetInfoBinding

class InfoSheet(
    private val onFeedback: () -> Unit,
    private val onDeleteAll: () -> Unit,
    private val onLogout: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetInfoBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.Theme_Mitra_BottomSheetDialog

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        _binding = BottomSheetInfoBinding.inflate(inf, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        super.onViewCreated(view, state)
        binding.btnOkay.setOnClickListener { dismiss() }
        binding.btnFeedback.setOnClickListener { dismiss(); onFeedback() }
        binding.btnDeleteAllChats.setOnClickListener {
            dismiss()
            onDeleteAll()
        }
        binding.btnLogout.setOnClickListener { dismiss(); onLogout() }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { d ->
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            sheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
