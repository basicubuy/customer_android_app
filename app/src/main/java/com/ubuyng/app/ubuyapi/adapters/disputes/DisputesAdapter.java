package com.ubuyng.app.ubuyapi.adapters.disputes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.chip.Chip;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.Dispute.DisputeResoulutionsActivity;
import com.ubuyng.app.Dispute.SingleDisputeActivity;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemDispute;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;


/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class DisputesAdapter extends RecyclerView.Adapter<DisputesAdapter.ItemRowHolder> {

    private ArrayList<ItemDispute> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String project_date, project_id;
    ArrayList<ItemBids> mBids;
    public RecyclerView pending_rv;
    public Integer bid_counter, deduct_avater_view;

    public DisputesAdapter(Context context, ArrayList<ItemDispute> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);


    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_disputes, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemDispute singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.dispute_task.setText(singleItem.getTaskName());
        holder.dispute_cat.setText(singleItem.getDisputeCat());
        holder.dispute_ref.setText(singleItem.getTaskRef());
        holder.dispute_time.setText(singleItem.getDisputeDate());

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DisputeIntent = new Intent(mContext, SingleDisputeActivity.class);
                DisputeIntent.putExtra("dispute_id", singleItem.getDisputeId());
                DisputeIntent.putExtra("dispute_cat", singleItem.getDisputeCat());
                DisputeIntent.putExtra("dispute_date", singleItem.getDisputeDate());
                DisputeIntent.putExtra("dispute_status", singleItem.getDisputeStatus());
                DisputeIntent.putExtra("dispute_task", singleItem.getTaskName());
                DisputeIntent.putExtra("dispute_ref", singleItem.getTaskRef());
                mContext.startActivity(DisputeIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView dispute_ref, dispute_cat, dispute_task, dispute_time;
        public LinearLayout lyt_parent;


        public ItemRowHolder(View itemView) {
            super(itemView);
            dispute_ref =  itemView.findViewById(R.id.dispute_ref);
            dispute_cat =  itemView.findViewById(R.id.dispute_cat);
            dispute_task =  itemView.findViewById(R.id.dispute_task);
            dispute_time =  itemView.findViewById(R.id.dispute_time);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }



}
