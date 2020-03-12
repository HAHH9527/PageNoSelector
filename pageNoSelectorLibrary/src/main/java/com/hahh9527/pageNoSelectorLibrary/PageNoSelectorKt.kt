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
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import kotlin.collections.ArrayList
import com.hahh9527.pageNoSelectorLibrary.PageButtonKt.PAGE_BUTTON_TYPE
import com.hahh9527.pageNoSelectorLibrary.PageButtonKt.PAGE_BUTTON_STATE

class PageNoSelectorKt : LinearLayout {

    private val pageNoButtonArray = ArrayList<PageButtonKt>()

    /**
     * 按钮数量最小为7,并一定为奇数,如是偶数则+1
     */
    var mPageButtonNum: Int = 0
        private set(value) {
            field = when {
                //最小为7
                value <= 7 -> 7
                //按钮数量必须为奇数
                value % 2 == 0 -> value + 1
                else -> value
            }
        }

    fun setPageButtonNum(int: Int) {
        mPageButtonNum = int
        selectedPageNo = 1
        selectedPageNoIndex = 0
        reloadView()
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

    var mPageCount: Int = 0
        private set

    fun setPageCount(int: Int) {
        mPageCount = int
        selectedPageNo = 1
        selectedPageNoIndex = 0
        reloadView()
    }

    lateinit var prePageButton: PageButtonKt
    lateinit var nextPageButton: PageButtonKt

    private var selectedPageNo = 1
    private var selectedPageNoIndex = 0

    //====================回调 begin====================
    /**
     * 翻页回调
     */
    var pageChangeCallBack: (pageNo: Int) -> Unit = {}
    //=====================回调 end=====================

    //====================constructor begin====================
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
    }
    //=====================constructor end=====================

    /**
     * 重新加载显示界面
     */
    fun reloadView() {
        removeAllViews()
        loadButton()
    }

    /**
     * 加载资源
     */
    private fun initAttrs(a: TypedArray) {
        mPageButtonNum = a.getInt(R.styleable.PageNoSelectorKt_pageButtonNum, 0)
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
        mPageCount = a.getInt(R.styleable.PageNoSelectorKt_pageCount, 0)
    }

    /**
     * 加载页码按钮
     * 使用[createPageButton]创建按钮
     */
    private fun loadButton() {
        prePageButton = createPageButton(PAGE_BUTTON_TYPE.PRE_PAGE)
        nextPageButton = createPageButton(PAGE_BUTTON_TYPE.NEXT_PAGE)
        if (pageNoButtonArray.isNotEmpty()) pageNoButtonArray.clear()
        //创建页码按钮
        for (i in 1..mPageButtonNum) {
            pageNoButtonArray.add(createPageButton())
        }
        //添加显示页码按钮
        addView(prePageButton)
        for (pb in pageNoButtonArray) {
            addView(pb)
        }
        addView(nextPageButton)

        updateSelectedPageNo(selectedPageNo)
    }

    /**
     * 创建按钮
     *
     * @param buttonType [PAGE_BUTTON_TYPE]
     */
    private fun createPageButton(buttonType: PAGE_BUTTON_TYPE = PAGE_BUTTON_TYPE.PAGE_NO): PageButtonKt {
        val pb = PageButtonKt(context, buttonType).apply {
            gravity = Gravity.CENTER
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.setMargins(pageButtonMargin, 0, pageButtonMargin, 0)
            layoutParams = lp
            minWidth = pageButtonWidth
            minHeight = pageButtonWidth
            setStyle(pageButtonBackgroundUnSelected, pageButtonTextColorUnselected)
            setPadding(pageButtonTextPadding, 0, pageButtonTextPadding, 0)
        }
        when (buttonType) {
            PAGE_BUTTON_TYPE.PAGE_NO -> {
                pb.setOnClickListener { v ->
                    val pageNo = (v as PageButtonKt).pageNo
                    updateSelectedPageNo(pageNo)
                }
            }
            PAGE_BUTTON_TYPE.PRE_PAGE -> {
                pb.setOnClickListener {
                    if (selectedPageNo <= 1) return@setOnClickListener
                    updateSelectedPageNo(selectedPageNo - 1)
                }
            }
            PAGE_BUTTON_TYPE.NEXT_PAGE -> {
                pb.setOnClickListener {
                    if (selectedPageNo >= mPageCount) return@setOnClickListener
                    updateSelectedPageNo(selectedPageNo + 1)
                }
            }
        }
        return pb
    }

    /**
     * 更新选中的页码按钮
     *
     * @param selectedPageNo 被选中的页码
     */
    private fun updateSelectedPageNo(selectedPageNo: Int) {
        if (selectedPageNo <= 0) return

        if (mPageCount <= mPageButtonNum) {
            //最大页码小于按钮数
            this.selectedPageNo = selectedPageNo
            selectedPageNoIndex = selectedPageNo - 1
            for (i in 0 until pageNoButtonArray.size) {
                if (i < mPageCount) {
                    pageNoButtonArray[i].apply {
                        visibility = View.VISIBLE
                        pageNo = i + 1
                        setState(
                            if (selectedPageNoIndex == i) PAGE_BUTTON_STATE.SELECTED
                            else PAGE_BUTTON_STATE.UNSELECTED
                        )
                    }
                } else {
                    pageNoButtonArray[i].apply {
                        visibility = View.GONE
                        pageNo = 0
                        setState(PAGE_BUTTON_STATE.UNSELECTED)
                    }
                }
            }
        } else {
            val midIndex = mPageButtonNum / 2

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
                selectedPageNo >= mPageCount - midIndex -> {
                    hideBeforePageNo = true
                    hideAfterPageNO = false
                    (mPageButtonNum - 1) - (mPageCount - selectedPageNo)
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
            pageNoButtonArray[pageNoButtonArray.size - 1].pageNo = mPageCount

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
                    mPageCount - 1
                }
            }

            val midPageNo = when {
                selectedPageNoIndex > midIndex -> mPageCount - midIndex
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
        pageChangeCallBack.invoke(selectedPageNo)
    }

    /**
     * 设置按钮状态
     *
     * @param state 不同的状态[PAGE_BUTTON_STATE]会产生不同的样式改变
     */
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