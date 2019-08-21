package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CreateBlogGson;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;
import com.dalao.yiban.util.StringUtils;
import com.sendtion.xrichtext.RichTextEditor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CreateBlogActivity extends BaseActivity {

    private String userId;

    private Button createBlogCancelButton;

    private TextView createBlogPublish;

    private EditText createBlogContentTitleEditText;

    private RichTextEditor createBlogContentEditText;

    private Button createBlogBottomNavPicButton;

    private String createBlogContentString;

    /**
     * 启动创建博客活动
     * @param context :
     * @param userId : 用户id
     */
    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, CreateBlogActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        // 初始化控件
        createBlogCancelButton = (Button) findViewById(R.id.create_blog_cancel_button);
        createBlogPublish = (TextView) findViewById(R.id.create_blog_publish);
        createBlogContentTitleEditText = (EditText) findViewById(R.id.create_blog_content_title_edit_text);
        createBlogContentEditText = (RichTextEditor) findViewById(R.id.create_blog_content_edit_text);
        createBlogBottomNavPicButton = (Button) findViewById(R.id.create_blog_bottom_nav_pic_button);

        // 设置点击事件
        createBlogCancelButton.setOnClickListener(this);
        createBlogPublish.setOnClickListener(this);
        createBlogBottomNavPicButton.setOnClickListener(this);

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消创建博客, 返回到社区界面
            case R.id.create_blog_cancel_button:
                finish();
                break;

            // 发表博客, 启动博客内容活动
            case R.id.create_blog_publish:
                createBlogContentString = StringUtils.getEditData(createBlogContentEditText);
                // 博客标题为空
                if (createBlogContentTitleEditText.getText().toString().equals("")) {
                Toast.makeText(this, HintConstant.CREATE_BLOG_TITLE_EMPTY,
                        Toast.LENGTH_SHORT).show();
                }
                // 博客内容过少
                else if (createBlogContentString.length() < CommunityConstant.CREATE_BLOG_CONTENT_MIN_LENGTH) {
                    Toast.makeText(this, HintConstant.CREATE_BLOG_CONTENT_LESS,
                            Toast.LENGTH_SHORT).show();
                }
                // 博客内容过多
                else if (createBlogContentString.length() > CommunityConstant.CREATE_BLOG_CONTENT_MAX_LENGTH) {
                    Toast.makeText(this, HintConstant.CREATE_BLOG_CONTENT_MORE,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    postBlogDataToServer();
                }
                break;

            // 选择图片
            case R.id.create_blog_bottom_nav_pic_button:
                //TODO:选择图片
                break;

            default:
                break;
        }
    }

    /**
     * 发送博客内容到服务器，请求创建博客
     */
    private void postBlogDataToServer() {
        FormBody formBody = new FormBody.Builder()
                .add(ServerPostDataConstant.USER_ID, userId)
                .add(ServerPostDataConstant.CREATE_BLOG_TITLE,
                        createBlogContentTitleEditText.getText().toString())
                .add(ServerPostDataConstant.CREATE_BLOG_CONTENT, createBlogContentString)
                .build();

        HttpUtil.sendHttpPost(ServerUrlConstant.CREATE_BLOG_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CreateBlogActivity.this.getCallList().add(call);
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreateBlogActivity.this,
                                HintConstant.CREATE_BLOG_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    CreateBlogActivity.this.getCallList().add(call);
                    String responseText = response.body().string();
                    CreateBlogGson createBlogGson = JsonUtil.handleCreateBlogResponse(responseText);
                    if (createBlogGson.getMsg().equals(CommunityConstant.CREATE_BLOG_SUCCESS)) {
                        // TODO:创建博客成功，跳转到博客内容活动
                        finish();
                    }
                    else if (createBlogGson.getMsg().equals(CommunityConstant.CREATE_BLOG_ERROR)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CreateBlogActivity.this,
                                        HintConstant.CREATE_BLOG_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CreateBlogActivity.this,
                                    HintConstant.CREATE_BLOG_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
