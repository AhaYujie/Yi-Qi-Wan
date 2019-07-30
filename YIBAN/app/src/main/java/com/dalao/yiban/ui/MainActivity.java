package com.dalao.yiban.ui;

import android.os.Bundle;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.custom.CustomViewPager;
import com.dalao.yiban.ui.fragment.CommunityFragment;
import com.dalao.yiban.ui.fragment.HomeFragment;
import com.dalao.yiban.ui.fragment.MessageFragment;
import com.dalao.yiban.ui.fragment.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CustomViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavigationView;

    // 首页，社区，消息，我的
    private List<Fragment> fragmentList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // 测试
                    viewPager.setCurrentItem(0);
                    // TODO
                    return true;
                case R.id.navigation_community:
                    // 测试
                    viewPager.setCurrentItem(1);
                    // TODO
                    return true;
                case R.id.navigation_message:
                    // 测试
                    viewPager.setCurrentItem(2);
                    // TODO
                    return true;
                case R.id.navigation_mine:
                    // 测试
                    viewPager.setCurrentItem(3);
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

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentList = initFragmentList();
        viewPagerAdapter.setList(fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        // 取消ViewPager的左右滑动切换界面动画
        viewPager.disableScroll(true);
    }

    private List<Fragment> initFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(CommunityFragment.newInstance());
        fragmentList.add(MessageFragment.newInstance());
        fragmentList.add(MineFragment.newInstance());
        return fragmentList;
    }

}
