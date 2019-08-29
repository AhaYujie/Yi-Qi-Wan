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

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.MineConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CommunityBlogListGson;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.gson.MyBLogGson;
import com.dalao.yiban.ui.adapter.CommunityBlogItemAdapter;
import com.dalao.yiban.ui.adapter.HomeItemAdapter;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MyCollectionActivity extends BaseActivity {

    private Button myCollectionBackButton;

    private RecyclerView myCollectionActivityRecyclerview;

    private RecyclerView myCollectionContestRecyclerview;

    private RecyclerView myCollectionBlogRecyclerview;

    private HomeItemAdapter activityListAdapter;

    private HomeItemAdapter contestListAdapter;

    private CommunityBlogItemAdapter blogListAdapter;

    private TextView myCollectionNotFoundLayout;

    private LinearLayout wrongPageLayout;

    private TextView wrongPageReload;

    private TabLayout myCollectionTablayout;

    private SwipeRefreshLayout myCollectionRefresh;

    private int categorySelected;

    private String userId;

    private HomeListGson activityListGson;

    private HomeListGson contestListGson;

    private CommunityBlogListGson blogListGson;

    /**
     *  启动函数
     * @param context：
     * @param userId：用户id
     */
    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, MyCollectionActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        // 初始化控件
        myCollectionBackButton = (Button) findViewById(R.id.my_collection_back_button);
        myCollectionActivityRecyclerview = (RecyclerView) findViewById(R.id.my_collection_activity_recyclerview);
        myCollectionContestRecyclerview = (RecyclerView) findViewById(R.id.my_collection_contest_recyclerview);
        myCollectionBlogRecyclerview = (RecyclerView) findViewById(R.id.my_collection_blog_recyclerview);
        myCollectionNotFoundLayout = (TextView) findViewById(R.id.my_collection_not_found_layout);
        wrongPageLayout = (LinearLayout) findViewById(R.id.wrong_page_layout);
        wrongPageReload = (TextView) findViewById(R.id.wrong_page_reload);
        myCollectionTablayout = (TabLayout) findViewById(R.id.my_collection_tablayout);
        myCollectionRefresh = (SwipeRefreshLayout) findViewById(R.id.my_collection_refresh);
        wrongPageLayout.setVisibility(View.GONE);
        myCollectionNotFoundLayout.setVisibility(View.GONE);
        categorySelected = myCollectionTablayout.getSelectedTabPosition();
        selectRecyclerView(categorySelected);

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);

        // 设置点击事件
        myCollectionBackButton.setOnClickListener(this);
        wrongPageReload.setOnClickListener(this);

        // 设置RecyclerView
        LinearLayoutManager activityLayoutManager = new LinearLayoutManager(this);
        myCollectionActivityRecyclerview.setLayoutManager(activityLayoutManager);
        activityListAdapter = new HomeItemAdapter(null, HomeConstant.SELECT_ACTIVITY, this, userId);
        myCollectionActivityRecyclerview.setAdapter(activityListAdapter);

        LinearLayoutManager contestLayoutManager = new LinearLayoutManager(this);
        myCollectionContestRecyclerview.setLayoutManager(contestLayoutManager);
        contestListAdapter = new HomeItemAdapter(null, HomeConstant.SELECT_CONTEST, this, userId);
        myCollectionContestRecyclerview.setAdapter(contestListAdapter);

        LinearLayoutManager blogLayoutManager = new LinearLayoutManager(this);
        myCollectionBlogRecyclerview.setLayoutManager(blogLayoutManager);
        blogListAdapter = new CommunityBlogItemAdapter(this, userId);
        myCollectionBlogRecyclerview.setAdapter(blogListAdapter);

        // 设置类别切换事件
        myCollectionTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                categorySelected = tab.getPosition();
                selectRecyclerView(categorySelected);
                requestDataFromServer();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                requestDataFromServer();
            }
        });

        myCollectionRefresh.setColorSchemeResources(R.color.colorPrimary);
        myCollectionRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            case R.id.my_collection_back_button:
                finish();
                break;
            case R.id.wrong_page_reload:
                requestDataFromServer();
                break;
            default:
                break;
        }
    }

    private void requestDataFromServer() {
        myCollectionRefresh.setRefreshing(true);

        String url;
        switch (categorySelected) {
            case HomeConstant.SELECT_ACTIVITY:
                url = ServerUrlConstant.MY_COLLECTION_ACTIVITY;
                break;
            case HomeConstant.SELECT_CONTEST:
                url = ServerUrlConstant.MY_COLLECTION_CONTEST;
                break;
            case HomeConstant.SELECT_BLOG:
                url = ServerUrlConstant.MY_COLLECTION_BLOG;
                break;
            default:
                return;
        }

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();

        Call call = HttpUtil.sendHttpPost(url, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myCollectionRefresh.setRefreshing(false);
                        wrongPageLayout.setVisibility(View.VISIBLE);
                        selectRecyclerView(HomeConstant.SELECT_NONE);
                        myCollectionNotFoundLayout.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    final String responseText = response.body().string();
                    switch (categorySelected) {
                        case HomeConstant.SELECT_ACTIVITY:
                            HomeListGson activityListGson = JsonUtil.handleHomeListResponse(responseText);
                            if (activityListGson == null || activityListGson.getData() == null ||
                                    activityListGson.getData().size() == 0) {
                                throw new NullPointerException();
                            }
                            MyCollectionActivity.this.activityListGson = activityListGson;
                            break;
                        case HomeConstant.SELECT_CONTEST:
                            HomeListGson contestListGson = JsonUtil.handleHomeListResponse(responseText);
                            if (contestListGson == null || contestListGson.getData() == null ||
                                    contestListGson.getData().size() == 0) {
                                throw new NullPointerException();
                            }
                            MyCollectionActivity.this.contestListGson = contestListGson;
                            break;
                        case HomeConstant.SELECT_BLOG:
                            CommunityBlogListGson blogListGson = JsonUtil.handleCommunityBlogListResponse(responseText);
                            if (blogListGson == null || blogListGson.getData() == null ||
                                    blogListGson.getData().size() == 0) {
                                throw new NullPointerException();
                            }
                            MyCollectionActivity.this.blogListGson = blogListGson;
                            break;
                        default:
                            return;
                    }
                    updateCollectionListUI();
                }
                catch (NullPointerException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myCollectionRefresh.setRefreshing(false);
                            selectRecyclerView(HomeConstant.SELECT_NONE);
                            wrongPageLayout.setVisibility(View.GONE);
                            myCollectionNotFoundLayout.setVisibility(View.VISIBLE);
                        }
                    });
                    e.printStackTrace();
                }
            }
        });

        getCallList().add(call);
    }

    /**
     * 更新收藏列表的UI
     */
    private void updateCollectionListUI() {
        myCollectionRefresh.setRefreshing(false);
        wrongPageLayout.setVisibility(View.GONE);
        myCollectionNotFoundLayout.setVisibility(View.GONE);
        selectRecyclerView(categorySelected);
        switch (categorySelected) {
            case HomeConstant.SELECT_ACTIVITY:
                activityListAdapter.setHomeListGson(activityListGson);
                activityListAdapter.notifyDataSetChanged();
                break;
            case HomeConstant.SELECT_CONTEST:
                contestListAdapter.setHomeListGson(contestListGson);
                contestListAdapter.notifyDataSetChanged();
                break;
            case HomeConstant.SELECT_BLOG:
                blogListAdapter.setDataBeanList(blogListGson.getData());
                blogListAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 选择显示的类别的RecyclerView
     * @param categorySelected：SELECT_ACTIVITY or SELECT_CONTEST or SELECT_BLOG or SELECT_NONE
     */
    private void selectRecyclerView(int categorySelected) {
        myCollectionActivityRecyclerview.setVisibility(View.GONE);
        myCollectionContestRecyclerview.setVisibility(View.GONE);
        myCollectionBlogRecyclerview.setVisibility(View.GONE);
        switch (categorySelected) {
            case HomeConstant.SELECT_ACTIVITY:
                myCollectionActivityRecyclerview.setVisibility(View.VISIBLE);
                break;
            case HomeConstant.SELECT_CONTEST:
                myCollectionContestRecyclerview.setVisibility(View.VISIBLE);
                break;
            case HomeConstant.SELECT_BLOG:
                myCollectionBlogRecyclerview.setVisibility(View.VISIBLE);
                break;
            case HomeConstant.SELECT_NONE:
                break;
            default:
                break;
        }
    }
}






















