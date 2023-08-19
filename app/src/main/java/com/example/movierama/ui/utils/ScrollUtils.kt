package com.example.movierama.ui.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Provides functionality for pagination at RecyclerView component
 *
 * @param visibleThreshold = presents the number of items that should be remaining before triggering
 * the load more action. By default, it's set to 5, but you can adjust it according to your needs.
 *
 * @param loadMoreAction = lambda function to me executed on loading state.
 * */
fun RecyclerView.addOnLoadMoreListener(
    visibleThreshold: Int = 2,
    loadMoreAction: () -> Unit
) {
    val layoutManager = layoutManager as? LinearLayoutManager ?: return

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val totalItemCount = layoutManager.itemCount
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            val needsToLoadMore = totalItemCount <= lastVisibleItemPosition + visibleThreshold
            if (needsToLoadMore) {
                loadMoreAction.invoke()
            }
        }
    })
}
