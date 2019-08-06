package com.dalao.yiban.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.db.Activity;
import com.dalao.yiban.gson.HomeListGson;
import com.dalao.yiban.ui.activity.ContestActivity;
import com.dalao.yiban.ui.fragment.HomeFragment;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {

    private HomeListGson homeListGson;

    public HomeListGson getHomeListGson() {
        return homeListGson;
    }

    public void setHomeListGson(HomeListGson homeListGson) {
        this.homeListGson = homeListGson;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout homeItem;
        TextView homeItemTitle;
        TextView homeItemTime;
        TextView homeItemPageViews;
        ImageView homeItemPic;

        public ViewHolder(View view) {
            super(view);
            homeItem = (RelativeLayout) view.findViewById(R.id.home_item);
            homeItemTitle = (TextView) view.findViewById(R.id.home_item_title);
            homeItemTime = (TextView) view.findViewById(R.id.home_item_time);
            homeItemPageViews = (TextView) view.findViewById(R.id.home_item_pageviews);
            homeItemPic = (ImageView) view.findViewById(R.id.home_item_pic);
        }
    }

    public HomeItemAdapter(HomeListGson homeListGson) {
        this.homeListGson = homeListGson;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final HomeListGson.DataBean dataBean = homeListGson.getData().get(position);
        holder.homeItemTitle.setText(dataBean.getTitle());
        holder.homeItemTime.setText(dataBean.getTime());
        holder.homeItemPageViews.setText(String.valueOf(dataBean.getPageviews()));
        holder.homeItemPic.setVisibility(View.GONE);

        // layout点击事件
        holder.homeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 启动ContestActivity
                ContestActivity.actionStart(view.getContext(), String.valueOf(dataBean.getId()));
            }
        });

//        //设置图片圆角角度
//        RoundedCorners roundedCorners= new RoundedCorners(20);
//        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
//        Glide.with(MyApplication.getContext()).load(R.drawable.ic_yiban).apply(options).into(holder.homeItemPic);
    }

    @Override
    public int getItemCount() {
        return homeListGson != null ? homeListGson.getData().size() : 0;
    }

}
