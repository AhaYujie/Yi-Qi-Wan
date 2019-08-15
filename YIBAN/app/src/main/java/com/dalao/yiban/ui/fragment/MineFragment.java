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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.gson.UserGson;
import com.dalao.yiban.ui.activity.EditNicknameActivity;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.dalao.yiban.constant.HomeConstant.SELECT_ACTIVITY;
import static com.dalao.yiban.constant.HomeConstant.SELECT_CONTEST;
import static com.dalao.yiban.constant.MineConstant.FEMALE;
import static com.dalao.yiban.constant.MineConstant.FEMALE_RESPONSE;
import static com.dalao.yiban.constant.MineConstant.FEMALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.MALE;
import static com.dalao.yiban.constant.MineConstant.MALE_RESPONSE;
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

    private RelativeLayout mineInfoFaceLayout;

    private CircleImageView mineFace;

    private TextView mineUsernameText;

    private RelativeLayout mineBlogLayout;

    private RelativeLayout mineCollectLayout;

    private RelativeLayout mineFollowingLayout;

    private int sexSelected;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MineFragment.
     */
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
        mineInfoFaceLayout = (RelativeLayout) view.findViewById(R.id.mine_info_face_layout);
        mineFace = (CircleImageView) view.findViewById(R.id.mine_face);
        mineUsernameText = (TextView) view.findViewById(R.id.mine_username_text);
        mineBlogLayout = (RelativeLayout) view.findViewById(R.id.mine_blog_layout);
        mineCollectLayout = (RelativeLayout) view.findViewById(R.id.mine_collect_layout);
        mineFollowingLayout = (RelativeLayout) view.findViewById(R.id.mine_following_layout);

        // 设置点击事件
        mineInfoFaceLayout.setOnClickListener(this);
        mineNicknameLayout.setOnClickListener(this);
        mineSexLayout.setOnClickListener(this);
        mineBlogLayout.setOnClickListener(this);
        mineCollectLayout.setOnClickListener(this);
        mineFollowingLayout.setOnClickListener(this);

        // 如果fragment可见，请求服务器获取数据
        onVisible();

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

            // 修改头像
            case R.id.mine_info_face_layout:
                //TODO:修改头像，请求服务器
                break;

            // 查看我的博客
            case R.id.mine_blog_layout:
                //TODO:启动查看我的博客活动
                break;

            // 查看收藏
            case R.id.mine_collect_layout:
                //TODO:启动查看收藏活动
                break;

            // 查看我关注的人
            case R.id.mine_following_layout:
                //TODO:启动查看我关注的人活动
                break;
            default:
                break;
        }
    }

    /**
     * 用户可见时进行的操作
     */
    protected void onVisible() {
        // 请求服务器获取数据
        if (isVisible && view != null)
            requestDataFromServer();
    }

    /**
     * 从服务器获取列表数据并刷新UI
     */
    private void requestDataFromServer() {

        FormBody formBody = new FormBody.Builder()
                .add(ServerPostDataConstant.USER_INFO_USER_ID, activity.userId)
                .build();

        HttpUtil.sendHttpPost(ServerUrlConstant.USER_INFO_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(),
                                HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                MineFragment.this.getCallList().add(call);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                MineFragment.this.getCallList().add(call);
                if (response.body() != null) {
                    final String responseText = response.body().string();
                    Log.d("yujie", responseText);
                    final UserGson userGson = JsonUtil.handleUserResponse(responseText);
                    if (userGson != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUserInfoUI(userGson);
                            }
                        });
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(),
                                        HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 更新用户信息UI
     * @param userGson:解析后的用户信息
     */
    private void updateUserInfoUI(UserGson userGson)  {
        mineUsernameText.setText(userGson.getUser().getUsername());
        mineNicknameText.setText(userGson.getUser().getNickname());
        Log.d("yujie", "" + userGson.getUser().getSex());
        if (userGson.getUser().getAvatar() == null)
            Log.d("yujie", "is null");
        Log.d("yujie", "" + userGson.getUser().getLevel());
        if (userGson.getUser().getSex() == MALE_RESPONSE) {
            mineSexText.setText(MALE_TEXT);
        }
        else if (userGson.getUser().getSex() == FEMALE_RESPONSE) {
            mineSexText.setText(FEMALE_TEXT);
        }
        Glide.with(this)
                .load(ServerUrlConstant.SERVER_URI + userGson.getUser().getAvatar())
                .into(mineFace);
    }

    /**
     * 设置nickname
     * @param nickName:
     */
    public void setNickName(String nickName) {
        // TODO:请求服务器和保存到数据库
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
