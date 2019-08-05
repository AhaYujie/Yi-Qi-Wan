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

public class BlogCommentAdapter extends RecyclerView.Adapter<BlogCommentAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView blogCommentFace;
        TextView blogCommentPersonName;
        TextView blogCommentText;
        TextView blogCommentTime;
        Button blogCommentReplyButton;
        TextView blogCommentReplyNum;

        public ViewHolder(View view) {
            super(view);
            blogCommentFace = (ImageView) view.findViewById(R.id.blog_comment_face);
            blogCommentPersonName = (TextView) view.findViewById(R.id.blog_comment_person_name);
            blogCommentText = (TextView) view.findViewById(R.id.blog_comment_text);
            blogCommentTime = (TextView) view.findViewById(R.id.blog_comment_time);
            blogCommentReplyButton = (Button) view.findViewById(R.id.blog_comment_reply_button);
            blogCommentReplyNum = (TextView) view.findViewById(R.id.blog_comment_reply_num);
        }

    }

    public BlogCommentAdapter() {
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
