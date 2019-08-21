package com.dalao.yiban.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalao.yiban.R;

public class ChooseLoginWayFragment extends BaseFragment {


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
        return inflater.inflate(R.layout.fragment_choose_login_way, container, false);
    }

    /**
     * 用户可见时进行的操作
     */
    @Override
    protected void onVisible() {

    }

}
