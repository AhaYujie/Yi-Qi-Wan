package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.db.CollectBlog;
import com.dalao.yiban.ui.adapter.MyBlogAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyBlogActivity extends BaseActivity {

    @Override
    public void onClick(View v) {

    }

    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, MyBlogActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        context.startActivity(intent);
    }

    private List<CollectBlog> BlogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_blog);

        //从上一个活动获取数据
        Intent intent = getIntent();
        String user_id = intent.getStringExtra(HomeConstant.USER_ID);
        int userid=Integer.parseInt(user_id);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.my_blog_progressbar);
        TextView textView = (TextView) findViewById(R.id.my_blog_notfound);

        RecyclerView recyclerView_Blog = (RecyclerView) findViewById(R.id.my_blog_RecyclerView);
        LinearLayoutManager layoutManager_Blog = new LinearLayoutManager(this);
        recyclerView_Blog.setLayoutManager(layoutManager_Blog);
        final MyBlogAdapter adapter_Blog = new MyBlogAdapter(BlogList);
        recyclerView_Blog.setAdapter(adapter_Blog);

        progressBar.setVisibility(View.VISIBLE);

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
                    Request request_blog = new Request.Builder().url("http://188888888.xyz:5000/user/myblog").post(formBody_blog).build();
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
                            recyclerView_Blog.setVisibility(View.VISIBLE);
                            adapter_Blog.notifyDataSetChanged();
                        }else {
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        }).start();
            }
        }
