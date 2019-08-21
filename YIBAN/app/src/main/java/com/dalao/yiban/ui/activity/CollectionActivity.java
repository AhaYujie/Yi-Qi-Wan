package com.dalao.yiban.ui.activity;

import android.os.Bundle;

import com.dalao.yiban.db.Activity;
import com.dalao.yiban.db.Blog;
import com.dalao.yiban.db.Contest;
import com.dalao.yiban.db.UsedSearch;
import com.dalao.yiban.ui.adapter.CollectionActivityAdapter;
import com.dalao.yiban.ui.adapter.CollectionBlogAdapter;
import com.dalao.yiban.ui.adapter.CollectionContestAdapter;
import com.dalao.yiban.ui.adapter.UsedSearchAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import com.dalao.yiban.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends BaseActivity {

    private List<Activity> ActivityList = new ArrayList<>();
    private List<Blog> BlogList = new ArrayList<>();
    private List<Contest> ContestList = new ArrayList<>();



    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_collection);

        //initData();//初始化数据

        //博客
        RecyclerView recyclerView_Blog = (RecyclerView) findViewById(R.id.my_collection_blog);
        LinearLayoutManager layoutManager_Blog = new LinearLayoutManager(this);
        recyclerView_Blog.setLayoutManager(layoutManager_Blog);
        final CollectionBlogAdapter adapter_Blog = new CollectionBlogAdapter(BlogList);
        recyclerView_Blog.setAdapter(adapter_Blog);

        //活动
        RecyclerView recyclerView_Activity = (RecyclerView) findViewById(R.id.my_collection_activity);
        LinearLayoutManager layoutManager_Activity = new LinearLayoutManager(this);
        recyclerView_Activity.setLayoutManager(layoutManager_Activity);
        final CollectionActivityAdapter adapter_Activity = new CollectionActivityAdapter(ActivityList);
        recyclerView_Activity.setAdapter(adapter_Activity);

        //竞赛
        RecyclerView recyclerView_Contest = (RecyclerView) findViewById(R.id.my_collection_contest);
        LinearLayoutManager layoutManager_Contest = new LinearLayoutManager(this);
        recyclerView_Contest.setLayoutManager(layoutManager_Contest);
        final CollectionContestAdapter adapter_Contest = new CollectionContestAdapter(ContestList);
        recyclerView_Contest.setAdapter(adapter_Contest);

        //TabLayout切换事件
        TabLayout tabLayout = (TabLayout) findViewById(R.id.my_collection_TabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("QAQb",tab.getText().toString());
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
