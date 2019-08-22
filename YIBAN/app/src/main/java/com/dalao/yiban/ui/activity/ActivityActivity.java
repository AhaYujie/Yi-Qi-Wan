package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.ActivityGson;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.ui.adapter.CommentAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;
import com.dalao.yiban.util.StringUtils;
import com.sendtion.xrichtext.RichTextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class ActivityActivity extends ActConBlogBaseActivity implements CommentInterface {

    private NestedScrollView activityScrollView;

    private TextView activityCommentTitle;

    private Button activityMoveToComment;

    private TextView activityTitle;

    private TextView activityContentTime;

    private RichTextView activityContent;

    private TextView activitySource;

    private Toolbar activityToolbar;

    private RecyclerView activityCommentRecyclerView;

    private CommentAdapter commentAdapter;

    private Button activityBottomNavCommentButton;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    private Menu menu;

    private MenuItem moreCollect;

    private String activityId;

    private String userId;

    private ActivityGson activityGson;

    private Button activityBottomNavCollect;

    private String commentToCommentId;

    private Button activityBottomNavForward;

    private WebView activityWebView;

    /**
     *
     * @param context :d'd
     * @param userId : 用户id
     * @param activityId : 活动id
     * @param activityTitle : 活动标题
     * @param activityContentTime : 活动时间
     */
    public static void actionStart(Context context, String userId, String activityId,
                                   String activityTitle, String activityContentTime) {
        Intent intent = new Intent(context, ActivityActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        intent.putExtra(HomeConstant.ACTIVITY_ID, activityId);
        intent.putExtra(HomeConstant.ACTIVITY_TITLE, activityTitle);
        intent.putExtra(HomeConstant.ACTIVITY_CONTENT_TIME, activityContentTime);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        // 初始化控件
        activityToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        activityCommentRecyclerView = (RecyclerView) findViewById(R.id.activity_comment_recyclerview);
        activityBottomNavCommentButton = (Button) findViewById(R.id.bottom_nav_comment);
        activityTitle = (TextView) findViewById(R.id.activity_title);
        activityContentTime = (TextView) findViewById(R.id.activity_content_time);
        activityContent = (RichTextView) findViewById(R.id.activity_content);
        activitySource = (TextView) findViewById(R.id.activity_source);
        activityBottomNavCollect = (Button) findViewById(R.id.bottom_nav_collect);
        activityMoveToComment = (Button) findViewById(R.id.move_to_comment);
        activityCommentTitle = (TextView) findViewById(R.id.activity_comment_title);
        activityScrollView = (NestedScrollView) findViewById(R.id.activity_scroll_view);
        activityBottomNavForward = (Button) findViewById(R.id.bottom_nav_forward);
        activityWebView = (WebView) findViewById(R.id.activity_content_webview); // TODO: test html

        setSupportActionBar(activityToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);
        activityId = intent.getStringExtra(HomeConstant.ACTIVITY_ID);
        activityTitle.setText(intent.getStringExtra(HomeConstant.ACTIVITY_TITLE));
        activityContentTime.setText(intent.getStringExtra(HomeConstant.ACTIVITY_CONTENT_TIME));

        // 设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityCommentRecyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this, this, userId,
                activityId, HomeConstant.SELECT_ACTIVITY);
        activityCommentRecyclerView.setAdapter(commentAdapter);

        // 请求服务器获取数据刷新UI
        requestDataFromServer(true, true);
    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 评论
            case R.id.bottom_nav_comment:
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                editCommentText(CommentConstant.COMMENT_TO_NO_ONE);
                break;

            // 发布评论
            case R.id.comment_publish_button:
                String content = commentEditText.getText().toString();
                if (!"".equals(content)) {
                    commentPopupWindow.dismiss();
                    HttpUtil.comment(this, this, activityId, userId,
                            commentToCommentId, content, HomeConstant.SELECT_ACTIVITY);
                }
                else {
                    // 评论为空，不能发表
                    Toast.makeText(this, HintConstant.COMMENT_NOT_EMPTY, Toast.LENGTH_SHORT).show();
                }
                break;

            // 在活动内容滑动到评论区，在评论区滑动到活动内容
            case R.id.move_to_comment:
                int[] position = new int[2];
                activityCommentTitle.getLocationOnScreen(position);
                // 滑动到评论区
                if (position[1] > 0) {
                    activityScrollView.smoothScrollTo(0, activityCommentTitle.getTop());
                }
                // 滑动到活动内容
                else  {
                    activityScrollView.smoothScrollTo(0, activityTitle.getTop());
                }
                break;

            // 收藏或取消收藏
            case R.id.bottom_nav_collect:
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                HttpUtil.collectContent(this, HomeConstant.SELECT_ACTIVITY, userId,
                        activityId, activityGson.getCollection());
                break;

            // 转发
            case R.id.bottom_nav_forward:
                CustomPopWindow.forwardPopWindow(v, this);
                break;
            default:
                break;
        }
    }

    /**
     * 创建菜单栏
     * @param menu:
     * @return :
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_menu, menu);
        this.menu = menu;
        this.moreCollect = menu.findItem(R.id.more_collect);
        // 获取数据前隐藏menu item
        menu.setGroupVisible(R.id.more_group, false);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Toolbar菜单栏点击事件
     * @param item:
     * @return :
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            // 收藏或取消收藏
            case R.id.more_collect:
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                HttpUtil.collectContent(this, HomeConstant.SELECT_ACTIVITY, userId,
                        activityId, activityGson.getCollection());
                break;

            case R.id.more_copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Label", activityGson.getAuthor());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(this, HintConstant.COPY_SUCCESS, Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.more_forward:
                CustomPopWindow.forwardPopWindow
                        (getWindow().getDecorView().findViewById(R.id.bottom_nav_forward), this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 请求服务器获取活动数据, 并解析刷新UI
     * @param updateContent : true则更新内容UI
     * @param updateComment : true则更新评论区UI
     */
    private void requestDataFromServer(final boolean updateContent, final boolean updateComment) {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.ACTIVITY_ID, activityId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();
        HttpUtil.sendHttpPost(ServerUrlConstant.ACTIVITY_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ActivityActivity.this.getCallList().add(call);
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityActivity.this, HintConstant.GET_DATA_FAILED,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ActivityActivity.this.getCallList().add(call);
                if (response.body() != null) {
                    String responseText = response.body().string();
                    final ActivityGson activityGson = JsonUtil.handleActivityResponse(responseText);
                    if (activityGson != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ActivityActivity.this.activityGson = activityGson;
                                if (updateContent) {
                                    updateContentUI();
                                }
                                if (updateComment) {
                                    updateCommentUI();
                                }
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ActivityActivity.this,
                                        HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityActivity.this,
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 刷新活动内容UI
     */
    private void updateContentUI() {
        // 设置点击事件
        activityBottomNavCommentButton.setOnClickListener(this);
        activityMoveToComment.setOnClickListener(this);
        activityBottomNavCollect.setOnClickListener(this);
        activityBottomNavForward.setOnClickListener(this);
        activityTitle.setText(activityGson.getTitle());
        activityContentTime.setText(activityGson.getTime());
        activitySource.setText(activityGson.getAuthor());
        menu.setGroupVisible(R.id.more_group, true);
        if (activityGson.getCollection() == HomeConstant.COLLECT) {
            activityBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_blue);
            moreCollect.setTitle(HomeConstant.UN_COLLECT_TEXT);
        }
        else if (activityGson.getCollection() == HomeConstant.UN_COLLECT) {
            activityBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_black);
            moreCollect.setTitle(HomeConstant.COLLECT_TEXT);
        }

        // type为h5用WebView加载内容
        if (activityGson.getType().equals(HomeConstant.CONTENT_RESPONSE_H5_TYPE)) {
            activityContent.setVisibility(View.GONE);
            activityWebView.setVisibility(View.VISIBLE);
            activityWebView.setWebViewClient(new WebViewClient());
            activityWebView.getSettings().setJavaScriptEnabled(true);
            activityWebView.loadDataWithBaseURL(null, activityGson.getContent(),
                    "text/html", "utf-8", null);
        }
        // type为word用RichText加载内容
        else if (activityGson.getType().equals(HomeConstant.CONTENT_RESPONSE_NORMAL_TYPE)) {
            activityContent.setVisibility(View.VISIBLE);
            activityWebView.setVisibility(View.GONE);
            activityContent.post(new Runnable() {
                @Override
                public void run() {
                    StringUtils.showContent(activityContent, activityGson.getContent());
                }
            });
        }
    }

    /**
     * 更新评论区UI
     */
    private void updateCommentUI() {
        commentAdapter.setCommentsBeanList(activityGson.getComments());
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 编辑评论
     * @param toCommentId:回复的评论的id(若无则为-1)
     */
    public void editCommentText(String toCommentId) {
        this.commentToCommentId = toCommentId;
        CustomPopWindow.PopWindowViewHelper popWindowViewHelper =
                CustomPopWindow.commentPopWindow(activityBottomNavCommentButton, this);
        commentEditText = popWindowViewHelper.editText;
        commentPopupWindow = popWindowViewHelper.popupWindow;
    }

    /**
     * 发表评论成功
     */
    public void publishComment() {
        Toast.makeText(this, HintConstant.COMMENT_SUCCESS, Toast.LENGTH_SHORT).show();
        requestDataFromServer(false, true);
        // 刷新评论区UI
        commentAdapter.setCommentsBeanList(activityGson.getComments());
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 收藏活动成功
     */
    @Override
    public void collectSuccess() {
        activityBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_blue);
        moreCollect.setTitle(HomeConstant.UN_COLLECT_TEXT);
        Toast.makeText(this, HintConstant.COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
        activityGson.setCollection(HomeConstant.COLLECT);
    }

    /**
     * 取消收藏活动成功
     */
    @Override
    public void unCollectSuccess() {
        activityBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_black);
        moreCollect.setTitle(HomeConstant.COLLECT_TEXT);
        Toast.makeText(this, HintConstant.UN_COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
        activityGson.setCollection(HomeConstant.UN_COLLECT);
    }

}





















