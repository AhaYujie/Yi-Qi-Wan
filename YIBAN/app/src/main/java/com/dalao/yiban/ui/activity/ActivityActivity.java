package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
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


public class ActivityActivity extends BaseActivity implements CommentInterface {

    private TextView activityTitle;

    private TextView activityContentTime;

    private RichTextView activityContent;

    private TextView activitySource;

    private Toolbar activityToolbar;

    private RecyclerView activityCommentRecyclerView;

    private CommentAdapter commentAdapter;

    private Button activityCommentButton;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    private Menu menu;

    private MenuItem moreCollect;

    private String activityId;

    private String userId;

    private ActivityGson activityGson;

    private Button activityCommentCollect;

    /**
     * 启动 ActivityActivity
     * @param context:
     * @param activityId:活动Id
     */
    public static void actionStart(Context context, String activityId) {
        Intent intent = new Intent(context, ActivityActivity.class);
        intent.putExtra(HomeConstant.ACTIVITY_ID, activityId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);

        // 初始化控件
        activityToolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        activityCommentRecyclerView = (RecyclerView) findViewById(R.id.activity_comment_recyclerview);
        activityCommentButton = (Button) findViewById(R.id.comment_button);
        activityTitle = (TextView) findViewById(R.id.activity_title);
        activityContentTime = (TextView) findViewById(R.id.activity_content_time);
        activityContent = (RichTextView) findViewById(R.id.activity_content);
        activitySource = (TextView) findViewById(R.id.activity_source);
        activityCommentCollect = (Button) findViewById(R.id.activity_comment_collect);

        // 设置点击事件
        activityCommentButton.setOnClickListener(this);

        setSupportActionBar(activityToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityCommentRecyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this, this);
        activityCommentRecyclerView.setAdapter(commentAdapter);

        // 获取数据
        Intent intent = getIntent();
        activityId = intent.getStringExtra(HomeConstant.ACTIVITY_ID);
        // TODO:本地获取用户id
        userId = "1";   // test
        // 请求服务器
        requestDataFromServer();

    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 评论
            case R.id.comment_button:
                editCommentText();
                break;

            // 发布评论
            case R.id.comment_publish_button:
                // TODO
                publishComment("-1");
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

            case R.id.more_collect:
                // TODO:收藏该活动
                Toast.makeText(ActivityActivity.this, "COLLECT",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.more_copy:
                // TODO：复制该活动链接
                Toast.makeText(ActivityActivity.this, "copy",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.more_forward:
                CustomPopWindow.forwardPopWindow
                        (getWindow().getDecorView().findViewById(R.id.activity_comment_forward), this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 请求服务器获取活动数据, 并解析刷新UI
     */
    private void requestDataFromServer() {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.ACTIVITY_ID, activityId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();
        HttpUtil.sendHttpPost(ServerUrlConstant.ACTIVITY_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
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
                if (response.body() != null) {
                    String responseText = response.body().string();
                    final ActivityGson activityGson = JsonUtil.handleActivityResponse(responseText);
                    if (activityGson != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateActivityUI(activityGson);
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
     * 刷新活动UI
     * @param activityGson:解析后的数据
     */
    private void updateActivityUI(final ActivityGson activityGson) {
        this.activityGson = activityGson;
        activityTitle.setText(activityGson.getTitle());
        activityContentTime.setText(activityGson.getTime());
        activitySource.setText(activityGson.getAuthor());
        activityContent.post(new Runnable() {
            @Override
            public void run() {
                StringUtils.showContestContent(activityContent, activityGson.getContent());
            }
        });
        if (activityGson.getCollection() == HomeConstant.COLLECT) {
            activityCommentCollect.setBackgroundResource(R.drawable.ic_collect_blue);
            moreCollect.setTitle(HomeConstant.COLLECT_TEXT);
        }
        else if (activityGson.getCollection() == HomeConstant.UN_COLLECT) {
            activityCommentCollect.setBackgroundResource(R.drawable.ic_collect_black);
            moreCollect.setTitle(HomeConstant.UN_COLLECT_TEXT);
        }
        commentAdapter.setCommentsBeanList(activityGson.getComments());
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 编辑评论
     */
    public void editCommentText() {
        CustomPopWindow.PopWindowViewHelper popWindowViewHelper =
                CustomPopWindow.commentPopWindow(activityCommentButton, this);
        commentEditText = popWindowViewHelper.editText;
        commentPopupWindow = popWindowViewHelper.popupWindow;
    }

    /**
     * 发表评论
     * @param toUserId:回复的用户id(若无则为-1)
     */
    public void publishComment(String toUserId) {
        // TODO
        commentPopupWindow.dismiss();
        String content = commentEditText.getText().toString();
        Toast.makeText(this, content + " to " + toUserId, Toast.LENGTH_SHORT).show();
    }

}





















