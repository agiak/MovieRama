package com.example.myutils

import android.view.View
import android.widget.ScrollView
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
