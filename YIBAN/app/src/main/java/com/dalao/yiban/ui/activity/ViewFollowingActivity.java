package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dalao.yiban.R;
import com.dalao.yiban.my_interface.FollowInterface;

public class ViewFollowingActivity extends BaseActivity implements FollowInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_following);
    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * 关注成功, 更新UI，数据库
     */
    @Override
    public void followSucceed() {
        //TODO:更新UI，数据库
    }

    /**
     * 取消关注成功, 更新UI，数据库
     */
    @Override
    public void unFollowSucceed() {
        //TODO:更新UI，数据库
    }

}
