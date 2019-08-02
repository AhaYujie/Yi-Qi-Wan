package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;

public class ActivityCommentAdapter extends RecyclerView.Adapter<ActivityCommentAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView activityCommentFace;
        TextView activityCommentPersonName;
        TextView activityCommentText;
        TextView activityCommentTime;
        Button activityCommentReplyButton;
        TextView activityCommentReplyNum;

        public ViewHolder(View view) {
            super(view);
            activityCommentFace = (ImageView) view.findViewById(R.id.activity_comment_face);
            activityCommentPersonName = (TextView) view.findViewById(R.id.activity_comment_person_name);
            activityCommentText = (TextView) view.findViewById(R.id.activity_comment_text);
            activityCommentTime = (TextView) view.findViewById(R.id.activity_comment_time);
            activityCommentReplyButton = (Button) view.findViewById(R.id.activity_comment_reply_button);
            activityCommentReplyNum = (TextView) view.findViewById(R.id.activity_comment_reply_num);
        }

    }

    public ActivityCommentAdapter() {
        // TODO
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
        // TODO
    }

    @Override
    public int getItemCount() {
        return 20;
    }

}
