package com.dalao.yiban.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.db.User;
import com.dalao.yiban.gson.EditUserInfoGson;
import com.dalao.yiban.gson.UserInfoGson;
import com.dalao.yiban.ui.activity.CollectionActivity;
import com.dalao.yiban.ui.activity.CreateBlogActivity;
import com.dalao.yiban.ui.activity.EditNicknameActivity;
import com.dalao.yiban.ui.activity.LoginActivity;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.activity.MyBlogActivity;
import com.dalao.yiban.ui.activity.MyStarActivity;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.util.CommonUtil;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.ImageUtils;
import com.dalao.yiban.util.JsonUtil;
import com.dalao.yiban.util.SDCardUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dalao.yiban.constant.MineConstant.FEMALE;
import static com.dalao.yiban.constant.MineConstant.FEMALE_RESPONSE;
import static com.dalao.yiban.constant.MineConstant.FEMALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.MALE;
import static com.dalao.yiban.constant.MineConstant.MALE_RESPONSE;
import static com.dalao.yiban.constant.MineConstant.MALE_TEXT;
import static com.dalao.yiban.constant.MineConstant.SECRET;
import static com.dalao.yiban.constant.MineConstant.SECRET_RESPONSE;
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

    private TextView mineSchoolText;

    private int sexSelected;

    private LinearLayout mineVisitorModeLayout;

    private LinearLayout mineLoginModeLayout;

    private Button mineLoginButton;

    private Button mineSignOutButton;

    private PopupWindow signOutPopWindow;

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
        mineSchoolText = (TextView) view.findViewById(R.id.mine_school_text);
        mineVisitorModeLayout = (LinearLayout) view.findViewById(R.id.mine_visitor_mode_layout);
        mineLoginModeLayout = (LinearLayout) view.findViewById(R.id.mine_login_mode_layout);
        mineLoginButton = (Button) view.findViewById(R.id.mine_login_button);
        mineSignOutButton = (Button) view.findViewById(R.id.mine_sign_out_button);

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
                EditNicknameActivity.actionStart(activity, mineNicknameText.getText().toString(),
                        activity.userId);
                break;

            // 修改性别
            case R.id.mine_sex_layout:
                chooseSexPopWindow = CustomPopWindow.chooseSexPopWindow(v, activity, sexSelected);
                break;

            // 修改头像
            case R.id.mine_info_face_layout:
                // 未授权
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.
                        WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.
                            CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                            HomeConstant.WRITE_EXTERNAL_STORAGE_AND_CAMERA_REQUEST_CODE);
                }
                // 授权则选择图片
                else {
                    selectImage();
                }
                break;

            // 查看我的博客
            case R.id.mine_blog_layout:
                MyBlogActivity.actionStart(activity, activity.userId);
                break;

            // 查看收藏
            case R.id.mine_collect_layout:
                CollectionActivity.actionStart(activity, activity.userId);
                break;

            // 查看我关注的人
            case R.id.mine_following_layout:
                MyStarActivity.actionStart(activity, activity.userId);
                break;

            // 易班登录
            case R.id.mine_login_button:
                DataSupport.deleteAll(User.class, "severId = ?", activity.userId);
                LoginActivity.actionStart(activity);
                break;

            // 退出登录
            case R.id.mine_sign_out_button:
                signOutPopWindow = CustomPopWindow.signOutPopWindow(v, activity);
            default:
                break;
        }
    }

    /**
     * 用户可见时进行的操作
     */
    protected void onVisible() {
        if (isVisible && view != null) {
            // 游客模式
            if (activity.userId.equals(HomeConstant.VISITOR_USER_ID)) {
                mineVisitorModeLayout.setVisibility(View.VISIBLE);
                mineLoginModeLayout.setVisibility(View.GONE);
                mineLoginButton.setOnClickListener(this);
            } else {
                mineVisitorModeLayout.setVisibility(View.GONE);
                mineLoginModeLayout.setVisibility(View.VISIBLE);
                requestDataFromServer();
            }
        }
    }

    /**
     * 传输头像给服务器
     */
    private void postFileToServer(final File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart(ServerPostDataConstant.EDIT_USER_FACE_IMAGE, file.getName(),
                        RequestBody.create(null, file))
                .addFormDataPart(ServerPostDataConstant.USER_INFO_USER_ID, activity.userId);

        HttpUtil.sendHttpPostFile(ServerUrlConstant.EDIT_USER_INFO_URI, builder, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                MineFragment.this.getCallList().add(call);
                e.printStackTrace();
                SDCardUtil.deleteFile(file.getPath());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(),
                                HintConstant.EDIT_USER_INFO_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                MineFragment.this.getCallList().add(call);
                try {
                    activity.getCallList().add(call);
                    String responseText = response.body().string();
                    EditUserInfoGson editUserInfoGson = JsonUtil.handleEditUserInfoResponse(responseText);
                    if (editUserInfoGson.getMsg().equals(MineConstant.EDIT_USER_INFO_SUCCESS)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                requestDataFromServer();
                                SDCardUtil.deleteFile(file.getPath());
                                Toast.makeText(MyApplication.getContext(),
                                        HintConstant.EDIT_USER_INFO_SUCCESS, Toast.LENGTH_SHORT).show();
                                SDCardUtil.deleteFile(file.getPath());
                            }
                        });
                    }
                    else if (editUserInfoGson.getMsg().equals(MineConstant.EDIT_USER_INFO_ERROR)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(),
                                        HintConstant.EDIT_USER_INFO_ERROR, Toast.LENGTH_SHORT).show();
                                SDCardUtil.deleteFile(file.getPath());
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.EDIT_USER_INFO_ERROR, Toast.LENGTH_SHORT).show();
                            SDCardUtil.deleteFile(file.getPath());
                        }
                    });
                }
            }
        });
    }

    /**
     * 从服务器获取列表数据并刷新UI
     */
    private void requestDataFromServer() {

        FormBody formBody = new FormBody.Builder()
                .add(ServerPostDataConstant.USER_INFO_USER_ID, activity.userId)
                .build();

        Call call = HttpUtil.sendHttpPost(ServerUrlConstant.USER_INFO_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    final String responseText = response.body().string();
                    final UserInfoGson userInfoGson = JsonUtil.handleUserResponse(responseText);
                    if (userInfoGson != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUserInfoUI(userInfoGson);
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
        MineFragment.this.getCallList().add(call);
    }

    /**
     * 更新用户信息UI
     * @param userInfoGson:解析后的用户信息
     */
    private void updateUserInfoUI(UserInfoGson userInfoGson)  {
        // 设置点击事件
        mineInfoFaceLayout.setOnClickListener(this);
        mineNicknameLayout.setOnClickListener(this);
        mineSexLayout.setOnClickListener(this);
        mineBlogLayout.setOnClickListener(this);
        mineCollectLayout.setOnClickListener(this);
        mineFollowingLayout.setOnClickListener(this);
        mineSignOutButton.setOnClickListener(this);
        mineUsernameText.setText(userInfoGson.getUser().getUsername());
        mineNicknameText.setText(userInfoGson.getUser().getNickname());
        mineSchoolText.setText(userInfoGson.getUser().getSchool());
        if (userInfoGson.getUser().getSex() == MALE_RESPONSE) {
            sexSelected = MALE;
            mineSexText.setText(MALE_TEXT);
        }
        else if (userInfoGson.getUser().getSex() == FEMALE_RESPONSE) {
            sexSelected = FEMALE;
            mineSexText.setText(FEMALE_TEXT);
        }
        else if (userInfoGson.getUser().getSex() == SECRET_RESPONSE) {
            sexSelected = SECRET;
            mineSexText.setText(SECRET_TEXT);
        }
        Glide.with(activity)
                .load(ServerUrlConstant.SERVER_URI + userInfoGson.getUser().getAvatar())
                .into(mineFace);
    }

    /**
     * 请求服务器修改sex
     * @param sexSelected:MALE, FEMALE, SECRET
     */
    public void setSex(int sexSelected) {
        chooseSexPopWindow.dismiss();
        this.sexSelected = sexSelected;
        HttpUtil.editUserInfo(activity, activity.userId, sexSelected, null, MineConstant.EDIT_SEX);
    }

    /**
     * 若服务器修改昵称成功，更新昵称UI
     */
    public void updateNicknameUI(String nickName) {
        Toast.makeText(activity, HintConstant.EDIT_USER_INFO_SUCCESS, Toast.LENGTH_SHORT).show();
        mineNicknameText.setText(nickName);
    }

    /**
     * 若服务器修改性别成功，更新性别UI
     */
    public void updateSexUI() {
        Toast.makeText(activity, HintConstant.EDIT_USER_INFO_SUCCESS, Toast.LENGTH_SHORT).show();
        switch (sexSelected) {
            case SECRET:
                mineSexText.setText(SECRET_TEXT);
                break;
            case MALE:
                mineSexText.setText(MALE_TEXT);
                break;
            case FEMALE:
                mineSexText.setText(FEMALE_TEXT);
                break;
            default:
                break;
        }
    }

    /**
     *  从手机本地选择图片
     */
    public void selectImage() {
        Matisse.from(activity)
                .choose(MimeType.ofImage())
                .imageEngine(new PicassoEngine())
                .theme(R.style.Matisse_Zhihu)
                .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .captureStrategy(new CaptureStrategy(true,"com.dalao.yiban.matisse.fileprovider"))//存储到哪里
                .forResult(HomeConstant.EDIT_USER_FACE_REQUEST_CODE);

    }

    /**
     * 压缩保存选择的图片并传输到服务器
     * @param data：选择的图片
     */
    public void handleSelectedImage(Intent data) {
        Uri imageUri = Matisse.obtainResult(data).get(0);
        String imagePath = SDCardUtil.getFilePathFromUri(activity, imageUri);
        int screenWidth = CommonUtil.getScreenWidth(activity);
        int screenHeight = CommonUtil.getScreenHeight(activity);
        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, screenWidth, screenHeight);// 压缩图片
        imagePath = SDCardUtil.saveToSdCard(bitmap);
        File file = new File(imagePath);
        postFileToServer(file);
    }

    /**
     * 退出登录
     */
    public void signOut() {
        signOutPopWindow.dismiss();
        User user = new User();
        user.setSeverId(HomeConstant.VISITOR_USER_ID);
        user.updateAll("severId = ?", activity.userId);
        activity.userId = HomeConstant.VISITOR_USER_ID;
        mineLoginModeLayout.setVisibility(View.GONE);
        mineVisitorModeLayout.setVisibility(View.VISIBLE);
        mineLoginButton.setOnClickListener(this);
    }

}
