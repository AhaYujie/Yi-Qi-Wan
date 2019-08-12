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

import static com.dalao.yiban.constant.MineConstant.FEMALE;
import static com.dalao.yiban.constant.MineConstant.FEMALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.MALE;
import static com.dalao.yiban.constant.MineConstant.MALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.SECRET;
import static com.dalao.yiban.constant.MineConstant.SECRET_TEXT;

public class MainActivity extends BaseActivity {

    private CustomViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    private BottomNavigationView bottomNavigationView;

    public CustomProgressDialog customProgressBar;

    private MineFragment mineFragment;

    private HomeFragment homeFragment;

    private CommunityFragment communityFragment;

    private MessageFragment messageFragment;

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
        switch (v.getId()) {
            // 选择性别男
            case R.id.choose_male_layout:
                mineFragment.setSex(MALE_TEXT);
                break;

            // 选择性别女
            case R.id.choose_female_layout:
                mineFragment.setSex(FEMALE_TEXT);
                break;

            // 选择性别男
            case R.id.choose_secret_layout:
                mineFragment.setSex(SECRET_TEXT);
                break;
        }
    }

    private List<Fragment> initFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        homeFragment = HomeFragment.newInstance();
        communityFragment = CommunityFragment.newInstance();
        messageFragment = MessageFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        fragmentList.add(homeFragment);
        fragmentList.add(communityFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(mineFragment);
        return fragmentList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MineConstant.EDIT_USER_NICKNAME_REQUEST_CODE:
                // 保存修改昵称
                if (resultCode == RESULT_OK) {
                    mineFragment.setNickName(data.getStringExtra(MineConstant.USER_NICKNAME));
                }
                break;
            default:
                break;
        }
    }

}
