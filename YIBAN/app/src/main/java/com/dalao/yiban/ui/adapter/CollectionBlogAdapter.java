package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.db.Blog;

import java.util.List;

public class CollectionBlogAdapter extends RecyclerView.Adapter<CollectionBlogAdapter.ViewHolder> {

    private List<Blog> mList;

    public CollectionBlogAdapter(List<Blog> BlogList)
    {
        mList = BlogList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView pageviews;
        TextView time;
        ImageView eye;

        public ViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.my_collection_blog_title);
            pageviews = (TextView) view.findViewById(R.id.my_collection_blog_pageviews);
            time = (TextView) view.findViewById(R.id.my_collection_blog_time);
            eye = (ImageView) view.findViewById(R.id.my_collection_blog_eye);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull CollectionBlogAdapter.ViewHolder holder, int position) {
        Blog blog = mList.get(position);
        holder.pageviews.setText(blog.getNumOfView());
        holder.time.setText(blog.getCreateTime().toString());
        holder.title.setText(blog.getTitle());
    }

    @NonNull
    @Override
    public CollectionBlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_collection_blog_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
