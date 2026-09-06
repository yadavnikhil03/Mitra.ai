package com.mitra.app.ui.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mitra.app.R
import com.mitra.app.databinding.BottomSheetConfirmDeleteBinding

class ConfirmDeleteSheet(
    private val onConfirmDelete: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetConfirmDeleteBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.Theme_Mitra_BottomSheetDialog

    override fun onCreateView(inf: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        _binding = BottomSheetConfirmDeleteBinding.inflate(inf, parent, false)
        return binding.root
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        super.onViewCreated(view, state)
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnConfirmDelete.setOnClickListener {
            dismiss()
            onConfirmDelete()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
