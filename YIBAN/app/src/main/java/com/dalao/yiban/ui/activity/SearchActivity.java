package com.dalao.yiban.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.dalao.yiban.R;
import com.dalao.yiban.UsedSearchDataBaseHelper;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.db.Contest;
import com.dalao.yiban.db.SearchResult;
import com.dalao.yiban.db.UsedSearch;
import com.dalao.yiban.ui.adapter.SearchResultAdapter;
import com.dalao.yiban.ui.adapter.UsedSearchAdapter;
import com.google.gson.JsonObject;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    //启动函数
    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(HomeConstant.USER_ID, userId);
        context.startActivity(intent);
    }

    private List<UsedSearch> UsedSearchList = new ArrayList<>();
    //这是曾经搜索的列表
    private List<SearchResult> SearchResultList = new ArrayList<>();
    //这是搜索结果的列表
    private RecyclerView recyclerView_Result;
    private RecyclerView recyclerView_UsedSearch;
    private TextView textview_clear;
    private ImageView imageView_clear;
    private Button button_back;
    private View view_blank1;
    private View view_blank2;
    private RelativeLayout function_bar;
    private ProgressBar progressBar;
    private TextView text_notfound;
    private SearchView searchView;
    private String user_id;
    private int userid;
    private SearchResultAdapter adapter_Result;
    private UsedSearchAdapter adapter_UsedSearch;
    private int page_UsedSearch;
    private int page_result;
    private TextView search_wait_pull;
    private TextView search_pulling;
    private TextView search_to_end;
    private UsedSearchDataBaseHelper dataBaseHelper;

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);

        dataBaseHelper = new UsedSearchDataBaseHelper(this,"UsedSearch.db",null,1);
        dataBaseHelper.getWritableDatabase();
        //从上一个活动获取数据
        Intent intent = getIntent();
        String user_id = intent.getStringExtra(HomeConstant.USER_ID);
        int userid = Integer.parseInt(user_id);

        page_result = 1;
        page_UsedSearch = 1;

        //控件初始化
        recyclerView_Result = (RecyclerView) findViewById(R.id.search_bar_result);
        recyclerView_UsedSearch = (RecyclerView) findViewById(R.id.search_bar_RecyclerView);
        textview_clear = (TextView) findViewById(R.id.UsedSearchClearText);
        imageView_clear = (ImageView) findViewById(R.id.UsedSearchClearImage);
        button_back = (Button) findViewById(R.id.SearchBarBackButton);
        view_blank1 = (View) findViewById(R.id.search_bar_blank1);
        view_blank2 = (View) findViewById(R.id.search_bar_blank2);
        function_bar = (RelativeLayout) findViewById(R.id.search_bar_function_bar);
        searchView = (SearchView) findViewById(R.id.search_bar_SearchView);
        progressBar = (ProgressBar) findViewById(R.id.search_bar_progressbar);
        text_notfound = (TextView) findViewById(R.id.search_result_notfound);
        /*search_wait_pull = (TextView) findViewById(R.id.search_wait_pull);
        search_pulling = (TextView) findViewById(R.id.search_pulling);
        search_to_end = (TextView) findViewById(R.id.search_to_end);*/

        LinearLayoutManager layoutManager_UsedSearch = new LinearLayoutManager(this);
        recyclerView_UsedSearch.setLayoutManager(layoutManager_UsedSearch);
        adapter_UsedSearch = new UsedSearchAdapter(UsedSearchList, SearchActivity.this);
        recyclerView_UsedSearch.setAdapter(adapter_UsedSearch);

        LinearLayoutManager layoutManager_Result = new LinearLayoutManager(this);
        recyclerView_Result.setLayoutManager(layoutManager_Result);
        adapter_Result = new SearchResultAdapter(SearchResultList);
        recyclerView_Result.setAdapter(adapter_Result);
        progressBar.setVisibility(View.GONE);

        //搜索记录初始化
        InitSearchData(userid);

        textview_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userid != -1) {
                    for(int i=0;i<UsedSearchList.size();i++)
                        Delete_the_search(UsedSearchList.get(i));
                }else{
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                    database.delete("UsedSearch",null,null);
                }
                UsedSearchList.clear();
                adapter_UsedSearch.notifyDataSetChanged();//刷新动画
            }
        });

        imageView_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userid!=-1) {
                    for(int i=0;i<UsedSearchList.size();i++)
                        Delete_the_search(UsedSearchList.get(i));
                    UsedSearchList.clear();
                    adapter_UsedSearch.notifyDataSetChanged();//刷新动画
                }else {
                    SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
                    database.delete("UsedSearch",null,null);
                }
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

        recyclerView_Result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int LastVisibleItem;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastVisibleItem = layoutManager_Result.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && LastVisibleItem + 1 == adapter_Result.getItemCount()) {
                    page_result++;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HashMap<String, String> paramsMap = new HashMap<>();//用哈希表来存参数
                                paramsMap.put("keyword", searchView.getQuery().toString());
                                paramsMap.put("userid", Integer.toString(userid));
                                paramsMap.put("page", Integer.toString(page_result));
                                FormBody.Builder builder = new FormBody.Builder();
                                for (String key : paramsMap.keySet()) {
                                    //追加表单信息
                                    builder.add(key, paramsMap.get(key));
                                }
                                OkHttpClient okHttpClient = new OkHttpClient();
                                RequestBody formBody = builder.build();
                                Request request = new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody).build();
                                Response response = okHttpClient.newCall(request).execute();
                                String responseData = response.body().string();
                                //以下是解析json
                                try {
                                    JSONObject jsnobject = new JSONObject(responseData);
                                    JSONArray jsonArray = jsnobject.getJSONArray("competition");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String time = jsonObject.getString("time");
                                        String title = jsonObject.getString("title");
                                        int pageviews = jsonObject.getInt("pageviews");
                                        int id = jsonObject.getInt("id");
                                        String avatar = jsonObject.getString("url");
                                        SearchResult content = new SearchResult(pageviews, time, title, id, user_id, 0, avatar);
                                        SearchResultList.add(content);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    JSONObject jsnobject = new JSONObject(responseData);
                                    JSONArray jsonArray = jsnobject.getJSONArray("activity");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String time = jsonObject.getString("time");
                                        String title = jsonObject.getString("title");
                                        int pageviews = jsonObject.getInt("pageviews");
                                        int id = jsonObject.getInt("id");
                                        String avatar = jsonObject.getString("url");
                                        SearchResult content = new SearchResult(pageviews, time, title, id, user_id, 1, avatar);
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
                                    adapter_Result.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        recyclerView_UsedSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int LastVisibleItem;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LastVisibleItem = layoutManager_UsedSearch.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && LastVisibleItem + 1 == adapter_UsedSearch.getItemCount()) {
                    page_UsedSearch++;
                    if (userid != -1) {//游客
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HashMap<String, String> paramsMap_UsedSearch = new HashMap<>();//用哈希表来存参数
                                    paramsMap_UsedSearch.put("userid", Integer.toString(userid));
                                    paramsMap_UsedSearch.put("page", Integer.toString(page_UsedSearch));
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
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String keyword = jsonObject.getString("keyword");
                                            int searchid = jsonObject.getInt("id");
                                            UsedSearch content = new UsedSearch(keyword, userid,searchid);
                                            UsedSearchList.add(content);
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
                                        adapter_UsedSearch.notifyDataSetChanged();
                                    }
                                });
                            }
                        }).start();
                    }//文件读取数据
                    else {
                        FileInputStream in = null;
                        BufferedReader reader = null;
                        try {
                            in = openFileInput("data");
                            reader = new BufferedReader(new InputStreamReader(in));
                            String line = "";
                            while ((line = reader.readLine()) != null) {
                                UsedSearch content = new UsedSearch(line, userid,0);
                                UsedSearchList.add(content);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                    UsedSearchList = rip_same_content(UsedSearchList);
                                    adapter_UsedSearch.notifyDataSetChanged();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(userid == -1){
                    save_keyword_in_file(query);
                }
                Toast.makeText(SearchActivity.this, "你想搜索的是:" + query + "。\n请稍等片刻(●'◡'●)", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                text_notfound.setVisibility(View.GONE);
                recyclerView_UsedSearch.setVisibility(View.GONE);
                function_bar.setVisibility(View.GONE);
                view_blank1.setVisibility(View.GONE);
                view_blank2.setVisibility(View.GONE);

                //以下是搜索接口
                page_result=1;
                SearchResultList.clear();//先清空搜索结果

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HashMap<String, String> paramsMap = new HashMap<>();//用哈希表来存参数
                            paramsMap.put("keyword", query);
                            paramsMap.put("userid", Integer.toString(userid));
                            paramsMap.put("page",Integer.toString(page_result));
                            FormBody.Builder builder = new FormBody.Builder();
                            for (String key : paramsMap.keySet()) {
                                //追加表单信息
                                builder.add(key, paramsMap.get(key));
                            }
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody formBody = builder.build();
                            Request request = new Request.Builder().url("http://188888888.xyz:5000/search").post(formBody).build();
                            Response response = okHttpClient.newCall(request).execute();
                            String responseData = response.body().string();
                            //以下是解析json
                            try {
                                JSONObject jsnobject = new JSONObject(responseData);
                                JSONArray jsonArray = jsnobject.getJSONArray("competition");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    String title = jsonObject.getString("title");
                                    int pageviews = jsonObject.getInt("pageviews");
                                    int id = jsonObject.getInt("id");
                                    String avatar = jsonObject.getString("url");
                                    SearchResult content = new SearchResult(pageviews, time, title , id, user_id, 0, avatar);
                                    SearchResultList.add(content);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONObject jsnobject = new JSONObject(responseData);
                                JSONArray jsonArray = jsnobject.getJSONArray("activity");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    String title = jsonObject.getString("title");
                                    int pageviews = jsonObject.getInt("pageviews");
                                    int id = jsonObject.getInt("id");
                                    String avatar = jsonObject.getString("url");
                                    SearchResult content = new SearchResult(pageviews, time, title, id, user_id, 1, avatar);
                                    SearchResultList.add(content);
                                }
                            }catch (Exception e){
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
                List<UsedSearch> list = new ArrayList<>();
                list = filter(UsedSearchList, newText);
                adapter_UsedSearch.setFilter(list);
                return true;
            }
        });
    }


    public void TouchContent(String query) {
        Toast.makeText(SearchActivity.this, "你想搜索的是:" + query + "。\n请稍等片刻(●'◡'●)", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        text_notfound.setVisibility(View.GONE);
        recyclerView_UsedSearch.setVisibility(View.GONE);
        function_bar.setVisibility(View.GONE);
        view_blank1.setVisibility(View.GONE);
        view_blank2.setVisibility(View.GONE);
        //以下是搜索接口
        SearchResultList.clear();//先清空搜索结果
        searchView.setQuery(query.toString(), true);
    }

    private List<UsedSearch> filter(List<UsedSearch> list, String text) {
        List<UsedSearch> filterString = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UsedSearch usedSearch = list.get(i);
            if (usedSearch.getContent().contains(text) == true) {
                filterString.add(usedSearch);
            }
        }
        return filterString;
    }

    public void delete_keyword_in_file(String keyword,int userid) {
        SQLiteDatabase database =dataBaseHelper.getWritableDatabase();
        database.delete("UsedSearch","content = ?",new String[]{keyword});
    }

    public void save_keyword_in_file(String keyword) {
        int flag=0;
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = db.query("UsedSearch",null,null,null,
                null,null,null);
        if(cursor.moveToFirst()){
            do{
                String content = cursor.getString(cursor.getColumnIndex("content"));
                if(content.equals(keyword)){
                    flag=1;
                }
            }while (cursor.moveToNext());
        }
        if(flag==0){
            ContentValues values = new ContentValues();
            values.put("content",keyword);
            db.insert("UsedSearch",null,values);
        }
        cursor.close();
    }

    private List<UsedSearch> rip_same_content (List<UsedSearch> mList){
        for(int i=0;i<mList.size();i++){
            for(int j=i+1;j<mList.size();j++){
                if(mList.get(j).getContent().equals(mList.get(i).getContent())==true){
                    mList.remove(j);
                }
            }
        }
        return mList;
    }

    private void InitSearchData(int userid){
        page_UsedSearch = 1;
        if (userid != -1) {//游客
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> paramsMap_UsedSearch = new HashMap<>();//用哈希表来存参数
                        paramsMap_UsedSearch.put("userid", Integer.toString(userid));
                        paramsMap_UsedSearch.put("page",Integer.toString(page_UsedSearch));
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
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String keyword = jsonObject.getString("keyword");
                                int searchid = jsonObject.getInt("id");
                                UsedSearch content = new UsedSearch(keyword, userid,searchid);
                                UsedSearchList.add(content);
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
                            if (UsedSearchList.size() != 0) {
                                recyclerView_UsedSearch.setVisibility(View.VISIBLE);
                                adapter_UsedSearch.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }).start();
        }//文件读取数据
        else {
            SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
            Cursor cursor = database.query("UsedSearch",null,null,null,
            null,null,null);
            if(cursor.moveToFirst()){
                do{
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    UsedSearch usedSearch = new UsedSearch(content,-1,0);
                    UsedSearchList.add(usedSearch);
                }while (cursor.moveToNext());
            }
            adapter_UsedSearch.notifyDataSetChanged();
            cursor.close();
        }
    }

    public void Delete_the_search(UsedSearch usedSearch){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HashMap<String, String> paramsMap_UsedSearch = new HashMap<>();//用哈希表来存参数
                    paramsMap_UsedSearch.put("userid",Integer.toString(usedSearch.getUserId()));
                    paramsMap_UsedSearch.put("searchid",Integer.toString(usedSearch.getSearchid()));
                    FormBody.Builder builder_UsedSearch = new FormBody.Builder();
                    for (String key : paramsMap_UsedSearch.keySet()) {
                        //追加表单信息
                        builder_UsedSearch.add(key, paramsMap_UsedSearch.get(key));
                    }
                    OkHttpClient okHttpClient_UsedSearch = new OkHttpClient();
                    RequestBody formBody_UsedSearch = builder_UsedSearch.build();
                    Request request_UsedSearch = new Request.Builder().url("http://188888888.xyz:5000/search/delete").post(formBody_UsedSearch).build();
                    Response response_UsedSearch = okHttpClient_UsedSearch.newCall(request_UsedSearch).execute();
                    String responseData_UsedSearch = response_UsedSearch.body().string();
                    //以下是解析json
                } catch (Exception e) {
                e.printStackTrace();
            };
            }
        }).start();
    }
}