package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.db.Activity;

import java.util.List;

public class CollectionActivityAdapter extends RecyclerView.Adapter<CollectionActivityAdapter.ViewHolder> {

    private List<Activity> mList;

    public CollectionActivityAdapter(List<Activity> ActivityList)
    {
        mList = ActivityList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView pageviews;
        TextView time;
        ImageView eye;
        ImageView picture;

        public ViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.my_collection_activity_title);
            pageviews = (TextView) view.findViewById(R.id.my_collection_activity_pageviews);
            time = (TextView) view.findViewById(R.id.smy_collection_activity_time);
            eye = (ImageView) view.findViewById(R.id.my_collection_activity_eye);
            picture = (ImageView) view.findViewById(R.id.my_collection_activity_pic);
        }
    }

    @NonNull
    @Override
    public CollectionActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_collection_activity_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionActivityAdapter.ViewHolder holder, int position) {
        Activity activity = mList.get(position);
        holder.pageviews.setText(activity.getNumOfView());
        holder.time.setText(activity.getCreateTime().toString());
        holder.title.setText(activity.getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
