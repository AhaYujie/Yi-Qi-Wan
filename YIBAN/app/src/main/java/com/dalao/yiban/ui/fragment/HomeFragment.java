package com.dalao.yiban.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.adapter.HomeItemAdapter;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.dalao.yiban.constant.HomeConstant.SELECT_ACTIVITY;
import static com.dalao.yiban.constant.HomeConstant.SELECT_CONTEST;
import static com.dalao.yiban.constant.HomeConstant.SELECT_HOT;
import static com.dalao.yiban.constant.HomeConstant.SELECT_TIME;

public class HomeFragment extends BaseFragment {

    private  int categorySelected;

    private int sortSelected;

    private SwipeRefreshLayout homeSwipeRefresh;

    private RecyclerView homeItemRecyclerView;

    private HomeItemAdapter homeItemAdapter;

    private TabLayout homeCategoryTablayout;

    private TabLayout homeSortTablayout;

    private Button homeSearchButton;

    private HomeListGson homeListGson = null;

    private MainActivity activity;

    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // 初始化控件
        homeSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh);
        homeItemRecyclerView =(RecyclerView) view.findViewById(R.id.home_item_recyclerView);
        homeCategoryTablayout = (TabLayout) view.findViewById(R.id.home_category_tablayout);
        homeSortTablayout = (TabLayout) view.findViewById(R.id.home_sort_tablayout);
        homeSearchButton = (Button) view.findViewById(R.id.home_search_button);
        activity = (MainActivity) getActivity();

        categorySelected = SELECT_CONTEST;
        sortSelected = SELECT_HOT;

        // 设置category切换事件
        homeCategoryTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == SELECT_CONTEST) {
                    categorySelected = SELECT_CONTEST;
                }
                else if (tab.getPosition() == SELECT_ACTIVITY) {
                    categorySelected = SELECT_ACTIVITY;
                }
                requestDataFromServer();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                requestDataFromServer();
            }
        });

        // 设置sort切换事件
        homeSortTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == SELECT_HOT) {
                    sortSelected = SELECT_HOT;
                }
                else if (tab.getPosition() == SELECT_TIME) {
                    sortSelected = SELECT_TIME;
                }
                requestDataFromServer();
        }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                requestDataFromServer();
            }
        });

        // 设置search button点击事件
        homeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Log.d("yujie", "click search button");
            }
        });

        // 设置swipe refresh事件
        homeSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        homeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDataFromServer();
            }
        });

        // 初始化RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        homeItemRecyclerView.setLayoutManager(linearLayoutManager);
        homeItemAdapter = new HomeItemAdapter(homeListGson, categorySelected, activity);
        homeItemRecyclerView.setAdapter(homeItemAdapter);

        // 如果view可见则请求服务器获取数据
        onVisible();

        return view;
    }

    /**
     * 用户可见view时进行的操作
     */
    @Override
    protected void onVisible() {
        // 请求服务器获取数据
        if (isVisible && view != null)
            requestDataFromServer();
    }

    /**
     * 从服务器获取列表数据并刷新UI
     */
    private void requestDataFromServer() {
        homeSwipeRefresh.setRefreshing(true);

        String url = null;
        if (categorySelected == SELECT_CONTEST) {
            url = ServerUrlConstant.CONTEST_LIST_URI;
        }
        else if (categorySelected == SELECT_ACTIVITY) {
            url = ServerUrlConstant.ACTIVITY_LIST_URI;
        }

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.SORT, String.valueOf(sortSelected))
                .build();

        HttpUtil.sendHttpPost(url, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeSwipeRefresh.setRefreshing(false);
                        Toast.makeText(MyApplication.getContext(), HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                HomeFragment.this.getCallList().add(call);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                HomeFragment.this.getCallList().add(call);
                if (response.body() != null) {
                    final String responseText = response.body().string();
                    final HomeListGson homeListGson = JsonUtil.handleHomeListResponse(responseText);
                    if (homeListGson != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HomeFragment.this.homeListGson = homeListGson;
                                updateHomeListUI();
                            }
                        });
                    }
                    else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                homeSwipeRefresh.setRefreshing(false);
                                Toast.makeText(MyApplication.getContext(),
                                        HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homeSwipeRefresh.setRefreshing(false);
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 刷新列表UI
     */
    private void updateHomeListUI() {
        homeItemAdapter.setHomeListGson(homeListGson);
        homeItemAdapter.setCategorySelected(categorySelected);
        homeItemAdapter.notifyDataSetChanged();
        homeSwipeRefresh.setRefreshing(false);
        homeItemRecyclerView.scrollToPosition(0);
        //homeItemRecyclerView.smoothScrollToPosition(0);
    }

}

