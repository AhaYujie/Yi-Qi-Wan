package com.dalao.yiban.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.ui.activity.EditNicknameActivity;
import com.dalao.yiban.ui.activity.MainActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    private MainActivity activity;

    private RelativeLayout mineNickname;

    private TextView mineNicknameText;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mine, container, false);

        // 初始化控件
        activity = (MainActivity) getActivity();
        mineNickname = (RelativeLayout) view.findViewById(R.id.mine_nickname);
        mineNicknameText = (TextView) view.findViewById(R.id.mine_nickname_text);

        // 设置点击事件
        mineNickname.setOnClickListener(this);

        return view;
    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 修改昵称
            case R.id.mine_nickname:
                EditNicknameActivity.actionStart(activity, mineNicknameText.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 用户可见时进行的操作
     */
    protected void onVisible() {
        //TODO
    }

    /**
     * 用户不可见时进行的操作
     */
    protected void onInvisible() {
        //TODO
    }

    /**
     * 设置nickname
     * @param nickName:
     */
    public void setNickName(String nickName) {
        // TODO
        mineNicknameText.setText(nickName);
    }

}
