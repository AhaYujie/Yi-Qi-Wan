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

    private String hint;

    /**
     * 创建进度对话框
     * @param context：
     * @param hint：提示
     */
    public CustomProgressDialog(Context context, String hint) {
        this.context = context;
        this.hint = hint;
    }

    /**
     * 显示进度对话框
     */
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(hint);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
