package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ubuyng.app.R;
import com.ubuyng.app.SearchInToolbar;
import com.ubuyng.app.ubuyapi.Models.ItemSlider;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class HomeSliderAdapter extends RecyclerView.Adapter<HomeSliderAdapter.ItemRowHolder> {

    private ArrayList<ItemSlider> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public HomeSliderAdapter(Context context, ArrayList<ItemSlider> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_home_slider, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemSlider singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

//        holder.textAddress.setText(singleItem.getPropertyAddress());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));
        Picasso.get().load("https://ubuy.ng/app_cdn/images/" + singleItem.getSlidePic())
//                .resize(150, 160)
                .error(R.drawable.loading_placeholder)
                .fit()
                .into(holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchInToolbar.class);
                intent.putExtra("Sub_Id", singleItem.getSourceId());
                intent.putExtra("Sub_Title", singleItem.getSlideName());

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
        public CardView lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.slide_image);
            lyt_parent = (CardView) itemView.findViewById(R.id.rootLayout);
        }
    }
}
