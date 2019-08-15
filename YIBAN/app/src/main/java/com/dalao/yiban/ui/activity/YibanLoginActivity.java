package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.YiBanConstant;

public class YibanLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yiban_login);
        final WebView webView =(WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

        // 获取url
        final String loginUrl = getIntent().getStringExtra(YiBanConstant.LOGIN_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("yujie", url);
                if (url.contains("yiban")) {
                    Log.d("yujie", "equal");
                    return false;
                }
                finish();
                return false;
            }
        });
        webView.loadUrl(loginUrl);
    }
}
