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

import com.ubuyng.app.Categories.SubCategoryProsActivity;
import com.ubuyng.app.PosterActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.SearchInToolbar;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class SearchSubCatAdapter extends RecyclerView.Adapter<SearchSubCatAdapter.ItemRowHolder> {

    private ArrayList<ItemSubcat> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public SearchSubCatAdapter(Context context, ArrayList<ItemSubcat> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_sub_category, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemSubcat singleItem = dataList.get(position);
        holder.sub_catname.setText(singleItem.getSubTitle());

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postTask = new Intent(mContext, PosterActivity.class);
                postTask.putExtra("sub_id", singleItem.getSubId());
                postTask.putExtra("sub_name", singleItem.getSubTitle());
                mContext.startActivity(postTask);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView sub_catname;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            sub_catname =  itemView.findViewById(R.id.sub_catname);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
