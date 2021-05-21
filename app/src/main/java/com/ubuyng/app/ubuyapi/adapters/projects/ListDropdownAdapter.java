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

import com.balysv.materialripple.MaterialRippleLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Interface.AdapCatInterface;
import com.ubuyng.app.ubuyapi.Models.ItemDropDown;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class ListDropdownAdapter extends RecyclerView.Adapter<ListDropdownAdapter.ItemRowHolder> {

    private ArrayList<ItemDropDown> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String project_date;
    private AdapCatInterface AdapCatInterface;


    public ListDropdownAdapter(Context context, ArrayList<ItemDropDown> dataList, AdapCatInterface AdapCatInterface) {
        this.dataList = dataList;
        this.mContext = context;
        this.AdapCatInterface = AdapCatInterface;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dropdown_list, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemDropDown singleItem = dataList.get(position);
        holder.list_name.setText(singleItem.getItemName());
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdapCatInterface.onCatClick(singleItem.getItemName(), singleItem.getItemId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
//        public ImageView image;
        public TextView list_name;
        public MaterialRippleLayout lyt_parent;
     

        public ItemRowHolder(View itemView) {
            super(itemView);
            list_name =  itemView.findViewById(R.id.list_name);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
        }
    }
}
