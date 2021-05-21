package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemRatings;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class ProfileReviewsAdapter extends RecyclerView.Adapter<ProfileReviewsAdapter.ItemRowHolder> {

    private ArrayList<ItemRatings> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public ProfileReviewsAdapter(Context context, ArrayList<ItemRatings> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_profile_reviews, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemRatings singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.review_name.setText(singleItem.getCusName());
        holder.review_msg.setText(singleItem.getRatingComment());
        holder.rating_date.setText(singleItem.getRateDate());
        holder.task_name.setText(singleItem.getProjectName());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));
        Picasso.get().load("https://ubuy.ng/uploads/backend/" + singleItem.getCusImage())
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(holder.review_image);

//        if (singleItem.getServiceVerified().equals("1")){
//            holder.verified_skill.setVisibility(View.VISIBLE);
//        }

        // FIXME: 20/01/2020 on services click instance open fragment bottom sheets
//        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, SingleCatActivity.class);
//                intent.putExtra("Sub_Id", singleItem.getCatId());
//                intent.putExtra("Sub_Title", singleItem.getCatTitle());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public CircularImageView review_image;
        public TextView review_name, task_name, rating_date, review_msg;
        public RatingBar rating;
        public CardView lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            review_image =  itemView.findViewById(R.id.review_image);
            task_name = (TextView) itemView.findViewById(R.id.task_name);
            review_name = (TextView) itemView.findViewById(R.id.review_name);
            rating_date = (TextView) itemView.findViewById(R.id.rating_date);
            review_msg = (TextView) itemView.findViewById(R.id.review_msg);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            lyt_parent = (CardView) itemView.findViewById(R.id.rootLayout);
        }
    }
}
