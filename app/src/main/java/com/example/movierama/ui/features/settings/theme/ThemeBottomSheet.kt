package com.example.movierama.ui.features.settings.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.example.movierama.R
import com.example.movierama.databinding.BottomSheetLanguageBinding
import com.example.movierama.ui.features.settings.SettingsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ThemeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLanguageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by navGraphViewModels(R.id.graph_settings)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        initObservations()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        binding.cancelButton.setOnClickListener { dismiss() }
    }

    private fun initObservations() {

    }

}