package com.example.movierama.ui.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.lists.DividerItemDecoration
import com.example.movierama.R
import com.example.movierama.databinding.FragmentSettingsBinding
import com.example.movierama.ui.base.MenuScreen
import com.example.myutils.disableFullScreenTheme
import com.example.myutils.setLightStatusBars
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by hiltNavGraphViewModels(R.id.graph_settings)

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
                settingsAdapter.submitList(settingsState.getSettingItemList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
