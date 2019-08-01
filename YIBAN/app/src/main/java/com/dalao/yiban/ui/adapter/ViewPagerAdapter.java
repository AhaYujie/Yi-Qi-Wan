package com.dalao.yiban.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    /**
     * 首页，社区，消息，我的 4个Fragment
     */
    private List<Fragment> fragmentList;

    public void setList(List<Fragment> list) {
        this.fragmentList = list;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0;
    }

}
