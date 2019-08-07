package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.ui.activity.BlogActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityBlogItemAdapter extends RecyclerView.Adapter<CommunityBlogItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout communityBlog;
        TextView communityBlogTitle;
        TextView communityBlogTime;
        TextView communityBlogPageViews;
        ImageView communityBlogPic;
        CircleImageView communityBlogAuthorFace;
        TextView communityBlogAuthorName;

        public ViewHolder(View view) {
            super(view);
            communityBlog = (RelativeLayout) view.findViewById(R.id.community_blog);
            communityBlogTitle = (TextView) view.findViewById(R.id.community_blog_title);
            communityBlogTime = (TextView) view.findViewById(R.id.community_blog_time);
            communityBlogPageViews = (TextView) view.findViewById(R.id.community_blog_pageviews);
            communityBlogPic = (ImageView) view.findViewById(R.id.community_blog_pic);
            communityBlogAuthorFace = (CircleImageView) view.findViewById(R.id.community_blog_author_face);
            communityBlogAuthorName = (TextView) view.findViewById(R.id.community_blog_author_name);
        }
    }

    public CommunityBlogItemAdapter() {
        //TODO
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.community_blog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //TODO
        holder.communityBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 启动BlogActivity
                BlogActivity.actionStart(view.getContext(), "0");
            }
        });
    }

    @Override
    public int getItemCount() {
        //TODO
        return 20;
    }

}
