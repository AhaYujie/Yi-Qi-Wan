package com.dalao.yiban.ui.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.adapter.ContestTeamAdapter;

public class ContestActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView contestTeamRecyclerview;

    private ContestTeamAdapter contestTeamAdapter;

    private Button contestTeamCreate;

    private Button contestMoveToTeam;

    private Button contestTeamCollect;

    private Button contestTeamForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);
        Toolbar toolbar = findViewById(R.id.contest_toolbar);
        setSupportActionBar(toolbar);

        // 初始化控件
        contestTeamRecyclerview = (RecyclerView) findViewById(R.id.contest_team_recyclerview);
        contestTeamCreate = (Button) findViewById(R.id.contest_team_create);
        contestMoveToTeam = (Button) findViewById(R.id.contest_move_to_team);
        contestTeamCollect = (Button) findViewById(R.id.contest_team_collect);
        contestTeamForward = (Button) findViewById(R.id.contest_team_forward);

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

        // 初始化RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contestTeamRecyclerview.setLayoutManager(layoutManager);
        contestTeamAdapter = new ContestTeamAdapter();
        contestTeamRecyclerview.setAdapter(contestTeamAdapter);
    }

    /**
     * 底部导航栏点击事件
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
                Toast.makeText(ContestActivity.this, "contest_move_to_team",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.contest_team_collect:
                // TODO
                Toast.makeText(ContestActivity.this, "contest_team_collect",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.contest_team_forward:
                // TODO
                Toast.makeText(ContestActivity.this, "contest_team_forward",
                        Toast.LENGTH_SHORT).show();
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
                // TODO forward
                Toast.makeText(ContestActivity.this, "forward",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
