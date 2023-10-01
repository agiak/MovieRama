package com.example.movierama.ui.features.settings.language

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.coroutineScope
import com.example.movierama.R
import com.example.movierama.databinding.BottomSheetLanguageBinding
import com.example.movierama.model.settings.Language
import com.example.movierama.ui.features.settings.SettingsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import java.util.Locale

class LanguageBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLanguageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by hiltNavGraphViewModels(R.id.graph_settings)

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

    private fun setLanguageGroupListener() {
        binding.languageChoiceGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.language_en -> Language.English
                R.id.language_gr -> Language.Greek
                else -> Language.English
            }.apply {
                requireContext().setAppLocale(this)
                viewModel.onLanguageChanged(this)
                dismiss()
            }
        }
    }

    private fun initObservations() {
        lifecycle.coroutineScope.launch {
            viewModel.settingsState.collect {
                setSelectedLanguage(it.selectedLanguage)
                setLanguageGroupListener()
            }
        }
    }

    private fun setSelectedLanguage(selectedLanguage: Language) {
        when (selectedLanguage) {
            Language.Greek -> binding.languageGr.isChecked = true
            Language.English -> binding.languageEn.isChecked = true
        }
    }

    private fun Context.setAppLocale(language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        createConfigurationContext(config)
    }
}