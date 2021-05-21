package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alexvasilkov.gestures.commons.circle.CircleImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class newCatAdapter extends RecyclerView.Adapter<newCatAdapter.ItemRowHolder> {

    private ArrayList<ItemCat> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public newCatAdapter(Context context, ArrayList<ItemCat> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_new_cat, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemCat singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.text.setText(singleItem.getCatTitle());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));
        Picasso.get().load("https://ubuy.ng/app_cdn/images/" + singleItem.getCatPic())
//                .resize(150, 160)
                .error(R.drawable.loading_placeholder)
                .fit()
                .into(holder.image);
//        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Objects.equals(singleItem.getSlideType(), '0')){
//                    Intent intent = new Intent(mContext, SingleCatActivity.class);
//                    intent.putExtra("Sub_Id", singleItem.getSourceId());
//                    mContext.startActivity(intent);
//                }else if(Objects.equals(singleItem.getSlideType(), '1')){
//                    Intent intent = new Intent(mContext, SuggestSubCatAdapter.class);
//                    intent.putExtra("Sub_Id", singleItem.getSourceId());
//                    mContext.startActivity(intent);
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public CircleImageView image;
        public TextView text;
        public CardView lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.cir_cat_image);
            text = (TextView) itemView.findViewById(R.id.new_cat_name);
            lyt_parent = (CardView) itemView.findViewById(R.id.rootLayout);
        }
    }
}
