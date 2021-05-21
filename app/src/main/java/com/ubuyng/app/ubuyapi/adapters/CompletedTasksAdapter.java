package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.card.MaterialCardView;
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
public class CompletedTasksAdapter extends RecyclerView.Adapter<CompletedTasksAdapter.ItemRowHolder> {

    private ArrayList<ItemProject> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String project_date;


    public CompletedTasksAdapter(Context context, ArrayList<ItemProject> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_done_project, parent, false);
        return new ItemRowHolder(v);
    }

//    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemProject singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.projectName.setText(singleItem.getSubCatName());
        holder.bid_count_text.setText(singleItem.getProjectBidCount());
     project_date = singleItem.getProjectDate();
        holder.projectDate.setText("Posted "+ project_date);
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));

        holder.ripple_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("project_id", singleItem.getProjectId());
                intent.putExtra("project_title", singleItem.getSubCatName());
                intent.putExtra("project_brief", singleItem.getProjectMsg());
                intent.putExtra("project_status", "Done");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
//        public ImageView image;
        public TextView projectName, projectDate, bid_count_text;
        public LinearLayout lyt_parent;
        public MaterialCardView bid_count_card;
        public MaterialRippleLayout ripple_root;

        public ItemRowHolder(View itemView) {
            super(itemView);
            projectName =  itemView.findViewById(R.id.project_name);
            projectDate =  itemView.findViewById(R.id.project_date);
            bid_count_card =  itemView.findViewById(R.id.bid_count_card);
            bid_count_text =  itemView.findViewById(R.id.bid_count_text);
            ripple_root =  itemView.findViewById(R.id.ripple_root);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
