package com.example.movierama.ui.utils

import android.view.View
import android.widget.ScrollView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun ScrollView.addTitleElevation(title: View){
    setOnScrollChangeListener { scrollView, _, _, _, _ ->
        title.isSelected = scrollView.canScrollVertically(-1)
    }
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
 * Provides functionality for pagination at RecyclerView component
 *
 * @param visibleThreshold = presents the number of items that should be remaining before triggering
 * the load more action. By default, it's set to 5, but you can adjust it according to your needs.
 *
 * @param loadMoreAction = lambda function to me executed on loading state.
 * */
fun RecyclerView.addOnLoadMoreListener(
    visibleThreshold: Int = 5,
    loadMoreAction: () -> Unit
) {
    val layoutManager = layoutManager as? LinearLayoutManager ?: return

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val totalItemCount = layoutManager.itemCount
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            if (totalItemCount <= lastVisibleItemPosition + visibleThreshold) {
                loadMoreAction.invoke()
            }
        }
    })
}

/**
 * When RecyclerView is scrolled up shows a button which on click scrolls to the top of the list
 *
 * @param moveUpButton = represents button
 *
 * Note that you need implement the on click listener to the button separately
 * */
fun RecyclerView.showUpButtonListener(moveUpButton: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            moveUpButton.isVisible = recyclerView.canScrollVertically(-1)
        }
    })
}

/**
 * Scrolls to the top of the list
 * */
fun RecyclerView.scrollToUp() = this.layoutManager?.smoothScrollToPosition(this, null,0)