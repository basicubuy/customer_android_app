package com.ubuyng.app.ubuyapi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemNotify;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class NotifiesAdapter extends RecyclerView.Adapter<NotifiesAdapter.ItemRowHolder> {

    private ArrayList<ItemNotify> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String project_date;


    public NotifiesAdapter(Context context, ArrayList<ItemNotify> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_cus_notify, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemNotify singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.notifyMsg.setText(singleItem.getNotifyMsg());
        holder.notifyDate.setText(singleItem.getNotifyDate());

//        switch (singleItem.getNotifyType()) {
//            case "1":
////                this is for new bids
//                holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, ProjectBidActivity.class);
//                        intent.putExtra("project_id", singleItem.getProjectId());
////                intent.putExtra("project_title", singleItem.getSubCatName());
////                intent.putExtra("project_brief", singleItem.getProjectMsg());
//                        mContext.startActivity(intent);
//                    }
//                });
//                break;
//            case "0":
////                this is for transaction notification
//                holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, ProjectBidActivity.class);
//                        intent.putExtra("project_id", singleItem.getProjectId());
////                intent.putExtra("project_title", singleItem.getSubCatName());
////                intent.putExtra("project_brief", singleItem.getProjectMsg());
//                        mContext.startActivity(intent);
//                    }
//                });
//                break;
//        }



    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView notifyMsg, notifyDate;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            notifyMsg =  itemView.findViewById(R.id.notify_msg);
            notifyDate =  itemView.findViewById(R.id.notify_date);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
