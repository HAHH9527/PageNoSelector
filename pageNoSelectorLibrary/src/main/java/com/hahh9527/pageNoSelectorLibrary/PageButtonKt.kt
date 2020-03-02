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

class PageButtonKt : androidx.appcompat.widget.AppCompatTextView {

    var buttonType = PAGE_BUTTON_TYPE.PAGE_NO
        /**
         * @param buttonType 在设置按钮类型[PAGE_BUTTON_TYPE]时自动确定使用上一页/下一页的文本
         */
        private set(buttonType) {
            field = buttonType
            when (buttonType) {
                PAGE_BUTTON_TYPE.PRE_PAGE -> text = "<"
                PAGE_BUTTON_TYPE.NEXT_PAGE -> text = ">"
            }
        }

    var pageNo = 0
        /**
         * @param pageNo 在设置页码时显示在当前按钮上,特殊情况为隐藏了更多页码的按钮
         */
        set(pageNo) {
            if (buttonType == PAGE_BUTTON_TYPE.PRE_PAGE || buttonType == PAGE_BUTTON_TYPE.NEXT_PAGE) return
            field = pageNo
            text = if (field != PAGE_BUTTON_STATE.valueOf("HIDE_MORE").value) {
                "$field"
            } else {
                "..."
            }
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context, PAGE_BUTTON_TYPE.PAGE_NO, attrs
    )

    constructor(context: Context, buttonType: PAGE_BUTTON_TYPE = PAGE_BUTTON_TYPE.PAGE_NO) : this(
        context, buttonType, null
    )

    /**
     * @param context
     * @param buttonType 按钮类型[PAGE_BUTTON_TYPE]
     * @param attrs
     */
    constructor(
        context: Context,
        buttonType: PAGE_BUTTON_TYPE = PAGE_BUTTON_TYPE.PAGE_NO,
        attrs: AttributeSet? = null
    ) : super(context, attrs) {
        this.buttonType = buttonType
    }

    /**
     * 设置按钮的样式
     *
     * @param background 背景样式[Drawable]
     * @param textColor 字体颜色[ColorInt]
     */
    fun setStyle(background: Drawable?, @ColorInt textColor: Int) {
        this.background = background
        setTextColor(textColor)
    }

    /**
     * 按钮类型
     *
     * 上一页按钮[PRE_PAGE]
     * 下一页按钮[NEXT_PAGE]
     * 页码按钮[PAGE_NO]
     */
    enum class PAGE_BUTTON_TYPE {
        PRE_PAGE, NEXT_PAGE, PAGE_NO
    }

    /**
     * 按钮状态
     *
     * 未被选中[UNSELECTED]
     * 被选中[SELECTED]
     * 省略更多页码[HIDE_MORE]
     */
    enum class PAGE_BUTTON_STATE(val value: Int) {
        UNSELECTED(-1), SELECTED(-2), HIDE_MORE(-3)
    }
}
