package com.dalao.yiban.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.db.Activity;
import com.dalao.yiban.db.SearchResult;
import com.dalao.yiban.ui.activity.ActivityActivity;
import com.dalao.yiban.ui.activity.CollectionActivity;

import java.util.List;

public class CollectionActivityAdapter extends RecyclerView.Adapter<CollectionActivityAdapter.ViewHolder> {

    private List<SearchResult> mList;

    public CollectionActivityAdapter(List<SearchResult> ActivityList)
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
        SearchResult searchResult = mList.get(position);
        holder.pageviews.setText(Integer.toString(searchResult.getPageviews()));
        holder.time.setText(searchResult.getTime());
        holder.title.setText(searchResult.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityActivity.actionStart(v.getContext(),searchResult.getUserid(),Integer.toString(searchResult.getId()),
                searchResult.getTitle(),searchResult.getTime());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
