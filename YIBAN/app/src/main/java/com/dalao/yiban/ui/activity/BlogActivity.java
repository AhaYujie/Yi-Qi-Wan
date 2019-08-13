package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.BlogGson;
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

public class BlogActivity extends BaseActivity implements CommentInterface {

    private Menu menu;

    private MenuItem moreCollect;

    private Toolbar blogToolbar;

    private RecyclerView blogCommentRecyclerView;

    private CommentAdapter commentAdapter;

    private TextView blogTitle;

    private TextView blogContentTime;

    private ImageView blogAuthorFace;

    private TextView blogAuthorName;

    private Button blogFollowAuthorButton;

    private RichTextView blogContent;


    private Button blogBottomNavCommentButton;

    private Button moveToComment;

    private Button blogBottomNavCollect;

    private Button blogBottomNavForward;

    private NestedScrollView blogScrollView;

    private TextView blogCommentTitle;

    private String commentToUserId;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    private BlogGson blogGson;

    private String blogId;

    private String userId;

    /**
     * 启动 BlogActivity
     * @param context :
     * @param userId :用户id
     * @param blogId : 博客Id
     * @param authorFace : 作者头像
     * @param authorName : 作者昵称
     * @param blogTitle : 博客标题
     * @param blogContentTime : 博客时间
     */
    public static void actionStart(Context context, String userId, String blogId, String authorFace,
                                   String authorName, String blogTitle, String blogContentTime) {
        Intent intent = new Intent(context, BlogActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        intent.putExtra(CommunityConstant.BLOG_ID, blogId);
        intent.putExtra(CommunityConstant.AUTHOR_FACE, authorFace);
        intent.putExtra(CommunityConstant.AUTHOR_NAME, authorName);
        intent.putExtra(CommunityConstant.BLOG_CONTENT_TIME, blogContentTime);
        intent.putExtra(CommunityConstant.BLOG_TITLE, blogTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        // 初始化控件
        blogToolbar = (Toolbar) findViewById(R.id.blog_toolbar);
        blogCommentRecyclerView = (RecyclerView) findViewById(R.id.blog_comment_recyclerview);
        blogTitle = (TextView) findViewById(R.id.blog_title);
        blogContentTime = (TextView) findViewById(R.id.blog_content_time);
        blogAuthorFace = (ImageView) findViewById(R.id.blog_author_face);
        blogAuthorName = (TextView) findViewById(R.id.blog_author_name);
        blogFollowAuthorButton = (Button) findViewById(R.id.blog_follow_author_button);
        blogContent = (RichTextView) findViewById(R.id.blog_content);
        blogBottomNavCommentButton = (Button) findViewById(R.id.bottom_nav_comment);
        moveToComment = (Button) findViewById(R.id.move_to_comment);
        blogBottomNavCollect = (Button) findViewById(R.id.bottom_nav_collect);
        blogBottomNavForward = (Button) findViewById(R.id.bottom_nav_forward);
        blogScrollView = (NestedScrollView) findViewById(R.id.blog_scroll_view);
        blogCommentTitle = (TextView) findViewById(R.id.blog_comment_title);

        // 设置点击事件
        blogAuthorFace.setOnClickListener(this);
        blogAuthorName.setOnClickListener(this);
        blogFollowAuthorButton.setOnClickListener(this);
        blogBottomNavCommentButton.setOnClickListener(this);
        blogBottomNavCollect.setOnClickListener(this);
        blogBottomNavForward.setOnClickListener(this);
        moveToComment.setOnClickListener(this);

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);
        blogId = intent.getStringExtra(CommunityConstant.BLOG_ID);
        blogAuthorName.setText(intent.getStringExtra(CommunityConstant.AUTHOR_NAME));
        blogTitle.setText(intent.getStringExtra(CommunityConstant.BLOG_TITLE));
        blogContentTime.setText(intent.getStringExtra(CommunityConstant.BLOG_CONTENT_TIME));
        String authorFace = getIntent().getStringExtra(CommunityConstant.AUTHOR_FACE);
        Glide.with(this)
                .load(ServerUrlConstant.SERVER_URI + authorFace)
                .into(blogAuthorFace);

        setSupportActionBar(blogToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        blogCommentRecyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(this, this);
        blogCommentRecyclerView.setAdapter(commentAdapter);

        // 请求服务器获取数据
        requestDataFromServer();

    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击作者
            case R.id.blog_author_face:
            case R.id.blog_author_name:
                //TODO
                Toast.makeText(this, "click author", Toast.LENGTH_SHORT).show();
                break;

            // 关注作者
            case R.id.blog_follow_author_button:
                //TODO
                Toast.makeText(this, "click follow", Toast.LENGTH_SHORT).show();
                break;

            // 在活动内容滑动到评论区，在评论区滑动到博客内容
            case R.id.move_to_comment:
                int[] position = new int[2];
                blogCommentTitle.getLocationOnScreen(position);
                // 滑动到评论区
                if (position[1] > 0) {
                    blogScrollView.smoothScrollTo(0, blogCommentTitle.getTop());
                }
                // 滑动到博客内容
                else  {
                    blogScrollView.smoothScrollTo(0, blogTitle.getTop());
                }
                break;

            // 评论
            case R.id.bottom_nav_comment:
                // TODO
                editCommentText("-1");
                break;

            // 收藏
            case R.id.bottom_nav_collect:
                //TODO
                Toast.makeText(this, "click collect", Toast.LENGTH_SHORT).show();
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
            // 返回
            case android.R.id.home:
                finish();
                break;

            case R.id.more_collect:
                // TODO:收藏该活动
                Toast.makeText(BlogActivity.this, "COLLECT",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.more_copy:
                // TODO：复制该活动链接
                Toast.makeText(BlogActivity.this, "copy",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.more_forward:
                CustomPopWindow.forwardPopWindow
                        (getWindow().getDecorView().findViewById(R.id.blog_comment_forward),
                                BlogActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 请求服务器获取博客数据, 并解析刷新UI
     */
    private void requestDataFromServer() {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.BLOG_ID, blogId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();
        HttpUtil.sendHttpPost(ServerUrlConstant.BLOG_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BlogActivity.this, HintConstant.GET_DATA_FAILED,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseText = response.body().string();
                    final BlogGson blogGson = JsonUtil.handleBlogResponse(responseText);
                    if (blogGson != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateActivityUI(blogGson);
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BlogActivity.this,
                                        HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BlogActivity.this,
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 刷新活动UI
     * @param blogGson:解析后的数据
     */
    private void updateActivityUI(final BlogGson blogGson) {
        this.blogGson = blogGson;
        if (blogGson.getFollow() == CommunityConstant.FOLLOW) {
            blogFollowAuthorButton.setText(CommunityConstant.UN_FOLLOW_TEXT);
        }
        else if (blogGson.getFollow() == CommunityConstant.UN_FOLLOW) {
            blogFollowAuthorButton.setText(CommunityConstant.FOLLOW_TEXT);
        }
        if (blogGson.getCollection() == HomeConstant.COLLECT) {
            blogBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_blue);
            moreCollect.setTitle(HomeConstant.COLLECT_TEXT);
        }
        else if (blogGson.getCollection() == HomeConstant.UN_COLLECT) {
            blogBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_black);
            moreCollect.setTitle(HomeConstant.UN_COLLECT_TEXT);
        }
        blogContent.post(new Runnable() {
            @Override
            public void run() {
                StringUtils.showContestContent(blogContent, blogGson.getContent());
            }
        });
        commentAdapter.setCommentsBeanList(blogGson.getComments());
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 编辑评论
     * @param toUserId:回复的用户id(若无则为-1)
     */
    public void editCommentText(String toUserId) {
        this.commentToUserId = toUserId;
        CustomPopWindow.PopWindowViewHelper popWindowViewHelper =
                CustomPopWindow.commentPopWindow(blogBottomNavCommentButton, this);
        commentEditText = popWindowViewHelper.editText;
        commentPopupWindow = popWindowViewHelper.popupWindow;
    }

    /**
     * 发表评论
     */
    public void publishComment() {
        // TODO
        commentPopupWindow.dismiss();
        String content = commentEditText.getText().toString();
        Toast.makeText(this, content + " to " + commentToUserId, Toast.LENGTH_SHORT).show();
    }

}
