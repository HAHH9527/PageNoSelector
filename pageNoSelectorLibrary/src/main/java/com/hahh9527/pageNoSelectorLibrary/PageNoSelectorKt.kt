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
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import kotlin.collections.ArrayList
import com.hahh9527.pageNoSelectorLibrary.PageButtonKt.PAGE_BUTTON_TYPE
import com.hahh9527.pageNoSelectorLibrary.PageButtonKt.PAGE_BUTTON_STATE

class PageNoSelectorKt : LinearLayout {

    private val pageNoButtonArray = ArrayList<PageButtonKt>()

    var pageButtonNum: Int = 0
        private set

    fun setPageButtonNum(value: Int) {
        pageButtonNum = value
        removeAllViews()
        loadButton()
    }

    var pageButtonBackgroundSelected: Drawable? = null

    var pageButtonBackgroundUnSelected: Drawable? = null

    @ColorInt
    var pageButtonTextColorSelected: Int = 0

    @ColorInt
    var pageButtonTextColorUnselected: Int = 0

    var pageButtonWidth: Int = 0

    var pageButtonMargin: Int = 0

    var pageButtonTextPadding: Int = 0

    var pageCount: Int = 0

    lateinit var prePageButton: PageButtonKt
    lateinit var nextPageButton: PageButtonKt

    private var selectedPageNo = 1
    private var selectedPageNoIndex = 0

    //==================constructor begin==================
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs, R.attr.defaultPageNoSelectorKtStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context, attrs, defStyleAttr, R.style.DefaultPageNoSelectorKtStyle
    )

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.PageNoSelectorKt, defStyleAttr, defStyleRes
        )
        initAttrs(a)
        a.recycle()

        loadButton()
        updateSelectedPageNo(1)
    }
    //===================constructor end===================

    /**
     * 加载资源
     */
    private fun initAttrs(a: TypedArray) {
        pageButtonNum = a.getInt(R.styleable.PageNoSelectorKt_pageButtonNum, 0).run {
            when {
                //最小为7
                this < 7 -> return@run 7
                //按钮数量必须为奇数
                this % 2 == 0 -> return@run this + 1
                else -> return@run this
            }
        }
        pageButtonBackgroundSelected =
            a.getDrawable(R.styleable.PageNoSelectorKt_pageButtonBackgroundSelected)
        pageButtonBackgroundUnSelected =
            a.getDrawable(R.styleable.PageNoSelectorKt_pageButtonBackgroundUnselected)
        pageButtonTextColorSelected =
            a.getColor(R.styleable.PageNoSelectorKt_pageButtonTextColorSelected, 0)
        pageButtonTextColorUnselected =
            a.getColor(R.styleable.PageNoSelectorKt_pageButtonTextColorUnselected, 0)
        pageButtonWidth =
            a.getDimensionPixelSize(R.styleable.PageNoSelectorKt_pageButtonWidth, 0) //页面按钮大小
        pageButtonMargin = a.getDimensionPixelSize(R.styleable.PageNoSelectorKt_pageButtonMargin, 0)
        pageButtonTextPadding =
            a.getDimensionPixelSize(R.styleable.PageNoSelectorKt_pageButtonTextPadding, 0)
        pageCount = a.getInt(R.styleable.PageNoSelectorKt_pageCount, 0)
    }

    /**
     * 加载页码按钮
     */
    private fun loadButton() {
        prePageButton = createPageButton(PAGE_BUTTON_TYPE.PRE_PAGE)
        nextPageButton = createPageButton(PAGE_BUTTON_TYPE.NEXT_PAGE)
        //创建页码按钮
        for (i in 1..pageButtonNum) {
            pageNoButtonArray.add(createPageButton())
        }
        //添加显示页码按钮
        addView(prePageButton)
        for (pb in pageNoButtonArray) {
            addView(pb)
        }
        addView(nextPageButton)
    }

    private fun createPageButton(buttonType: PAGE_BUTTON_TYPE = PAGE_BUTTON_TYPE.PAGE_NO): PageButtonKt {
        val pb = PageButtonKt(context, buttonType)
        pb.gravity = Gravity.CENTER
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(pageButtonMargin, 0, pageButtonMargin, 0)
        pb.layoutParams = lp
        pb.minWidth = pageButtonWidth
        pb.minHeight = pageButtonWidth
        pb.setStyle(pageButtonBackgroundUnSelected, pageButtonTextColorUnselected)
        pb.setPadding(pageButtonTextPadding, 0, pageButtonTextPadding, 0)
        if (buttonType == PAGE_BUTTON_TYPE.PAGE_NO) {
            pb.setOnClickListener { v ->
                val pageNo = (v as PageButtonKt).pageNo
                updateSelectedPageNo(pageNo)
            }
        }
        return pb
    }

    private fun updateSelectedPageNo(selectedPageNo: Int) {
        // TODO 最大页码小于按钮数情况还未做
        val midIndex = pageButtonNum / 2

        this.selectedPageNo = selectedPageNo

        val afterSelectedPageNoIndex = this.selectedPageNoIndex
        var hideBeforePageNo = false
        var hideAfterPageNO = false

        selectedPageNoIndex = when {
            //不需要前省略 但 需要后省略
            selectedPageNo <= 1 + midIndex -> {
                hideBeforePageNo = false
                hideAfterPageNO = true
                selectedPageNo - 1
            }
            //不需要后省略 但 需要前省略
            selectedPageNo >= pageCount - midIndex -> {
                hideBeforePageNo = true
                hideAfterPageNO = false
                (pageButtonNum - 1) - (pageCount - selectedPageNo)
            }
            //前后省略都需要
            else -> {
                hideBeforePageNo = true
                hideAfterPageNO = true
                midIndex
            }
        }

        pageNoButtonArray[afterSelectedPageNoIndex].setState(PAGE_BUTTON_STATE.UNSELECTED)
        pageNoButtonArray[selectedPageNoIndex].setState(PAGE_BUTTON_STATE.SELECTED)

        pageNoButtonArray[0].pageNo = 1
        pageNoButtonArray[pageNoButtonArray.size - 1].pageNo = pageCount

        pageNoButtonArray[1].apply {
            pageNo = if (hideBeforePageNo) {
                setState(PAGE_BUTTON_STATE.HIDE_MORE)
                PAGE_BUTTON_STATE.valueOf("HIDE_MORE").value
            } else {
                setState(if (selectedPageNoIndex == 1) PAGE_BUTTON_STATE.SELECTED else PAGE_BUTTON_STATE.UNSELECTED)
                2
            }
        }

        pageNoButtonArray[pageNoButtonArray.size - 2].apply {
            pageNo = if (hideAfterPageNO) {
                setState(PAGE_BUTTON_STATE.HIDE_MORE)
                PAGE_BUTTON_STATE.valueOf("HIDE_MORE").value
            } else {
                setState(if (selectedPageNoIndex == pageNoButtonArray.size - 2) PAGE_BUTTON_STATE.SELECTED else PAGE_BUTTON_STATE.UNSELECTED)
                pageCount - 1
            }
        }

        val midPageNo = when {
            selectedPageNoIndex > midIndex -> pageCount - midIndex
            selectedPageNoIndex < midIndex -> 1 + midIndex
            else -> selectedPageNo
        }

        pageNoButtonArray[midIndex].pageNo = midPageNo
        //从中间选中的页码往两边显示
        for (i in 1..midIndex - 2) {
            pageNoButtonArray[midIndex - i].pageNo = midPageNo - i
            pageNoButtonArray[midIndex + i].pageNo = midPageNo + i
        }
    }

    private fun PageButtonKt.setState(state: PAGE_BUTTON_STATE) {
        when (state) {
            PAGE_BUTTON_STATE.UNSELECTED -> {
                this.setStyle(pageButtonBackgroundUnSelected, pageButtonTextColorUnselected)
                this.isClickable = true
            }
            PAGE_BUTTON_STATE.SELECTED -> {
                this.setStyle(pageButtonBackgroundSelected, pageButtonTextColorSelected)
                this.isClickable = false
            }
            PAGE_BUTTON_STATE.HIDE_MORE -> {
                this.setStyle(null, pageButtonTextColorUnselected)
                this.pageNo = -1
                this.isClickable = false
            }
        }
    }

}