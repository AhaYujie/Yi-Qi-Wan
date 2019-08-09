package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.dalao.yiban.R;
import com.dalao.yiban.util.SystemUiUtil;

/**
 * 竞赛，活动，博客内容基类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏为白底黑字
        SystemUiUtil.changeStatusBarToWhite(this);
    }

}
