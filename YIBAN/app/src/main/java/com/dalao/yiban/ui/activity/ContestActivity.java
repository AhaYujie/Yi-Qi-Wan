package com.dalao.yiban.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.ContestConstant;
import com.dalao.yiban.ui.adapter.ContestTeamAdapter;
import com.dalao.yiban.ui.custom.CustomPopWindow;
import com.dalao.yiban.util.ImageUtils;
import com.dalao.yiban.util.StringUtils;
import com.dalao.yiban.util.SystemUiUtil;
import com.sendtion.xrichtext.RichTextView;

import java.util.List;

public class ContestActivity extends BaseActivity {

    private NestedScrollView contestScrollView;

    private RecyclerView contestTeamRecyclerview;

    private ContestTeamAdapter contestTeamAdapter;

    private TextView contestTitle;

    private RichTextView richTextView;

    private TextView contestTeamTitle;

    private Button contestTeamCreate;

    private Button contestMoveToTeam;

    private Button contestTeamCollect;

    private Button contestTeamForward;

    private Toolbar contestToolbar;

    /**contest_toolbar
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

        // 初始化控件
        contestToolbar = findViewById(R.id.contest_toolbar);
        contestScrollView = (NestedScrollView) findViewById(R.id.contest_scroll_view);
        contestTeamRecyclerview = (RecyclerView) findViewById(R.id.contest_team_recyclerview);
        contestTeamCreate = (Button) findViewById(R.id.contest_team_create);
        contestMoveToTeam = (Button) findViewById(R.id.contest_move_to_team);
        contestTeamCollect = (Button) findViewById(R.id.contest_team_collect);
        contestTeamForward = (Button) findViewById(R.id.contest_team_forward);
        contestTeamTitle = (TextView) findViewById(R.id.contest_team_title);
        contestTitle = (TextView) findViewById(R.id.contest_title);
        richTextView = (RichTextView) findViewById(R.id.contest_content);

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

        // test
        richTextView.post(new Runnable() {
            @Override
            public void run() {
                showEditData("测试测试测试测试测试测试测试测试测试测试测试测试" +
                        "测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                        "测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                        "<img src=\"https://i11.hoopchina.com.cn/hupuapp/bbs/133437271753938/thread_133437271753938_20190802070638_s_760723_w_750_h_3784_56213.jpg?x-oss-process=image/resize,w_800/format,webp\" >" +
                        "测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                        "测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                        "<img src=\"https://i4.hoopchina.com.cn/hupuapp/bbs/244653993008752/thread_244653993008752_20190803012459_s_56694_w_640_h_360_13312.jpg?x-oss-process=image/resize,w_800/format,webp\" >");
            }
        });

    }

    protected void showEditData(String content) {

        richTextView.clearAllLayout();

        List<String> textList = StringUtils.cutStringByImgTag(content);
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            Log.d("yujie", text);
            if (text.contains("<img")) {
                String imagePath = StringUtils.getImgSrc(text);
//                int width = ScreenUtils.getScreenWidth(this);
//                int height = ScreenUtils.getScreenHeight(this);
                richTextView.measure(0,0);
//                Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
                if (imagePath != null){
                    richTextView.addImageViewAtIndex(richTextView.getLastIndex(), imagePath);
                } else {
                    richTextView.addTextViewAtIndex(richTextView.getLastIndex(), text);
                }
            }
            else {
                richTextView.addTextViewAtIndex(richTextView.getLastIndex(), text);
            }
        }

    }

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
                CustomPopWindow.initPopWindow(v, ContestActivity.this);
                break;
            default:
                break;
        }
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
                // TODO:返回到MainActivity的竞赛列表界面
                Toast.makeText(ContestActivity.this, "back",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.contest_more_collect:
                // TODO:收藏该竞赛
                Toast.makeText(ContestActivity.this, "collect",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.contest_more_copy:
                // TODO：复制该竞赛链接
                Toast.makeText(ContestActivity.this, "copy",
                        Toast.LENGTH_SHORT).show();
                break;

            // 转发button弹出PopWindow
            case R.id.contest_more_forward:
                CustomPopWindow.initPopWindow
                        (getWindow().getDecorView().findViewById(R.id.contest_team_forward),
                                ContestActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}



















