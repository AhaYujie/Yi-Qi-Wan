package com.dalao.yiban.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalao.yiban.R;

import java.util.List;

public class ContestTeamAdapter extends RecyclerView.Adapter<ContestTeamAdapter.ViewHolder> {

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

    public ContestTeamAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.contest_team_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO
    }

    @Override
    public int getItemCount() {
        return 20;
    }

}
