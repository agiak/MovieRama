package com.example.common.myutils

import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams

private const val ANIM_FAST = 400L
private const val ANIM_NORMAL = 1000L

// To animate view slide out from left to right
fun View.slideToRight() {
    val animate = TranslateAnimation(0F, width.toFloat(), 0F, 0F)
    animate.duration = ANIM_NORMAL
    animate.fillAfter = true
    startAnimation(animate)
    isVisible = true
}

// To animate view slide out from right to left
fun View.slideToLeft() {
    val animate = TranslateAnimation(0F, -width.toFloat(), 0F, 0F)
    animate.duration = ANIM_NORMAL
    animate.fillAfter = true
    startAnimation(animate)
    isVisible = true
}


fun View.slideToRightShow(){
    val transition = Slide(Gravity.END)
    transition.apply {
        duration = ANIM_FAST
        addTarget(this@slideToRightShow)
    }
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    isVisible = true
}

fun View.slideToLeftShow(){
    val transition = Slide(Gravity.START)
    transition.apply {
        duration = ANIM_FAST
        addTarget(this@slideToLeftShow)
    }
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    isVisible = true
}

fun View.slideFromBottom(){
    val transition = Slide(Gravity.BOTTOM)
    transition.apply {
        duration = ANIM_FAST
        addTarget(this@slideFromBottom)
    }
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    isVisible = true
}

fun View.getDimension(dimenId: Int): Long = context.resources.getDimension(dimenId).toLong()

// Set the top margin of a View
fun View.setMarginTop(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = margin
    }
}

// Set the bottom margin of a View
fun View.setMarginBottom(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        bottomMargin = margin
    }
}

// Set the left margin of a View
fun View.setMarginLeft(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = margin
    }
}

// Set the right margin of a View
fun View.setMarginRight(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        rightMargin = margin
    }
}

// Set the vertical margins (top and bottom) of a View
fun View.setMarginVertical(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = margin
        bottomMargin = margin
    }
}

// Set the horizontal margins (left and right) of a View
fun View.setMarginHorizontal(margin: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = margin
        rightMargin = margin
    }
}

// Extension function to apply fade-in animation to a View
fun View.fadeIn(duration: Long = ANIM_FAST, onAnimationEnd: () -> Unit = {}) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction(onAnimationEnd)
}

// Extension function to apply fade-out animation to a View
fun View.fadeOut(duration: Long = ANIM_FAST, onAnimationEnd: () -> Unit = {}) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            visibility = View.GONE
            onAnimationEnd()
        }
}

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isVisible = false
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}