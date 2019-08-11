package com.dalao.yiban.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.ui.adapter.ViewPagerAdapter;
import com.dalao.yiban.ui.custom.CustomProgressDialog;
import com.dalao.yiban.ui.custom.CustomViewPager;
import com.dalao.yiban.ui.fragment.CommunityFragment;
import com.dalao.yiban.ui.fragment.HomeFragment;
import com.dalao.yiban.ui.fragment.MessageFragment;
import com.dalao.yiban.ui.fragment.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private CustomViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    private BottomNavigationView bottomNavigationView;

    public CustomProgressDialog customProgressBar;

    // 首页，社区，消息，我的
    private List<Fragment> fragmentList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // 测试
                    viewPager.setCurrentItem(HomeConstant.SELECT_HOME);
                    // TODO
                    return true;
                case R.id.navigation_community:
                    // 测试
                    viewPager.setCurrentItem(HomeConstant.SELECT_COMMUNITY);
                    // TODO
                    return true;
                case R.id.navigation_message:
                    // 测试
                    viewPager.setCurrentItem(HomeConstant.SELECT_MESSAGE);
                    // TODO
                    return true;
                case R.id.navigation_mine:
                    // 测试
                    viewPager.setCurrentItem(HomeConstant.SELECT_MINE);
                    // TODO
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        bottomNavigationView = findViewById(R.id.nav_view);
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        customProgressBar = new CustomProgressDialog(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentList = initFragmentList();
        viewPagerAdapter.setList(fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        // 取消ViewPager的左右滑动切换界面动画
        viewPager.disableScroll(true);
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        //TODO
    }

    private List<Fragment> initFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(CommunityFragment.newInstance());
        fragmentList.add(MessageFragment.newInstance());
        fragmentList.add(MineFragment.newInstance());
        return fragmentList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MineConstant.EDIT_USER_NICKNAME_REQUEST_CODE:
                // 保存修改
                if (resultCode == RESULT_OK) {
                    MineFragment mineFragment = (MineFragment) fragmentList.get(HomeConstant.SELECT_MINE);
                    mineFragment.setNickName(data.getStringExtra(MineConstant.USER_NICKNAME));
                }
                break;
            default:
                break;
        }
    }

}
