package com.dalao.yiban.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.db.User;
import com.dalao.yiban.ui.adapter.ViewPagerAdapter;
import com.dalao.yiban.ui.custom.CustomProgressDialog;
import com.dalao.yiban.ui.custom.CustomViewPager;
import com.dalao.yiban.ui.fragment.CommunityFragment;
import com.dalao.yiban.ui.fragment.HomeFragment;
import com.dalao.yiban.ui.fragment.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.dalao.yiban.constant.MineConstant.FEMALE;
import static com.dalao.yiban.constant.MineConstant.MALE;
import static com.dalao.yiban.constant.MineConstant.SECRET;

public class MainActivity extends BaseActivity {

    // public String userId;

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
        viewPager = (CustomViewPager) findViewById(R.id.home_view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        customProgressBar = new CustomProgressDialog(this, HintConstant.LOADING);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentList = initFragmentList();
        viewPagerAdapter.setList(fragmentList);
        // 取消ViewPager的左右滑动切换界面动画
        viewPager.disableScroll(true);
        viewPager.setOffscreenPageLimit(2);

        // 从本地获取用户id，若无则跳转到登录界面
        List<User> userList = DataSupport.findAll(User.class);
        if (userList != null && userList.size() != 0) {
            userId = userList.get(0).getSeverId();
            viewPager.setAdapter(viewPagerAdapter);
        }
        else {
            LoginActivity.actionStart(this);
        }
        Log.d("yujie", "main create userid : " + userId);

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

            // 退出登录
            case R.id.sign_out_popwindow_confirm_button:
                mineFragment.signOut();
                break;
            default:
                break;
        }
    }

    /**
     * 按顺序初始化首页，社区，我的碎片
     * @return :
     */
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
            case HomeConstant.EDIT_USER_NICKNAME_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mineFragment.updateNicknameUI(data.getStringExtra(MineConstant.USER_NICKNAME));
                }
                break;

            // 修改头像
            case HomeConstant.EDIT_USER_FACE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mineFragment.handleSelectedImage(data);
                }
                break;

            // 登录
            case HomeConstant.LOGIN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    viewPager.setAdapter(viewPagerAdapter);
                    bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu()
                            .getItem(HomeConstant.SELECT_HOME).getItemId());
                    viewPager.setCurrentItem(HomeConstant.SELECT_HOME);
                    Toast.makeText(this, HintConstant.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
                    userId = data.getStringExtra(HomeConstant.USER_ID);
                    User user = new User();
                    user.setSeverId(userId);
                    user.setSearchList(null);
                    user.save();
                    Log.d("yujie", "main login userid : " + userId);
                    List<User> userList = DataSupport.findAll(User.class);
                    Log.d("yujie", "main user num : " + userList.size());
                }
                else {
                    finish();
                }
                break;

            // 创建博客
            case HomeConstant.CREATE_BLOG_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, HintConstant.CREATE_BLOG_SUCCESS, Toast.LENGTH_SHORT).show();
                    communityFragment.requestDataFromServer(true);
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
            case HomeConstant.WRITE_EXTERNAL_STORAGE_AND_CAMERA_REQUEST_CODE:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
