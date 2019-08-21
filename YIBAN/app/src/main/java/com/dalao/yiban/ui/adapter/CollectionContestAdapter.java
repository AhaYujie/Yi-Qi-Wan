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

import java.util.List;

public class CollectionContestAdapter extends RecyclerView.Adapter<CollectionContestAdapter.ViewHolder> {

    private List<Contest> mList;

    public CollectionContestAdapter(List<Contest> ContestList)
    {
        mList = ContestList;
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
            title = (TextView) view.findViewById(R.id.my_collection_contest_title);
            pageviews = (TextView) view.findViewById(R.id.my_collection_contest_pageviews);
            time = (TextView) view.findViewById(R.id.my_collection_contest_time);
            eye = (ImageView) view.findViewById(R.id.my_collection_contest_eye);
            picture = (ImageView) view.findViewById(R.id.my_collection_contest_pic);
        }
    }

    @NonNull
    @Override
    public CollectionContestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_collection_competition_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionContestAdapter.ViewHolder holder, int position) {
        Contest contest = mList.get(position);
        holder.pageviews.setText(contest.getNumOfView());
        holder.time.setText(contest.getCreateTIme().toString());
        holder.title.setText(contest.getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
