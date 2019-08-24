package com.dalao.yiban.ui.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public abstract class BaseFragment extends Fragment {

    private List<Call> callList;

    protected boolean isVisible;

    public List<Call> getCallList() {
        return callList;
    }

    public void setCallList(List<Call> callList) {
        this.callList = callList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        callList = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

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
    private void onInvisible() {
        cancelCall();
    }

    /**
     * 取消所有网络请求
     */
    private void cancelCall() {
        if (callList != null) {
            for (Call call : callList) {
                if (!call.isCanceled()) {
                    call.cancel();
                    Log.d("yujie", "frag : " + call.toString());
                }
            }
        }
    }

}
