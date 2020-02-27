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

        int pageButtonNum = a.getInt(R.styleable.PageNoSelector_pageButtonNum, 9);
        if (pageButtonNum < 7) pageButtonNum = 7; //最小为7
        if (pageButtonNum % 2 == 0) pageButtonNum++; //按钮数量必须为奇数

        for (int i = 0; i < pageButtonNum + 2; i++) {
            PageButtonKt pb;
            if (i == 0) pb = createPageButton(a, PageButtonKt.PRE_PAGE);
            else if (i == pageButtonNum + 1) pb = createPageButton(a, PageButtonKt.NEXT_PAGE);
            else pb = createPageButton(a, i);
            pbArray.add(pb);
            this.addView(pb);
        }

        a.recycle();

        showPageNo();

        //================TestBegin==================
        pbArray.get(1).setChecked(true);
        //=================TestEnd===================
    }

    private PageButtonKt createPageButton(TypedArray a, final int buttonIndex) {
        PageButtonKt pb = new PageButtonKt(getContext(), buttonIndex);

        int width = a.getDimensionPixelSize(R.styleable.PageNoSelector_pageButtonWidth, 0); //页面按钮大小
        Drawable background = a.getDrawable(R.styleable.PageNoSelector_pageButtonBackground);
        ColorStateList textColor = a.getColorStateList(R.styleable.PageNoSelector_pageButtonTextColor);
        int margin = a.getDimensionPixelSize(R.styleable.PageNoSelector_pageButtonMargin, 0);
        int padding = a.getDimensionPixelSize(R.styleable.PageNoSelector_pageButtonTextPadding, 0);

        pb.setButtonDrawable(null);
        pb.setGravity(Gravity.CENTER);
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(margin, 0, margin, 0);
        pb.setLayoutParams(lp);
        pb.setMinWidth(width);
        pb.setHeight(width);
        pb.setBackground(background);
        pb.setTextColor(textColor);
        pb.setPadding(padding, 0, padding, 0);
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
