package com.ubuyng.app.ubuyapi.adapters.projects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.chip.Chip;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.Models.ItemServices;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;


/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ItemRowHolder> {

    private ArrayList<ItemServices> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public ServicesAdapter(Context context, ArrayList<ItemServices> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);


    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_pro_service, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemServices singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.service_name.setText(singleItem.getServiceName());
        Picasso.get().load(singleItem.getServiceImage())
                .fit()
                .error(R.drawable.pro_cover_photo)
                .into(holder.service_img);
//        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(mContext, ProjectBidActivity.class);
//                intent.putExtra("project_id", singleItem.getProjectId());
//                intent.putExtra("project_title", singleItem.getSubCatName());
//                intent.putExtra("project_brief", singleItem.getProjectMsg());
//                intent.putExtra("project_amount", singleItem.getProjectBudget());
//                intent.putExtra("project_version", singleItem.getProjectVersion());
//                mContext.startActivity(intent);
//            }
//        });




    }


    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
//        public ImageView image;
        public TextView service_name;
        public LinearLayout lyt_parent;
        public ImageView service_img;


        public ItemRowHolder(View itemView) {
            super(itemView);
            service_name =  itemView.findViewById(R.id.service_name);
            service_img =  itemView.findViewById(R.id.service_img);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }



}
