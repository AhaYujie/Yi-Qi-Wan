package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.db.Contest;
import com.dalao.yiban.db.SearchResult;
import com.dalao.yiban.ui.activity.ActivityActivity;
import com.dalao.yiban.ui.activity.ContestActivity;
import com.dalao.yiban.ui.activity.SearchActivity;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<SearchResult> mSearchResultList;

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
            title = (TextView) view.findViewById(R.id.search_result_title);
            pageviews = (TextView) view.findViewById(R.id.search_result_pageviews);
            time = (TextView) view.findViewById(R.id.search_result_time);
            eye = (ImageView) view.findViewById(R.id.search_result_eye);
            picture = (ImageView) view.findViewById(R.id.search_result_pic);
        }
    }

    public SearchResultAdapter(List<SearchResult> SearchResultList){
        mSearchResultList = SearchResultList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResult searchResult = mSearchResultList.get(position);
        holder.title.setText(searchResult.getTitle());
        holder.time.setText(searchResult.getTime());
        holder.pageviews.setText(Integer.toString(searchResult.getPageviews()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchResult.getType()==0)
                    ContestActivity.actionStart(v.getContext(),searchResult.getUserid(),Integer.toString(searchResult.getId()),searchResult.getTitle(),searchResult.getTime());
                else
                    ActivityActivity.actionStart(v.getContext(),searchResult.getUserid(),Integer.toString(searchResult.getId()),searchResult.getTitle(),searchResult.getTime());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchResultList.size();
    }
}
