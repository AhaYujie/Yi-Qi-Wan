package com.dalao.yiban.ui.activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.LoginConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.ui.adapter.ViewPagerAdapter;
import com.dalao.yiban.ui.custom.CustomProgressDialog;
import com.dalao.yiban.ui.custom.CustomViewPager;
import com.dalao.yiban.ui.fragment.ChooseLoginWayFragment;
import com.dalao.yiban.ui.fragment.LoginFragment;
import com.dalao.yiban.ui.fragment.MineFragment;
import com.dalao.yiban.ui.fragment.RegisterFragment;
import com.dalao.yiban.ui.fragment.YibanLoginFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    public CustomViewPager customViewPager;

    private List<Fragment> fragmentList;

    private ChooseLoginWayFragment chooseLoginWayFragment;

    private YibanLoginFragment yibanLoginFragment;

    public CustomProgressDialog customProgressDialog;

    /**
     * 启动 LoginActivity
     * @param context:
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        ((MainActivity) context).startActivityForResult(intent, HomeConstant.LOGIN_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化控件
        customViewPager = (CustomViewPager) findViewById(R.id.login_view_pager);
        customProgressDialog = new CustomProgressDialog(LoginActivity.this);
        initFragmentList();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setList(fragmentList);
        customViewPager.setAdapter(viewPagerAdapter);

        // 取消ViewPager的左右滑动切换界面动画
        customViewPager.disableScroll(true);
        customViewPager.setOffscreenPageLimit(0);
        // 设置chooseLoginWayFragment为主页面
        customViewPager.setCurrentItem(LoginConstant.SELECT_CHOOSE);

    }

    /**
     * 按顺序初始化选择登录方式，易班登录fragment
     */
    private void initFragmentList() {
        chooseLoginWayFragment = ChooseLoginWayFragment.newInstance();
        yibanLoginFragment = YibanLoginFragment.newInstance();
        fragmentList = new ArrayList<>();
        fragmentList.add(chooseLoginWayFragment);
        fragmentList.add(yibanLoginFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 游客试用方式
            case R.id.choose_visitor_way:
                Intent intent = new Intent();
                intent.putExtra(HomeConstant.USER_ID, HomeConstant.VISITOR_USER_ID);
                setResult(RESULT_OK, intent);
                finish();
                break;

            // 易班登录方式
            case R.id.choose_yiban_login_way:
                customViewPager.setCurrentItem(LoginConstant.SELECT_YIBAN);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // 若在易班登录的fragment按下返回键，则回到选择登录方式界面
        if (customViewPager.getCurrentItem() == LoginConstant.SELECT_YIBAN) {
            customViewPager.setCurrentItem(LoginConstant.SELECT_CHOOSE);
        }
        // 在选择登录方式界面按下返回键，返回首页结束MainActivity
        else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * 带着用户id回到主页面
     * @param userId：用户id
     */
    public void backToHome(String userId) {
        Intent intent = new Intent();
        intent.putExtra(HomeConstant.USER_ID, userId);
        setResult(RESULT_OK, intent);
        customProgressDialog.closeProgressBar();
        finish();
    }

}

