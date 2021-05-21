package com.ubuyng.app.ubuyapi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemGallery;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ItemRowHolder> {

    private ArrayList<ItemGallery> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public GalleryAdapter(Context context, ArrayList<ItemGallery> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_single_gallery, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemGallery singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;
        Picasso.get().load(singleItem.getGalleryImg())
//                .resize(150, 160)
                .error(R.drawable.loading_placeholder)
                .fit()
                .into(holder.gallery_img);


        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("gallery_title", singleItem.getGalleryTitle());
                intent.putExtra("gallery_img", singleItem.getGalleryImg());

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
        public ImageView gallery_img;

        public ItemRowHolder(View itemView) {
            super(itemView);
            gallery_img =  itemView.findViewById(R.id.gallery_img);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
