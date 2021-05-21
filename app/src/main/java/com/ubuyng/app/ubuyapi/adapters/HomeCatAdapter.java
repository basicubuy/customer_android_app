package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ubuyng.app.R;
import com.ubuyng.app.SingleCatActivity;
import com.ubuyng.app.ubuyapi.Models.ItemCat;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class HomeCatAdapter extends RecyclerView.Adapter<HomeCatAdapter.ItemRowHolder> {

    private ArrayList<ItemCat> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public HomeCatAdapter(Context context, ArrayList<ItemCat> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_home_cat, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemCat singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.text.setText(singleItem.getCatTitle());
//        holder.textAddress.setText(singleItem.getPropertyAddress());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));
//        Picasso.get().load("https://ubuy.ng/uploads/backend/" + singleItem.getCatPic())
//                .resize(150, 160)
//                .error(R.drawable.loading_placeholder)
////                .fit()
//                .into(holder.image);

        String colorCode = singleItem.getCatColor();
        Log.i("oncolorTester", colorCode);

//        if (colorCode != null) {
//            holder.color_lyt.setBackgroundColor(Color.parseColor(colorCode));
//        }

//        tODO:: jUST TO TRY GLIDE INSTEAD OF PICASS
        Glide.with(mContext)
                .load("https://ubuy.ng/uploads/backend/" + singleItem.getCatPic())
                .centerCrop()
                .placeholder(R.drawable.loading_placeholder)
                .into(holder.image);
//        todo: glide ends here

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
        public ImageView image;
        public TextView text;
        public LinearLayout lyt_parent, color_lyt;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image =  itemView.findViewById(R.id.cat_image);
            text = itemView.findViewById(R.id.cat_name);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
            color_lyt = itemView.findViewById(R.id.color_lyt);
        }
    }
}
