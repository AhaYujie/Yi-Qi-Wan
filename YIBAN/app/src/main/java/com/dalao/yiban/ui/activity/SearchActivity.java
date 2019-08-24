package com.dalao.yiban.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.db.Contest;
import com.dalao.yiban.db.SearchResult;
import com.dalao.yiban.db.UsedSearch;
import com.dalao.yiban.ui.adapter.SearchResultAdapter;
import com.dalao.yiban.ui.adapter.UsedSearchAdapter;
import com.google.gson.JsonObject;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import javax.security.auth.login.LoginException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {

    public static void actionStart(Context context, String userId, String activityId,
                                   String activityTitle, String activityContentTime) {
        Intent intent = new Intent(context, ActivityActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        intent.putExtra(HomeConstant.ACTIVITY_ID, activityId);
        intent.putExtra(HomeConstant.ACTIVITY_TITLE, activityTitle);
        intent.putExtra(HomeConstant.ACTIVITY_CONTENT_TIME, activityContentTime);
        context.startActivity(intent);
    }

    final String FILE_NAME = "used_search";

    private boolean deng(String a,String b){
        if(a.equals(b))
            return true;
        else
            return false;
    }

    private List<UsedSearch> Rip(List<UsedSearch> UsedSearchList){

        for(int i=0;i<UsedSearchList.size();i++){
            UsedSearch a = UsedSearchList.get(i);
            int j;
            for(j=0;j<i;j++){
                UsedSearch b = UsedSearchList.get(j);
                if(deng(a.getContent(),b.getContent())==true)
                    break;
            }
            if(j!=i)
                UsedSearchList.remove(i);
        }

        return UsedSearchList;
    }

    private List<UsedSearch> UsedSearchList = new ArrayList<>();
    //这是曾经搜索的列表
    private List<SearchResult> SearchResultList = new ArrayList<>();
    //这是搜索结果的列表

    //从上一个活动获取数据
    Intent intent = getIntent();
    String user_id = intent.getStringExtra(HomeConstant.USER_ID);
    int userid=Integer.parseInt(HomeConstant.USER_ID);

    private RecyclerView recyclerView_Result ;
    private RecyclerView recyclerView_UsedSearch ;
    private TextView textview_clear;
    private ImageView imageView_clear ;
    private Button button_back ;
    private View view_blank1 ;
    private View view_blank2 ;
    private LinearLayout function_bar ;
    private ProgressBar progressBar;
    private TextView text_notfound;
    //搜索历史那一栏
    private SearchView searchView;

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);

        recyclerView_Result = (RecyclerView) findViewById(R.id.search_bar_result);
        recyclerView_UsedSearch = (RecyclerView) findViewById(R.id.search_bar_RecyclerView);
        textview_clear = (TextView) findViewById(R.id.UsedSearchClearText);
        imageView_clear = (ImageView) findViewById(R.id.UsedSearchClearImage);
        button_back = (Button) findViewById(R.id.SearchBarBackButton);
        view_blank1 = (View) findViewById(R.id.search_bar_blank1);
        view_blank2 = (View) findViewById(R.id.search_bar_blank2);
        function_bar = (LinearLayout) findViewById(R.id.search_bar_function_bar);
        //搜索历史那一栏
        searchView = (SearchView) findViewById(R.id.search_bar_SearchView);
        progressBar = (ProgressBar) findViewById(R.id.Search_bar_ProgressBar);
        text_notfound = (TextView) findViewById(R.id.search_result_notfound);

        LinearLayoutManager layoutManager_UsedSearch = new LinearLayoutManager(this);
        recyclerView_UsedSearch.setLayoutManager(layoutManager_UsedSearch);
        final UsedSearchAdapter adapter_UsedSearch = new UsedSearchAdapter(UsedSearchList,SearchActivity.this);
        recyclerView_UsedSearch.setAdapter(adapter_UsedSearch);

        LinearLayoutManager layoutManager_Result = new LinearLayoutManager(this);
        recyclerView_Result.setLayoutManager(layoutManager_Result);
        SearchResultAdapter adapter_Result = new SearchResultAdapter(SearchResultList);
        recyclerView_Result.setAdapter(adapter_Result);

        //搜索记录初始化
        if(userid!=-1){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramsMap_UsedSearch = new HashMap<>();//用哈希表来存参数
                    paramsMap_UsedSearch.put("userid", Integer.toString(userid));
                    FormBody.Builder builder_UsedSearch = new FormBody.Builder();
                    for (String key : paramsMap_UsedSearch.keySet()) {
                        //追加表单信息
                        builder_UsedSearch.add(key, paramsMap_UsedSearch.get(key));
                    }
                    OkHttpClient okHttpClient_UsedSearch = new OkHttpClient();
                    RequestBody formBody_UsedSearch = builder_UsedSearch.build();
                    Request request_UsedSearch = new Request.Builder().url("http://188888888.xyz:5000/search/history").post(formBody_UsedSearch).build();
                    Response response_UsedSearch = okHttpClient_UsedSearch.newCall(request_UsedSearch).execute();
                    String responseData_UsedSearch = response_UsedSearch.body().string();
                    //以下是解析json
                    try {
                        JSONObject jsnobject = new JSONObject(responseData_UsedSearch);
                        JSONArray jsonArray = jsnobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i += 2) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String keyword = jsonObject.getString("keyword");
                            UsedSearch content = new UsedSearch(keyword);
                            UsedSearchList.add(content);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                UsedSearchList=Rip(UsedSearchList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (UsedSearchList.size() != 0) {
                            recyclerView_UsedSearch.setVisibility(View.VISIBLE);
                            adapter_UsedSearch.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
        }//文件读取数据
        else{
            FileInputStream in = null;
            BufferedReader reader = null;
            try{
                in = openFileInput("data");
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine())!=null){
                    UsedSearch content = new UsedSearch(line);
                    UsedSearchList.add(content);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(reader != null){
                    try {
                        reader.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }


        textview_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsedSearchList.clear();
                adapter_UsedSearch.notifyDataSetChanged();//刷新动画
            }
        });

        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsedSearchList.clear();
                adapter_UsedSearch.notifyDataSetChanged();//刷新动画
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchView.setSubmitButtonEnabled(true);//显示提交按钮
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this, "你想搜索的是:" + query + "。\n请稍等片刻(●'◡'●)", Toast.LENGTH_SHORT).show();
                if(userid==-1) {
                    String data = query;
                    FileOutputStream out =null;
                    BufferedWriter writer = null;
                    try {
                        out  = openFileOutput("data",Context.MODE_APPEND);
                        writer = new BufferedWriter(new OutputStreamWriter(out));
                        writer.write(query);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            if(writer != null){
                                writer.close();
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                progressBar.setVisibility(View.VISIBLE);
                text_notfound.setVisibility(View.GONE);
                recyclerView_UsedSearch.setVisibility(View.GONE);
                function_bar.setVisibility(View.GONE);
                view_blank1.setVisibility(View.GONE);
                view_blank2.setVisibility(View.GONE);

                //以下是搜索接口
                SearchResultList.clear();//先清空搜索结果

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //以下是POST方法
                            HashMap<String, String> paramsMap_competition = new HashMap<>();//用哈希表来存参数
                            paramsMap_competition.put("competition", query);
                            paramsMap_competition.put("userid", Integer.toString(userid));
                            FormBody.Builder builder_competition = new FormBody.Builder();
                            for (String key : paramsMap_competition.keySet()) {
                                //追加表单信息
                                builder_competition.add(key, paramsMap_competition.get(key));
                            }
                            OkHttpClient okHttpClient_competition = new OkHttpClient();
                            RequestBody formBody_competition = builder_competition.build();
                            Request request_competition = new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody_competition).build();
                            Response response_competition = okHttpClient_competition.newCall(request_competition).execute();
                            String responseData_competition = response_competition.body().string();
                            //以下是解析json


                            try {
                                JSONObject jsnobject = new JSONObject(responseData_competition);
                                JSONArray jsonArray = jsnobject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    String title = jsonObject.getString("title");
                                    int pageviews = jsonObject.getInt("pageviews");
                                    int id = jsonObject.getInt("id");
                                    SearchResult content = new SearchResult(pageviews, time, title, "西楼", id,Integer.toString(userid),0);
                                    SearchResultList.add(content);
                                    //Log.e("qaq",Integer.toString(SearchResultList.size()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            HashMap<String, String> paramsMap_activity = new HashMap<>();//用哈希表来存参数
                            paramsMap_activity.put("activity", query);
                            paramsMap_activity.put("userid", Integer.toString(userid));
                            FormBody.Builder builder_activity = new FormBody.Builder();
                            for (String key : paramsMap_activity.keySet()) {
                                //追加表单信息
                                builder_activity.add(key, paramsMap_activity.get(key));
                            }
                            OkHttpClient okHttpClient_activity = new OkHttpClient();
                            RequestBody formBody_activity = builder_activity.build();
                            Request request_activity = new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody_activity).build();
                            Response response_activity = okHttpClient_activity.newCall(request_activity).execute();
                            String responseData_activity = response_activity.body().string();
                            //以下是解析json
                            try {
                                JSONObject jsnobject = new JSONObject(responseData_activity);
                                JSONArray jsonArray = jsnobject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    String title = jsonObject.getString("title");
                                    int pageviews = jsonObject.getInt("pageviews");
                                    int id = jsonObject.getInt("id");
                                    SearchResult content = new SearchResult(pageviews, time, title, "西楼", id,user_id,1);
                                    SearchResultList.add(content);
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
                                if (SearchResultList.size() != 0) {
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView_Result.setVisibility(View.VISIBLE);
                                    adapter_Result.notifyDataSetChanged();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    text_notfound.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                }).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }//改变字符时的监听事件
        });
    }

    public void TouchContent(String query){
        //搜索历史那一栏
        searchView = (SearchView) findViewById(R.id.search_bar_SearchView);
        progressBar = (ProgressBar) findViewById(R.id.Search_bar_ProgressBar);
        text_notfound = (TextView) findViewById(R.id.search_result_notfound);

        progressBar.setVisibility(View.VISIBLE);
        text_notfound.setVisibility(View.GONE);
        recyclerView_UsedSearch.setVisibility(View.GONE);
        function_bar.setVisibility(View.GONE);
        view_blank1.setVisibility(View.GONE);
        view_blank2.setVisibility(View.GONE);

        //以下是搜索接口
        SearchResultList.clear();//先清空搜索结果

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //以下是POST方法
                    HashMap<String, String> paramsMap_competition = new HashMap<>();//用哈希表来存参数
                    paramsMap_competition.put("competition", query);
                    paramsMap_competition.put("userid", Integer.toString(userid));
                    FormBody.Builder builder_competition = new FormBody.Builder();
                    for (String key : paramsMap_competition.keySet()) {
                        //追加表单信息
                        builder_competition.add(key, paramsMap_competition.get(key));
                    }
                    OkHttpClient okHttpClient_competition = new OkHttpClient();
                    RequestBody formBody_competition = builder_competition.build();
                    Request request_competition = new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody_competition).build();
                    Response response_competition = okHttpClient_competition.newCall(request_competition).execute();
                    String responseData_competition = response_competition.body().string();
                    Thread.sleep(1000);
                    //以下是解析json


                    try {
                        JSONObject jsnobject = new JSONObject(responseData_competition);
                        JSONArray jsonArray = jsnobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String time = jsonObject.getString("time");
                            String title = jsonObject.getString("title");
                            int pageviews = jsonObject.getInt("pageviews");
                            int id = jsonObject.getInt("id");
                            SearchResult content = new SearchResult(pageviews, time, title, "西楼", id,Integer.toString(userid),0);
                            SearchResultList.add(content);
                            //Log.e("qaq",Integer.toString(SearchResultList.size()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    HashMap<String, String> paramsMap_activity = new HashMap<>();//用哈希表来存参数
                    paramsMap_activity.put("activity", query);
                    paramsMap_activity.put("userid", Integer.toString(userid));
                    FormBody.Builder builder_activity = new FormBody.Builder();
                    for (String key : paramsMap_activity.keySet()) {
                        //追加表单信息
                        builder_activity.add(key, paramsMap_activity.get(key));
                    }
                    OkHttpClient okHttpClient_activity = new OkHttpClient();
                    RequestBody formBody_activity = builder_activity.build();
                    Request request_activity = new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody_activity).build();
                    Response response_activity = okHttpClient_activity.newCall(request_activity).execute();
                    String responseData_activity = response_activity.body().string();
                    //以下是解析json
                    try {
                        JSONObject jsnobject = new JSONObject(responseData_activity);
                        JSONArray jsonArray = jsnobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String time = jsonObject.getString("time");
                            String title = jsonObject.getString("title");
                            int pageviews = jsonObject.getInt("pageviews");
                            int id = jsonObject.getInt("id");
                            SearchResult content = new SearchResult(pageviews, time, title, "西楼", id,user_id,1);
                            SearchResultList.add(content);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                UsedSearchList=Rip(UsedSearchList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (SearchResultList.size() != 0) {
                            progressBar.setVisibility(View.GONE);
                            recyclerView_Result.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            text_notfound.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

        }).start();
    }
}