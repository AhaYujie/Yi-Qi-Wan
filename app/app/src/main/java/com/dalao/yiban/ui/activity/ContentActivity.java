package com.dalao.yiban.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.adapter.CommentAdapter;

/**
 * 活动，竞赛，博客，查看回复活动基类
 */
public abstract class ContentActivity extends BaseActivity {

    protected ProgressBar progressBar;

    protected TextView noMoreCommentLayout;

    protected CommentAdapter.CommentsLoadingLayout commentsLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCommentsLoadingLayout();
    }

    protected void initCommentsLoadingLayout() {
        commentsLoadingLayout = new CommentAdapter.CommentsLoadingLayout() {
            @Override
            public void showProgressBar() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void closeProgressBar() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void showNoMoreComments() {
                noMoreCommentLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void closeNoMoreComments() {
                noMoreCommentLayout.setVisibility(View.GONE);
            }
        };
    }

}
