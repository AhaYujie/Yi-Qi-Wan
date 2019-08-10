package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.ActivityGson;

import java.util.List;

public class ActivityCommentAdapter extends RecyclerView.Adapter<ActivityCommentAdapter.ViewHolder>
    implements View.OnClickListener {

    private List<ActivityGson.CommentsBean> commentsBeanList;

    public List<ActivityGson.CommentsBean> getCommentsBeanList() {
        return commentsBeanList;
    }

    public void setCommentsBeanList(List<ActivityGson.CommentsBean> commentsBeanList) {
        this.commentsBeanList = commentsBeanList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView activityCommentFace;
        TextView activityCommentPersonName;
        TextView activityCommentText;
        TextView activityCommentTime;
        Button activityCommentReplyButton;
        TextView activityCommentReplyNum;
        LinearLayout activityCommentReply;

        public ViewHolder(View view) {
            super(view);
            activityCommentFace = (ImageView) view.findViewById(R.id.activity_comment_face);
            activityCommentPersonName = (TextView) view.findViewById(R.id.activity_comment_person_name);
            activityCommentText = (TextView) view.findViewById(R.id.activity_comment_text);
            activityCommentTime = (TextView) view.findViewById(R.id.activity_comment_time);
            activityCommentReplyButton = (Button) view.findViewById(R.id.activity_comment_reply_button);
            activityCommentReplyNum = (TextView) view.findViewById(R.id.activity_comment_reply_num);
            activityCommentReply = (LinearLayout) view.findViewById(R.id.activity_comment_reply);
        }

    }

    public ActivityCommentAdapter(List<ActivityGson.CommentsBean> commentsBeanList) {
        this.commentsBeanList = commentsBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ActivityGson.CommentsBean commentsBean = commentsBeanList.get(position);
        holder.activityCommentPersonName.setText(commentsBean.getAuthor());
        holder.activityCommentText.setText(commentsBean.getContent());
        holder.activityCommentTime.setText(commentsBean.getTime());
        holder.activityCommentReplyNum.setText(String.valueOf(commentsBean.getNumber()));
        Glide.with(MyApplication.getContext())
                .load(ServerUrlConstant.SERVER_URI + commentsBean.getAvatar())
                .into(holder.activityCommentFace);

        // 设置点击事件
        holder.activityCommentPersonName.setOnClickListener(this);
        holder.activityCommentFace.setOnClickListener(this);
        holder.activityCommentReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(MyApplication.getContext(), "click comment " + commentsBean.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.activityCommentReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Toast.makeText(MyApplication.getContext(), "click reply " + commentsBean.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsBeanList != null ? commentsBeanList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_comment_person_name:
            case R.id.activity_comment_face:
                //TODO
                Toast.makeText(MyApplication.getContext(), "click person", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
