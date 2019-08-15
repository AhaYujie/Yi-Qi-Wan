package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.sendtion.xrichtext.RichTextEditor;

public class CreateBlogActivity extends BaseActivity {

    private String userId;

    private Button createBlogCancelButton;

    private TextView createBlogPublish;

    private EditText createBlogContentTitleEditText;

    private RichTextEditor createBlogContentEditText;

    private Button createBlogBottomNavPicButton;

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
            // 取消创建博客
            case R.id.create_blog_cancel_button:
                finish();
                break;

            // 发表博客
            case R.id.create_blog_publish:
                //TODO:请求服务器，返回到上个活动
                break;

            // 选择图片
            case R.id.create_blog_bottom_nav_pic_button:
                //TODO:选择图片
                break;

            default:
                break;
        }
    }

}
