package com.example.myutils

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
            moveUpButton.isVisible = recyclerView.canScrollVertically(-1)
        }
    })
}
