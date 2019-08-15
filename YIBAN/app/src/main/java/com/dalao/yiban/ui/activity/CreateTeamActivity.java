package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;

public class CreateTeamActivity extends BaseActivity {

    private Button createTeamCancelButton;

    private EditText createTeamNameEditText;

    private EditText createTeamNumberEditText;

    private EditText createTeamCommentEditText;

    private Button createTeamButton;

    /**
     * 启动 CreateTeamActivity
     * @param context:
     * @param contestId:竞赛Id
     */
    public static void actionStart(Context context, String contestId) {
        Intent intent = new Intent(context, CreateTeamActivity.class);
        intent.putExtra(HomeConstant.CONTEST_ID, contestId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        // 初始化控件
        createTeamCancelButton = (Button) findViewById(R.id.create_team_cancel_button);
        createTeamNameEditText = (EditText) findViewById(R.id.create_team_name_edit_text);
        createTeamNumberEditText = (EditText) findViewById(R.id.create_team_number_edit_text);
        createTeamCommentEditText = (EditText) findViewById(R.id.create_team_comment_edit_text);
        createTeamButton = (Button) findViewById(R.id.create_team_button);

        // 设置点击事件
        createTeamCancelButton.setOnClickListener(this);
        createTeamButton.setOnClickListener(this);

    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_team_cancel_button:
                finish();
                break;
            case R.id.create_team_button:
                // TODO;
                break;
            default:
                break;
        }
    }

}
