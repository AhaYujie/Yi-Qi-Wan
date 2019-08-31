package com.dalao.yiban.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.LoginConstant;
import com.dalao.yiban.constant.YiBanConstant;
import com.dalao.yiban.ui.activity.LoginActivity;

import org.json.JSONObject;

import cn.yiban.open.Authorize;

public class YibanLoginFragment extends BaseFragment {

    private View view;

    private LoginActivity loginActivity;

    private WebView yibanLoginWebview;

    private MyJavaScriptInterface myJavaScriptInterface;

    private ProgressBar webViewProgressBar;

    public YibanLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment YibanLoginFragment.
     */
    public static YibanLoginFragment newInstance() {
        return new YibanLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_yiban_login, container, false);
        loginActivity = (LoginActivity) getActivity();

        // 初始化控件
        yibanLoginWebview = (WebView) view.findViewById(R.id.yiban_login_webview);
        webViewProgressBar = (ProgressBar) view.findViewById(R.id.web_view_progress_bar);
        return view;
    }

    /**
     * 用户可见时, 加载易班登录授权界面
     */
    @Override
    protected void onVisible() {
        if (isVisible && view != null) {
            Authorize authorize = new Authorize(YiBanConstant.APP_ID, YiBanConstant.APP_SECRET);
            String url = authorize.forwardurl(YiBanConstant.BACK_URL, "QUERY",
                    Authorize.DISPLAY_TAG_T.MOBILE);
            yibanLoginWebview.getSettings().setJavaScriptEnabled(true);
            myJavaScriptInterface = new MyJavaScriptInterface();
            yibanLoginWebview.addJavascriptInterface(myJavaScriptInterface, "INTERFACE");
            yibanLoginWebview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // 易班登录授权
                    if (url.contains(YiBanConstant.REQUEST_YIBAN_URL)) {
                        yibanLoginWebview.setVisibility(View.VISIBLE);
                    }
                    // 回调接口获取用户id
                    else {
                        loginActivity.customProgressDialog.showProgressDialog();
                        yibanLoginWebview.setVisibility(View.INVISIBLE);
                    }
                    return false;
                }

                // 添加js代码来获取html内容
                @Override
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
                }
            });

            yibanLoginWebview.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {

                    if(newProgress==100){
                        webViewProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    }
                    else{
                        webViewProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        webViewProgressBar.setProgress(newProgress);//设置进度值
                    }

                }
            });

            yibanLoginWebview.loadUrl(url);
        }
    }

    /**
     * 用于获取页面html内容的类
     */
    class MyJavaScriptInterface {

        private MyJavaScriptInterface() {
        }

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processContent(String content) {
            if (content.contains(LoginConstant.USER_ID)) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String userId = jsonObject.getString(LoginConstant.USER_ID);
                    loginActivity.backToHome(userId);
                }
                catch (Exception e) {
                    // 回到选择登录方式界面
                    Toast.makeText(loginActivity, HintConstant.LOGIN_ERROR, Toast.LENGTH_SHORT).show();
                    loginActivity.customViewPager.setCurrentItem(LoginConstant.SELECT_CHOOSE);
                    e.printStackTrace();
                }
            }
        }
    }

}
