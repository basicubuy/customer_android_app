package com.ubuyng.app.ubuyapi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemPortfolio;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ItemRowHolder> {

    private ArrayList<ItemPortfolio> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public PortfolioAdapter(Context context, ArrayList<ItemPortfolio> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_pro_feed, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemPortfolio singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;
        Picasso.get().load(singleItem.getPortImg())
                .error(R.drawable.loading_placeholder)
                .into(holder.feed_image);

//        Toast.makeText(mContext, singleItem.getPortImg(),Toast.LENGTH_LONG).show();

        holder.feed_title.setText(singleItem.getPortTitle());
        holder.feed_comment.setText(singleItem.getPortComments());
        holder.feed_like.setText(singleItem.getPortLikes());
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("gallery_title", singleItem.getPortTitle());
                intent.putExtra("gallery_img", singleItem.getPortImg());
                intent.putExtra("project_id", singleItem.getPortId());



//                intent.putExtra("project_title", singleItem.getPortTitle());
//                intent.putExtra("project_comments", singleItem.getPortComments());
//                intent.putExtra("project_likes", singleItem.getPortLikes());
//                intent.putExtra("project_img", singleItem.getPortImg());
//
//                intent.putExtra("unique_ref_id", singleItem.getUnique_ref_id());
//                intent.putExtra("sub_category_id", singleItem.getSub_category_id());
//                intent.putExtra("status", singleItem.getStatus());
//                intent.putExtra("user_id", singleItem.getUser_id());
//                intent.putExtra("pro_id", singleItem.getPro_id());
//                intent.putExtra("project_tit", singleItem.getProject_title());
//                intent.putExtra("pro_name", singleItem.getPro_name());
//                intent.putExtra("cus_name", singleItem.getCus_name());
//                intent.putExtra("sub_category_name", singleItem.getSub_category_name());
//                intent.putExtra("sub_category_slug", singleItem.getSub_category_slug());
//                intent.putExtra("user_role", singleItem.getUser_role());
//                intent.putExtra("project_message", singleItem.getProject_message());
//                intent.putExtra("phone_number", singleItem.getPhone_number());
//                intent.putExtra("lat", singleItem.getLat());
//                intent.putExtra("lng", singleItem.getLng());
//                intent.putExtra("address", singleItem.getAddress());
//                intent.putExtra("city", singleItem.getCity());
//                intent.putExtra("state", singleItem.getState());
//                intent.putExtra("version_", singleItem.getVersion_());
//                intent.putExtra("notify_pros", singleItem.getNotify_pros());
//                intent.putExtra("notify_sms_reminder", singleItem.getNotify_sms_reminder());
//                intent.putExtra("upay_type", singleItem.getUpay_type());
//                intent.putExtra("post_medium", singleItem.getPost_medium());
//                intent.putExtra("started_at", singleItem.getStarted_at());
//                intent.putExtra("paused_at", singleItem.getPaused_at());
//                intent.putExtra("ended_at", singleItem.getEnded_at());
//                intent.putExtra("deleted_at", singleItem.getDeleted_at());
//                intent.putExtra("created_at", singleItem.getCreated_at());
//                intent.putExtra("updated_at", singleItem.getUpdated_at());

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
//        public ImageView image;
        public LinearLayout lyt_parent;
        public ImageView feed_image;
        public TextView feed_title, feed_comment, feed_like;
        public ItemRowHolder(View itemView) {
            super(itemView);
            feed_image =  itemView.findViewById(R.id.feed_image);
            feed_title =  itemView.findViewById(R.id.feed_title);
            feed_like =  itemView.findViewById(R.id.feed_like);
            feed_comment =  itemView.findViewById(R.id.feed_comment);
            lyt_parent = itemView.findViewById(R.id.rootLayout);
        }
    }
}
