package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.YiBanConstant;
import com.dalao.yiban.ui.adapter.CommentTestAdapter;

public class YibanLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yiban_login);
        final WebView webView =(WebView) findViewById(R.id.web_view);
        //webView.getSettings().setJavaScriptEnabled(true);
        RecyclerView testComment = (RecyclerView) findViewById(R.id.test_comment);
        CommentTestAdapter adapter = new CommentTestAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        testComment.setLayoutManager(linearLayoutManager);
        testComment.setAdapter(adapter);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.huodongxing.com/event/4504924302700?utm_source=%e4%b8%bb%e9%a1%b5&utm_medium=%e5%bc%ba%e5%8a%9b%e6%8e%a8%e8%8d%90&utm_campaign=homepage&qd=9828363067853&type=SITE_RECOMMEND_AD_CLICK&tag=%e6%94%b9%e8%a3%85%e8%bd%a6%2c%e9%9f%b3%e4%b9%90%2c%e6%96%87%e5%8c%96");
    }
}
