package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.db.MyStar;

import java.util.ArrayList;
import java.util.List;

public class MyStarAdapter extends RecyclerView.Adapter<MyStarAdapter.ViewHolder> {

    List<MyStar> myStarList = new ArrayList<>();

    public MyStarAdapter(List<MyStar>myStarList){
        this.myStarList=myStarList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView authorname;
        ImageView picture;
        TextView follow;
        TextView unfollow;

        public ViewHolder(View view)
        {
            super(view);
            authorname = (TextView) view.findViewById(R.id.my_star_authorname);
            follow = (TextView) view.findViewById(R.id.my_star_follow);
            unfollow = (TextView) view.findViewById(R.id.my_star_unfollow);
            picture = (ImageView) view.findViewById(R.id.my_collection_activity_pic);
        }
    }

    @Override
    public int getItemCount() {
        return myStarList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyStarAdapter.ViewHolder holder, int position) {
        MyStar myStar =  myStarList.get(position);
        holder.authorname.setText(myStar.getAuthor());
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    public MyStarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_star_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
}
