package com.example.myutils

import android.view.View
import android.widget.ScrollView
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

fun ScrollView.addTitleElevation(title: View){
    setOnScrollChangeListener { scrollView, _, _, _, _ ->
        title.isSelected = scrollView.canScrollVertically(-1)
    }
}

// Constants for height percentages used in calculations
private const val HALF_HEIGHT_PERCENTAGE = 0.5
private const val SEVENTY_HEIGHT_PERCENTAGE = 0.7

/**
 * Adds a scroll listener to the AppBarLayout.
 *
 * @param onCollapse Callback triggered when the AppBarLayout is fully collapsed.
 * @param onFullExpand Callback triggered when the AppBarLayout is fully expanded.
 * @param onScrolling Callback triggered when the AppBarLayout is being scrolled, providing the current offset.
 * @param onHalfExpand Callback triggered when the AppBarLayout is half-expanded (or more).
 * @param onAlmostExpand Callback triggered when the AppBarLayout is almost fully
 *                       expanded (reaches 70% of total scroll range).
 */
fun AppBarLayout.addScrollListener(
    onCollapse: () -> Unit = {},
    onFullExpand: () -> Unit = {},
    onScrolling: (offset: Int) -> Unit = {},
    onHalfExpand: () -> Unit = {},
    onAlmostExpand: () -> Unit = {}
) {
    addOnOffsetChangedListener { appBarLayout, verticalOffset ->
        when {
            // Check if the AppBarLayout is fully collapsed
            abs(verticalOffset) >= appBarLayout.totalScrollRange -> onCollapse()
            // Check if the AppBarLayout is fully expanded
            verticalOffset == 0 -> onFullExpand()
            // Check if the AppBarLayout is almost fully expanded (70% of total scroll range)
            abs(verticalOffset) >= (appBarLayout.totalScrollRange * SEVENTY_HEIGHT_PERCENTAGE) -> onAlmostExpand()
            // Check if the AppBarLayout is at least half-expanded
            abs(verticalOffset) >= (appBarLayout.totalScrollRange * HALF_HEIGHT_PERCENTAGE) -> onHalfExpand()
            // If none of the above conditions are met, the AppBarLayout is being scrolled, trigger onScrolling callback
            else -> onScrolling(verticalOffset)
        }
    }
}
