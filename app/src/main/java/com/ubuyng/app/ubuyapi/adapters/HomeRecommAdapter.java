package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class HomeRecommAdapter extends RecyclerView.Adapter<HomeRecommAdapter.ItemRowHolder> {

    private ArrayList<ItemSubcat> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public HomeRecommAdapter(Context context, ArrayList<ItemSubcat> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_recom, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemSubcat singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.text.setText(singleItem.getSubTitle());
//        holder.textAddress.setText(singleItem.getPropertyAddress());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));

//        if(position % 7 == 0){
//            holder.rec_card.setCardBackgroundColor(R.color.color_bg1);
//        }else if(position % 7 == 1){
//            holder.rec_card.setCardBackgroundColor(Resource.);
//        }else if(position % 7 == 2){
//            holder.rec_card.setImageResource(R.drawable.ic_bg_gra3);
//        }else if(position % 7 == 3){
//            holder.rec_card.setImageResource(R.drawable.ic_bg_gra1);
//        }else if(position % 7 == 4){
//            holder.rec_card.setImageResource(R.drawable.ic_bg_gra4);
//        }else if(position % 7 == 5){
//            holder.rec_card.setImageResource(R.drawable.ic_bg_4);
//        }else if(position % 7 > 6){
//            holder.rec_card.setImageResource(R.drawable.ic_bg_4);
//        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public LinearLayout lyt_parent;
        public CardView rec_card;

        public ItemRowHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.sub_name);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            rec_card = itemView.findViewById(R.id.rec_card);
        }
    }
}
