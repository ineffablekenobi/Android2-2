package com.example.rough;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAd extends  RecyclerView.Adapter<myAd.ViewHolder>{
    private ArrayList<EachPlayerScore> scoreList = new ArrayList<>();

    public ArrayList<EachPlayerScore> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<EachPlayerScore> scoreList) {
        this.scoreList = scoreList;
    }
    Context context;
    public myAd(Context context) {
        this.context = context;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_eachplayerscore, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull  myAd.ViewHolder holder, int position) {
        holder.player.setText(scoreList.get(position).getPlayer());
        holder.score.setText(scoreList.get(position).getScore());

    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout parent;
        TextView player , score;
        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            player = itemView.findViewById(R.id.player);
            score = itemView .findViewById(R.id.score);
        }
    }
}
