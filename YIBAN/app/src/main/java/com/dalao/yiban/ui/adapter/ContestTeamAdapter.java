package com.dalao.yiban.ui.adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dalao.yiban.MyApplication;
import com.dalao.yiban.R;
import com.dalao.yiban.constant.ServerUrlConstant;
import com.dalao.yiban.gson.ContestGson;

import java.util.List;

public class ContestTeamAdapter extends RecyclerView.Adapter<ContestTeamAdapter.ViewHolder>
    implements View.OnClickListener {

    private List<ContestGson.TeamBean> teamBeanList;

    public List<ContestGson.TeamBean> getTeamBeanList() {
        return teamBeanList;
    }

    public void setTeamBeanList(List<ContestGson.TeamBean> teamBeanList) {
        this.teamBeanList = teamBeanList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView contestTeamFace;
        TextView contestTeamPersonName;
        TextView contestTeamName;
        TextView contestTeamNumber;
        TextView contestTeamComment;
        TextView contestTeamTime;
        Button contestTeamJoin;

        public ViewHolder(View view) {
            super(view);
            contestTeamFace = (ImageView) view.findViewById(R.id.contest_team_face);
            contestTeamPersonName = (TextView) view.findViewById(R.id.contest_team_person_name);
            contestTeamName = (TextView) view.findViewById(R.id.contest_team_name);
            contestTeamNumber = (TextView) view.findViewById(R.id.contest_team_number);
            contestTeamComment = (TextView) view.findViewById(R.id.contest_team_comment);
            contestTeamTime = (TextView) view.findViewById(R.id.contest_team_time);
            contestTeamJoin = (Button) view.findViewById(R.id.contest_team_join);
        }
    }

    public ContestTeamAdapter(List<ContestGson.TeamBean> teamBeanList) {
        this.teamBeanList = teamBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.contest_team_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // 刷新UI
        ContestGson.TeamBean teamBean = teamBeanList.get(position);
        holder.contestTeamPersonName.setText(teamBean.getAuthor());
        holder.contestTeamName.setText(teamBean.getName());
        holder.contestTeamNumber.setText(String.valueOf(teamBean.getCount()));
        holder.contestTeamComment.setText(teamBean.getContent());
        holder.contestTeamTime.setText(teamBean.getTime());
        Glide.with(MyApplication.getContext())
                .load(ServerUrlConstant.SERVER_URI + teamBean.getAvatar())
                .into(holder.contestTeamFace);

        // 设置点击事件
        holder.contestTeamFace.setOnClickListener(this);
        holder.contestTeamPersonName.setOnClickListener(this);
        holder.contestTeamJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Toast.makeText(MyApplication.getContext(), "click join " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamBeanList != null ? teamBeanList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contest_team_face:
            case R.id.contest_team_person_name:
                // TODO
                Toast.makeText(MyApplication.getContext(), "click person", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
