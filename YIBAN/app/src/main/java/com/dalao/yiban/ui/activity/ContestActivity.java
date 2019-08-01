package com.dalao.yiban.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.ContestConstant;
import com.dalao.yiban.ui.adapter.ContestTeamAdapter;

public class ContestActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView contestScrollView;

    private RecyclerView contestTeamRecyclerview;

    private ContestTeamAdapter contestTeamAdapter;

    private TextView contestTitle;

    private TextView contestTeamTitle;

    private Button contestTeamCreate;

    private Button contestMoveToTeam;

    private Button contestTeamCollect;

    private Button contestTeamForward;

    /**
     * 启动ContestActivity
     * @param context
     * @param contestId:竞赛ID
     */
    public static void actionStart(Context context, String contestId) {
        Intent intent = new Intent(context, ContestActivity.class);
        intent.putExtra(ContestConstant.contestId, contestId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);
        Toolbar toolbar = findViewById(R.id.contest_toolbar);
        setSupportActionBar(toolbar);

        // 初始化控件
        contestScrollView = (NestedScrollView) findViewById(R.id.contest_scroll_view);
        contestTeamRecyclerview = (RecyclerView) findViewById(R.id.contest_team_recyclerview);
        contestTeamCreate = (Button) findViewById(R.id.contest_team_create);
        contestMoveToTeam = (Button) findViewById(R.id.contest_move_to_team);
        contestTeamCollect = (Button) findViewById(R.id.contest_team_collect);
        contestTeamForward = (Button) findViewById(R.id.contest_team_forward);
        contestTeamTitle = (TextView) findViewById(R.id.contest_team_title);
        contestTitle = (TextView) findViewById(R.id.contest_title);

        // 设置button点击事件
        contestTeamCreate.setOnClickListener(this);
        contestMoveToTeam.setOnClickListener(this);
        contestTeamCollect.setOnClickListener(this);
        contestTeamForward.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            // 设置返回button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // 去除默认title
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 改变状态栏为白底黑字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // 获取数据
        Intent intent = getIntent();
        String contestId = intent.getStringExtra(ContestConstant.contestId);
        // TODO:本地获取用户id

        // TODO:请求服务器

        // 初始化RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contestTeamRecyclerview.setLayoutManager(layoutManager);
        contestTeamAdapter = new ContestTeamAdapter();
        contestTeamRecyclerview.setAdapter(contestTeamAdapter);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contest_team_create:
                // TODO
                Toast.makeText(ContestActivity.this, "contest_team_create",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.contest_move_to_team:
                // TODO
                int[] position = new int[2];
                contestTeamTitle.getLocationOnScreen(position);
                // 滑动到组队区
                if (position[1] > 0)
                    contestScrollView.smoothScrollTo(0, contestTeamTitle.getTop());
                // 滑动到竞赛内容
                else
                    contestScrollView.smoothScrollTo(0, contestTitle.getTop());
                //
                break;
            case R.id.contest_team_collect:
                // TODO
                Toast.makeText(ContestActivity.this, "contest_team_collect",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.forward_wechat_friend:
                // TODO
                Toast.makeText(ContestActivity.this, "forward_wechat_friend",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.forward_friend_circle:
                // TODO
                Toast.makeText(ContestActivity.this, "forward_friend_circle",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.forward_qq_friend:
                // TODO
                Toast.makeText(ContestActivity.this, "forward_qq_friend",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.forward_qq_sapce:
                // TODO
                Toast.makeText(ContestActivity.this, "forward_qq_sapce",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.forward_yiban:
                // TODO
                Toast.makeText(ContestActivity.this, "forward_yiban",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.contest_team_forward:
                initPopWindow(v);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contest_more_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Toolbar菜单栏点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO back
                Toast.makeText(ContestActivity.this, "back",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.contest_more_collect:
                // TODO collect
                Toast.makeText(ContestActivity.this, "collect",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.contest_more_copy:
                // TODO copy
                Toast.makeText(ContestActivity.this, "copy",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.contest_more_forward:
                initPopWindow(getWindow().getDecorView().findViewById(R.id.contest_team_forward));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPopWindow(View v) {
        // 透明化屏幕其他区域
        backgroundAlpha(0.6f);

        View view = LayoutInflater.from(ContestActivity.this).
                inflate(R.layout.forward_popwindow, null, false);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 初始化pop window 里面的 button
        Button forwardWechatFriend = (Button) view.findViewById(R.id.forward_wechat_friend);
        Button forwardFriendCircle = (Button) view.findViewById(R.id.forward_friend_circle);
        Button forwardQQFriend = (Button) view.findViewById(R.id.forward_qq_friend);
        Button forwardQQSpace = (Button) view.findViewById(R.id.forward_qq_sapce);
        Button forwardYiban = (Button) view.findViewById(R.id.forward_yiban);
        Button forwardCancel = (Button) view.findViewById(R.id.forward_cancel);
        // 设置点击事件
        forwardWechatFriend.setOnClickListener(this);
        forwardFriendCircle.setOnClickListener(this);
        forwardQQFriend.setOnClickListener(this);
        forwardQQSpace.setOnClickListener(this);
        forwardYiban.setOnClickListener(this);
        forwardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setAnimationStyle(R.style.popWindowAnim);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow消失的时候恢复成原来的透明度
                backgroundAlpha(1f);
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

}



















