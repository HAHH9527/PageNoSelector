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
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt

class PageButtonKt(
    context: Context,
    buttonType: PAGE_BUTTON_TYPE = PAGE_BUTTON_TYPE.PAGE_NO,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    constructor(context: Context, buttonType: PAGE_BUTTON_TYPE = PAGE_BUTTON_TYPE.PAGE_NO) : this(
        context,
        buttonType,
        null
    )

    companion object {
//        const val TYPE.PRE_PAGE = -1
//        const val TYPE.NEXT_PAGE = -2
//        const val TYPE.PAGE_NO = 0
    }

    var buttonType = PAGE_BUTTON_TYPE.PAGE_NO
        private set(value) {
            field = value
            when (buttonType) {
                PAGE_BUTTON_TYPE.PRE_PAGE -> text = "<"
                PAGE_BUTTON_TYPE.NEXT_PAGE -> text = ">"
            }
        }

    var pageNo = 0
        set(value) {
            if (buttonType == PAGE_BUTTON_TYPE.PRE_PAGE || buttonType == PAGE_BUTTON_TYPE.NEXT_PAGE) return
            field = value
            text = if (field != PAGE_BUTTON_STATE.valueOf("HIDE_MORE").value) {
                "$field"
            } else {
                "..."
            }
            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        }

    init {
        this.buttonType = buttonType
        when (buttonType) {
            PAGE_BUTTON_TYPE.PRE_PAGE -> text = "<"
            PAGE_BUTTON_TYPE.NEXT_PAGE -> text = ">"
            else -> {
//                setOnClickListener {
//                    isChecked = true
//                }
            }
        }
    }

    fun setStyle(background: Drawable?, @ColorInt textColor: Int) {
        this.background = background
        setTextColor(textColor)
    }

    enum class PAGE_BUTTON_TYPE {
        PRE_PAGE, NEXT_PAGE, PAGE_NO
    }

    enum class PAGE_BUTTON_STATE(val value: Int) {
        UNSELECTED(-1), SELECTED(-2), HIDE_MORE(-3)
    }
}

//enum class PAGE_BUTTON_TYPE {
//    PRE_PAGE, NEXT_PAGE, PAGE_NO
//}
//
//enum class PAGE_BUTTON_STATE(val value: Int) {
//    UNSELECTED(-1), SELECTED(-2), HIDE_MORE(-3)
//}
