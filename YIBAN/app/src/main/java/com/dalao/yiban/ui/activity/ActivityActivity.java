package com.dalao.yiban.ui.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.ui.adapter.ActivityCommentAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;


public class ActivityActivity extends BaseActivity {

    private Toolbar activityToolbar;

    private RecyclerView activityCommentRecyclerView;

    private ActivityCommentAdapter activityCommentAdapter;

    private Button activityCommentButton;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    /**
     * 启动 ActivityActivity
     * @param context:
     * @param activityId:活动Id
     */
    public static void actionStart(Context context, String activityId) {
        Intent intent = new Intent(context, ActivityActivity.class);
        intent.putExtra(HomeConstant.activityId, activityId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        // 初始化控件
        activityToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        activityCommentRecyclerView = (RecyclerView) findViewById(R.id.activity_comment_recyclerview);
        activityCommentButton = (Button) findViewById(R.id.activity_comment_button);

        // 设置点击事件
        activityCommentButton.setOnClickListener(this);

        setSupportActionBar(activityToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 初始化RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityCommentRecyclerView.setLayoutManager(layoutManager);
        activityCommentAdapter = new ActivityCommentAdapter();
        activityCommentRecyclerView.setAdapter(activityCommentAdapter);

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 评论
            case R.id.activity_comment_button:
                CustomPopWindow.PopWindowViewHelper popWindowViewHelper =  CustomPopWindow.commentPopWindow(v, this);
                commentEditText = popWindowViewHelper.editText;
                commentPopupWindow = popWindowViewHelper.popupWindow;
                break;

            // 发布评论
            case R.id.comment_publish_button:
                //TODO
                commentPopupWindow.dismiss();
                String content = commentEditText.getText().toString();
                Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * Toolbar菜单栏点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO:返回到MainActivity的活动列表界面
                Toast.makeText(ActivityActivity.this, "back",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.contest_more_collect:
                // TODO:收藏该活动
                Toast.makeText(ActivityActivity.this, "collect",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.contest_more_copy:
                // TODO：复制该活动链接
                Toast.makeText(ActivityActivity.this, "copy",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.contest_more_forward:
                CustomPopWindow.forwardPopWindow
                        (getWindow().getDecorView().findViewById(R.id.activity_comment_forward), this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
