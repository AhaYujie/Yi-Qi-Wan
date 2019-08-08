package com.dalao.yiban.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.adapter.CommunityBlogItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends BaseFragment {

    private View view;

    private MainActivity activity;

    private RecyclerView communityBlogRecyclerView;

    private CommunityBlogItemAdapter communityBlogItemAdapter;

    private Spinner communitySortSpinner;

    private SwipeRefreshLayout communityBlogRefresh;

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        // 初始化控件
        activity = (MainActivity) getActivity();
        communityBlogRecyclerView = (RecyclerView) view.findViewById(R.id.community_blog_recyclerView);
        communitySortSpinner = (Spinner) view.findViewById(R.id.community_sort_spinner);
        communityBlogRefresh = (SwipeRefreshLayout) view.findViewById(R.id.community_blog_refresh);

        // 设置spinner
        String[] sortItems = getResources().getStringArray(R.array.community_sort_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (activity, android.R.layout.simple_spinner_item, sortItems);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        communitySortSpinner.setAdapter(arrayAdapter);
        communitySortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //TODO
            }
        });

        // 初始化RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        communityBlogRecyclerView.setLayoutManager(linearLayoutManager);
        communityBlogItemAdapter = new CommunityBlogItemAdapter();
        communityBlogRecyclerView.setAdapter(communityBlogItemAdapter);

        // 设置swipe refresh事件
        communityBlogRefresh.setColorSchemeResources(R.color.colorPrimary);
        communityBlogRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                communityBlogRecyclerView.scrollToPosition(5);
                communityBlogRecyclerView.smoothScrollToPosition(0);
                communityBlogRefresh.setRefreshing(false);
            }
        });

        // 如果view可见则请求服务器获取数据
        onVisible();

        return view;
    }

    /**
     * 用户可见view时进行的操作
     */
    @Override
    protected void onVisible() {
        //TODO
    }

    /**
     * 用户不可见view时进行的操作
     */
    @Override
    protected void onInvisible() {
        //TODO
    }

}















