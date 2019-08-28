package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.ui.custom.CustomProgressDialog;
import com.dalao.yiban.util.HttpUtil;

public class EditNicknameActivity extends BaseActivity {

    private Button editNicknameCancelButton;

    private Button editNicknameConfirmButton;

    private EditText editNicknameEditText;

    public CustomProgressDialog uploadingProgressDialog;

    private String nickname;

    private String userId;

    /**
     * 启动 EditNicknameActivity
     * @param context:
     * @param nickname:用户昵称(若无则为空字符串)
     */
    public static void actionStart(Context context, String nickname, String usrId) {
        Intent intent = new Intent(context, EditNicknameActivity.class);
        intent.putExtra(MineConstant.USER_NICKNAME, nickname);
        intent.putExtra(HomeConstant.USER_ID, usrId);
        ((MainActivity) context).startActivityForResult(intent, HomeConstant.EDIT_USER_NICKNAME_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nick_name);

        // 初始化控件
        editNicknameCancelButton = (Button) findViewById(R.id.edit_nickname_cancel_button);
        editNicknameConfirmButton = (Button) findViewById(R.id.edit_nickname_confirm_button);
        editNicknameEditText = (EditText) findViewById(R.id.edit_nickname_edit_text);
        uploadingProgressDialog = new CustomProgressDialog(this, HintConstant.UPLOADING);

        // 设置点击事件
        editNicknameCancelButton.setOnClickListener(this);
        editNicknameConfirmButton.setOnClickListener(this);

        // 从上个活动活动用户昵称数据
        Intent intent = getIntent();
        nickname = intent.getStringExtra(MineConstant.USER_NICKNAME);
        userId = intent.getStringExtra(HomeConstant.USER_ID);

        // 初始化UI
        if (!"".equals(nickname)) {
            editNicknameEditText.setText(nickname);
        }

    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消，不保存
            case R.id.edit_nickname_cancel_button:
                setResult(RESULT_CANCELED);
                finish();
                break;

            // 保存修改
            case R.id.edit_nickname_confirm_button:
                // 昵称不能为空
                if (editNicknameEditText.getText().toString().equals("")) {
                    Toast.makeText(this, HintConstant.EDIT_NICKNAME_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadingProgressDialog.showProgressDialog();
                    String nickName = editNicknameEditText.getText().toString();
                    HttpUtil.editUserInfo(this, userId, -1, nickName, MineConstant.EDIT_NICKNAME);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // 取消，不保存
        setResult(RESULT_CANCELED);
        finish();
    }

}
