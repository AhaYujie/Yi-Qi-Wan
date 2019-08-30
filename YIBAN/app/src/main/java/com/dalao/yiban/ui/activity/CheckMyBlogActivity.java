package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CommunityBlogListGson;
import com.dalao.yiban.gson.MyBLogGson;
import com.dalao.yiban.ui.adapter.CommunityBlogItemAdapter;
import com.dalao.yiban.ui.fragment.CommunityFragment;
import com.dalao.yiban.ui.fragment.MineFragment;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CheckMyBlogActivity extends BaseActivity {

    private Button myBlogBackButtonl;

    private RecyclerView myBlogRecyclerview;

    private TextView myBlogNoBlogLayout;

    private CommunityBlogItemAdapter communityBlogItemAdapter;

    private SwipeRefreshLayout myBlogRefresh;

    private LinearLayout wrongPageLayout;

    private TextView wrongPageReload;

    private MyBLogGson myBLogGson;

    private String userId;

    private String userName;

    /**
     *  启动函数
     * @param context：
     * @param userId：用户id
     * @param userName : 用户昵称
     */
    public static void actionStart(Context context, String userId, String userName) {
        Intent intent = new Intent(context, CheckMyBlogActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        intent.putExtra(MineConstant.USER_NICKNAME, userName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_my_blog);

        // 初始化控件
        myBlogBackButtonl = (Button) findViewById(R.id.my_blog_back_button);
        myBlogRecyclerview = (RecyclerView) findViewById(R.id.my_blog_recyclerview);
        myBlogNoBlogLayout = (TextView) findViewById(R.id.my_blog_no_blog_layout);
        myBlogRefresh = (SwipeRefreshLayout) findViewById(R.id.my_blog_refresh);
        wrongPageLayout = (LinearLayout) findViewById(R.id.wrong_page_layout);
        wrongPageReload = (TextView) findViewById(R.id.wrong_page_reload);
        myBlogRecyclerview.setVisibility(View.VISIBLE);
        myBlogNoBlogLayout.setVisibility(View.GONE);
        wrongPageLayout.setVisibility(View.GONE);

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);
        userName = intent.getStringExtra(MineConstant.USER_NICKNAME);

        // 设置点击事件
        myBlogBackButtonl.setOnClickListener(this);
        wrongPageReload.setOnClickListener(this);

        // 设置RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        myBlogRecyclerview.setLayoutManager(linearLayoutManager);
        communityBlogItemAdapter = new CommunityBlogItemAdapter(this, userId);
        myBlogRecyclerview.setAdapter(communityBlogItemAdapter);

        // 设置swipe refresh事件
        myBlogRefresh.setColorSchemeResources(R.color.colorPrimary);
        myBlogRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cancelCall();
                requestDataFromServer();
            }
        });

        requestDataFromServer();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.my_blog_back_button:
                finish();
                break;

            // 错误页面刷新
            case R.id.wrong_page_reload:
                requestDataFromServer();
                break;
            default:
                break;
        }
    }

    /**
     * 从服务器获取我的博客列表
     */
    private void requestDataFromServer() {
        myBlogRefresh.setRefreshing(true);

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();

        Call call = HttpUtil.sendHttpPost(ServerUrlConstant.MY_BLOG_LIST_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myBlogRefresh.setRefreshing(false);
                        wrongPageLayout.setVisibility(View.VISIBLE);
                        myBlogRecyclerview.setVisibility(View.GONE);
                        myBlogNoBlogLayout.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    final String responseText = response.body().string();
                    final MyBLogGson myBLogGson =
                            JsonUtil.handleMyBlogResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 无数据
                            if (myBLogGson == null || myBLogGson.getData() == null ||
                                    myBLogGson.getData().size() == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myBlogRefresh.setRefreshing(false);
                                        myBlogRecyclerview.setVisibility(View.GONE);
                                        wrongPageLayout.setVisibility(View.GONE);
                                        myBlogNoBlogLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            // 有数据
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CheckMyBlogActivity.this.myBLogGson = myBLogGson;
                                        updateBlogListUI();
                                    }
                                });
                            }
                        }
                    });
                }
                catch (NullPointerException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myBlogRefresh.setRefreshing(false);
                            myBlogRecyclerview.setVisibility(View.GONE);
                            wrongPageLayout.setVisibility(View.GONE);
                            myBlogNoBlogLayout.setVisibility(View.VISIBLE);
                        }
                    });
                    e.printStackTrace();
                }
            }
        });

        getCallList().add(call);
    }

    /**
     * 更新博客列表UI
     */
    private void updateBlogListUI() {
        myBlogRefresh.setRefreshing(false);
        myBlogRecyclerview.setVisibility(View.VISIBLE);
        wrongPageLayout.setVisibility(View.GONE);
        myBlogNoBlogLayout.setVisibility(View.GONE);
        communityBlogItemAdapter.setDataBeanList(myBlogGsonToCommunityBlogListGson(myBLogGson).getData());
        communityBlogItemAdapter.notifyDataSetChanged();
    }

    /**
     * 将MyBLogGson转换为CommunityBlogListGson
     * @param myBLogGson：
     * @return :
     */
    private CommunityBlogListGson myBlogGsonToCommunityBlogListGson(MyBLogGson myBLogGson) {
        CommunityBlogListGson communityBlogListGson = new CommunityBlogListGson();
        List<CommunityBlogListGson.DataBean> dataBeanList = new ArrayList<>();
        communityBlogListGson.setData(dataBeanList);
        for (MyBLogGson.dataBean myblogsBean : myBLogGson.getData()) {
            CommunityBlogListGson.DataBean dataBean = new CommunityBlogListGson.DataBean();
            dataBean.setAuthor(userName);
            dataBean.setAuthorid(Integer.valueOf(userId));
            dataBean.setAvatar(myblogsBean.getAvatar());
            dataBean.setId(myblogsBean.getId());
            dataBean.setPageviews(myblogsBean.getPageviews());
            dataBean.setTime(myblogsBean.getTime());
            dataBean.setTitle(myblogsBean.getTitle());
            communityBlogListGson.getData().add(dataBean);
        }
        return communityBlogListGson;
    }

}
