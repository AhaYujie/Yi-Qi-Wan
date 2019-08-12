package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CommentBean;
import com.dalao.yiban.gson.ReplyGson;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.ui.adapter.CommentAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ViewReplyActivity extends BaseActivity implements CommentInterface {

    private RecyclerView viewReplyRecyclerView;

    private CommentBean masterCommentBean;

    private CommentAdapter commentAdapter;

    private Button viewReplyCancelButton;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    private String commentToUserId;

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
     */
    public static void actionStart(Context context, @NonNull CommentBean masterCommentBean) {
        Intent intent = new Intent(context, ViewReplyActivity.class);
        intent.putExtra(CommentConstant.COMMENT_BEAN, masterCommentBean);
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

        // 获取传递给这个活动的数据
        this.masterCommentBean = (CommentBean) getIntent().getSerializableExtra(CommentConstant.COMMENT_BEAN);

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
        commentAdapter = new CommentAdapter(this, this);
        viewReplyRecyclerView.setAdapter(commentAdapter);

        // 设置点击事件
        viewReplyCancelButton.setOnClickListener(this);
        commentReplyButton.setOnClickListener(this);

        // 请求服务器获取数据并刷新UI
        requestDataFromServer();

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
            // 编辑评论
            case R.id.comment_reply_button:
                editCommentText("-1");
                break;

            // 发布评论
            case R.id.comment_publish_button:
                // TODO
                publishComment();
                break;
            default:
                break;
        }
    }

    /**
     * 请求服务器获取评论数据, 并解析刷新UI
     */
    private void requestDataFromServer() {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.REPLY_ID, String.valueOf(masterCommentBean.getId()))
                .build();
        HttpUtil.sendHttpPost(ServerUrlConstant.REPLY_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ViewReplyActivity.this, HintConstant.GET_DATA_FAILED,
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
                                Log.d("yujie", "replyGson != null");
                                updateReplyUI(replyGson);
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
     *  @param toUserId:回复的用户id(若无则为-1)
     */
    public void editCommentText(String toUserId) {
        this.commentToUserId =toUserId;
        CustomPopWindow.PopWindowViewHelper popWindowViewHelper =
                CustomPopWindow.commentPopWindow(viewReplyRecyclerView, this);
        commentEditText = popWindowViewHelper.editText;
        commentPopupWindow = popWindowViewHelper.popupWindow;
    }

    /**
     * 发表评论
     */
    public void publishComment() {
        commentPopupWindow.dismiss();
        String content = commentEditText.getText().toString();
        Toast.makeText(this, content + " to " + commentToUserId, Toast.LENGTH_SHORT).show();
    }

}

















