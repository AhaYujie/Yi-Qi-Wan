package com.dalao.yiban.ui.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.adapter.ActivityCommentAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;


public class ActivityActivity extends BaseActivity {

    private Toolbar activityToolbar;

    private RecyclerView activityCommentRecyclerView;

    private ActivityCommentAdapter activityCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        // 初始化控件
        activityToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        activityCommentRecyclerView = (RecyclerView) findViewById(R.id.activity_comment_recyclerview);

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
        // TODO
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
                CustomPopWindow.initPopWindow
                        (getWindow().getDecorView().findViewById(R.id.activity_comment_forward),
                                ActivityActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
