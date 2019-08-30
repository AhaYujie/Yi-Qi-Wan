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
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.MyFollowingGson;
import com.dalao.yiban.my_interface.FollowInterface;
import com.dalao.yiban.ui.adapter.MyFollowingItemAdapter;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ViewFollowingActivity extends BaseActivity{

    private Button myFollowingBackButton;

    private RecyclerView myFollowingRecyclerView;

    private TextView myFollowingNotFoundLayout;

    private LinearLayout wrongPageLayout;

    private TextView wrongPageReload;

    private MyFollowingItemAdapter myFollowingItemAdapter;

    private SwipeRefreshLayout myFollowingRefresh;

    private String userId;

    private MyFollowingGson myFollowingGson;

    /**
     *  启动函数
     * @param context：
     * @param userId：用户id
     */
    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, ViewFollowingActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_following);

        // 初始化控件
        myFollowingBackButton = (Button) findViewById(R.id.my_following_back_button);
        myFollowingRecyclerView = (RecyclerView) findViewById(R.id.my_following_recyclerview);
        myFollowingNotFoundLayout = (TextView) findViewById(R.id.my_following_not_found_layout);
        wrongPageLayout = (LinearLayout) findViewById(R.id.wrong_page_layout);
        wrongPageReload = (TextView) findViewById(R.id.wrong_page_reload);
        myFollowingRefresh = (SwipeRefreshLayout) findViewById(R.id.my_following_refresh);
        myFollowingRecyclerView.setVisibility(View.VISIBLE);
        wrongPageLayout.setVisibility(View.GONE);
        myFollowingNotFoundLayout.setVisibility(View.GONE);

        // 从上个活动获取数据
        Intent intent = getIntent();
        userId = intent.getStringExtra(HomeConstant.USER_ID);

        // 设置点击事件
        myFollowingBackButton.setOnClickListener(this);
        wrongPageReload.setOnClickListener(this);

        // 设置RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        myFollowingRecyclerView.setLayoutManager(linearLayoutManager);
        myFollowingItemAdapter = new MyFollowingItemAdapter(this, userId);
        myFollowingRecyclerView.setAdapter(myFollowingItemAdapter);

        // 设置下拉刷新
        myFollowingRefresh.setColorSchemeResources(R.color.colorPrimary);
        myFollowingRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDataFromServer();
            }
        });

        requestDataFromServer();
    }

    /**
     * 点击事件
     * @param v:
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_following_back_button:
                finish();
                break;
            case R.id.wrong_page_reload:
                requestDataFromServer();
                break;
            default:
                break;
        }
    }

    /**
     * 从服务器获取数据
     */
    private void requestDataFromServer() {
        myFollowingRefresh.setRefreshing(true);

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();

        Call call = HttpUtil.sendHttpPost(ServerUrlConstant.MY_FOLLOWING_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myFollowingRefresh.setRefreshing(false);
                        // 如果无数据则显示错误页面
                        if (myFollowingGson == null || myFollowingGson.getData().size() == 0) {
                            myFollowingRecyclerView.setVisibility(View.GONE);
                            myFollowingNotFoundLayout.setVisibility(View.GONE);
                            wrongPageLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String responseText = response.body().string();
                    MyFollowingGson myFollowingGson = JsonUtil.handleMyFollowingResponse(responseText);
                    // 无数据
                    if (myFollowingGson == null || myFollowingGson.getData() == null ||
                            myFollowingGson.getData().size() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myFollowingRefresh.setRefreshing(false);
                                myFollowingRecyclerView.setVisibility(View.GONE);
                                wrongPageLayout.setVisibility(View.GONE);
                                myFollowingNotFoundLayout.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    // 有数据
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ViewFollowingActivity.this.myFollowingGson = myFollowingGson;
                                updateMyFollowingListUI();
                            }
                        });
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myFollowingRefresh.setRefreshing(false);
                            myFollowingRecyclerView.setVisibility(View.GONE);
                            wrongPageLayout.setVisibility(View.GONE);
                            myFollowingNotFoundLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        getCallList().add(call);
    }

    /**
     * 更新我关注的人列表UI
     */
    private void updateMyFollowingListUI() {
        for (MyFollowingGson.DataBean dataBean : myFollowingGson.getData()) {
            dataBean.setFollowing(false);
            dataBean.setIsFollow(CommunityConstant.FOLLOW);
        }
        myFollowingItemAdapter.setMyFollowingGson(myFollowingGson);
        myFollowingItemAdapter.notifyDataSetChanged();
        myFollowingRefresh.setRefreshing(false);
        myFollowingRecyclerView.setVisibility(View.VISIBLE);
        myFollowingNotFoundLayout.setVisibility(View.GONE);
        wrongPageLayout.setVisibility(View.GONE);
    }

}
























