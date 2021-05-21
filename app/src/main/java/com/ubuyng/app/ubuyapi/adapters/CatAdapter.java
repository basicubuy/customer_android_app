package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.R;
import com.ubuyng.app.SingleCatActivity;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ItemRowHolder> {

    private ArrayList<ItemCat> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public CatAdapter(Context context, ArrayList<ItemCat> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_cat, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemCat singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.cat_name.setText(singleItem.getCatTitle());
//        holder.textAddress.setText(singleItem.getPropertyAddress());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));
        Picasso.get().load(singleItem.getCatPic())
                .resize(80, 80)
                .error(R.drawable.loading_placeholder)
//                .fit()
                .into(holder.cat_image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SingleCatActivity.class);
                intent.putExtra("Sub_Id", singleItem.getCatId());
                intent.putExtra("Sub_Title", singleItem.getCatTitle());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView cat_image;
        public TextView cat_name;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            cat_image = itemView.findViewById(R.id.cat_image);
            cat_name = itemView.findViewById(R.id.cat_name);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
        }
    }
}
