package com.hahh9527.pageNoSelectorLibrary

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class PageButtonKt(
    context: Context,
    buttonIndex: Int,
    attrs: AttributeSet? = null
) : AppCompatRadioButton(context, attrs) {

    companion object {
        const val PRE_PAGE = -1
        const val NEXT_PAGE = -2
    }

    var buttonIndex = 0
        private set

    init {
        this.buttonIndex = buttonIndex
        text = when (buttonIndex) {
            PRE_PAGE -> "<"
            NEXT_PAGE -> ">"
            else -> ""
        }
        setOnClickListener { isChecked = true }
    }

    constructor(context: Context, buttonIndex: Int) : this(context, buttonIndex, null)

//    constructor(context: Context, buttonIndex: Int, attrs: AttributeSet? = null) : this(
//        context, buttonIndex, attrs, 0
//    )

//    constructor(
//        context: Context, buttonIndex: Int, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//    ) : this(context, buttonIndex, attrs, defStyleAttr, 0)

//    constructor(
//        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
//    ) : super(context, attrs, defStyleAttr, defStyleRes) {
//
//    }

}
