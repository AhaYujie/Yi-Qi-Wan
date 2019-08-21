package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.ui.adapter.ViewPagerAdapter;
import com.dalao.yiban.ui.custom.CustomViewPager;
import com.dalao.yiban.ui.fragment.ChooseLoginWayFragment;
import com.dalao.yiban.ui.fragment.LoginWayFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private CustomViewPager customViewPager;

    private List<Fragment> fragmentList;

    private ChooseLoginWayFragment chooseLoginWayFragment;

    private LoginWayFragment loginWayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化控件
        customViewPager = (CustomViewPager) findViewById(R.id.login_view_pager);
        initFragmentList();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setList(fragmentList);
        customViewPager.setAdapter(viewPagerAdapter);

        // 取消ViewPager的左右滑动切换界面动画
        customViewPager.disableScroll(true);
        customViewPager.setOffscreenPageLimit(0);

        customViewPager.setCurrentItem(1);  //TODO:test
    }

    private void initFragmentList() {
        fragmentList = new ArrayList<>();
        chooseLoginWayFragment = ChooseLoginWayFragment.newInstance();
        loginWayFragment = LoginWayFragment.newInstance();
        fragmentList.add(chooseLoginWayFragment);
        fragmentList.add(loginWayFragment);
    }

    @Override
    public void onClick(View v) {
        
    }

}

