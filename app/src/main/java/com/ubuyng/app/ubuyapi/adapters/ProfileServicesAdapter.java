package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemServices;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class ProfileServicesAdapter extends RecyclerView.Adapter<ProfileServicesAdapter.ItemRowHolder> {

    private ArrayList<ItemServices> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public ProfileServicesAdapter(Context context, ArrayList<ItemServices> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_profile_services, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemServices singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.service_name.setText(singleItem.getServiceName());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));


//        if (singleItem.getServiceVerified().equals("1")){
//            holder.verified_skill.setVisibility(View.VISIBLE);
//        }

        // FIXME: 20/01/2020 on services click instance open fragmebt bottom sheets
//        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, SingleCatActivity.class);
//                intent.putExtra("Sub_Id", singleItem.getCatId());
//                intent.putExtra("Sub_Title", singleItem.getCatTitle());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView service_image;
        public TextView service_name, verified_skill;
        public CardView lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            service_name = (TextView) itemView.findViewById(R.id.service_name);
            lyt_parent = (CardView) itemView.findViewById(R.id.rootLayout);
        }
    }
}
