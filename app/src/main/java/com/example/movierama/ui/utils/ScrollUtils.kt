package gr.baseapps.baselistapp.ui.utils

import android.view.View
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView

fun ScrollView.addTitleElevation(title: View){
    setOnScrollChangeListener { scrollView, _, _, _, _ ->
        title.isSelected = scrollView.canScrollVertically(-1)
    }
}

fun RecyclerView.addTitleElevationAnimation(title: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            title.isSelected = recyclerView.canScrollVertically(-1)
        }
    })
}