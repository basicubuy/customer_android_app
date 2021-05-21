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

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.R;
import com.ubuyng.app.SearchInToolbar;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class SuggestSubCatAdapter extends RecyclerView.Adapter<SuggestSubCatAdapter.ItemRowHolder> {

    private ArrayList<ItemProject> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public SuggestSubCatAdapter(Context context, ArrayList<ItemProject> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_search_subcats, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemProject singleItem = dataList.get(position);
        holder.sub_catname.setText(singleItem.getSubCatName());
        holder.sub_catdes.setText(singleItem.getSubCatDes());
        Picasso.get().load("https://ubuy.ng/uploads/backend/" + singleItem.getSubCatIcon())
//                .resize(150, 160)
                .error(R.drawable.loading_placeholder)
                .fit()
                .into(holder.cat_icon);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchInToolbar.class);
                intent.putExtra("Sub_Id", singleItem.getSubCatId());
                intent.putExtra("Sub_Title", singleItem.getSubCatName());
                mContext.startActivity(intent);
            }
        });

}

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView sub_catname, sub_catdes;
        public LinearLayout lyt_parent;
        public CircularImageView cat_icon;

        public ItemRowHolder(View itemView) {
            super(itemView);
            sub_catname =  itemView.findViewById(R.id.sub_catname);
            sub_catdes =  itemView.findViewById(R.id.sub_catdes);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            cat_icon = itemView.findViewById(R.id.cat_icon);
        }
    }
}
