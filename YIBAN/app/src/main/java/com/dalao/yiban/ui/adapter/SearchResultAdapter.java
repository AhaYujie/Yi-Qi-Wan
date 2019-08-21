package com.dalao.yiban.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;
import com.dalao.yiban.db.SearchResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<SearchResult> mSearchResultList;//

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
            title = (TextView) view.findViewById(R.id.search_result_item_title);
            pageviews = (TextView) view.findViewById(R.id.search_result_item_pageviews);
            time = (TextView) view.findViewById(R.id.search_result_item_time);
            eye = (ImageView) view.findViewById(R.id.search_result_item_eye);
            picture = (ImageView) view.findViewById(R.id.search_result_item_pic);
        }
    }

    public SearchResultAdapter(List<SearchResult> SearchResultList)
    {
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
    }

    @Override
    public int getItemCount() {
        return mSearchResultList.size();
    }

    //  添加数据
    public void addData(int position,int pageviwes,String time,String title,String author,int id) {
//      在list中添加数据，并通知条目加入一条
        SearchResult content = new SearchResult(pageviwes,time,title,author,id);
        mSearchResultList.add(position, content);
        //position是增加的位置
        //后面那个是list里面具体的一个实例
        //添加动画
        notifyItemInserted(position);
    }

    //  删除数据
    public void removeData(int position) {
        mSearchResultList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void refresh( List<SearchResult> list) {
        mSearchResultList = list;
    }
}
