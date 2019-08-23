package com.dalao.yiban.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.activity.LoginActivity;

public class ChooseLoginWayFragment extends BaseFragment {

    private Button chooseYibanLoginWay;

    private Button chooseVisitorWay;

    private LoginActivity loginActivity;

    private View view;

    public ChooseLoginWayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChooseLoginWayFragment.
     */
    public static ChooseLoginWayFragment newInstance() {
        return new ChooseLoginWayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_choose_login_way, container, false);

        // 初始化控件
        chooseYibanLoginWay = (Button) view.findViewById(R.id.choose_yiban_login_way);
        chooseVisitorWay = (Button) view.findViewById(R.id.choose_visitor_way);
        loginActivity = (LoginActivity) getActivity();

        onVisible();

        return view;
    }

    /**
     * 用户可见时进行的操作
     */
    @Override
    protected void onVisible() {
        // 设置点击事件
        if (isVisible && view != null) {
            chooseVisitorWay.setOnClickListener(loginActivity);
            chooseYibanLoginWay.setOnClickListener(loginActivity);
        }
    }

}
