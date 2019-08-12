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

    private int sortSelected;

    private CommunityBlogListGson communityBlogListGson;

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
        communityCreateBlogButton = (Button) view.findViewById(R.id.community_create_blog_button);
        sortSelected = SELECT_HOT;

        // 设置spinner
        String[] sortItems = getResources().getStringArray(R.array.community_sort_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>
                (activity, android.R.layout.simple_spinner_item, sortItems);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        communitySortSpinner.setAdapter(arrayAdapter);
        communitySortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    // 按热度排序
                    case SELECT_HOT:
                        sortSelected = SELECT_HOT;
                        requestDataFromServer();
                        break;
                    // 按时间排序
                    case SELECT_TIME:
                        sortSelected = SELECT_TIME;
                        requestDataFromServer();
                        break;
                    // 只看关注的人
                    case SELECT_FOLLOWING:
                        sortSelected = SELECT_FOLLOWING;
                        requestDataFromServer();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
                requestDataFromServer();
            }
        });

        // 设置点击事件
        communityCreateBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
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
        // 请求服务器获取数据
        if (isVisible && view != null)
            requestDataFromServer();
    }

    /**
     * 用户不可见view时进行的操作
     */
    @Override
    protected void onInvisible() {
    }

    /**
     * 从服务器获取博客列表数据并刷新UI
     */
    private void requestDataFromServer() {
        communityBlogRefresh.setRefreshing(true);

        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.SORT, String.valueOf(sortSelected))
                .build();

        HttpUtil.sendHttpPost(ServerUrlConstant.COMMUNITY_BLOG_LIST_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        communityBlogRefresh.setRefreshing(false);
                        Toast.makeText(MyApplication.getContext(), HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    final String responseText = response.body().string();
                    final CommunityBlogListGson communityBlogListGson =
                            JsonUtil.handleCommunityBlogListResponse(responseText);
                    if (communityBlogListGson != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateHomeListUI(communityBlogListGson);
                            }
                        });
                    }
                    else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                communityBlogRefresh.setRefreshing(false);
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
                            communityBlogRefresh.setRefreshing(false);
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
     * @param communityBlogListGson : 解析后的json数据
     */
    private void updateHomeListUI(@NonNull CommunityBlogListGson communityBlogListGson) {
        this.communityBlogListGson = communityBlogListGson;
        communityBlogItemAdapter.setDataBeanList(this.communityBlogListGson.getData());
        communityBlogItemAdapter.notifyDataSetChanged();
        communityBlogRecyclerView.scrollToPosition(5);
        communityBlogRecyclerView.smoothScrollToPosition(0);
        communityBlogRefresh.setRefreshing(false);
    }

}















