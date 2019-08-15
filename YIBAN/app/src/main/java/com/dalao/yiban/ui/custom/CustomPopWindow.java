package com.dalao.yiban.ui.custom;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.dalao.yiban.R;
import com.dalao.yiban.db.Activity;
import com.dalao.yiban.ui.activity.BaseActivity;
import com.dalao.yiban.util.SystemUiUtil;

import static com.dalao.yiban.constant.MineConstant.FEMALE;
import static com.dalao.yiban.constant.MineConstant.MALE;
import static com.dalao.yiban.constant.MineConstant.SECRET;

public class CustomPopWindow {

    /**
     * 转发PopWindow
     * @param v：父容器参照View
     * @param activity:当前活动
     */
    public static void forwardPopWindow(View v, final BaseActivity activity) {
        // 透明化屏幕其他区域
        SystemUiUtil.backgroundAlpha(activity, 0.6f);

        View view = LayoutInflater.from(activity).
                inflate(R.layout.forward_popwindow, null, false);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 初始化pop window 里面的 button
        Button forwardWechatFriend = (Button) view.findViewById(R.id.forward_wechat_friend);
        Button forwardFriendCircle = (Button) view.findViewById(R.id.forward_friend_circle);
        Button forwardQQFriend = (Button) view.findViewById(R.id.forward_qq_friend);
        Button forwardQQSpace = (Button) view.findViewById(R.id.forward_qq_sapce);
        Button forwardYiban = (Button) view.findViewById(R.id.forward_yiban);
        Button forwardCancel = (Button) view.findViewById(R.id.forward_cancel);
        // 设置点击事件
        forwardWechatFriend.setOnClickListener(activity);
        forwardFriendCircle.setOnClickListener(activity);
        forwardQQFriend.setOnClickListener(activity);
        forwardQQSpace.setOnClickListener(activity);
        forwardYiban.setOnClickListener(activity);
        forwardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setAnimationStyle(R.style.popWindowAnim);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow消失的时候恢复成原来的透明度
                SystemUiUtil.backgroundAlpha(activity, 1f);
            }
        });
    }

    /**
     * 评论pop window
     * @param v:参照view
     * @param activity:当前活动
     * @return : 返回popwindow和edittext的实例
     */
    public static PopWindowViewHelper commentPopWindow(View v, final BaseActivity activity) {
        // 透明化屏幕其他区域
        SystemUiUtil.backgroundAlpha(activity, 0.6f);

        View view = LayoutInflater.from(activity).
                inflate(R.layout.comment_popwindow, null, false);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 初始化控件
        EditText commentEditText = (EditText) view.findViewById(R.id.comment_edit_text);
        Button commentPublishButton = (Button) view.findViewById(R.id.comment_publish_button);

        // 设置点击事件
        commentPublishButton.setOnClickListener(activity);

        popupWindow.setAnimationStyle(R.style.popWindowAnim);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow消失的时候恢复成原来的透明度
                SystemUiUtil.backgroundAlpha(activity, 1f);
            }
        });

        return new PopWindowViewHelper(popupWindow, commentEditText);
    }

    /**
     * choose sex PopWindow
     * @param v：父容器参照View
     * @param activity:
     */
    public static PopupWindow chooseSexPopWindow(View v, final BaseActivity activity, int sexSelected) {
        // 透明化屏幕其他区域
        SystemUiUtil.backgroundAlpha(activity, 0.6f);

        View view = LayoutInflater.from(activity).
                inflate(R.layout.choose_sex_popwindow, null, false);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // 初始化pop window 里面的控件
        RelativeLayout chooseMaleLayout = view.findViewById(R.id.choose_male_layout);
        RelativeLayout chooseFemaleLayout = view.findViewById(R.id.choose_female_layout);
        RelativeLayout chooseSecretLayout = view.findViewById(R.id.choose_secret_layout);
        ImageView chooseMaleIcon = view.findViewById(R.id.choose_male_icon);
        ImageView chooseFemaleIcon = view.findViewById(R.id.choose_female_icon);
        ImageView chooseSecretIcon = view.findViewById(R.id.choose_secret_icon);
        chooseMaleIcon.setVisibility(View.INVISIBLE);
        chooseFemaleIcon.setVisibility(View.INVISIBLE);
        chooseSecretIcon.setVisibility(View.INVISIBLE);

        switch (sexSelected) {
            case MALE:
                chooseMaleIcon.setVisibility(View.VISIBLE);
                break;
            case FEMALE:
                chooseFemaleIcon.setVisibility(View.VISIBLE);
                break;
            case SECRET:
                chooseSecretIcon.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


        // 设置点击事件
        chooseMaleLayout.setOnClickListener(activity);
        chooseFemaleLayout.setOnClickListener(activity);
        chooseSecretLayout.setOnClickListener(activity);

        popupWindow.setAnimationStyle(R.style.popWindowAnim);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow消失的时候恢复成原来的透明度
                SystemUiUtil.backgroundAlpha(activity, 1f);
            }
        });
        return popupWindow;
    }

    public static class PopWindowViewHelper {

        public final PopupWindow popupWindow;
        public final EditText editText;

        PopWindowViewHelper(PopupWindow popupWindow, EditText editText) {
            this.popupWindow = popupWindow;
            this.editText = editText;
        }

    }

}
