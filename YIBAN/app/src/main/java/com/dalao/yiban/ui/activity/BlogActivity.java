package com.dalao.yiban.ui.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.ui.adapter.BlogCommentAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;

public class BlogActivity extends BaseActivity {

    private Toolbar blogToolbar;

    private RecyclerView blogCommentRecyclerView;

    private BlogCommentAdapter blogCommentAdapter;

    /**
     * 启动 BlogActivity
     * @param context:
     * @param blogId:博客Id
     */
    public static void actionStart(Context context, String blogId) {
        Intent intent = new Intent(context, BlogActivity.class);
        intent.putExtra(HomeConstant.blogId, blogId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        // 初始化控件
        blogToolbar = (Toolbar) findViewById(R.id.blog_toolbar);
        blogCommentRecyclerView = (RecyclerView) findViewById(R.id.blog_comment_recyclerview);

        setSupportActionBar(blogToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        blogCommentRecyclerView.setLayoutManager(linearLayoutManager);
        blogCommentAdapter = new BlogCommentAdapter();
        blogCommentRecyclerView.setAdapter(blogCommentAdapter);

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        //TODO
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
                Toast.makeText(BlogActivity.this, "back",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.contest_more_collect:
                // TODO:收藏该活动
                Toast.makeText(BlogActivity.this, "collect",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.contest_more_copy:
                // TODO：复制该活动链接
                Toast.makeText(BlogActivity.this, "copy",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.contest_more_forward:
                CustomPopWindow.forwardPopWindow
                        (getWindow().getDecorView().findViewById(R.id.blog_comment_forward),
                                BlogActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
