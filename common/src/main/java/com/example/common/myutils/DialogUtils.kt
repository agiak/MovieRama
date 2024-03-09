package com.example.common.myutils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.common.R

/**
 * Shows a [Dialog] with a generic layout on top of this Activity.
 *
 * @param title An optional title on top of layout.
 * @param drawableId An optional image between the title and the message.
 * @param message An optional message at the center.
 * @param optionalButton A label for an optional button.
 * @param mandatoryButton A label for a mandatory button.
 * @param optionalAction Optional button's action.
 * @param mandatoryAction Mandatory button's action.
 */
fun showDialog(
    context: Context,
    title: String? = null,
    drawableId: Int? = null,
    message: String? = null,
    optionalButton: String? = null,
    mandatoryButton: String?,
    optionalAction: () -> Unit = {},
    mandatoryAction: () -> Unit = {},
    isCancelable: Boolean = false
): Dialog {
    return Dialog(context).apply {
        setContentView(R.layout.dialog_base)
        title?.let {
            findViewById<TextView>(R.id.dialogTitle).apply {
                isVisible = true
                text = it
            }
        }
        drawableId?.let {
            findViewById<ImageView>(R.id.dialogImage).setImageResource(it)
        }
        message?.let {
            findViewById<TextView>(R.id.dialogMessage).apply {
                isVisible = true
                text = it
            }
        }
        optionalButton?.let {
            findViewById<Button>(R.id.optionalButton).apply {
                isVisible = true
                text = it
                setOnClickListener {
                    dismiss()
                    optionalAction()
                }
            }
        }
        findViewById<Button>(R.id.mandatoryButton).apply {
            text = mandatoryButton
            setOnClickListener {
                dismiss()
                mandatoryAction()
            }
        }
        setCancelable(isCancelable)
        show()
        addDim()
        buildLayout()
    }
}

fun Dialog.addDim(dimAmount: Float = 0.8f) {
    window?.apply {
        attributes.dimAmount = dimAmount
        addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }
}

fun Dialog.buildLayout() {
    window?.apply {
        setLayout(if (context.resources.configuration.smallestScreenWidthDp < 600)
            ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setGravity(Gravity.CENTER_VERTICAL)
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_dialog))
    }
}
