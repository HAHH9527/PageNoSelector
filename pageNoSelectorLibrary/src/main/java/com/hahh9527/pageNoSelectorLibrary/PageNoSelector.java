/*
 * MIT License
 *
 * Copyright (c) 2020 昏暗槐花
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

package com.hahh9527.pageNoSelectorLibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PageNoSelector extends RadioGroup {

    private ArrayList<PageButtonKt> pbArray = new ArrayList<PageButtonKt>();
    private PageButtonKt prePageButton;
    private PageButtonKt nextPageButton;
    private int pageButtonNum;
    private int pageButtonWidth;
    private Drawable pageButtonBackground;
    private ColorStateList pageButtonTextColor;
    private int pageButtonMargin;
    private int pageButtonTextPadding;

    public PageNoSelector(Context context) {
        this(context, null);
    }

    public PageNoSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.defaultPageNoSelectorStyle);
    }

    public PageNoSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.DefaultPageNoSelectorStyle);
    }

    public PageNoSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageNoSelector, defStyleAttr, defStyleRes);

        //================TestBegin==================
        //=================TestEnd===================
        int orientation = a.getInt(R.styleable.PageNoSelector_android_orientation, HORIZONTAL);
        setOrientation(orientation);

        pageButtonNum = a.getInt(R.styleable.PageNoSelector_pageButtonNum, 9);
        if (pageButtonNum < 7) pageButtonNum = 7; //最小为7
        if (pageButtonNum % 2 == 0) pageButtonNum++; //按钮数量必须为奇数

        pageButtonWidth = a.getDimensionPixelSize(R.styleable.PageNoSelector_pageButtonWidth, 0); //页面按钮大小
        pageButtonBackground = a.getDrawable(R.styleable.PageNoSelector_pageButtonBackground);
        pageButtonTextColor = a.getColorStateList(R.styleable.PageNoSelector_pageButtonTextColor);
        pageButtonMargin = a.getDimensionPixelSize(R.styleable.PageNoSelector_pageButtonMargin, 0);
        pageButtonTextPadding = a.getDimensionPixelSize(R.styleable.PageNoSelector_pageButtonTextPadding, 0);

        a.recycle();

        //创建页码按钮
        prePageButton = createPageButton(PageButtonKt.PRE_PAGE);
        nextPageButton = createPageButton(PageButtonKt.NEXT_PAGE);
        for (int i = 1; i <= pageButtonNum; i++) pbArray.add(createPageButton(i));

        //添加显示页码按钮
        addView(prePageButton);
        for (PageButtonKt pb : pbArray) addView(pb);
        addView(nextPageButton);

        showPageNo();

        //================TestBegin==================
        pbArray.get(1).setChecked(true);
        //=================TestEnd===================
    }

    private PageButtonKt createPageButton(final int buttonIndex) {
        PageButtonKt pb = new PageButtonKt(getContext(), buttonIndex);

        pb.setButtonDrawable(null);
        pb.setGravity(Gravity.CENTER);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(pageButtonMargin, 0, pageButtonMargin, 0);
        pb.setLayoutParams(lp);
        pb.setMinWidth(pageButtonWidth);
        pb.setHeight(pageButtonWidth);
        pb.setBackground(pageButtonBackground);
        pb.setTextColor(pageButtonTextColor);
        pb.setPadding(pageButtonTextPadding, 0, pageButtonTextPadding, 0);
        pb.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((PageButtonKt) buttonView).getButtonIndex();
                }
            }
        });

        return pb;
    }

    private void showPageNo() {
        for (int i = 1; i < pbArray.size() - 1; i++) {
            PageButtonKt pb = pbArray.get(i);
            pb.setText(String.format("%d", i));
        }
    }
}
