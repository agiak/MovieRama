package com.example.common.myutils

import androidx.viewpager2.widget.ViewPager2


fun ViewPager2.moveNext() {
    this.currentItem = this.currentItem + 1
}

fun ViewPager2.movePrevious() {
    this.currentItem = this.currentItem - 1
}
