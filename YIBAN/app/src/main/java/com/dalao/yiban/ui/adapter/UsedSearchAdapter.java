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

    private SearchActivity mcontext;

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
        this.mcontext=context;
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
                if(usedSearch.getUserId()==-1){
                    mcontext.delete_keyword_in_file(usedSearch.getContent(),usedSearch.getUserId());
                    mUsedSearchList.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    mcontext.Delete_the_search(usedSearch);
                    mUsedSearchList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.TouchContent(usedSearch.getContent());
            }
        });
    }

    @Override
    public int getItemCount() {
            return mUsedSearchList.size();
    }

    //  删除数据
    public void removeData(int position) {
        mUsedSearchList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setFilter(List<UsedSearch>filterWords){
        mUsedSearchList=filterWords;
        notifyDataSetChanged();
    }

}
