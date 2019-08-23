package com.dalao.yiban.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.db.Activity;
import com.dalao.yiban.db.Blog;
import com.dalao.yiban.db.CollectBlog;
import com.dalao.yiban.db.Contest;
import com.dalao.yiban.db.SearchResult;
import com.dalao.yiban.db.UsedSearch;
import com.dalao.yiban.ui.adapter.CollectionActivityAdapter;
import com.dalao.yiban.ui.adapter.CollectionBlogAdapter;
import com.dalao.yiban.ui.adapter.CollectionContestAdapter;
import com.dalao.yiban.ui.adapter.UsedSearchAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollectionActivity extends BaseActivity {

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

    private List<SearchResult> ActivityList = new ArrayList<>();
    private List<CollectBlog> BlogList = new ArrayList<>();
    private List<SearchResult> ContestList = new ArrayList<>();

    private RecyclerView recyclerView_Blog;
    private RecyclerView recyclerView_Contest;
    private RecyclerView recyclerView_Activity;

    //从上一个活动获取数据
    Intent intent = getIntent();
    String user_id = intent.getStringExtra(HomeConstant.USER_ID);
    int userid=Integer.parseInt(user_id);

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_collection);

        //initData();//初始化数据
        TextView activity_notfound = (TextView) findViewById(R.id.collection_activity_notfound);
        TextView blog_notfound = (TextView) findViewById(R.id.collection_blog_notfound);
        TextView contest_notfound = (TextView) findViewById(R.id.collection_contest_notfound);

        TextView isVisiter = (TextView) findViewById(R.id.collection_isVisit);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.my_collection_ProgressBar);


        //博客
        RecyclerView recyclerView_Blog = (RecyclerView) findViewById(R.id.my_collection_blog_RecyclerView);
        LinearLayoutManager layoutManager_Blog = new LinearLayoutManager(this);
        recyclerView_Blog.setLayoutManager(layoutManager_Blog);
        final CollectionBlogAdapter adapter_Blog = new CollectionBlogAdapter(BlogList);
        recyclerView_Blog.setAdapter(adapter_Blog);

        //活动
        RecyclerView recyclerView_Activity = (RecyclerView) findViewById(R.id.my_collection_activity_RecyclerView);
        LinearLayoutManager layoutManager_Activity = new LinearLayoutManager(this);
        recyclerView_Activity.setLayoutManager(layoutManager_Activity);
        final CollectionActivityAdapter adapter_Activity = new CollectionActivityAdapter(ActivityList);
        recyclerView_Activity.setAdapter(adapter_Activity);

        //竞赛
        RecyclerView recyclerView_Contest = (RecyclerView) findViewById(R.id.my_collection_contest_RecyclerView);
        LinearLayoutManager layoutManager_Contest = new LinearLayoutManager(this);
        recyclerView_Contest.setLayoutManager(layoutManager_Contest);
        final CollectionContestAdapter adapter_Contest = new CollectionContestAdapter(ContestList);
        recyclerView_Contest.setAdapter(adapter_Contest);

        BlogList.clear();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView_Activity.setVisibility(View.GONE);
        recyclerView_Blog.setVisibility(View.GONE);
        recyclerView_Contest.setVisibility(View.GONE);

        //博客初始化
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //以下是POST方法
                    HashMap<String, String> paramsMap_blog = new HashMap<>();//用哈希表来存参数
                    paramsMap_blog.put("userid", Integer.toString(userid));
                    FormBody.Builder builder_blog = new FormBody.Builder();
                    for (String key : paramsMap_blog.keySet()) {
                        //追加表单信息
                        builder_blog.add(key, paramsMap_blog.get(key));
                    }
                    OkHttpClient okHttpClient_blog = new OkHttpClient();
                    RequestBody formBody_blog = builder_blog.build();
                    Request request_blog = new Request.Builder().url("http://188888888.xyz:5000/user/colblog").post(formBody_blog).build();
                    Response response_blog = okHttpClient_blog.newCall(request_blog).execute();
                    String responseData_blog = response_blog.body().string();
                    //以下是解析json


                    try {
                        JSONObject jsnobject = new JSONObject(responseData_blog);
                        JSONArray jsonArray = jsnobject.getJSONArray("myblogs");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String time = jsonObject.getString("time");
                            String title = jsonObject.getString("title");
                            int id = jsonObject.getInt("id");
                            int pageviews = jsonObject.getInt("pageviews");
                            CollectBlog content = new CollectBlog(title,time,pageviews,id,userid);
                            BlogList.add(content);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(BlogList.size()!=0){
                            progressBar.setVisibility(View.GONE);
                            recyclerView_Activity.setVisibility(View.GONE);
                            recyclerView_Blog.setVisibility(View.VISIBLE);
                            recyclerView_Contest.setVisibility(View.GONE);
                            adapter_Blog.notifyDataSetChanged();
                        }else {
                            progressBar.setVisibility(View.GONE);
                            blog_notfound.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        }).start();

        //TabLayout切换事件
        TabLayout tabLayout = (TabLayout) findViewById(R.id.my_collection_TabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                contest_notfound.setVisibility(View.GONE);
                blog_notfound.setVisibility(View.GONE);
                activity_notfound.setVisibility(View.GONE);

                Log.e("QAQb",tab.getText().toString());
                if(tab.getText().toString().equals("活动")) {

                    ActivityList.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView_Activity.setVisibility(View.GONE);
                    recyclerView_Blog.setVisibility(View.GONE);
                    recyclerView_Contest.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //以下是POST方法
                                HashMap<String, String> paramsMap_activity = new HashMap<>();//用哈希表来存参数
                                paramsMap_activity.put("userid", Integer.toString(userid));
                                FormBody.Builder builder_activity = new FormBody.Builder();
                                for (String key : paramsMap_activity.keySet()) {
                                    //追加表单信息
                                    builder_activity.add(key, paramsMap_activity.get(key));
                                }
                                OkHttpClient okHttpClient_activity = new OkHttpClient();
                                RequestBody formBody_activity = builder_activity.build();
                                Request request_activity = new Request.Builder().url("http://188888888.xyz:5000/user/colactivity").post(formBody_activity).build();
                                Response response_activity = okHttpClient_activity.newCall(request_activity).execute();
                                String responseData_activity = response_activity.body().string();
                                //以下是解析json


                                try {
                                    JSONObject jsnobject = new JSONObject(responseData_activity);
                                    JSONArray jsonArray = jsnobject.getJSONArray("myblogs");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String time = jsonObject.getString("time");
                                        String title = jsonObject.getString("title");
                                        int pageviews = jsonObject.getInt("pageviews");
                                        int id = jsonObject.getInt("id");
                                        SearchResult content = new SearchResult(pageviews, time, title, "西楼", id,user_id,1);
                                        ActivityList.add(content);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(ActivityList.size()!=0){
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView_Activity.setVisibility(View.VISIBLE);
                                        recyclerView_Blog.setVisibility(View.GONE);
                                        recyclerView_Contest.setVisibility(View.GONE);
                                        adapter_Activity.notifyDataSetChanged();
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        activity_notfound.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        }
                    }).start();
                }

                else if(tab.getText().toString().equals("博客")){
                    //增加博客
                    BlogList.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView_Activity.setVisibility(View.GONE);
                    recyclerView_Blog.setVisibility(View.GONE);
                    recyclerView_Contest.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //以下是POST方法
                                HashMap<String, String> paramsMap_blog = new HashMap<>();//用哈希表来存参数
                                paramsMap_blog.put("userid", Integer.toString(userid));
                                FormBody.Builder builder_blog = new FormBody.Builder();
                                for (String key : paramsMap_blog.keySet()) {
                                    //追加表单信息
                                    builder_blog.add(key, paramsMap_blog.get(key));
                                }
                                OkHttpClient okHttpClient_blog = new OkHttpClient();
                                RequestBody formBody_blog = builder_blog.build();
                                Request request_blog = new Request.Builder().url("http://188888888.xyz:5000/user/colblog").post(formBody_blog).build();
                                Response response_blog = okHttpClient_blog.newCall(request_blog).execute();
                                String responseData_blog = response_blog.body().string();
                                //以下是解析json


                                try {
                                    JSONObject jsnobject = new JSONObject(responseData_blog);
                                    JSONArray jsonArray = jsnobject.getJSONArray("myblogs");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String time = jsonObject.getString("time");
                                        String title = jsonObject.getString("title");
                                        int id = jsonObject.getInt("id");
                                        int pageviews = jsonObject.getInt("pageviews");
                                        CollectBlog content = new CollectBlog(title,time,pageviews,id,userid);
                                        BlogList.add(content);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(BlogList.size()!=0){
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView_Activity.setVisibility(View.GONE);
                                        recyclerView_Blog.setVisibility(View.VISIBLE);
                                        recyclerView_Contest.setVisibility(View.GONE);
                                        adapter_Blog.notifyDataSetChanged();
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        blog_notfound.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        }
                    }).start();
                }
                else if(tab.getText().toString().equals("竞赛")){
                    //增加竞赛
                    ContestList.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView_Activity.setVisibility(View.GONE);
                    recyclerView_Blog.setVisibility(View.GONE);
                    recyclerView_Contest.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //以下是POST方法
                                HashMap<String, String> paramsMap_Contest = new HashMap<>();//用哈希表来存参数
                                paramsMap_Contest.put("userid", Integer.toString(userid));
                                FormBody.Builder builder_Contest = new FormBody.Builder();
                                for (String key : paramsMap_Contest.keySet()) {
                                    //追加表单信息
                                    builder_Contest.add(key, paramsMap_Contest.get(key));
                                }
                                OkHttpClient okHttpClient_Contest = new OkHttpClient();
                                RequestBody formBody_Contest = builder_Contest.build();
                                Request request_Contest = new Request.Builder().url("http://188888888.xyz:5000/user/colcompete").post(formBody_Contest).build();
                                Response response_Contest = okHttpClient_Contest.newCall(request_Contest).execute();
                                String responseData_Contest = response_Contest.body().string();
                                //以下是解析json


                                try {
                                    JSONObject jsnobject = new JSONObject(responseData_Contest);
                                    JSONArray jsonArray = jsnobject.getJSONArray("myblogs");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String time = jsonObject.getString("time");
                                        String title = jsonObject.getString("title");
                                        int pageviews = jsonObject.getInt("pageviews");
                                        int id = jsonObject.getInt("id");
                                        SearchResult content = new SearchResult(pageviews, time, title, "西楼", id,user_id,0);
                                        ContestList.add(content);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(ContestList.size()!=0){
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView_Contest.setVisibility(View.VISIBLE);
                                        recyclerView_Blog.setVisibility(View.GONE);
                                        recyclerView_Activity.setVisibility(View.GONE);
                                        adapter_Contest.notifyDataSetChanged();
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        contest_notfound.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        }
                    }).start();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        };


}

