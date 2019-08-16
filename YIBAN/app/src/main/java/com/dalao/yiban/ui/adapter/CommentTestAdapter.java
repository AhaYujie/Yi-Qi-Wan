package com.dalao.yiban.ui.adapter;

import android.content.Context;
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
import com.dalao.yiban.gson.CommentBean;
import com.dalao.yiban.my_interface.CommentInterface;
import com.dalao.yiban.ui.activity.ViewReplyActivity;

import java.util.List;

public class CommentTestAdapter extends RecyclerView.Adapter<CommentTestAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView commentFace;
        TextView commentPersonName;
        TextView commentText;
        TextView commentTime;
        Button commentReplyButton;
        TextView commentReplyNum;
        LinearLayout commentReply;

        private ViewHolder(View view) {
            super(view);
            commentFace = (ImageView) view.findViewById(R.id.comment_author_face);
            commentPersonName = (TextView) view.findViewById(R.id.comment_person_name);
            commentText = (TextView) view.findViewById(R.id.comment_text);
            commentTime = (TextView) view.findViewById(R.id.comment_time);
            commentReplyButton = (Button) view.findViewById(R.id.comment_reply_button);
            commentReplyNum = (TextView) view.findViewById(R.id.comment_reply_num);
            commentReply = (LinearLayout) view.findViewById(R.id.comment_reply);
        }

    }

    public CommentTestAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 20;
    }

}
