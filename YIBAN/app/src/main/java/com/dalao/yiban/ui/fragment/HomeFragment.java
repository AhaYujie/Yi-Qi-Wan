package com.dalao.yiban.ui.fragment;

import android.graphics.LightingColorFilter;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.activity.SearchActivity;
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

    private int contestSortSelected;

    private int activitySortSelected;

    private boolean categoryChanging;

    private int contestPage;        // 竞赛当前页数

    private int activityPage;       // 活动当前页数

    private boolean moreContest;    // true则继续分页加载，false则无更多

    private boolean moreActivity;   // true则继续分页加载，false则无更多

    private boolean updating;       // 是否正在网络请求

    private SwipeRefreshLayout homeSwipeRefresh;

    private RecyclerView homeItemRecyclerView;

    private HomeItemAdapter contestItemAdapter;

    private HomeItemAdapter activityItemAdapter;

    private TabLayout homeCategoryTablayout;

    private TabLayout homeSortTablayout;

    private Button homeSearchButton;

    private LinearLayoutManager linearLayoutManager;

    private HomeListGson contestListGson;

    private HomeListGson activityListGson;

    private MainActivity activity;

    private View view;

    private LinearLayout wrongPageLayout;

    private TextView wrongPageReload;

    private ProgressBar progressBar;

    private int contestFirstVisibleItem;

    private int activityFirstVisibleItem;

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
        wrongPageReload = (TextView) view.findViewById(R.id.wrong_page_reload);
        wrongPageLayout = (LinearLayout) view.findViewById(R.id.wrong_page_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        wrongPageLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        activity = (MainActivity) getActivity();

        categorySelected = homeCategoryTablayout.getSelectedTabPosition();
        contestSortSelected = SELECT_HOT;
        activitySortSelected = SELECT_HOT;
        contestPage = 1;
        activityPage = 1;
        moreContest = true;
        moreActivity = true;
        updating = false;

        // 设置RecyclerView
        linearLayoutManager = new LinearLayoutManager(activity);
        homeItemRecyclerView.setLayoutManager(linearLayoutManager);
        contestItemAdapter = new HomeItemAdapter(contestListGson, SELECT_CONTEST, activity);
        activityItemAdapter = new HomeItemAdapter(activityListGson, SELECT_ACTIVITY, activity);
        if (categorySelected == SELECT_ACTIVITY) {
            homeItemRecyclerView.setAdapter(activityItemAdapter);
        }
        else if (categorySelected == SELECT_CONTEST) {
            homeItemRecyclerView.setAdapter(contestItemAdapter);
        }

        // 设置category切换事件
        homeCategoryTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                categoryChanging = true;
                cancelCall(); // 取消之前的网络请求
                homeItemRecyclerView.setVisibility(View.VISIBLE);
                wrongPageLayout.setVisibility(View.GONE);
                // 竞赛
                if (tab.getPosition() == SELECT_CONTEST) {
                    categorySelected = SELECT_CONTEST;
                    homeSortTablayout.getTabAt(contestSortSelected).select();
                    homeItemRecyclerView.setAdapter(contestItemAdapter);
                    homeItemRecyclerView.scrollToPosition(contestFirstVisibleItem);

                    // 若这个类别没有数据则请求服务器获取数据
                    if (contestItemAdapter.getHomeListGson() == null ||
                            contestItemAdapter.getHomeListGson().getData().size() == 0) {
                        homeSwipeRefresh.setRefreshing(true);
                        requestDataFromServer(true);
                    }
                }
                // 活动
                else if (tab.getPosition() == SELECT_ACTIVITY) {
                    categorySelected = SELECT_ACTIVITY;
                    homeSortTablayout.getTabAt(activitySortSelected).select();
                    homeItemRecyclerView.setAdapter(activityItemAdapter);
                    homeItemRecyclerView.scrollToPosition(activityFirstVisibleItem);
                    // 若这个类别没有数据则请求服务器获取数据
                    if (activityItemAdapter.getHomeListGson() == null ||
                            activityItemAdapter.getHomeListGson().getData().size() == 0) {
                        homeSwipeRefresh.setRefreshing(true);
                        requestDataFromServer(true);
                    }
                }
                categoryChanging = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                cancelCall(); // 取消之前的网络请求
                homeSwipeRefresh.setRefreshing(true);
                moveToTop();
                requestDataFromServer(true);
            }
        });

        // 设置sort切换事件
        homeSortTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (categorySelected == SELECT_CONTEST) {
                    contestSortSelected = tab.getPosition();
                }
                else if (categorySelected == SELECT_ACTIVITY) {
                    activitySortSelected = tab.getPosition();
                }
                // 若不是切换类别则刷新
                if (!categoryChanging) {
                    cancelCall(); // 取消之前的网络请求
                    moveToTop();
                    homeSwipeRefresh.setRefreshing(true);
                    requestDataFromServer(true);
                }
        }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                cancelCall(); // 取消之前的网络请求
                // 若不是切换类别则刷新
                if (!categoryChanging) {
                    moveToTop();
                    homeSwipeRefresh.setRefreshing(true);
                    requestDataFromServer(true);
                }
            }
        });

        // 设置search button点击事件
        homeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.actionStart(activity, activity.userId);
            }
        });

        // 设置错误页面重载点击事件
        wrongPageReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeSwipeRefresh.setRefreshing(true);
                requestDataFromServer(true);
            }
        });

        // 设置swipe refresh事件
        homeSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        homeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cancelCall(); // 取消之前的网络请求
                requestDataFromServer(true);
            }
        });

        // 设置RecyclerView滑动监听事件
        homeItemRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 若不在进行网络请求且有更多数据则分页加载
                if (!updating && newState == RecyclerView.SCROLL_STATE_IDLE &&
                    linearLayoutManager.getItemCount() < linearLayoutManager.findLastVisibleItemPosition() + 3) {
                    if (categorySelected == SELECT_CONTEST && moreContest) {
                        contestPage = contestPage + 1;
                        progressBar.setVisibility(View.VISIBLE);
                        requestDataFromServer(false);
                    }
                    else if (categorySelected == SELECT_ACTIVITY && moreActivity) {
                        activityPage = activityPage + 1;
                        progressBar.setVisibility(View.VISIBLE);
                        requestDataFromServer(false);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (categorySelected == SELECT_CONTEST) {
                    contestFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                }
                else if (categorySelected == SELECT_ACTIVITY) {
                    activityFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                }
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
        // 若当前类别无数据则请求服务器获取数据
        if (isVisible && view != null) {
            homeItemRecyclerView.setVisibility(View.VISIBLE);
            wrongPageLayout.setVisibility(View.GONE);
            if (categorySelected == SELECT_CONTEST && (contestListGson == null ||
                    contestListGson.getData().size() == 0)) {
                homeSwipeRefresh.setRefreshing(true);
                requestDataFromServer(true);
            }
            if (categorySelected == SELECT_ACTIVITY && (activityListGson == null ||
                    activityListGson.getData().size() == 0)) {
                homeSwipeRefresh.setRefreshing(true);
                requestDataFromServer(true);
            }
        }
    }

    /**
     * 从服务器获取列表数据并刷新UI
     * @param reset : true则重置列表数据
     */
    private void requestDataFromServer(final boolean reset) {
        updating = true;

        int page = -1;
        String url = null;
        int sortSelected = -1;
        if (categorySelected == SELECT_CONTEST) {
            if (reset) {
                contestPage = 1;
                moreContest = true;
            }
            page = contestPage;
            url = ServerUrlConstant.CONTEST_LIST_URI;
            sortSelected = contestSortSelected;
        }
        else if (categorySelected == SELECT_ACTIVITY) {
            if (reset) {
                activityPage = 1;
                moreActivity = true;
            }
            page = activityPage;
            url = ServerUrlConstant.ACTIVITY_LIST_URI;
            sortSelected = activitySortSelected;
        }

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.SORT, String.valueOf(sortSelected))
                .add(ServerPostDataConstant.PAGE, String.valueOf(page))
                .build();

        Call call = HttpUtil.sendHttpPost(url, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeSwipeRefresh.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        updating = false;
                        // 若现在的类别数据为空则显示错误页面
                        if (categorySelected == SELECT_CONTEST && (contestListGson == null ||
                                contestListGson.getData().size() == 0)) {
                            homeItemRecyclerView.setVisibility(View.GONE);
                            wrongPageLayout.setVisibility(View.VISIBLE);
                        }
                        else if (categorySelected == SELECT_ACTIVITY && (activityListGson == null ||
                                activityListGson.getData().size() == 0)) {
                            homeItemRecyclerView.setVisibility(View.GONE);
                            wrongPageLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    updating = false;
                    final String responseText = response.body().string();
                    final HomeListGson homeListGson = JsonUtil.handleHomeListResponse(responseText);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 无数据
                            if (homeListGson.getData().size() == 0) {
                                if (categorySelected == SELECT_CONTEST) {
                                    moreContest = false;
                                }
                                else if (categorySelected == SELECT_ACTIVITY) {
                                    moreActivity = false;
                                }
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(activity, HintConstant.NO_MORE, Toast.LENGTH_SHORT).show();
                            }
                            // 有数据
                            else {
                                if (categorySelected == SELECT_CONTEST) {
                                    // 重置
                                    if (contestListGson == null || reset) {
                                        contestListGson = homeListGson;
                                    }
                                    // 添加
                                    else {
                                        contestListGson.getData().addAll(homeListGson.getData());
                                    }
                                } else if (categorySelected == SELECT_ACTIVITY || reset) {
                                    // 重置
                                    if (activityListGson == null || reset) {
                                        activityListGson = homeListGson;
                                    }
                                    // 添加
                                    else {
                                        activityListGson.getData().addAll(homeListGson.getData());
                                    }
                                }
                                updateHomeListUI();
                            }
                        }
                    });
                }
                catch (NullPointerException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homeSwipeRefresh.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        HomeFragment.this.getCallList().add(call);
    }

    /**
     * 刷新列表UI
     */
    private void updateHomeListUI() {
        homeItemRecyclerView.setVisibility(View.VISIBLE);
        wrongPageLayout.setVisibility(View.GONE);
        if (categorySelected == SELECT_CONTEST) {
            contestItemAdapter.setHomeListGson(contestListGson);
            contestItemAdapter.notifyDataSetChanged();
        }
        else if (categorySelected == SELECT_ACTIVITY) {
            activityItemAdapter.setHomeListGson(activityListGson);
            activityItemAdapter.notifyDataSetChanged();
        }
        homeSwipeRefresh.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 移动到顶部
     */
    private void moveToTop() {
        if (linearLayoutManager.findFirstVisibleItemPosition() >= 5) {
            homeItemRecyclerView.scrollToPosition(5);
        }
        homeItemRecyclerView.smoothScrollToPosition(0);
    }

}

