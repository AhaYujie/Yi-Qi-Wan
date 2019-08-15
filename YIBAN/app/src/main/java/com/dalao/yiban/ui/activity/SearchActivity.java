package com.dalao.yiban.ui.activity;

import android.os.Bundle;

import com.dalao.yiban.R;
import com.dalao.yiban.db.SearchResult;
import com.dalao.yiban.db.UsedSearch;
import com.dalao.yiban.ui.adapter.SearchResultAdapter;
import com.dalao.yiban.ui.adapter.UsedSearchAdapter;

import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {

    private List<UsedSearch> UsedSearchList = new ArrayList<>();
    //这是曾经搜索的列表
    private List<SearchResult> SearchResultList = new ArrayList<>();
    //这是搜索结果的列表

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);
        InitList();

        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.search_bar_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager);
        final UsedSearchAdapter adapter = new UsedSearchAdapter(UsedSearchList);
        recyclerView1.setAdapter(adapter);

        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.search_bar_result);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager1);
        final SearchResultAdapter adapter1 = new SearchResultAdapter(SearchResultList);
        recyclerView2.setAdapter(adapter1);


        TextView textview = (TextView) findViewById(R.id.UsedSearchClearText);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsedSearchList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.UsedSearchClearImage);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsedSearchList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        Button button1 = (Button) findViewById(R.id.SearchBarButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SearchView searchView = (SearchView) findViewById(R.id.search_bar_SearchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this,"你想查询的是:1111",Toast.LENGTH_SHORT).show();
                recyclerView2.setVisibility(View.VISIBLE);
                recyclerView1.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(SearchActivity.this,"你想查询的是:222221312312",Toast.LENGTH_SHORT).show();
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);
                return false;
            }
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
        SearchResult content5 = new SearchResult(1,"111","222","333",5);
        SearchResultList.add(content5);
        //position是增加的位置
        //后面那个是list里面具体的一个实例
        //添加动画
    }
}
