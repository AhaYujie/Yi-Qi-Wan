package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dalao.yiban.R;
import com.dalao.yiban.db.UsedSearch;
import com.dalao.yiban.ui.activity.SearchActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsedSearchAdapter extends RecyclerView.Adapter<UsedSearchAdapter.ViewHolder> {

    private List<UsedSearch> mUsedSearchList;

    SearchActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView UsedSearchContent;
        ImageView Delete;

        public ViewHolder(View view)
        {
            super(view);
            UsedSearchContent = (TextView) view.findViewById(R.id.UsedSearchItem);
            Delete = (ImageView) view.findViewById(R.id.UsedSearchDelete);
        }
    }

    public UsedSearchAdapter(List<UsedSearch> UsedSearchList,SearchActivity context)
    {
        mUsedSearchList = UsedSearchList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.used_search_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsedSearch usedSearch = mUsedSearchList.get(position);
        holder.UsedSearchContent.setText(usedSearch.getContent());
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.TouchContent(usedSearch.getContent());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mUsedSearchList.size()<=10)
            return mUsedSearchList.size();
        else
            return 10;
    }

    //  添加数据
    public void addData(int position) {
//      在list中添加数据，并通知条目加入一条
        UsedSearch content1 = new UsedSearch("acm");
        mUsedSearchList.add(position, content1);
        //position是增加的位置
        //后面那个是list里面具体的一个实例
        //添加动画
        notifyItemInserted(position);
    }

    //  删除数据
    public void removeData(int position) {
        mUsedSearchList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

}
