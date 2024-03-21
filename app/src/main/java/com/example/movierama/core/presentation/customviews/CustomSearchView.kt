package com.example.movierama.core.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.common.myutils.showKeyboard
import com.example.movierama.R
import com.example.movierama.databinding.CustomSearchViewBinding

/**
 * CustomHomeToolbar is a specialized toolbar widget in Kotlin, extending ConstraintLayout.
 * It provides a customizable toolbar with features such as a title, menu button, and search functionality.
 * This class allows you to easily create and manage a toolbar for home screens, including options
 * for showing/hiding a search view and defining custom actions for the search bar.
 */
class CustomSearchView : ConstraintLayout {

    private var attrs: AttributeSet? = null

    private var _binding: CustomSearchViewBinding? = null
    private val binding: CustomSearchViewBinding
        get() = _binding!!

    // Constructor for creating the toolbar with no attributes
    constructor(
        context: Context,
    ) : super(context) {
        init()
    }

    // Constructor for creating the toolbar with attributes
    constructor(
        context: Context,
        attrs: AttributeSet?,
    ) : super(context, attrs) {
        this.attrs = attrs
        init()
    }

    // Constructor for creating the toolbar with attributes and style
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        init()
    }

    private fun init() {
        _binding = CustomSearchViewBinding.inflate(LayoutInflater.from(context), this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomToolbar,
            0, 0
        ).apply {
            val hint = getString(R.styleable.CustomSearchView_searchHint)
                ?: context.getString(R.string.movies_search_hint)
            setHint(hint)
        }

        // Initialize views within the toolbar
        initViews()
    }

    private fun setHint(hint: String) {
        binding.searchBar.hint = hint
    }

    // Initialize views and their behavior
    private fun initViews() {
        // Clear text in the search bar when the clear button is clicked
        binding.clearTextBtn.setOnClickListener {
            binding.searchBar.text?.clear()
        }

        // Show or hide the clear text button based on search bar text changes
        binding.searchBar.apply {
            doOnTextChanged { text, _, _, _ ->
                binding.clearTextBtn.isVisible = text.isNullOrEmpty().not()
            }
        }

        // Show the search view and configure its behavior
        binding.searchBtn.setOnClickListener {
            binding.searchBar.apply {
                isVisible = true
                requestFocus()
                showKeyboard()
            }
        }
    }

    // Set custom actions for the search view
    fun setSearchViewActions(debounceViewActions: DebounceViewActions) {
        binding.searchBar.setActions(debounceViewActions)
    }

    fun clearText() {
        binding.searchBar.text?.clear()
    }

    fun hideSearchBar() {
        binding.searchBar.isVisible = false
        binding.clearTextBtn.isVisible = false
    }
}

