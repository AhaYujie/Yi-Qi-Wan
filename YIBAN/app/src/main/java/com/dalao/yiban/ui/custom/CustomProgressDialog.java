package com.dalao.yiban.ui.custom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.dalao.yiban.constant.HintConstant;

public class CustomProgressDialog {

    private final Context context;

    private ProgressDialog progressDialog;

    public CustomProgressDialog(Context context) {
        this.context = context;
    }

    /**
     * 显示进度对话框
     */
    public void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(HintConstant.LOADING);
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
