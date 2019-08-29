package com.dalao.yiban.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.db.CollectBlog;
import com.dalao.yiban.db.MyStar;
import com.dalao.yiban.ui.adapter.MyStarAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyStarActivity extends BaseActivity {

    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, MyStarActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    List<MyStar> myStarList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_star);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra(HomeConstant.USER_ID);
        int userid = Integer.parseInt(user_id);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_star_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyStarAdapter myStarAdapter = new MyStarAdapter(myStarList);
        recyclerView.setAdapter(myStarAdapter);

        TextView textView = (TextView) findViewById(R.id.my_star_notfound);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.my_star_progressbar);
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.my_star_picture);

        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //以下是POST方法
                    HashMap<String, String> paramsMap_myStar = new HashMap<>();//用哈希表来存参数
                    paramsMap_myStar.put("sort", "2");
                    paramsMap_myStar.put("userid", Integer.toString(userid));
                    FormBody.Builder builder_myStar = new FormBody.Builder();
                    for (String key : paramsMap_myStar.keySet()) {
                        //追加表单信息
                        builder_myStar.add(key, paramsMap_myStar.get(key));
                    }
                    OkHttpClient okHttpClient_myStar = new OkHttpClient();
                    RequestBody formBody_myStar = builder_myStar.build();
                    Request request_myStar = new Request.Builder().url("http://188888888.xyz:5000/community").post(formBody_myStar).build();
                    Response response_myStar = okHttpClient_myStar.newCall(request_myStar).execute();
                    String responseData_myStar = response_myStar.body().string();
                    //以下是解析json


                    try {
                        JSONObject jsnobject = new JSONObject(responseData_myStar);
                        JSONArray jsonArray = jsnobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String author = jsonObject.getString("author");
                            String avatar = jsonObject.getString("avatar");
                            int authorid = jsonObject.getInt("authorid");
                            String time = jsonObject.getString("time");
                            String title = jsonObject.getString("title");
                            int id = jsonObject.getInt("id");
                            int pageviews = jsonObject.getInt("pageviews");
                            MyStar myStar = new MyStar(avatar, author, authorid, userid);
                            myStarList.add(myStar);
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
                        if (myStarList.size() != 0) {
                            myStarList = rip_the_same(myStarList);
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            myStarAdapter.notifyDataSetChanged();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        }).start();
    }

    private List<MyStar> rip_the_same(List<MyStar> myStarList) {
        for (int i = 0; i < myStarList.size(); i++) {
            for (int j = i; j < myStarList.size(); j++) {
                if (myStarList.get(j).getAuthor().equals(myStarList.get(i).getAuthor()) == true) {
                    myStarList.remove(j);
                }
            }
        }
        return myStarList;
    }
}

