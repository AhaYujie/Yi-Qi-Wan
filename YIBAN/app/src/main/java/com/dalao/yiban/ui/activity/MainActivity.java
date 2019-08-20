package com.dalao.yiban.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.ui.adapter.ViewPagerAdapter;
import com.dalao.yiban.ui.custom.CustomProgressDialog;
import com.dalao.yiban.ui.custom.CustomViewPager;
import com.dalao.yiban.ui.fragment.CommunityFragment;
import com.dalao.yiban.ui.fragment.HomeFragment;
import com.dalao.yiban.ui.fragment.MessageFragment;
import com.dalao.yiban.ui.fragment.MineFragment;
import com.dalao.yiban.util.SDCardUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zhihu.matisse.Matisse;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dalao.yiban.constant.MineConstant.FEMALE;
import static com.dalao.yiban.constant.MineConstant.FEMALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.MALE;
import static com.dalao.yiban.constant.MineConstant.MALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.SECRET;
import static com.dalao.yiban.constant.MineConstant.SECRET_TEXT;

public class MainActivity extends BaseActivity {

    public String userId = "2"; // test

    private CustomViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    private BottomNavigationView bottomNavigationView;

    public CustomProgressDialog customProgressBar;

    public MineFragment mineFragment;

    private HomeFragment homeFragment;

    private CommunityFragment communityFragment;

    // 首页，社区，消息，我的
    private List<Fragment> fragmentList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(HomeConstant.SELECT_HOME);
                    return true;
                case R.id.navigation_community:
                    viewPager.setCurrentItem(HomeConstant.SELECT_COMMUNITY);
                    return true;
                case R.id.navigation_mine:
                    viewPager.setCurrentItem(HomeConstant.SELECT_MINE);
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
        viewPager.setOffscreenPageLimit(2);
    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 选择性别男
            case R.id.choose_male_layout:
                mineFragment.setSex(MALE);
                break;

            // 选择性别女
            case R.id.choose_female_layout:
                mineFragment.setSex(FEMALE);
                break;

            // 选择性别保密
            case R.id.choose_secret_layout:
                mineFragment.setSex(SECRET);
                break;
        }
    }

    private List<Fragment> initFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        homeFragment = HomeFragment.newInstance();
        communityFragment = CommunityFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        fragmentList.add(homeFragment);
        fragmentList.add(communityFragment);
        fragmentList.add(mineFragment);
        return fragmentList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 修改昵称
            case MineConstant.EDIT_USER_NICKNAME_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mineFragment.setNickName(data.getStringExtra(MineConstant.USER_NICKNAME));
                }
                break;

            // 修改头像
            case MineConstant.EDIT_USER_FACE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mineFragment.handleSelectedImage(data);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            // 更改头像请求权限
            case HomeConstant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mineFragment.selectImage();
                }
                else {
                    Toast.makeText(this, HintConstant.PERMISSION_REFUSE, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
