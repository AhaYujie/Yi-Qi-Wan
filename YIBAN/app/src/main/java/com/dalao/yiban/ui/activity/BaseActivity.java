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

    /**
     * 创建菜单栏
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_more_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
