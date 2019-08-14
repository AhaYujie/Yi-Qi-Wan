package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.db.Activity;
import com.dalao.yiban.gson.CommunityBlogListGson;
import com.dalao.yiban.ui.activity.BlogActivity;
import com.dalao.yiban.ui.activity.MainActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityBlogItemAdapter extends RecyclerView.Adapter<CommunityBlogItemAdapter.ViewHolder> {

    private MainActivity activity;

    private List<CommunityBlogListGson.DataBean> dataBeanList;

    public List<CommunityBlogListGson.DataBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<CommunityBlogListGson.DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout communityBlog;
        TextView communityBlogTitle;
        TextView communityBlogTime;
        TextView communityBlogPageViews;
        ImageView communityBlogPic;
        CircleImageView communityBlogAuthorFace;
        TextView communityBlogAuthorName;

        private ViewHolder(View view) {
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

    public CommunityBlogItemAdapter(MainActivity activity) {
        this.activity = activity;
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
        final CommunityBlogListGson.DataBean dataBean = dataBeanList.get(position);
        holder.communityBlogTitle.setText(dataBean.getTitle());
        holder.communityBlogAuthorName.setText(dataBean.getAuthor());
        holder.communityBlogTime.setText(dataBean.getTime());
        holder.communityBlogPageViews.setText(String.valueOf(dataBean.getPageviews()));
        holder.communityBlogPic.setVisibility(View.GONE);
        Glide.with(MyApplication.getContext())
                .load(ServerUrlConstant.SERVER_URI + dataBean.getAvatar())
                .into(holder.communityBlogAuthorFace);
        holder.communityBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 启动BlogActivity
                BlogActivity.actionStart(view.getContext(), activity.userId,
                        String.valueOf(dataBean.getId()), dataBean.getAvatar(),
                        String.valueOf(dataBean.getAuthor()), dataBean.getTitle(),
                        dataBean.getTime(), String.valueOf(dataBean.getAuthorid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBeanList != null ? dataBeanList.size() : 0;
    }

}
