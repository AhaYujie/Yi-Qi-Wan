package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;

public class EditNicknameActivity extends BaseActivity {

    private Button editNicknameCancelButton;

    private Button editNicknameConfirmButton;

    private EditText editNicknameEditText;

    private String nickname;

    /**
     * 启动 EditNicknameActivity
     * @param context:
     * @param nickname:用户昵称(若无则为空字符串)
     */
    public static void actionStart(Context context, String nickname) {
        Intent intent = new Intent(context, EditNicknameActivity.class);
        intent.putExtra(MineConstant.USER_NICKNAME, nickname);
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

        // 设置点击事件
        editNicknameCancelButton.setOnClickListener(this);
        editNicknameConfirmButton.setOnClickListener(this);

        // 从上个活动活动用户昵称数据
        nickname = getIntent().getStringExtra(MineConstant.USER_NICKNAME);

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
                Intent intent = new Intent();
                intent.putExtra(MineConstant.USER_NICKNAME, editNicknameEditText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
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
