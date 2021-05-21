package com.ubuyng.app.ubuyapi.adapters.Categories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.ProProfile.ProfileActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemPros;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class SubTrendingProsAdapter extends RecyclerView.Adapter<SubTrendingProsAdapter.ItemRowHolder> {

    private ArrayList<ItemPros> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public SubTrendingProsAdapter(Context context, ArrayList<ItemPros> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_pros, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemPros singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.user_name.setText(singleItem.getProName());
        holder.service_name.setText(singleItem.getProService());
        holder.total_jobs.setText(singleItem.getProjectCount() + " jobs completed");


        Picasso.get().load(singleItem.getProfileImage())
                .resize(180, 150)
                .error(R.drawable.loading_placeholder)
                .into(holder.user_profile_img);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("pro_id", singleItem.getProId());
                intent.putExtra("pro_name", singleItem.getProName());
                intent.putExtra("premium_pro", singleItem.getPremiumPro());
                intent.putExtra("profile_img", singleItem.getProfileImage());
                intent.putExtra("task_done", singleItem.getProjectCount());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView user_profile_img;
        public TextView user_name, service_name, total_jobs;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            user_profile_img =  itemView.findViewById(R.id.user_profile_img);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            service_name = (TextView) itemView.findViewById(R.id.service_name);
            total_jobs = (TextView) itemView.findViewById(R.id.total_jobs);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
