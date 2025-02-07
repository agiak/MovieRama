package com.example.movierama.ui.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.example.movierama.R
import com.example.movierama.ui.utils.DEFAULT_DEBOUNCE_TIME
import com.example.movierama.ui.utils.DebounceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
    Custom EditText view with debounce functionality.
    Injects DebounceUtil using Dagger's @Inject annotation.

    Debounce time can be set through the custom attribute 'debounceTime' in XML.
    Use setAfterDebounceListener to set the functionality you want to be executed after debounce method.
 */
@AndroidEntryPoint
class DebounceEditText : AppCompatEditText {

    @Inject
    lateinit var debounceUtil: DebounceUtil

    private var attrs: AttributeSet? = null

    constructor(
        context: Context
    ) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        this.attrs = attrs
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        init()
    }

    private fun init() {
        // Retrieve custom attribute 'debounceTime' from XML
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DebounceEditText,
            0, 0
        ).apply {

            try {
                // Get debounceTime value and set it in DebounceUtil
                val debounceTime = getInt(R.styleable.DebounceEditText_debounceTime, DEFAULT_DEBOUNCE_TIME)
                debounceUtil.setDebounceTime(debounceTime)
            } finally {
                recycle()
            }
        }
    }

    /**
     * Sets the actions which will be executed after user type a text
     *
     * @param doAfterDebounce = Action which will be executed after the debounce delay.
     * Like a search or filtering action. This action is required since is the main action
     *
     * @param doBeforeDebounce = Action which will be executed before the debounce delay and right after
     * user has stopped typing. This can be a loading action. Bu default is set an empty action
     *
     * */
    fun setActions(doBeforeDebounce: (text: String) -> Unit = {}, doAfterDebounce: (text: String) -> Unit) {
        doAfterTextChanged { text ->
            with(text.toString()) {
                doBeforeDebounce(this)
                debounceUtil.debounce { doAfterDebounce(this) }
            }
        }
    }

}
