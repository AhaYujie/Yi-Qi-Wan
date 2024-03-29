package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.BlogGson;
import com.dalao.yiban.my_interface.CollectInterface;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.my_interface.FollowInterface;
import com.dalao.yiban.my_interface.RequestDataInterface;
import com.dalao.yiban.ui.adapter.CommentAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.ui.custom.GlideSimpleLoader;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.ImageUtils;
import com.dalao.yiban.util.JsonUtil;
import com.dalao.yiban.util.StringUtils;
import com.github.ielse.imagewatcher.ImageWatcherHelper;
import com.sendtion.xrichtext.RichTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class BlogActivity extends ContentActivity implements CommentInterface, FollowInterface,
        CollectInterface, RequestDataInterface {

    private CommentAdapter commentAdapter;

    private TextView blogTitle;

    private TextView blogContentTime;

    private ImageView blogAuthorFace;

    private TextView blogAuthorName;

    private Button blogFollowAuthorButton;

    private RichTextView blogContentRichText;

    private Button blogBottomNavCommentButton;

    private Button moveToComment;

    private Button blogBottomNavCollect;

    private Button blogBottomNavForward;

    private NestedScrollView blogScrollView;

    private TextView blogCommentTitle;

    private String commentToCommentId;

    private EditText commentEditText;

    private PopupWindow commentPopupWindow;

    private LinearLayout wrongPageLayout;

    private TextView wrongPageReload;

    private RelativeLayout blogContentLayout;

    private BlogGson blogGson;

    private String blogId;

    //private String userId;

    private String authorId;

    private ImageWatcherHelper imageWatcherHelper;

    private String blogContentString;

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
                                   String authorName, String blogTitle, String blogContentTime,
                                   String authorId) {
        Intent intent = new Intent(context, BlogActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        intent.putExtra(CommunityConstant.BLOG_ID, blogId);
        intent.putExtra(CommunityConstant.AUTHOR_FACE, authorFace);
        intent.putExtra(CommunityConstant.AUTHOR_NAME, authorName);
        intent.putExtra(CommunityConstant.BLOG_CONTENT_TIME, blogContentTime);
        intent.putExtra(CommunityConstant.BLOG_TITLE, blogTitle);
        intent.putExtra(CommunityConstant.AUTHOR_ID, authorId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        // 初始化控件
        Toolbar blogToolbar = (Toolbar) findViewById(R.id.blog_toolbar);
        RecyclerView blogCommentRecyclerView = (RecyclerView) findViewById(R.id.blog_comment_recyclerview);
        blogTitle = (TextView) findViewById(R.id.blog_title);
        blogContentTime = (TextView) findViewById(R.id.blog_content_time);
        blogAuthorFace = (ImageView) findViewById(R.id.blog_author_face);
        blogAuthorName = (TextView) findViewById(R.id.blog_author_name);
        blogFollowAuthorButton = (Button) findViewById(R.id.blog_follow_author_button);
        blogContentRichText = (RichTextView) findViewById(R.id.blog_content);
        blogBottomNavCommentButton = (Button) findViewById(R.id.bottom_nav_comment);
        moveToComment = (Button) findViewById(R.id.move_to_comment);
        blogBottomNavCollect = (Button) findViewById(R.id.bottom_nav_collect);
        blogBottomNavForward = (Button) findViewById(R.id.bottom_nav_forward);
        blogScrollView = (NestedScrollView) findViewById(R.id.blog_scroll_view);
        blogCommentTitle = (TextView) findViewById(R.id.blog_comment_title);
        imageWatcherHelper = ImageWatcherHelper.with(this, new GlideSimpleLoader());
        wrongPageLayout = (LinearLayout) findViewById(R.id.wrong_page_layout);
        wrongPageReload = (TextView) findViewById(R.id.wrong_page_reload);
        blogContentLayout = (RelativeLayout) findViewById(R.id.blog_content_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        noMoreCommentLayout = (TextView) findViewById(R.id.no_more_comment_layout);
        wrongPageLayout.setVisibility(View.GONE);
        blogContentLayout.setVisibility(View.VISIBLE);

        // 设置错误页面点击事件
        wrongPageReload.setOnClickListener(this);

        // 从上个活动获取数据
        Intent intent = getIntent();
        authorId = intent.getStringExtra(CommunityConstant.AUTHOR_ID);
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

        // 设置RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        blogCommentRecyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(this, this,
                blogId, HomeConstant.SELECT_BLOG, commentsLoadingLayout);
        blogCommentRecyclerView.setAdapter(commentAdapter);

        // 设置NestedScrollView滑动监听事件
        blogScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 滑动到底部，加载更多评论
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    commentAdapter.loadMoreComments(ServerUrlConstant.BLOG_URI,
                            ServerPostDataConstant.BLOG_ID, blogId);
                }
            }
        });

        // 图片点击事件
        blogContentRichText.setOnRtImageClickListener(new RichTextView.OnRtImageClickListener() {
            @Override
            public void onRtImageClick(View view, String imagePath) {
                try {
                    ArrayList<String> imageList = StringUtils.getTextFromHtml(blogContentString, true);
                    int currentPosition = imageList.indexOf(imagePath);

                    List<Uri> dataList = new ArrayList<>();
                    for (int i = 0; i < imageList.size(); i++) {
                        dataList.add(ImageUtils.getUriFromPath(imageList.get(i)));
                    }
                    imageWatcherHelper.show(dataList, currentPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        // 请求服务器获取数据
        requestDataFromServer(true, true);

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
                // 取消功能
                break;

            // 关注或者取消关注作者
            case R.id.blog_follow_author_button:
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                // 关注
                if (blogGson.getFollow() == CommunityConstant.UN_FOLLOW) {
                    HttpUtil.followBlogAuthor(this, null, this,
                            userId, authorId, HomeConstant.BLOG_ACTIVITY, CommunityConstant.FOLLOW);
                }
                // 取消关注
                else if (blogGson.getFollow() == CommunityConstant.FOLLOW) {
                    HttpUtil.followBlogAuthor(this, null, this,
                            userId, authorId, HomeConstant.BLOG_ACTIVITY, CommunityConstant.UN_FOLLOW);
                }
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

            // 收藏或取消收藏
            case R.id.bottom_nav_collect:
                // 游客禁止使用此功能
                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                HttpUtil.collectContent(this, HomeConstant.SELECT_BLOG, userId,
                        blogId, blogGson.getCollection(), this);
                break;

            // 转发
            case R.id.bottom_nav_forward:
                CustomPopWindow.forwardPopWindow(v, this);
                break;

            // 错误页面点击事件
            case R.id.wrong_page_reload:
                wrongPageLayout.setVisibility(View.GONE);
                blogContentLayout.setVisibility(View.VISIBLE);
                requestDataFromServer(true, true);
            default:
                break;
        }
    }

//    /**
//     * 创建菜单栏
//     * @param menu:
//     * @return :
//     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.more_menu, menu);
//        this.menu = menu;
//        this.moreCollect = menu.findItem(R.id.more_collect);
//        // 获取数据前隐藏menu
//        menu.setGroupVisible(R.id.more_group, false);
//        return super.onCreateOptionsMenu(menu);
//    }

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

//            // 收藏或取消收藏
//            case R.id.more_collect:
//                // 游客禁止使用此功能
//                if (userId.equals(HomeConstant.VISITOR_USER_ID)) {
//                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                HttpUtil.collectContent(this, HomeConstant.SELECT_BLOG, userId,
//                        blogId, blogGson.getCollection());
//                break;
//
//            case R.id.more_copy:
//                break;
//
//            // 转发button弹出PopWindow
//            case R.id.more_forward:
//                CustomPopWindow.forwardPopWindow
//                        (getWindow().getDecorView().findViewById(R.id.bottom_nav_forward),
//                                BlogActivity.this);
//                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 请求服务器获取博客数据, 并解析刷新UI
     * @param updateContent : true则更新内容UI
     * @param updateComment : true则更新评论区UI
     */
    @Override
    public void requestDataFromServer(final boolean updateContent, final boolean updateComment) {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.BLOG_ID, blogId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();

        Call call = HttpUtil.sendHttpPost(ServerUrlConstant.BLOG_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wrongPageLayout.setVisibility(View.VISIBLE);
                        blogContentLayout.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.body() == null) {
                        throw new NullPointerException();
                    }
                    String responseText = response.body().string();
                    final BlogGson blogGson = JsonUtil.handleBlogResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BlogActivity.this.blogGson = blogGson;
                            if (updateContent) {
                                updateBlogContentUI();
                            }
                            if (updateComment) {
                                updateBlogCommentUI();
                            }
                        }
                    });
                }
                catch (NullPointerException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BlogActivity.this,
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        BlogActivity.this.getCallList().add(call);
    }

    /**
     * 刷新博客内容UI
     */
    private void updateBlogContentUI() {
        wrongPageLayout.setVisibility(View.GONE);
        blogContentLayout.setVisibility(View.VISIBLE);
        // 设置点击事件
        blogAuthorFace.setOnClickListener(this);
        blogAuthorName.setOnClickListener(this);
        blogFollowAuthorButton.setOnClickListener(this);
        blogBottomNavCommentButton.setOnClickListener(this);
        blogBottomNavCollect.setOnClickListener(this);
        blogBottomNavForward.setOnClickListener(this);
        moveToComment.setOnClickListener(this);
        blogContentString = blogGson.getContent();
        // 刷新UI
        blogTitle.setText(blogGson.getTitle());
        blogContentTime.setText(blogGson.getTime());
        blogAuthorName.setText(blogGson.getAuthor());
        authorId = String.valueOf(blogGson.getAuthorId());
        if (blogGson.getFollow() == CommunityConstant.FOLLOW) {
            blogFollowAuthorButton.setText(CommunityConstant.UN_FOLLOW_TEXT);
        }
        else if (blogGson.getFollow() == CommunityConstant.UN_FOLLOW) {
            blogFollowAuthorButton.setText(CommunityConstant.FOLLOW_TEXT);
        }
        if (blogGson.getCollection() == HomeConstant.COLLECT) {
            blogBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_blue);
        }
        else if (blogGson.getCollection() == HomeConstant.UN_COLLECT) {
            blogBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_black);
        }
        blogContentRichText.post(new Runnable() {
            @Override
            public void run() {
                StringUtils.showContent(blogContentRichText, blogGson.getContent());
            }
        });
    }

    /**
     * 刷新博客评论区UI
     */
    private void updateBlogCommentUI() {
        commentAdapter.setCommentsBeanList(blogGson.getComments());
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 编辑评论
     * @param toCommentId:回复的评论的id(若无则为-1)
     */
    public void editCommentText(String toCommentId) {
        this.commentToCommentId = toCommentId;
        CustomPopWindow.PopWindowViewHelper popWindowViewHelper =
                CustomPopWindow.commentPopWindow(blogBottomNavCommentButton, this);
        commentEditText = popWindowViewHelper.editText;
        commentPopupWindow = popWindowViewHelper.popupWindow;
    }

    /**
     * 发表评论成功, 更新UI
     */
    public void publishComment() {
        String content = commentEditText.getText().toString();
        if ("".equals(content)) {
            // 评论为空，不能发表
            Toast.makeText(this, HintConstant.COMMENT_NOT_EMPTY, Toast.LENGTH_SHORT).show();
            return;
        }
        commentPopupWindow.dismiss();
        HttpUtil.comment(this, commentAdapter, blogId, userId,
                commentToCommentId, content, HomeConstant.SELECT_BLOG);
    }

    /**
     * 收藏博客成功, 更新UI
     */
    @Override
    public void collectSuccess() {
        Toast.makeText(this, HintConstant.COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
        blogBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_blue);
        blogGson.setCollection(HomeConstant.COLLECT);
        blogBottomNavCollect.setClickable(true);
    }

    /**
     * 取消收藏博客成功，更新UI
     */
    @Override
    public void unCollectSuccess() {
        Toast.makeText(this, HintConstant.UN_COLLECT_SUCCESS, Toast.LENGTH_SHORT).show();
        blogBottomNavCollect.setBackgroundResource(R.drawable.ic_collect_black);
        blogGson.setCollection(HomeConstant.UN_COLLECT);
        blogBottomNavCollect.setClickable(true);
    }

    /**
     * 收藏或取消收藏失败
     */
    @Override
    public void collectError() {
        Toast.makeText(this, HintConstant.COLLECT_ERROR, Toast.LENGTH_SHORT).show();
        blogBottomNavCollect.setClickable(true);
    }

    /**
     * 进行收藏或取消收藏操作
     */
    @Override
    public void collectStart() {
        blogBottomNavCollect.setClickable(false);
    }

    /**
     * 关注成功, 更新UI，数据库
     */
    @Override
    public void followSucceed(String userId) {
        blogFollowAuthorButton.setText(CommunityConstant.UN_FOLLOW_TEXT);
        Toast.makeText(this, HintConstant.BLOG_AUTHOR_FOLLOW_SUCCESS, Toast.LENGTH_SHORT).show();
        blogGson.setFollow(CommunityConstant.FOLLOW);
        blogFollowAuthorButton.setClickable(true);
    }

    /**
     * 取消关注成功, 更新UI，数据库
     */
    @Override
    public void unFollowSucceed(String userId) {
        blogFollowAuthorButton.setText(CommunityConstant.FOLLOW_TEXT);
        Toast.makeText(this, HintConstant.BLOG_AUTHOR_UN_FOLLOW_SUCCESS, Toast.LENGTH_SHORT).show();
        blogGson.setFollow(CommunityConstant.UN_FOLLOW);
        blogFollowAuthorButton.setClickable(true);
    }

    /**
     * 关注或取消关注失败
     */
    @Override
    public void followError(String userId) {
        Toast.makeText(this, HintConstant.BLOG_AUTHOR_FOLLOW_ERROR, Toast.LENGTH_SHORT).show();
        blogFollowAuthorButton.setClickable(true);
    }

    /**
     * 进行关注或取消关注操作
     */
    @Override
    public void followStart(String userId) {
        blogFollowAuthorButton.setClickable(false);
    }

    @Override
    public void onBackPressed() {
        if (!imageWatcherHelper.handleBackPressed()) {
            super.onBackPressed();
        }
    }

}
