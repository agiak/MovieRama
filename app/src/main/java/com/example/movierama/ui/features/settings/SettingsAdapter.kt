package com.example.movierama.ui.features.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movierama.R
import com.example.movierama.databinding.ItemSettingBinding
import com.example.movierama.model.settings.Language
import com.example.movierama.model.settings.Orientation
import com.example.movierama.model.settings.Theme

class SettingsAdapter(
    private val onClick: (setting: SettingItem) -> Unit
) : ListAdapter<SettingItem, SettingsAdapter.SettingItemViewHolder>(SettingItemDiffCallback()) {

    private lateinit var context: Context

    var mutableSettingsList: MutableList<SettingItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingBinding.inflate(inflater, parent, false)
        context = parent.context
        return SettingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingItemViewHolder, position: Int) {
        val settingItem = getItem(position)
        holder.bind(settingItem)
    }

    inner class SettingItemViewHolder(private val binding: ItemSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(settingItem: SettingItem) {
            with(settingItem) {
                binding.title.text = context.getString(settingItem.displayNameId)
                binding.startIcon.setImageResource(startIconId)
                binding.endIcon.setImageResource(endIconId)
                binding.root.setOnClickListener { onClick(settingItem) }
            }
        }
    }

    private class SettingItemDiffCallback : DiffUtil.ItemCallback<SettingItem>() {
        override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem.displayNameId == newItem.displayNameId
        }

        override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
            return oldItem == newItem
        }
    }
}

sealed class SettingItem {
    abstract val startIconId: Int
    abstract val displayNameId: Int
    val endIconId: Int = R.drawable.ic_forward

    data class SettingItemLanguage(
        override val startIconId: Int = R.drawable.ic_language,
        val item: Language = Language.English
    ) : SettingItem() {
        override val displayNameId: Int
            get() = item.displayedNameId
    }

    data class SettingItemOrientation(
        override val startIconId: Int = R.drawable.ic_orientation,
        val item: Orientation = Orientation.Portrait
    ) : SettingItem() {
        override val displayNameId: Int
            get() = item.displayedNameId
    }

    data class SettingItemTheme(
        override val startIconId: Int = R.drawable.ic_dark_mode,
        val item: Theme = Theme.Light
    ) : SettingItem() {
        override val displayNameId: Int
            get() = item.displayedNameId
    }
}