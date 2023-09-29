package com.example.movierama.ui.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.lists.DividerItemDecoration
import com.example.movierama.R
import com.example.movierama.databinding.FragmentSettingsBinding
import com.example.movierama.model.settings.Language
import com.example.movierama.model.settings.Orientation
import com.example.movierama.model.settings.Theme
import com.example.movierama.model.settings.mapToSettingItem
import com.example.movierama.ui.base.MenuScreen
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.setLightStatusBars
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by navGraphViewModels(R.id.graph_settings)

    private val settingsAdapter =
        SettingsAdapter { selectedItemSetting -> handleSettingItemClick(selectedItemSetting) }

    private fun handleSettingItemClick(selectedItem: SettingItem) {
        when (selectedItem) {
            is SettingItem.SettingItemLanguage -> findNavController().navigate(R.id.action_nav_settings_to_nav_language)
            is SettingItem.SettingItemOrientation -> findNavController().navigate(R.id.action_nav_settings_to_nav_orientation)
            is SettingItem.SettingItemTheme -> findNavController().navigate(R.id.action_nav_settings_to_nav_theme)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        disableFullScreenTheme()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableFullScreenTheme()
        setLightStatusBars(true)
        initViews()
    }

    override fun onStart() {
        super.onStart()
        initSubscriptions()
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            (requireActivity() as? MenuScreen)?.let { setMenuListener(it.getSideMenu()) }
        }
    }

    private fun initViews() {
        binding.settingList.apply {
            adapter = settingsAdapter
            settingsAdapter.submitList(viewModel.getDefaultSettingsItem())
            addItemDecoration(
                DividerItemDecoration(
                    ContextCompat.getDrawable(
                        requireContext(),
                        com.example.myutils.R.drawable.divider_bg
                    )!!
                )
            )
        }
    }

    private fun initSubscriptions() {
        lifecycle.coroutineScope.launchWhenResumed {
            viewModel.settingsState.collect { settingsState ->
                //settingsAdapter.submitList(settingsState.getSettingItemList())
                handleLanguageState(settingsState.selectedLanguage)
                handleOrientationState(settingsState.selectedOrientation)
                handleThemeState(settingsState.selectedTheme)
            }
        }
    }

    private fun handleLanguageState(selectedLanguage: Language) {
        settingsAdapter.updateSettingItem(selectedLanguage.mapToSettingItem())
    }

    private fun handleOrientationState(selectedOrientation: Orientation) {
        settingsAdapter.updateSettingItem(selectedOrientation.mapToSettingItem())
    }

    private fun handleThemeState(selectedTheme: Theme) {
        settingsAdapter.updateSettingItem(selectedTheme.mapToSettingItem())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
