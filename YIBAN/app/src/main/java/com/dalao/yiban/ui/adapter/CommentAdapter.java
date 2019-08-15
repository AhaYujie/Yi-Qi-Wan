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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
    implements View.OnClickListener {

    private CommentInterface commentInterface;

    private Context context;

    private List<CommentBean> commentsBeanList;

    private String userId;

    private String contentId;

    private int category;

    public List<CommentBean> getCommentsBeanList() {
        return commentsBeanList;
    }

    public void setCommentsBeanList(List<CommentBean> commentsBeanList) {
        this.commentsBeanList = commentsBeanList;
    }

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

    /**
     *
     * @param context :
     * @param commentInterface :
     * @param userId : 用户id
     * @param contentId : 活动或博客的id
     * @param category : SELECT_ACTIVITY or SELECT_BLOG
     */
    public CommentAdapter(Context context, CommentInterface commentInterface, String userId,
                          String contentId, int category) {
        this.context = context;
        this.commentInterface = commentInterface;
        this.userId = userId;
        this.contentId = contentId;
        this.category = category;
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
        final CommentBean commentBean = commentsBeanList.get(position);
        holder.commentPersonName.setText(commentBean.getAuthor());
        holder.commentText.setText(commentBean.getContent());
        holder.commentTime.setText(commentBean.getTime());
        Glide.with(MyApplication.getContext())
                .load(ServerUrlConstant.SERVER_URI + commentBean.getAvatar())
                .into(holder.commentFace);
        if (commentBean.getNumber() == 0) {
            holder.commentReply.setVisibility(View.GONE);
        }
        else {
            holder.commentReply.setVisibility(View.VISIBLE);
            holder.commentReplyNum.setText(String.valueOf(commentBean.getNumber()));
            holder.commentReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 启动查看回复activity
                    ViewReplyActivity.actionStart(context, commentBean, userId, contentId, category);
                }
            });
        }

        // 设置点击事件
        holder.commentPersonName.setOnClickListener(this);
        holder.commentFace.setOnClickListener(this);
        holder.commentReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentInterface.editCommentText(String.valueOf(commentBean.getId()));
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
            case R.id.comment_person_name:
            case R.id.comment_author_face:
                //TODO
                Toast.makeText(MyApplication.getContext(), "click person", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
