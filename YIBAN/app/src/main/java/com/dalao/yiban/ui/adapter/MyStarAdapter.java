package com.dalao.yiban.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.db.MyStar;
import com.dalao.yiban.ui.activity.MyStarActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyStarAdapter extends RecyclerView.Adapter<MyStarAdapter.ViewHolder> {

    List<MyStar> myStarList = new ArrayList<>();

    public MyStarAdapter(List<MyStar>myStarList){
        this.myStarList=myStarList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView authorname;
        CircleImageView picture;
        TextView follow;
        TextView unfollow;

        public ViewHolder(View view)
        {
            super(view);
            authorname = (TextView) view.findViewById(R.id.my_star_authorname);
            follow = (TextView) view.findViewById(R.id.my_star_follow);
            picture = (CircleImageView) view.findViewById(R.id.my_star_picture);
        }
    }

    @Override
    public int getItemCount() {
        return myStarList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyStarAdapter.ViewHolder holder, int position) {
        MyStar myStar = myStarList.get(position);
        holder.authorname.setText(myStar.getAuthor());
        Glide.with(MyApplication.getContext()).load(ServerUrlConstant.SERVER_URI + myStar.getAvatar())
                .into(holder.picture);
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.follow.getText().equals("已关注")==true) {
                    holder.follow.setText("+ 关注");
                    holder.follow.setBackgroundResource(R.drawable.background_blue);
                    holder.follow.setTextColor(Color.parseColor("#003E80"));
                }else{
                    holder.follow.setText("已关注");
                    holder.follow.setBackgroundResource(R.drawable.background_grey);
                    holder.follow.setTextColor(Color.parseColor("#312323"));
                }
                notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //以下是POST方法
                            HashMap<String, String> paramsMap = new HashMap<>();//用哈希表来存参数
                            paramsMap.put("follower",Integer.toString(myStar.getId()) );
                            paramsMap.put("followed",Integer.toString(myStar.getAuthorid()) );
                            FormBody.Builder builder = new FormBody.Builder();
                            for (String key : paramsMap.keySet()) {
                                //追加表单信息
                                builder.add(key, paramsMap.get(key));
                            }
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody formBody = builder.build();
                            if(holder.follow.getText().equals("已关注")==true) {
                                Request request = new Request.Builder().url("http://188888888.xyz:5000/unfollow").post(formBody).build();
                                okHttpClient.newCall(request).execute();
                            }else{
                                Request request = new Request.Builder().url("http://188888888.xyz:5000/follow").post(formBody).build();
                                okHttpClient.newCall(request).execute();
                            }
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
