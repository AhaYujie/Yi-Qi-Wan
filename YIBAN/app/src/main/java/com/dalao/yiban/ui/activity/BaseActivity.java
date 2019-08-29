package com.dalao.yiban.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dalao.yiban.R;
import com.dalao.yiban.util.SystemUiUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    // 网络请求
    private List<Call> callList;

    public List<Call> getCallList() {
        return callList;
    }

    public void setCallList(List<Call> callList) {
        this.callList = callList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 改变状态栏为白底黑字
        SystemUiUtil.changeStatusBarToWhite(this);
        callList = new ArrayList<>();
    }

    @Override
    protected void onStop() {
        cancelCall();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }

    /**
     * 取消所有网络请求
     */
    protected void cancelCall() {
        if (callList != null) {
            for (Call call : callList) {
                if (!call.isCanceled()) {
                    call.cancel();
                }
            }
        }
    }

}
