package com.dalao.yiban.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.CommentConstant;
import com.dalao.yiban.constant.CommunityConstant;
import com.dalao.yiban.constant.HintConstant;
import com.dalao.yiban.constant.HomeConstant;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.MyFollowingGson;
import com.dalao.yiban.my_interface.FollowInterface;
import com.dalao.yiban.ui.activity.BaseActivity;
import com.dalao.yiban.ui.activity.ViewFollowingActivity;
import com.dalao.yiban.util.HttpUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFollowingItemAdapter extends RecyclerView.Adapter<MyFollowingItemAdapter.ViewHolder>
    implements FollowInterface{

    private MyFollowingGson myFollowingGson;

    private ViewFollowingActivity activity;

    //private String userId;

    public MyFollowingGson getMyFollowingGson() {
        return myFollowingGson;
    }

    public void setMyFollowingGson(MyFollowingGson myFollowingGson) {
        this.myFollowingGson = myFollowingGson;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView myFollowingAvatar;
        TextView myFollowingName;
        Button myFollowingButton;

        public ViewHolder(View view) {
            super(view);
            myFollowingAvatar = (CircleImageView) view.findViewById(R.id.my_following_avatar);
            myFollowingName = (TextView) view.findViewById(R.id.my_following_name);
            myFollowingButton = (Button) view.findViewById(R.id.my_following_button);
        }
    }

    public MyFollowingItemAdapter(ViewFollowingActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_following_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MyFollowingGson.DataBean dataBean = myFollowingGson.getData().get(position);
        holder.myFollowingName.setText(dataBean.getUsername());
        if (dataBean.getIsFollow() == CommunityConstant.FOLLOW) {
            holder.myFollowingButton.setText(CommunityConstant.UN_FOLLOW_TEXT);
        }
        else if (dataBean.getIsFollow() == CommunityConstant.UN_FOLLOW) {
            holder.myFollowingButton.setText(CommunityConstant.FOLLOW_TEXT);
        }
        Glide.with(activity)
                .load(ServerUrlConstant.SERVER_URI + dataBean.getAvatar())
                .placeholder(R.drawable.ic_avatar_grey)
                .error(R.drawable.ic_avatar_grey)
                .fallback(R.drawable.ic_avatar_grey)
                .into(holder.myFollowingAvatar);

        // 设置关注点击事件
        holder.myFollowingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 取消关注
                if (!dataBean.getFollowing() && dataBean.getIsFollow() == CommunityConstant.FOLLOW) {
                    HttpUtil.followBlogAuthor(null, activity, MyFollowingItemAdapter.this,
                            activity.userId, String.valueOf(dataBean.getUserid()),
                            HomeConstant.VIEW_FOLLOWING_ACTIVITY, CommunityConstant.UN_FOLLOW);
                }
                // 关注
                else if (!dataBean.getFollowing() && dataBean.getIsFollow() == CommunityConstant.UN_FOLLOW) {
                    HttpUtil.followBlogAuthor(null, activity, MyFollowingItemAdapter.this,
                            activity.userId, String.valueOf(dataBean.getUserid()),
                            HomeConstant.VIEW_FOLLOWING_ACTIVITY, CommunityConstant.FOLLOW);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myFollowingGson != null ? myFollowingGson.getData().size() : 0;
    }

    /**
     * 关注成功
     * @param userId : 关注的用户id
     */
    public void followSucceed(String userId) {
        Toast.makeText(activity, HintConstant.BLOG_AUTHOR_FOLLOW_SUCCESS, Toast.LENGTH_SHORT).show();
        for (MyFollowingGson.DataBean dataBean : myFollowingGson.getData()) {
            if (dataBean.getUserid() == Integer.valueOf(userId)) {
                dataBean.setIsFollow(CommunityConstant.FOLLOW);
                dataBean.setFollowing(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 取消关注成功
     * @param userId : 关注的用户id
     */
    public void unFollowSucceed(String userId) {
        Toast.makeText(activity, HintConstant.BLOG_AUTHOR_UN_FOLLOW_SUCCESS, Toast.LENGTH_SHORT).show();
        for (MyFollowingGson.DataBean dataBean : myFollowingGson.getData()) {
            if (dataBean.getUserid() == Integer.valueOf(userId)) {
                dataBean.setIsFollow(CommunityConstant.UN_FOLLOW);
                dataBean.setFollowing(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 关注或取消关注失败
     * @param userId : 关注的用户id
     */
    public void followError(String userId) {
        Toast.makeText(activity, HintConstant.BLOG_AUTHOR_FOLLOW_ERROR, Toast.LENGTH_SHORT).show();
        for (MyFollowingGson.DataBean dataBean : myFollowingGson.getData()) {
            if (dataBean.getUserid() == Integer.valueOf(userId)) {
                dataBean.setFollowing(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 进行关注或取消关注操作
     * @param userId : 关注的用户id
     */
    public void followStart(String userId) {
        for (MyFollowingGson.DataBean dataBean : myFollowingGson.getData()) {
            if (dataBean.getUserid() == Integer.valueOf(userId)) {
                dataBean.setFollowing(true);
            }
        }
        notifyDataSetChanged();
    }

}




















