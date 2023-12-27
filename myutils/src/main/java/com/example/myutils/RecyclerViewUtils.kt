package com.example.myutils

import android.content.Context
import android.view.View
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Disables scroll to a recyclerView
 * */
fun RecyclerView.disableScroll(context: Context){
    val lm = object : LinearLayoutManager(context) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }
    layoutManager = lm
}


/**
 * Scrolls to the top of the list
 * */
fun RecyclerView.scrollToUp() = this.layoutManager?.smoothScrollToPosition(this, null,0)

/**
 * When RecyclerView is scrolled up shows a button which on click scrolls to the top of the list
 *
 * @param moveUpButton = represents button
 *
 * Note that you need implement the on click listener to the button separately.
 * */
fun RecyclerView.showUpButtonListener(moveUpButton: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if(recyclerView.canScrollVertically(-1)) {
                // in case button is already visible we don't want to trigger the animation again
                if (moveUpButton.isVisible) return
                moveUpButton.fadeIn()
            } else {
                // in case button is already hidden we don't want to trigger the animation again
                if (moveUpButton.isVisible.not()) return
                moveUpButton.fadeOut()
            }
        }
    })
}


/**
 * When RecyclerView is scrolled up adds elevation to the given titleView
 *
 * @param titleView = represents the title view
 *
 * Note that you need to set stateListAnimator with @animator/elevation_title and a background to your
 * titleView
 * */
fun RecyclerView.addTitleElevationAnimation(titleView: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            titleView.isSelected = recyclerView.canScrollVertically(-1)
        }
    })
}

/**
 * When RecyclerView is scrolled up adds elevation to the given titleView
 *
 * @param titleView = represents the title view
 *
 * Note that you need to set stateListAnimator with @animator/elevation_title and a background to your
 * titleView
 * */
fun RecyclerView.addTitleElevationAnimation(titleViews: List<View>) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val isScrolled = recyclerView.canScrollVertically(-1)
            titleViews.forEach {
                it.isSelected = isScrolled
            }
        }
    })
}

/**
 * Hide keyboard if visible and user scrolls the recyclerView
 * */
fun RecyclerView.hideKeyboardOnScroll() {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                hideKeyboard()
            }
        }
    })
}