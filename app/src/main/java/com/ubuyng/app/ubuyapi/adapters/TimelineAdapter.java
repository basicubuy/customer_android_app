package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemTracker;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ItemRowHolder> {

    private ArrayList<ItemTracker> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public TimelineAdapter(Context context, ArrayList<ItemTracker> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_single_timeline, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemTracker singleItem = dataList.get(position);
        holder.tracker_date.setText(singleItem.getCreatedAt());
        holder.track_message.setText(singleItem.getMessage());

        if (singleItem.getTrackType().equals("project_status")){
            holder.time_ball.setColorFilter(ContextCompat.getColor(mContext, R.color.receiving_color));
            holder.time_card.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.receiving_color));

        }
//        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, SearchInToolbar.class);
//                intent.putExtra("Sub_Id", singleItem.getSubCatId());
//                intent.putExtra("Sub_Title", singleItem.getSubCatName());
//                mContext.startActivity(intent);
//            }
//        });

}

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView tracker_date, track_message;
        public LinearLayout rootLayout;
        public ImageView time_ball;
        public CardView time_card;

        public ItemRowHolder(View itemView) {
            super(itemView);
            tracker_date =  itemView.findViewById(R.id.tracker_date);
            track_message =  itemView.findViewById(R.id.track_message);
            time_ball =  itemView.findViewById(R.id.time_ball);
            time_card =  itemView.findViewById(R.id.time_card);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
