package com.dalao.yiban.ui.fragment;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        }
        else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 用户可见时进行的操作
     */
    protected abstract void onVisible();

    /**
     * 用户不可见时进行的操作
     */
    protected abstract void onInvisible();

}
