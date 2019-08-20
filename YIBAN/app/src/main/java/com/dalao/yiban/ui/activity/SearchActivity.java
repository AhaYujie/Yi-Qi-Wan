package com.dalao.yiban.ui.activity;

import android.os.Bundle;

import com.dalao.yiban.R;
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
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {

    private List<UsedSearch> UsedSearchList = new ArrayList<>();
    //这是曾经搜索的列表
    private List<SearchResult> SearchResultList = new ArrayList<>();
    private List<SearchResult> temp = new ArrayList<>();
    //这是搜索结果的列表

    int userid=5;//用户id，这个要靠上一个活动传递的参数来决定

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);
        InitList();

        RecyclerView recyclerView_UsedSearch = (RecyclerView) findViewById(R.id.search_bar_RecyclerView);
        LinearLayoutManager layoutManager_UsedSearch = new LinearLayoutManager(this);
        recyclerView_UsedSearch.setLayoutManager(layoutManager_UsedSearch);
        final UsedSearchAdapter adapter_UsedSearch = new UsedSearchAdapter(UsedSearchList);
        recyclerView_UsedSearch.setAdapter(adapter_UsedSearch);

        RecyclerView recyclerView_Result = (RecyclerView) findViewById(R.id.search_bar_result);
        LinearLayoutManager layoutManager_Result = new LinearLayoutManager(this);
        recyclerView_Result.setLayoutManager(layoutManager_Result);
        final SearchResultAdapter adapter_Result = new SearchResultAdapter(SearchResultList);
        recyclerView_Result.setAdapter(adapter_Result);

        TextView textview_clear = (TextView) findViewById(R.id.UsedSearchClearText);
        textview_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsedSearchList.clear();
                adapter_UsedSearch.notifyDataSetChanged();//刷新动画
            }
        });
        ImageView imageView_clear = (ImageView) findViewById(R.id.UsedSearchClearImage);
        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsedSearchList.clear();
                adapter_UsedSearch.notifyDataSetChanged();//刷新动画
            }
        });
        Button button_back = (Button) findViewById(R.id.SearchBarBackButton);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View view_blank1 = (View) findViewById(R.id.search_bar_blank1);
        View view_blank2 = (View) findViewById(R.id.search_bar_blank2);
        LinearLayout function_bar = (LinearLayout) findViewById(R.id.search_bar_function_bar);
        //搜索历史那一栏

        SearchView searchView = (SearchView) findViewById(R.id.search_bar_SearchView);
        searchView.setSubmitButtonEnabled(true);//显示提交按钮
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this,"你想搜索的是:"+query,Toast.LENGTH_SHORT).show();
                //以下是搜索接口
                SearchResultList.clear();//先清空搜索结果

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            //以下是POST方法
                            HashMap<String,String> paramsMap_competition=new HashMap<>();//用哈希表来存参数
                            paramsMap_competition.put("competition",query);
                            paramsMap_competition.put("userid",Integer.toString(userid));
                            FormBody.Builder builder_competition = new FormBody.Builder();
                            for (String key : paramsMap_competition.keySet()) {
                                //追加表单信息
                                builder_competition.add(key, paramsMap_competition.get(key));
                            }
                            OkHttpClient okHttpClient_competition=new OkHttpClient();
                            RequestBody formBody_competition=builder_competition.build();
                            Request request_competition=new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody_competition).build();
                            Response response_competition = okHttpClient_competition.newCall(request_competition).execute();
                            Thread.sleep(300);
                            String responseData_competition = response_competition.body().string();
                            //以下是解析json
                            try{
                                JSONObject jsnobject = new JSONObject(responseData_competition);
                                JSONArray jsonArray = jsnobject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    String title = jsonObject.getString("title");
                                    int pageviews =jsonObject.getInt("pageviews");
                                    int id =jsonObject.getInt("id");
                                    SearchResult content = new SearchResult(pageviews,time,title,"西楼",id);
                                    SearchResultList.add(content);
                                    //Log.e("qaq",Integer.toString(SearchResultList.size()));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                            HashMap<String,String> paramsMap_activity=new HashMap<>();//用哈希表来存参数
                            paramsMap_activity.put("activity",query);
                            paramsMap_activity.put("userid",Integer.toString(userid));
                            FormBody.Builder builder_activity = new FormBody.Builder();
                            for (String key : paramsMap_activity.keySet()) {
                                //追加表单信息
                                builder_activity.add(key, paramsMap_activity.get(key));
                            }
                            OkHttpClient okHttpClient_activity=new OkHttpClient();
                            RequestBody formBody_activity=builder_activity.build();
                            Request request_activity=new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody_activity).build();
                            Response response_activity = okHttpClient_activity.newCall(request_activity).execute();
                            Thread.sleep(300);
                            String responseData_activity = response_activity.body().string();
                            //以下是解析json
                            try{
                                JSONObject jsnobject = new JSONObject(responseData_activity);
                                JSONArray jsonArray = jsnobject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String time = jsonObject.getString("time");
                                        String title = jsonObject.getString("title");
                                        int pageviews =jsonObject.getInt("pageviews");
                                        int id =jsonObject.getInt("id");
                                        SearchResult content = new SearchResult(pageviews,time,title,"西楼",id);
                                        SearchResultList.add(content);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

                recyclerView_Result.setVisibility(View.VISIBLE);
                recyclerView_UsedSearch.setVisibility(View.GONE);
                function_bar.setVisibility(View.GONE);
                view_blank1.setVisibility(View.GONE);
                view_blank2.setVisibility(View.GONE);

                for(int i=0;i<SearchResultList.size();i++)
                    temp.add(SearchResultList.get(i));

                SearchResultList.clear();
                for(int i=0;i<temp.size();i++) {
                    SearchResultList.add(temp.get(i));
                    adapter_Result.notifyItemInserted(i);
                }

                adapter_Result.notifyDataSetChanged();
                Log.e("qaq","刷新了");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_Result.notifyDataSetChanged();
                return false;
            }//改变字符时的监听事件
        });





    }

    private void InitList(){//加载数据
        UsedSearch content1 = new UsedSearch("ACM-ICPC");
        UsedSearchList.add(content1);
        UsedSearch content2 = new UsedSearch("Kaggle");
        UsedSearchList.add(content2);
        UsedSearch content3 = new UsedSearch("数学建模");
        UsedSearchList.add(content3);
        UsedSearch content4 = new UsedSearch("C语言");
        UsedSearchList.add(content4);
        //在list中添加数据，并通知条目加入一条
        //position是增加的位置
        //后面那个是list里面具体的一个实例
        //添加动画
    }
}
