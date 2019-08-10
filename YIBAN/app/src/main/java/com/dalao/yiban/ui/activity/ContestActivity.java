package com.dalao.yiban.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerPostDataConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.ContestGson;
import com.dalao.yiban.ui.adapter.ContestTeamAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.util.HttpUtil;
import com.dalao.yiban.util.JsonUtil;
import com.dalao.yiban.util.StringUtils;
import com.sendtion.xrichtext.RichTextView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ContestActivity extends BaseActivity {

    private MenuItem moreCollect;

    private ContestGson contestGson = null;

    private NestedScrollView contestScrollView;

    private RecyclerView contestTeamRecyclerView;

    private ContestTeamAdapter contestTeamAdapter;

    private TextView contestTitle;

    private TextView contestContentTime;

    private RichTextView richTextView;

    private TextView contestSource;

    private TextView contestTeamTitle;

    private Button contestTeamCreate;

    private Button contestMoveToTeam;

    private Button contestTeamCollect;

    private Button contestTeamForward;

    private Toolbar contestToolbar;

    private String contestId;

    private String userId;

    private Menu menu;

    /**
     * 启动ContestActivity
     * @param context:
     * @param contestId:竞赛ID
     */
    public static void actionStart(Context context, String contestId) {
        Intent intent = new Intent(context, ContestActivity.class);
        intent.putExtra(HomeConstant.CONTEST_ID, contestId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        // 初始化控件
        contestTeamRecyclerView = (RecyclerView) findViewById(R.id.contest_team_recyclerview);
        contestScrollView = (NestedScrollView) findViewById(R.id.contest_scroll_view);
        contestContentTime = (TextView) findViewById(R.id.contest_content_time);
        contestTeamCreate = (Button) findViewById(R.id.contest_team_create);
        contestMoveToTeam = (Button) findViewById(R.id.contest_move_to_team);
        contestTeamCollect = (Button) findViewById(R.id.contest_team_collect);
        contestTeamForward = (Button) findViewById(R.id.contest_team_forward);
        contestTeamTitle = (TextView) findViewById(R.id.contest_team_title);
        richTextView = (RichTextView) findViewById(R.id.contest_content);
        contestSource = (TextView) findViewById(R.id.contest_source);
        contestTitle = (TextView) findViewById(R.id.contest_title);
        contestToolbar = findViewById(R.id.contest_toolbar);

        // 设置button点击事件
        contestTeamCreate.setOnClickListener(this);
        contestMoveToTeam.setOnClickListener(this);
        contestTeamCollect.setOnClickListener(this);
        contestTeamForward.setOnClickListener(this);

        setSupportActionBar(contestToolbar);
        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 初始化RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contestTeamRecyclerView.setLayoutManager(layoutManager);
        contestTeamAdapter = new ContestTeamAdapter(this);
        contestTeamRecyclerView.setAdapter(contestTeamAdapter);

        // 获取数据
        Intent intent = getIntent();
        contestId = intent.getStringExtra(HomeConstant.CONTEST_ID);
        // TODO:本地获取用户id
        userId = "5";   // test
        // 请求服务器
        requestDataFromServer();

//
//        if (ContextCompat.checkSelfPermission(ContestActivity.this, Manifest.permission.
//                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(ContestActivity.this, new String[]{
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, 1);
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                }
//                else {
//                    Toast.makeText(this, "拒接权限将无法使用此功能", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.contest_team_create:
                // TODO：启动创建队伍activity
                Toast.makeText(ContestActivity.this, "contest_team_create",
                        Toast.LENGTH_SHORT).show();
                break;

            // 在竞赛内容滑动到组队区，在组队区滑动到竞赛内容
            case R.id.contest_move_to_team:
                int[] position = new int[2];
                contestTeamTitle.getLocationOnScreen(position);
                // 滑动到组队区
                if (position[1] > 0) {
                    contestScrollView.smoothScrollTo(0, contestTeamTitle.getTop());
                }
                // 滑动到竞赛内容
                else  {
                    contestScrollView.smoothScrollTo(0, contestTitle.getTop());
                }
                //
                break;

            case R.id.contest_team_collect:
                // TODO:收藏该竞赛，保存到本地和服务器
                Toast.makeText(ContestActivity.this, "contest_team_collect",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_wechat_friend:
                // TODO:转发该竞赛到微信好友
                Toast.makeText(ContestActivity.this, "forward_wechat_friend",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_friend_circle:
                // TODO:转发该竞赛到朋友圈
                Toast.makeText(ContestActivity.this, "forward_friend_circle",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_qq_friend:
                // TODO:转发该竞赛到QQ好友
                Toast.makeText(ContestActivity.this, "forward_qq_friend",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_qq_sapce:
                // TODO:转发该竞赛到QQ空间
                Toast.makeText(ContestActivity.this, "forward_qq_sapce",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.forward_yiban:
                // TODO:转发该竞赛到易班
                Toast.makeText(ContestActivity.this, "forward_yiban",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.contest_team_forward:
                CustomPopWindow.forwardPopWindow(v, this);
                break;
            default:
                break;
        }
    }

    /**
     * 创建菜单栏
     * @param menu:
     * @return :
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_menu, menu);
        this.menu = menu;
        this.moreCollect = menu.findItem(R.id.more_collect);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Toolbar菜单栏点击事件
     * @param item:
     * @return :
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.more_collect:
                // TODO:收藏该竞赛
                Toast.makeText(ContestActivity.this, "COLLECT",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.more_copy:
                // TODO：复制该竞赛链接
                Toast.makeText(ContestActivity.this, "copy",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.more_forward:
                CustomPopWindow.forwardPopWindow
                        (getWindow().getDecorView().findViewById(R.id.contest_team_forward), this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 请求服务器获取竞赛数据, 并解析刷新UI
     */
    private void requestDataFromServer() {
        FormBody formBody  = new FormBody.Builder()
                .add(ServerPostDataConstant.CONTEST_ID, contestId)
                .add(ServerPostDataConstant.USER_ID, userId)
                .build();
        HttpUtil.sendHttpPost(ServerUrlConstant.CONTEST_URI, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ContestActivity.this, HintConstant.GET_DATA_FAILED,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    String responseText = response.body().string();
                    final ContestGson contestGson = JsonUtil.handleContestResponse(responseText);
                    if (contestGson != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateContestUI(contestGson);
                            }
                        });
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ContestActivity.this,
                                        HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ContestActivity.this,
                                    HintConstant.GET_DATA_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 刷新竞赛UI
     * @param contestGson:解析后的数据
     */
    private void updateContestUI(@NonNull final ContestGson contestGson) {
        this.contestGson = contestGson;
        contestTitle.setText(contestGson.getTitle());
        contestContentTime.setText(contestGson.getTime());
        contestSource.setText(contestGson.getAuthor());
        richTextView.post(new Runnable() {
            @Override
            public void run() {
                StringUtils.showContestContent(richTextView, contestGson.getContent());
            }
        });
        if (contestGson.getCollection() == HomeConstant.COLLECT) {
            contestTeamCollect.setBackgroundResource(R.drawable.ic_collect_blue);
            moreCollect.setTitle(HomeConstant.COLLECT_TEXT);
        }
        else if (contestGson.getCollection() == HomeConstant.UN_COLLECT) {
            contestTeamCollect.setBackgroundResource(R.drawable.ic_collect_black);
            moreCollect.setTitle(HomeConstant.UN_COLLECT_TEXT);
        }
        contestTeamAdapter.setTeamBeanList(contestGson.getTeam());
        contestTeamAdapter.notifyDataSetChanged();
    }

}



















