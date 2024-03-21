package com.example.movierama.core.presentation.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.example.movierama.R
import com.example.movierama.databinding.CustomToolbarBinding

/**
 * CustomHomeToolbar is a specialized toolbar widget in Kotlin, extending ConstraintLayout.
 * It provides a customizable toolbar with features such as a title, menu button, and search functionality.
 * This class allows you to easily create and manage a toolbar for home screens, including options
 * for showing/hiding a search view and defining custom actions for the search bar.
 */
class CustomToolbar : ConstraintLayout {

    private var attrs: AttributeSet? = null

    private var _binding: CustomToolbarBinding? = null
    private val binding: CustomToolbarBinding
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

    // Initialization function called from constructors
    private fun init() {
        // Inflate the toolbar layout and bind it
        _binding = CustomToolbarBinding.inflate(LayoutInflater.from(context), this, true)

        // Obtain styled attributes defined in XML
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomToolbar,
            0, 0
        ).apply {
            // Get and set the title from attributes or use an empty string
            setTitle(getString(R.styleable.CustomToolbar_screenTitle) ?: "")
            binding.menuBtn.setImageResource(getResourceId(R.styleable.CustomToolbar_leftIconImage, R.drawable.ic_menu))
        }
    }

    fun setLeftIconListener(onClick: () -> Unit = {}) {
        binding.menuBtn.setOnClickListener { onClick() }
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

    // Set the title text for the toolbar
    fun setTitle(screenTitle: String) {
        binding.screenTitle.text = screenTitle
    }
}

