package com.ubuyng.app.ubuyapi.adapters;

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
import com.ubuyng.app.ubuyapi.Models.ItemUpay;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class UpayAdapter extends RecyclerView.Adapter<UpayAdapter.ItemRowHolder> {

    private ArrayList<ItemUpay> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public UpayAdapter(Context context, ArrayList<ItemUpay> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_upay, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemUpay singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.project_name.setText(singleItem.getUpayProjectName());
        holder.pro_name.setText(singleItem.getUpayProName());
        holder.project_date.setText(singleItem.getUpayDate());
        holder.amount.setText(singleItem.getUpayAmount());

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("project_id", singleItem.getUpayProjectId());
                intent.putExtra("project_title", singleItem.getUpayProjectName());
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
        public TextView project_name, pro_name, amount, project_date;
        public LinearLayout lyt_parent;
        public CardView bid_count_card;
        public CircularImageView status_ping;

        public ItemRowHolder(View itemView) {
            super(itemView);
            project_name =  itemView.findViewById(R.id.project_name);
            pro_name =  itemView.findViewById(R.id.pro_name);
            amount =  itemView.findViewById(R.id.amount);
            project_date =  itemView.findViewById(R.id.project_date);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
