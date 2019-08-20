package com.dalao.yiban.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dalao.yiban.R;
import com.dalao.yiban.constant.YiBanConstant;
import com.dalao.yiban.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.yiban.open.Authorize;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestYiBanActivity extends AppCompatActivity {

    private Button testYiban;

    private Button getCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_yi_ban);

        testYiban = (Button) findViewById(R.id.test_yiban);
        getCode = (Button) findViewById(R.id.get_code);

        testYiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Authorize authorize = new Authorize(YiBanConstant.APP_ID, YiBanConstant.APP_SECRET);
                String url = authorize.forwardurl(YiBanConstant.BACK_URL, "QUERY",
                        Authorize.DISPLAY_TAG_T.MOBILE);
                Intent intent = new Intent(TestYiBanActivity.this, YibanLoginActivity.class);
                intent.putExtra(YiBanConstant.LOGIN_URL, url);
                startActivity(intent);
            }
        });

    }
}


