package com.dalao.yiban.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    private Boolean disable = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return disable ? false : super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return disable ? false : super.onTouchEvent(event);
    }

    /**
     * 取消ViewPager的左右滑动切换页面的效果
     * @param disable:When disable = true not work the scroll and when disble = false work the scroll
     */
    public void disableScroll(Boolean disable){
        this.disable = disable;
    }
}

