package com.dalao.yiban.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.ContestGson;
import com.dalao.yiban.my_interface.CollectInterface;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.my_interface.RequestDataInterface;
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

public class ContestActivity extends BaseActivity implements CommentInterface, CollectInterface,
        RequestDataInterface {

    private MenuItem moreCollect;

    private ContestGson contestGson = null;

    private NestedScrollView contestScrollView;

    private CommentAdapter commentAdapter;

    private TextView contestTitle;

    private RichTextView richTextView;

    private TextView contestSource;

    private TextView contestCommentTitle;

    private Button contestCommentButton;

    private Button contestMoveToComment;

    private Button bottomNavCollect;

    private Button bottomNavForward;

    private String contestId;

    private String userId;

    private Menu menu;

    private String commentToCommentId;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    private WebView contestContentWebview;

    private TextView contestContentTime;

    /**
     *
     * @param context :
     * @param userId : 用户id
     * @param contestId：竞赛id
     * @param contestTitle：竞赛标题
     * @param contestContentTime：竞赛时间
     */
    public static void actionStart(Context context, String userId, String contestId,
                                   String contestTitle, String contestContentTime) {
        Intent intent = new Intent(context, ContestActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        intent.putExtra(HomeConstant.CONTEST_ID, contestId);
        intent.putExtra(HomeConstant.CONTEST_TITLE, contestTitle);
        intent.putExtra(HomeConstant.CONTEST_CONTENT_TIME, contestContentTime);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        // 初始化控件
        RecyclerView contestTeamRecyclerView = (RecyclerView) findViewById(R.id.contest_comment_recyclerview);
        contestScrollView = (NestedScrollView) findViewById(R.id.contest_scroll_view);
        contestCommentButton = (Button) findViewById(R.id.bottom_nav_comment);
        contestMoveToComment = (Button) findViewById(R.id.move_to_comment);
        bottomNavCollect = (Button) findViewById(R.id.bottom_nav_collect);
        bottomNavForward = (Button) findViewById(R.id.bottom_nav_forward);
        contestCommentTitle = (TextView) findViewById(R.id.contest_comment_title);
        richTextView = (RichTextView) findViewById(R.id.contest_content);
        contestSource = (TextView) findViewById(R.id.contest_source);
        contestTitle = (TextView) findViewById(R.id.contest_title);
        contestContentWebview = (WebView) findViewById(R.id.contest_content_webview);
        contestContentTime = (TextView) findViewById(R.id.contest_content_time);
        Toolbar contestToolbar = findViewById(R.id.contest_toolbar);

        setSupportActionBar(contestToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);
        contestId = intent.getStringExtra(HomeConstant.CONTEST_ID);
        contestTitle.setText(intent.getStringExtra(HomeConstant.CONTEST_TITLE));
        contestContentTime.setText(intent.getStringExtra(HomeConstant.CONTEST_CONTENT_TIME));

        // 初始化RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contestTeamRecyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this, this, userId, contestId,
                HomeConstant.SELECT_CONTEST);
        contestTeamRecyclerView.setAdapter(commentAdapter);

        // 请求服务器获得数据刷新UI
        requestDataFromServer(true, true);

    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 编辑评论
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
                publishComment();
                break;

            // 在竞赛内容滑动到评论区，在评论区滑动到竞赛内容
            case R.id.move_to_comment:
                int[] position = new int[2];
                contestCommentTitle.getLocationOnScreen(position);
                // 滑动到评论区
                if (position[1] > 0) {
                    contestScrollView.smoothScrollTo(0, contestCommentTitle.getTop());
                }
                // 滑动到竞赛内容
                else  {
                    contestScrollView.smoothScrollTo(0, contestTitle.getTop());
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
                HttpUtil.collectContent(this, HomeConstant.SELECT_CONTEST,
                        userId, contestId, contestGson.getCollection(), this);
                break;

            case R.id.forward_wechat_friend:
                // TODO:转发该竞赛到微信好友
                Toast.makeText(ContestActivity.this, "forward_wechat_friend",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_friend_circle:
                // TODO:转发该竞赛到朋友圈
                Toast.makeText(ContestActivity.this, "forward_friend_circle",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_qq_friend:
                // TODO:转发该竞赛到QQ好友
                Toast.makeText(ContestActivity.this, "forward_qq_friend",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_qq_sapce:
                // TODO:转发该竞赛到QQ空间
                Toast.makeText(ContestActivity.this, "forward_qq_sapce",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_yiban:
                // TODO:转发该竞赛到易班
                Toast.makeText(ContestActivity.this, "forward_yiban",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
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
        // 获取数据前隐藏menu
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

            case R.id.more_collect:
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                HttpUtil.collectContent(this, HomeConstant.SELECT_CONTEST,
                        userId, contestId, contestGson.getCollection(), this);
                break;

            case R.id.more_copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Label", contestGson.getAuthor());
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
     * 请求服务器获取竞赛数据, 并解析刷新UI
     * @param updateContent : true则更新内容UI
     * @param updateComment : true则更新评论区UI
     */
    public void requestDataFromServer(final boolean updateContent, final boolean updateComment) {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.CONTEST_ID, contestId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();
        HttpUtil.sendHttpPost(ServerUrlConstant.CONTEST_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ContestActivity.this.getCallList().add(call);
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ContestActivity.this, HintConstant.GET_DATA_FAILED,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    ContestActivity.this.getCallList().add(call);
                    String responseText = response.body().string();
                    final ContestGson contestGson = JsonUtil.handleContestResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ContestActivity.this.contestGson = contestGson;
                            if (updateContent) {
                                updateContestContentUI();
                            }
                            if (updateComment) {
                                updateContestCommentUI();
                            }
                        }
                    });
                }
                catch (NullPointerException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ContestActivity.this,
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 刷新竞赛内容UI
     */
    private void updateContestContentUI() {
        // 设置button点击事件
        contestCommentButton.setOnClickListener(this);
        contestMoveToComment.setOnClickListener(this);
        bottomNavCollect.setOnClickListener(this);
        bottomNavForward.setOnClickListener(this);
        menu.setGroupVisible(R.id.more_group, true);
        contestTitle.setText(contestGson.getTitle());
        contestContentTime.setText(contestGson.getTime());
        contestSource.setText(contestGson.getAuthor());
        if (contestGson.getCollection() == HomeConstant.COLLECT) {
            bottomNavCollect.setBackgroundResource(R.drawable.ic_collect_blue);
            moreCollect.setTitle(HomeConstant.UN_COLLECT_TEXT);
        }
        else if (contestGson.getCollection() == HomeConstant.UN_COLLECT) {
            bottomNavCollect.setBackgroundResource(R.drawable.ic_collect_black);
            moreCollect.setTitle(HomeConstant.COLLECT_TEXT);
        }

        // type为h5用WebView加载内容
        if (contestGson.getType().equals(HomeConstant.CONTENT_RESPONSE_H5_TYPE)) {
            richTextView.setVisibility(View.GONE);
            contestContentWebview.setVisibility(View.VISIBLE);
            contestContentWebview.setWebViewClient(new WebViewClient());
            contestContentWebview.getSettings().setJavaScriptEnabled(true);
            contestContentWebview.loadDataWithBaseURL(null, contestGson.getContent(),
                    "text/html", "utf-8", null);
        }
        // type为word用RichText加载内容
        else if (contestGson.getType().equals(HomeConstant.CONTENT_RESPONSE_NORMAL_TYPE)) {
            richTextView.setVisibility(View.VISIBLE);
            contestContentWebview.setVisibility(View.GONE);
            richTextView.post(new Runnable() {
                @Override
                public void run() {
                    StringUtils.showContent(richTextView, contestGson.getContent());
                }
            });
        }
    }

    /**
     * 刷新竞赛评论区UI
     */
    private void updateContestCommentUI() {
        commentAdapter.setCommentsBeanList(contestGson.getComments());
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 收藏竞赛成功, 刷新UI
     */
    @Override
    public void collectSuccess() {
        bottomNavCollect.setBackgroundResource(R.drawable.ic_collect_blue);
        moreCollect.setTitle(HomeConstant.UN_COLLECT_TEXT);
        Toast.makeText(this, HintConstant.COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
        contestGson.setCollection(HomeConstant.COLLECT);
    }

    /**
     * 取消收藏成功, 刷新UI
     */
    @Override
    public void unCollectSuccess() {
        bottomNavCollect.setBackgroundResource(R.drawable.ic_collect_black);
        moreCollect.setTitle(HomeConstant.COLLECT_TEXT);
        Toast.makeText(this, HintConstant.UN_COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
        contestGson.setCollection(HomeConstant.UN_COLLECT);
    }

    /**
     * 编辑评论
     * @param toCommentId:回复的评论的id(若无则为-1)
     */
    public void editCommentText(String toCommentId) {
        this.commentToCommentId = toCommentId;
        CustomPopWindow.PopWindowViewHelper popWindowViewHelper =
                CustomPopWindow.commentPopWindow(contestCommentButton, this);
        commentEditText = popWindowViewHelper.editText;
        commentPopupWindow = popWindowViewHelper.popupWindow;
    }

    /**
     * 发表评论成功
     */
    public void publishComment() {
        String content = commentEditText.getText().toString();
        if ("".equals(content)) {
            // 评论为空，不能发表
            Toast.makeText(this, HintConstant.COMMENT_NOT_EMPTY, Toast.LENGTH_SHORT).show();
            return;
        }
        commentPopupWindow.dismiss();
        HttpUtil.comment(this, this, contestId, userId,
                commentToCommentId, content, HomeConstant.SELECT_CONTEST);
    }

}



















