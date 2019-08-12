package com.dalao.yiban.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.activity.EditNicknameActivity;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.custom.CustomPopWindow;

import static com.dalao.yiban.constant.MineConstant.FEMALE;
import static com.dalao.yiban.constant.MineConstant.FEMALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.MALE;
import static com.dalao.yiban.constant.MineConstant.MALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.SECRET;
import static com.dalao.yiban.constant.MineConstant.SECRET_TEXT;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    private MainActivity activity;

    private RelativeLayout mineNicknameLayout;

    private TextView mineNicknameText;

    private RelativeLayout mineSexLayout;

    private PopupWindow chooseSexPopWindow;

    private TextView mineSexText;

    private int sexSelected;

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
        mineNicknameLayout = (RelativeLayout) view.findViewById(R.id.mine_nickname_layout);
        mineNicknameText = (TextView) view.findViewById(R.id.mine_nickname_text);
        mineSexLayout = (RelativeLayout) view.findViewById(R.id.mine_sex_layout);
        mineSexText = (TextView) view.findViewById(R.id.mine_sex_text);

        // 设置点击事件
        mineNicknameLayout.setOnClickListener(this);
        mineSexLayout.setOnClickListener(this);

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
            case R.id.mine_nickname_layout:
                EditNicknameActivity.actionStart(activity, mineNicknameText.getText().toString());
                break;

            // 修改性别
            case R.id.mine_sex_layout:
                chooseSexPopWindow = CustomPopWindow.chooseSexPopWindow(v, activity, sexSelected);
                if (chooseSexPopWindow == null)
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

    /**
     * 设置sex
     * @param sex:MALE_TEXT, FEMALE_TEXT, SECRET_TEXT
     */
    public void setSex(String sex) {
        // TODO
        switch (sex) {
            case MALE_TEXT:
                sexSelected = MALE;
                break;
            case FEMALE_TEXT:
                sexSelected = FEMALE;
                break;
            case SECRET_TEXT:
                sexSelected = SECRET;
                break;
            default:
                break;
        }
        chooseSexPopWindow.dismiss();
        mineSexText.setText(sex);
    }

}
