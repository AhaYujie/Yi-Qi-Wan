package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CommentBean;
import com.dalao.yiban.gson.ReplyGson;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.my_interface.RequestDataInterface;
import com.dalao.yiban.ui.adapter.CommentAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ViewReplyActivity extends ContentActivity implements CommentInterface, RequestDataInterface {

    private RecyclerView viewReplyRecyclerView;

    private CommentBean masterCommentBean;

    private CommentAdapter commentAdapter;

    private Button viewReplyCancelButton;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    private NestedScrollView viewReplyScrollView;

    private String commentToCommentId;

    //public String userId;

    public String contentId;

    public int category;

    // 被回复者的控件
    private ImageView commentFace;
    private TextView commentPersonName;
    private TextView commentText;
    private TextView commentTime;
    private Button commentReplyButton;
    private TextView commentReplyNum;
    private LinearLayout commentReply;

    /**
     * 启动 ViewReplyActivity
     * @param context:
     * @param masterCommentBean:被回复的用户的CommentBean
     * @param userId : 用户id
     * @param contentId : 活动或者博客的id
     * @param category : SELECT_ACTIVITY or SELECT_BLOG or SELECT_CONTEST
     */
    public static void actionStart(Context context, @NonNull CommentBean masterCommentBean,
                                   String userId, String contentId, int category) {
        Intent intent = new Intent(context, ViewReplyActivity.class);
        intent.putExtra(CommentConstant.COMMENT_BEAN, masterCommentBean);
        intent.putExtra(CommentConstant.USER_ID, userId);
        intent.putExtra(CommentConstant.CONTENT_ID, contentId);
        intent.putExtra(CommentConstant.CATEGORY, category);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reply);

        // 初始化控件
        viewReplyRecyclerView = (RecyclerView) findViewById(R.id.view_reply_recyclerview);
        viewReplyCancelButton = (Button) findViewById(R.id.view_reply_cancel_button);
        commentFace = (ImageView) findViewById(R.id.comment_author_face);
        commentPersonName = (TextView) findViewById(R.id.comment_person_name);
        commentText = (TextView) findViewById(R.id.comment_text);
        commentTime = (TextView) findViewById(R.id.comment_time);
        commentReplyButton = (Button) findViewById(R.id.comment_reply_button);
        commentReplyNum = (TextView) findViewById(R.id.comment_reply_num);
        commentReply = (LinearLayout) findViewById(R.id.comment_reply);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        noMoreCommentLayout = (TextView) findViewById(R.id.no_more_comment_layout);
        viewReplyScrollView = (NestedScrollView) findViewById(R.id.view_reply_scroll_view);

        // 获取传递给这个活动的数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(CommentConstant.USER_ID);
        contentId = intent.getStringExtra(CommentConstant.CONTENT_ID);
        category = intent.getIntExtra(CommentConstant.CATEGORY, CommentConstant.SELECT_CATEGORY_NONE);
        masterCommentBean = (CommentBean) intent.getSerializableExtra(CommentConstant.COMMENT_BEAN);

        // 设置被回复者的控件
        commentPersonName.setText(masterCommentBean.getAuthor());
        commentText.setText(masterCommentBean.getContent());
        commentTime.setText(masterCommentBean.getTime());
        Glide.with(MyApplication.getContext())
                .load(ServerUrlConstant.SERVER_URI + masterCommentBean.getAvatar())
                .into(commentFace);
        commentReply.setVisibility(View.GONE);

        // 设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewReplyRecyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this, this,
                contentId, category, commentsLoadingLayout);
        viewReplyRecyclerView.setAdapter(commentAdapter);

        // 设置NestedScrollView滑动监听事件
        viewReplyScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 滑动到底部，加载更多评论
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    String uri, contentIdKey;
//                    switch (category) {
//                        case HomeConstant.SELECT_ACTIVITY:
//                            uri = ServerUrlConstant.ACTIVITY_URI;
//                            contentIdKey = ServerPostDataConstant.ACTIVITY_ID;
//                            break;
//                        case HomeConstant.SELECT_CONTEST:
//                            uri = ServerUrlConstant.CONTEST_URI;
//                            contentIdKey = ServerPostDataConstant.CONTEST_ID;
//                            break;
//                        case HomeConstant.SELECT_BLOG:
//                            uri = ServerUrlConstant.BLOG_URI;
//                            contentIdKey = ServerPostDataConstant.BLOG_ID;
//                            break;
//                        default:
//                            return;
//                    }
                    commentAdapter.loadMoreComments(ServerUrlConstant.REPLY_URI,
                            ServerPostDataConstant.REPLY_ID, String.valueOf(masterCommentBean.getId()));
                }
            }
        });

        // 设置点击事件
        viewReplyCancelButton.setOnClickListener(this);
        commentReplyButton.setOnClickListener(this);

        // 请求服务器获取数据并刷新UI
        requestDataFromServer(false, true);

    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.view_reply_cancel_button:
                finish();
                break;

            // 评论
            case R.id.comment_reply_button:
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                editCommentText(String.valueOf(masterCommentBean.getId()));
                break;

            // 发布评论
            case R.id.comment_publish_button:
                publishComment();
                break;
            default:
                break;
        }
    }

    /**
     * 请求服务器获取评论数据, 并解析刷新UI
     * @param updateContent : true则更新内容UI(无用)
     * @param updateComment : true则更新评论区UI
     */
    @Override
    public void requestDataFromServer(final boolean updateContent, final boolean updateComment) {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.REPLY_ID, String.valueOf(masterCommentBean.getId()))
                .build();
        Call call = HttpUtil.sendHttpPost(ServerUrlConstant.REPLY_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseText = response.body().string();
                    final ReplyGson replyGson = JsonUtil.handleReplyResponse(responseText);
                    if (replyGson != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (updateComment) {
                                    updateReplyUI(replyGson);
                                }
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ViewReplyActivity.this,
                                        HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ViewReplyActivity.this,
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        ViewReplyActivity.this.getCallList().add(call);
    }

    /**
     * 刷新查看回复UI
     * @param replyGson:解析后的数据
     */
    private void updateReplyUI(@NonNull final ReplyGson replyGson) {
        commentAdapter.setCommentsBeanList(replyGson.getComments());
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 编辑评论
     *  @param toCommentId:回复的评论的id(若无则为-1)
     */
    public void editCommentText(String toCommentId) {
        this.commentToCommentId = toCommentId;
        CustomPopWindow.PopWindowViewHelper popWindowViewHelper =
                CustomPopWindow.commentPopWindow(viewReplyRecyclerView, this);
        commentEditText = popWindowViewHelper.editText;
        commentPopupWindow = popWindowViewHelper.popupWindow;
    }

    /**
     * 发表评论
     */
    public void publishComment() {
        String content = commentEditText.getText().toString();
        if ("".equals(content)) {
            // 评论为空，不能发表
            Toast.makeText(this, HintConstant.COMMENT_NOT_EMPTY, Toast.LENGTH_SHORT).show();
            return;
        }
        commentPopupWindow.dismiss();
        HttpUtil.comment(this, commentAdapter, String.valueOf(masterCommentBean.getId()),
                userId, commentToCommentId, content, HomeConstant.SELECT_NONE);
    }

}

















