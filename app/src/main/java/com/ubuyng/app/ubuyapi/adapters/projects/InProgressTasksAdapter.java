package com.ubuyng.app.ubuyapi.adapters.projects;

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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.ProjectBidInProgressActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class InProgressTasksAdapter extends RecyclerView.Adapter<InProgressTasksAdapter.ItemRowHolder> {

    private ArrayList<ItemProject> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String project_date, project_id;
    ArrayList<ItemBids> mBids;
    public RecyclerView pending_rv;
    public Integer bid_counter, deduct_avater_view;

    public InProgressTasksAdapter(Context context, ArrayList<ItemProject> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);


    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_single_inprogress_task, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemProject singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.projectName.setText(singleItem.getSubCatName());
        holder.project_message.setText(singleItem.getProjectMsg());

        holder.pro_name.setText(singleItem.getProName());
        holder.project_started_date.setText(singleItem.getProjectStartDate());
        holder.bid_amount_txt.setText(singleItem.getProjectBudget());

        Picasso.get().load(singleItem.getSelectedPro())
                .fit()
                .error(R.drawable.loading_placeholder)
                .into(holder.bidder_image);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ProjectBidInProgressActivity.class);
                intent.putExtra("project_id", singleItem.getProjectId());
                intent.putExtra("project_title", singleItem.getSubCatName());
                intent.putExtra("project_brief", singleItem.getProjectMsg());
                intent.putExtra("project_version", singleItem.getProjectVersion());
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
        public TextView projectName, bid_amount_txt, project_started_date, project_message, pro_skill, pro_name;
        public LinearLayout lyt_parent;
        RelativeLayout bidder_avater_showcase;
        public ShapeOfView project_btn_sh;
        public CircularImageView bidder_image;

        public ItemRowHolder(View itemView) {
            super(itemView);
            projectName =  itemView.findViewById(R.id.project_title);
            pro_name =  itemView.findViewById(R.id.pro_name);
            pro_skill =  itemView.findViewById(R.id.pro_skill);
            project_message =  itemView.findViewById(R.id.project_message);
            bidder_image =  itemView.findViewById(R.id.bidder_image);
            bid_amount_txt =  itemView.findViewById(R.id.bid_amount_txt);
            project_started_date =  itemView.findViewById(R.id.project_started_date);
            project_btn_sh =  itemView.findViewById(R.id.project_btn_sh);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }



}
