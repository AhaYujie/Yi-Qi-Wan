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
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
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

    private int contestSortSelected;

    private int activitySortSelected;

    private boolean categoryChanging;

    private int contestPage;        // 竞赛当前页数

    private int activityPage;       // 活动当前页数

    private boolean moreContest;    // true则继续分页加载，false则无更多

    private boolean moreActivity;   // true则继续分页加载，false则无更多

    private boolean updating;

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

    private int contestFirstVisibleItem;

    private int activityFirstVisibleItem;

    private int contestLastVisibleItem;

    private int activityLastVisibleItem;

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
        wrongPageLayout.setVisibility(View.GONE);
        activity = (MainActivity) getActivity();

        categorySelected = SELECT_CONTEST;
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
        homeItemRecyclerView.setAdapter(contestItemAdapter);

        // 设置category切换事件
        homeCategoryTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                homeItemRecyclerView.setVisibility(View.VISIBLE);
                wrongPageLayout.setVisibility(View.GONE);
                categoryChanging = true;
                cancelCall();   // 取消切换类别前的网络请求
                if (tab.getPosition() == SELECT_CONTEST) {
                    categorySelected = SELECT_CONTEST;
                    homeSortTablayout.getTabAt(contestSortSelected).select();
                    homeItemRecyclerView.setAdapter(contestItemAdapter);
                    homeItemRecyclerView.scrollToPosition(contestFirstVisibleItem);
                    Log.d("yujie", "contest move : " + contestFirstVisibleItem);

                    // 若这个类别没有数据则请求服务器获取数据
                    if (contestItemAdapter.getHomeListGson() == null ||
                            contestItemAdapter.getHomeListGson().getData().size() == 0) {
                        homeSwipeRefresh.setRefreshing(true);
                        requestDataFromServer(true);
                    }
                }
                else if (tab.getPosition() == SELECT_ACTIVITY) {
                    categorySelected = SELECT_ACTIVITY;
                    homeSortTablayout.getTabAt(activitySortSelected).select();
                    homeItemRecyclerView.setAdapter(activityItemAdapter);
                    homeItemRecyclerView.scrollToPosition(activityFirstVisibleItem);
                    Log.d("yujie", "act move : " + activityFirstVisibleItem);
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
                homeSwipeRefresh.setRefreshing(true);
                if (linearLayoutManager.findFirstVisibleItemPosition() >= 5) {
                    homeItemRecyclerView.scrollToPosition(5);
                }
                homeItemRecyclerView.smoothScrollToPosition(0);

                requestDataFromServer(true);
            }
        });

        // 设置sort切换事件
        homeSortTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == SELECT_HOT) {
                    if (categorySelected == SELECT_CONTEST) {
                        contestSortSelected = SELECT_HOT;
                    }
                    else if (categorySelected == SELECT_ACTIVITY) {
                        activitySortSelected = SELECT_HOT;
                    }
                }
                else if (tab.getPosition() == SELECT_TIME) {
                    if (categorySelected == SELECT_CONTEST) {
                        contestSortSelected = SELECT_TIME;
                    }
                    else if (categorySelected == SELECT_ACTIVITY) {
                        activitySortSelected = SELECT_TIME;
                    }
                }
                // 若不是切换类别则刷新
                if (!categoryChanging) {
                    homeSwipeRefresh.setRefreshing(true);
                    if (linearLayoutManager.findFirstVisibleItemPosition() >= 5) {
                        homeItemRecyclerView.scrollToPosition(5);
                    }
                    homeItemRecyclerView.smoothScrollToPosition(0);
                    requestDataFromServer(true);
                }
        }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 若不是切换类别则刷新
                if (!categoryChanging) {
                    homeSwipeRefresh.setRefreshing(true);
                    if (linearLayoutManager.findFirstVisibleItemPosition() >= 5) {
                        homeItemRecyclerView.scrollToPosition(5);
                    }
                    homeItemRecyclerView.smoothScrollToPosition(0);
                    requestDataFromServer(true);
                }
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
                requestDataFromServer(true);
            }
        });

        // 设置RecyclerView滑动监听事件
        homeItemRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 分页加载
                if (!updating && (newState == RecyclerView.SCROLL_STATE_IDLE ||
                    linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition())) {
                    if (categorySelected == SELECT_CONTEST && moreContest &&
                            (contestLastVisibleItem + 2) > linearLayoutManager.getItemCount()) {
                        contestPage = contestPage + 1;
                        requestDataFromServer(false);
                        Log.d("yujie", "contest : " + contestPage);
                    }
                    else if (categorySelected == SELECT_ACTIVITY && moreActivity &&
                            (activityLastVisibleItem + 2) > linearLayoutManager.getItemCount()) {
                        activityPage = activityPage + 1;
                        requestDataFromServer(false);
                        Log.d("yujie", "act : " + activityPage);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (categorySelected == SELECT_CONTEST) {
                    contestFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    contestLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    Log.d("yujie", "contest first : " + contestFirstVisibleItem);
                }
                else if (categorySelected == SELECT_ACTIVITY) {
                    activityFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    activityLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    Log.d("yujie", "act first : " + activityFirstVisibleItem);
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
        // homeSwipeRefresh.setRefreshing(true);
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

        Log.d("yujie", "page : " + page);

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.SORT, String.valueOf(sortSelected))
                .add(ServerPostDataConstant.PAGE, String.valueOf(page))
                .build();

        Call call = HttpUtil.sendHttpPost(url, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeSwipeRefresh.setRefreshing(false);
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
                            // 无更多数据
                            if (homeListGson.getData().size() == 0) {
                                if (categorySelected == SELECT_CONTEST) {
                                    moreContest = false;
                                }
                                else if (categorySelected == SELECT_ACTIVITY) {
                                    moreActivity = false;
                                }
                            }
                            // 添加数据
                            else {
                                if (categorySelected == SELECT_CONTEST) {
                                    if (contestListGson == null || reset) {
                                        Log.d("yujie", "contest reset");
                                        contestListGson = homeListGson;
                                    } else {
                                        contestListGson.getData().addAll(homeListGson.getData());
                                    }
                                } else if (categorySelected == SELECT_ACTIVITY || reset) {
                                    if (activityListGson == null || reset) {
                                        Log.d("yujie", "act reset");
                                        activityListGson = homeListGson;
                                    } else {
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
            Log.d("yujie", "contest : " + contestListGson.getData().size());
        }
        else if (categorySelected == SELECT_ACTIVITY) {
            activityItemAdapter.setHomeListGson(activityListGson);
            activityItemAdapter.notifyDataSetChanged();
            Log.d("yujie", "act : " + activityListGson.getData().size());
        }
        homeSwipeRefresh.setRefreshing(false);
    }

}

