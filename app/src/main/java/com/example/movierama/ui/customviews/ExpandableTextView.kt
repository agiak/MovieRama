package com.example.movierama.ui.customviews

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import com.example.myutils.fadeIn
import com.example.myutils.fadeOut

// TODO fix a bug when expanded text is not expanded all
class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var maxLinesCollapsed: Int = 3
    private var originalText: CharSequence? = null
    private var isCollapsed: Boolean = true
    private var moreText = " More"
    private var lessText = " Less"

    private val moreButton: Button

    init {
        // ... Read attributes and other initialization

        // Create the "More" button
        moreButton = Button(context).apply {
            text = moreText
            background = null // Remove the underline
            visibility = View.GONE
            setOnClickListener {
                toggleText()
            }
        }

        // Enable clickable links within the TextView
        movementMethod = LinkMovementMethod.getInstance()
        // Disable ellipsis (text truncation) for this view
        ellipsize = null
        // Set a click listener to toggle text expansion/collapse
        setOnClickListener {
            toggleText()
        }
    }

    // Toggle between collapsed and expanded text
    private fun toggleText() {
        isCollapsed = !isCollapsed
        animateText()
    }

    // Animate the text and button visibility
    private fun animateText() {
        val targetText = if (isCollapsed) {
            getCollapsedText()
        } else {
            getFullText()
        }

        if (isCollapsed) {
            moreButton.fadeOut() // Fade out the button
        } else {
            moreButton.fadeIn() // Fade in the button
        }

        // Fade out the text and set the new text when animation ends
        fadeOut {
            text = targetText
            fadeIn()
        }
    }

    // Set the number of collapsed lines
    fun setCollapsedLines(lines: Int) {
        maxLinesCollapsed = lines
    }

    // Set the full text for the view
    fun setFullText(text: CharSequence) {
        originalText = text
        updateText()
    }

    // Update the text displayed in the TextView based on collapse state
    private fun updateText() {
        if (originalText.isNullOrEmpty()) return

        val layout = layout ?: return
        val lineCount = layout.lineCount

        text = if (lineCount > maxLinesCollapsed) {
            val newText = if (isCollapsed) {
                getCollapsedText()
            } else {
                getFullText()
            }

            newText
        } else {
            originalText
        }
    }

    // Generate collapsed text with "More" link
    private fun getCollapsedText(): CharSequence {
        val layout = layout ?: return originalText ?: ""
        val endPos = layout.getLineEnd(maxLinesCollapsed - 1)
        val truncatedText = originalText?.subSequence(0, endPos)
        return SpannableString("$truncatedText...  $moreText").apply {
            setClickableSpan(moreText)
        }
    }

    // Generate expanded text with "Less" link
    private fun getFullText(): CharSequence {
        val fullText = originalText?.toString() ?: ""
        return SpannableString("$fullText  $lessText").apply {
            setClickableSpan(lessText)
        }
    }

    // Add clickable span to the text
    private fun SpannableString.setClickableSpan(clickableText: String) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                toggleText()
            }
        }

        val startIndex = this.indexOf(clickableText)
        val endIndex = startIndex + clickableText.length

        setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}
