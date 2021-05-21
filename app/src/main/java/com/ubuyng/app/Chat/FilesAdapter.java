package com.ubuyng.app.Chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ItemRowHolder> {

    private ArrayList<FilesModel> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;


    public FilesAdapter(Context context, ArrayList<FilesModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_files, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final FilesModel singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.file_name.setText(singleItem.getFile_name());
        holder.file_type.setText(singleItem.getFile_type());
        holder.file_date.setText(singleItem.getFile_date());

        if (singleItem.getIs_image().equals("1")){
            Picasso.get().load(singleItem.getFile_url())
                    .resize(150, 150)
                    .error(R.drawable.loading_placeholder)
                    .into(holder.file_image);
            holder.not_image.setVisibility(View.GONE);
        }

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(singleItem.getFile_url()));
                mContext.startActivity(openUrlIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView file_image;
        public CardView not_image;
        public TextView file_type, file_name, file_date;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            file_image = itemView.findViewById(R.id.file_image);
            not_image =  itemView.findViewById(R.id.not_image);
            file_type =  itemView.findViewById(R.id.file_type);
            file_name =  itemView.findViewById(R.id.file_name);
            file_date =  itemView.findViewById(R.id.file_date);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
