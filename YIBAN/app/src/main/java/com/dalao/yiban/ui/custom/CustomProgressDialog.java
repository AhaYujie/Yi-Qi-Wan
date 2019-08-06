package com.dalao.yiban.ui.custom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ProgressBar;

public class CustomProgressDialog {

    private final Activity activity;

    private ProgressDialog progressDialog;

    public CustomProgressDialog(Activity activity) {
        this.activity = activity;
    }

    /**
     * 显示进度对话框
     */
    public void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    public void closeProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
