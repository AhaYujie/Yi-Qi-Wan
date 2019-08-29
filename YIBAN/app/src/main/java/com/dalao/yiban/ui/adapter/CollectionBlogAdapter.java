package com.dalao.yiban.ui.adapter;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.db.Blog;
import com.dalao.yiban.db.CollectBlog;
import com.dalao.yiban.ui.activity.BlogActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollectionBlogAdapter extends RecyclerView.Adapter<CollectionBlogAdapter.ViewHolder> {

    private List<CollectBlog> mList;

    public CollectionBlogAdapter(List<CollectBlog> BlogList)
    {
        mList = BlogList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView pageviews;
        TextView time;
        ImageView eye;
        ImageView face;
        TextView name;

        public ViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.my_collection_blog_title);
            pageviews = (TextView) view.findViewById(R.id.my_collection_blog_pageviews);
            time = (TextView) view.findViewById(R.id.my_collection_blog_time);
            eye = (ImageView) view.findViewById(R.id.my_collection_blog_eye);
            face = (ImageView) view.findViewById(R.id.my_collection_blog_author_face);
            name = (TextView) view.findViewById(R.id.my_collection_blog_author_name);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull CollectionBlogAdapter.ViewHolder holder, int position) {
        CollectBlog collectBlog = mList.get(position);
        holder.pageviews.setText(Integer.toString(collectBlog.getPageviews()));
        holder.time.setText(collectBlog.getTime());
        holder.title.setText(collectBlog.getTitle());
        holder.name.setText(collectBlog.getName());
        Glide.with(MyApplication.getContext()).load(ServerUrlConstant.SERVER_URI + collectBlog.getAvatar()).into(holder.face);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogActivity.actionStart(v.getContext(),Integer.toString(collectBlog.getUserid()),Integer.toString(collectBlog.getId()),
                        collectBlog.getAvatar(),collectBlog.getName(),collectBlog.getTitle(),collectBlog.getTime(),collectBlog.getAuthorid());Integer.toString(collectBlog.getId());
            }
        });
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
        return mList.size();
    }
}
