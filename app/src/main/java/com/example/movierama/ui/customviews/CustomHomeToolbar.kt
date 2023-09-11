package com.example.movierama.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import com.example.movierama.R
import com.example.movierama.databinding.LayoutHomeToolbarBinding
import com.example.movierama.ui.utils.DEFAULT_DEBOUNCE_TIME
import com.example.myutils.showKeyboard

/**
 * CustomHomeToolbar is a specialized toolbar widget in Kotlin, extending ConstraintLayout.
 * It provides a customizable toolbar with features such as a title, menu button, and search functionality.
 * This class allows you to easily create and manage a toolbar for home screens, including options
 * for showing/hiding a search view and defining custom actions for the search bar.
 */
class CustomHomeToolbar : ConstraintLayout {

    private var attrs: AttributeSet? = null

    private var _binding: LayoutHomeToolbarBinding? = null
    private val binding: LayoutHomeToolbarBinding
        get() = _binding!!

    // Constructor for creating the toolbar with no attributes
    constructor(
        context: Context
    ) : super(context) {
        init()
    }

    // Constructor for creating the toolbar with attributes
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        this.attrs = attrs
        init()
    }

    // Constructor for creating the toolbar with attributes and style
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        init()
    }

    // Initialization function called from constructors
    private fun init() {
        // Inflate the toolbar layout and bind it
        _binding = LayoutHomeToolbarBinding.inflate(LayoutInflater.from(context), this, true)

        // Obtain styled attributes defined in XML
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomHomeToolbar,
            0, 0
        ).apply {
            // Get and set the title from attributes or use an empty string
            setTitle(getString(R.styleable.CustomHomeToolbar_screenTitle) ?: "")

            // Set the visibility of the menu button based on attributes
            binding.menuBtn.isVisible =
                getBoolean(R.styleable.CustomHomeToolbar_showMenuButton, true)

            // If the search view is supported, configure it
            if (getBoolean(R.styleable.CustomHomeToolbar_supportsSearchView, false)) {
                showSearchView()
                binding.searchBar.setDebounceTime(
                    getInt(
                        R.styleable.CustomHomeToolbar_searchDebounceTime,
                        DEFAULT_DEBOUNCE_TIME
                    )
                )
            }
        }

        // Initialize views within the toolbar
        initViews()
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
    }

    // Set a click listener for the menu button to open/close a DrawerLayout
    fun setMenuListener(drawerLayout: DrawerLayout) {
        binding.menuBtn.setOnClickListener {
            if (drawerLayout.isOpen) {
                drawerLayout.close()
            } else {
                drawerLayout.open()
            }
        }
    }

    // Show the search view and configure its behavior
    fun showSearchView() {
        binding.searchBtn.apply {
            isVisible = true
            binding.searchBar.isVisible = false

            // Show the search bar, focus it, and show the keyboard when the search button is clicked
            setOnClickListener {
                binding.searchBar.apply {
                    isVisible = true
                    requestFocus()
                    showKeyboard()
                }
            }
        }
    }

    // Set custom actions for the search view
    fun setSearchViewActions(debounceViewActions: DebounceViewActions) {
        binding.searchBar.setActions(debounceViewActions)
    }

    // Set the title text for the toolbar
    fun setTitle(screenTitle: String) {
        binding.screenTitle.text = screenTitle
    }
}

