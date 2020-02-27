/*
 * MIT License
 *
 * Copyright (c) 2020 HAHH9527(昏暗槐花)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
