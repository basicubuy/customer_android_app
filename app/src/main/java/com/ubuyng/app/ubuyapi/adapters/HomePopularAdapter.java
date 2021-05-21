package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubuyng.app.Categories.SubCategoryProsActivity;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ubuyng.app.R;
import com.ubuyng.app.SearchInToolbar;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class HomePopularAdapter extends RecyclerView.Adapter<HomePopularAdapter.ItemRowHolder> {

    private ArrayList<ItemSubcat> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public HomePopularAdapter(Context context, ArrayList<ItemSubcat> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemSubcat singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.text.setText(singleItem.getSubTitle());
//        holder.textAddress.setText(singleItem.getPropertyAddress());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));
        Picasso.get().load(singleItem.getSubPic())
                .resize(180, 150)
                .error(R.drawable.loading_placeholder)
                .into(holder.image);


        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SubCategoryProsActivity.class);
                intent.putExtra("Sub_Id", singleItem.getSubId());
                intent.putExtra("Sub_Title", singleItem.getSubTitle());
                intent.putExtra("Sub_image", singleItem.getSubPic());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.sub_image_main);
            text = (TextView) itemView.findViewById(R.id.sub_name);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
