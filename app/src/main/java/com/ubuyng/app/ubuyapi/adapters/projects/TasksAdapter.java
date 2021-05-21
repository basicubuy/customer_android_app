package com.ubuyng.app.ubuyapi.adapters.projects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ItemRowHolder> {

    private ArrayList<ItemProject> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String project_date;


    public TasksAdapter(Context context, ArrayList<ItemProject> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_receiving_project, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemProject singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.projectName.setText(singleItem.getSubCatName());
        holder.bid_count_text.setText(singleItem.getProjectBidCount());

        switch (singleItem.getProjectBidStatus()) {
            case "1":
                holder.bid_count_card.setVisibility(View.VISIBLE);
                holder.bid_count_card.setCardBackgroundColor( mContext.getResources().getColor(R.color.receiving_color));
                holder.status_ping.setBackgroundColor( mContext.getResources().getColor(R.color.receiving_color));
//               click listener
                 holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("project_id", singleItem.getProjectId());
                intent.putExtra("project_title", singleItem.getSubCatName());
                intent.putExtra("project_brief", singleItem.getProjectMsg());
                intent.putExtra("project_status", "Open for bids");
                mContext.startActivity(intent);
            }
        });

                break;
            case "0":
                holder.bid_count_card.setVisibility(View.GONE);
                holder.status_ping.setBackgroundColor( mContext.getResources().getColor(R.color.waiting_color));
//               click listener
                 holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("project_id", singleItem.getProjectId());
                intent.putExtra("project_title", singleItem.getSubCatName());
                intent.putExtra("project_brief", singleItem.getProjectMsg());
                intent.putExtra("project_status", "Pending");
                mContext.startActivity(intent);
            }
        });

                break;
            case "4":
                holder.tasks_status.setText("Hired");
                holder.bid_count_card.setVisibility(View.GONE);
                holder.status_ping.setBackgroundColor( mContext.getResources().getColor(R.color.waiting_color));
//              click listener
                 holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("project_id", singleItem.getProjectId());
                intent.putExtra("project_title", singleItem.getSubCatName());
                intent.putExtra("project_brief", singleItem.getProjectMsg());
                intent.putExtra("project_status", "40");
                mContext.startActivity(intent);
            }
        });

                break;
        }
//        holder.projectAddress.setText(singleItem.getProjectAdd());
        project_date = singleItem.getProjectDate();
        holder.bid_count_text.setText("Posted "+ project_date);
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
//        public ImageView image;
        public TextView projectName, project_budget, bid_count_text, tasks_status;
        public LinearLayout lyt_parent;
        public CardView bid_count_card;
        public CircularImageView status_ping;

        public ItemRowHolder(View itemView) {
            super(itemView);
            projectName =  itemView.findViewById(R.id.project_name);
            project_budget =  itemView.findViewById(R.id.project_budget);
            bid_count_text =  itemView.findViewById(R.id.bid_count_text);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
