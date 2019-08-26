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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.CommunityBlogListGson;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.ui.activity.CreateBlogActivity;
import com.dalao.yiban.ui.activity.MainActivity;
import com.dalao.yiban.ui.adapter.CommunityBlogItemAdapter;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.dalao.yiban.constant.HomeConstant.SELECT_ACTIVITY;
import static com.dalao.yiban.constant.HomeConstant.SELECT_CONTEST;
import static com.dalao.yiban.constant.HomeConstant.SELECT_FOLLOWING;
import static com.dalao.yiban.constant.HomeConstant.SELECT_HOT;
import static com.dalao.yiban.constant.HomeConstant.SELECT_TIME;

public class CommunityFragment extends BaseFragment {

    private View view;

    private MainActivity activity;

    private RecyclerView communityBlogRecyclerView;

    private CommunityBlogItemAdapter communityBlogItemAdapter;

    private Spinner communitySortSpinner;

    private SwipeRefreshLayout communityBlogRefresh;

    private Button communityCreateBlogButton;

    private LinearLayoutManager linearLayoutManager;

    private LinearLayout wrongPageLayout;

    private TextView wrongPageReload;

    private int sortSelected;

    private CommunityBlogListGson communityBlogListGson;

    private ProgressBar progressBar;

    private int page;           // 当前页数

    private boolean moreBlog;   // true则有更多数据

    private boolean updating;   // true则表示在进行网络请求

    public CommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CommunityFragment.
     */
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
        communityCreateBlogButton = (Button) view.findViewById(R.id.community_create_blog_button);
        wrongPageLayout = (LinearLayout) view.findViewById(R.id.wrong_page_layout);
        wrongPageReload = (TextView) view.findViewById(R.id.wrong_page_reload);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        communityBlogRecyclerView.setVisibility(View.VISIBLE);
        wrongPageLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        sortSelected = SELECT_HOT;
        page = 1;
        moreBlog = true;
        updating = false;

        // 设置spinner
        String[] sortItems = getResources().getStringArray(R.array.community_sort_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (activity, android.R.layout.simple_spinner_item, sortItems);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        communitySortSpinner.setAdapter(arrayAdapter);
        communitySortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isVisible && view != null) {
                    moveToTop();
                    cancelCall();   // 取消切换排序方式前的网络请求
                    communityBlogRefresh.setRefreshing(true);
                    switch (i) {
                        // 按热度排序
                        case SELECT_HOT:
                            sortSelected = SELECT_HOT;
                            requestDataFromServer(true);
                            break;
                        // 按时间排序
                        case SELECT_TIME:
                            sortSelected = SELECT_TIME;
                            requestDataFromServer(true);
                            break;
                        // 只看关注的人
                        case SELECT_FOLLOWING:
                            sortSelected = SELECT_FOLLOWING;
                            requestDataFromServer(true);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // 初始化RecyclerView
        linearLayoutManager = new LinearLayoutManager(activity);
        communityBlogRecyclerView.setLayoutManager(linearLayoutManager);
        communityBlogItemAdapter = new CommunityBlogItemAdapter(activity);
        communityBlogRecyclerView.setAdapter(communityBlogItemAdapter);

        // 设置RecyclerView监听事件
        communityBlogRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 若不在进行网络请求且有更多数据则分页加载
                if (!updating && moreBlog && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        linearLayoutManager.getItemCount() < linearLayoutManager.findLastVisibleItemPosition() + 3) {
                    page = page + 1;
                    progressBar.setVisibility(View.VISIBLE);
                    requestDataFromServer(false);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // 设置swipe refresh事件
        communityBlogRefresh.setColorSchemeResources(R.color.colorPrimary);
        communityBlogRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cancelCall();
                requestDataFromServer(true);
            }
        });

        // 设置创建点击事件
        communityCreateBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 游客禁止使用此功能
                if (activity.userId.equals(HomeConstant.VISITOR_USER_ID)) {
                    Toast.makeText(MyApplication.getContext(), HintConstant.VISITOR_NOT_ALLOW,
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    // 启动创建博客活动
                    CreateBlogActivity.actionStart(activity, activity.userId);
                }
            }
        });

        // 设置错误页面重载点击事件
        wrongPageReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDataFromServer(true);
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
        // 若无数据则请求服务器获取数据
        if (isVisible && view != null && (communityBlogListGson == null ||
                communityBlogListGson.getData().size() == 0)) {
            communityBlogRecyclerView.setVisibility(View.VISIBLE);
            wrongPageLayout.setVisibility(View.GONE);
            communityBlogRefresh.setRefreshing(true);
            requestDataFromServer(true);
        }
    }

    /**
     * 从服务器获取博客列表数据并刷新UI
     * @param reset : true则重载列表数据
     */
    public void requestDataFromServer(boolean reset) {
        updating = true;

        if (reset) {
            page = 1;
            moreBlog = true;
        }

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.SORT, String.valueOf(sortSelected))
                .add(ServerPostDataConstant.USER_ID, activity.userId)
                .add(ServerPostDataConstant.PAGE, String.valueOf(page))
                .build();

        Call call = HttpUtil.sendHttpPost(ServerUrlConstant.COMMUNITY_BLOG_LIST_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        communityBlogRefresh.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        updating = false;
                        // 若博客列表数据为空则显示错误页面
                        if (communityBlogListGson == null || communityBlogListGson.getData().size() == 0) {
                            communityBlogRecyclerView.setVisibility(View.GONE);
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
                    final CommunityBlogListGson communityBlogListGson =
                            JsonUtil.handleCommunityBlogListResponse(responseText);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 无数据
                            if (communityBlogListGson.getData().size() == 0) {
                                moreBlog = false;
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(activity, HintConstant.NO_MORE, Toast.LENGTH_SHORT).show();
                            }
                            // 有数据
                            else {
                                // 重置
                                if (CommunityFragment.this.communityBlogListGson == null || reset) {
                                    CommunityFragment.this.communityBlogListGson = communityBlogListGson;
                                }
                                // 添加
                                else {
                                    CommunityFragment.this.communityBlogListGson.getData()
                                            .addAll(communityBlogListGson.getData());
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
                            communityBlogRefresh.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MyApplication.getContext(),
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        CommunityFragment.this.getCallList().add(call);
    }

    /**
     * 刷新列表UI
     */
    private void updateHomeListUI() {
        communityBlogItemAdapter.setDataBeanList(this.communityBlogListGson.getData());
        communityBlogItemAdapter.notifyDataSetChanged();
        communityBlogRecyclerView.setVisibility(View.VISIBLE);
        wrongPageLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        communityBlogRefresh.setRefreshing(false);
    }

    /**
     * 滑动到顶部
     */
    private void moveToTop() {
        if (linearLayoutManager.findFirstVisibleItemPosition() >= 5) {
            communityBlogRecyclerView.scrollToPosition(5);
        }
        communityBlogRecyclerView.smoothScrollToPosition(0);
    }

}















