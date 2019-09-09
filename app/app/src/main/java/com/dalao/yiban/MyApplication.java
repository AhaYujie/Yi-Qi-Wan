package com.dalao.yiban;

import android.app.Application;
import android.content.Context;

import com.dalao.yiban.ui.custom.CustomProgressDialog;

import org.litepal.LitePalApplication;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }

    public static Context getContext() {
        return context;
    }

}
