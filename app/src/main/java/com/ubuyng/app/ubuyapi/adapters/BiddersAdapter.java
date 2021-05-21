package com.ubuyng.app.ubuyapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.Chat.ChatterActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ProProfile.SingleBidActivity;
import com.ubuyng.app.ubuyapi.Interface.AdapBidsInterface;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import java.util.ArrayList;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class BiddersAdapter extends RecyclerView.Adapter<BiddersAdapter.ItemRowHolder> {

    private ArrayList<ItemBids> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private AdapBidsInterface AdapBidsInterface;

    public BiddersAdapter(Context context, ArrayList<ItemBids> dataList, AdapBidsInterface AdapBidsInterface) {
        this.dataList = dataList;
        this.mContext = context;
        this.AdapBidsInterface = AdapBidsInterface;
        databaseHelper = new DatabaseHelper(mContext);

    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_single_bid, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemBids singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.proName.setText(singleItem.getBidderName());
        holder.proMessage.setText(singleItem.getBidMessage());
//      FIXME  holder.proDate.setText(singleItem.getBidDate());
//      FIXME  holder.proBid.setText('₦'+singleItem.getBidAmount());
        holder.proBid.setText('₦'+singleItem.getBidAmount());
        //holder.ratingView.setRating(Float.parseFloat(singleItem.getRateAvg()));
        Picasso.get().load(singleItem.getBidderImage())
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(holder.bidder_image);

//


        holder.bid_to_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bid_intent = new Intent(mContext, ChatterActivity.class);
                bid_intent.putExtra("project_id", singleItem.getProjectId());
                bid_intent.putExtra("bid_id", singleItem.getBidId());
                bid_intent.putExtra("bidder_id", singleItem.getBidderId());
                bid_intent.putExtra("bidder_name", singleItem.getBidderName());
                bid_intent.putExtra("bidder_amount", singleItem.getBidAmount());
                bid_intent.putExtra("bidder_image", singleItem.getBidderImage());
                bid_intent.putExtra("project_title", singleItem.getProjectTitle());
                bid_intent.putExtra("bidder_message", singleItem.getBidMessage());
                mContext.startActivity(bid_intent);
            }
        });
        holder.bidder_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bid_intent = new Intent(mContext, SingleBidActivity.class);
                bid_intent.putExtra("project_id", singleItem.getProjectId());
                bid_intent.putExtra("bid_id", singleItem.getBidId());
                bid_intent.putExtra("bidder_id", singleItem.getBidderId());
                bid_intent.putExtra("bidder_name", singleItem.getBidderName());
                bid_intent.putExtra("bidder_amount", singleItem.getBidAmount());
                bid_intent.putExtra("bidder_image", singleItem.getBidderImage());
                bid_intent.putExtra("project_title", singleItem.getProjectTitle());
                bid_intent.putExtra("bidder_message", singleItem.getBidMessage());
                bid_intent.putExtra("bid_version", singleItem.getBidVersion());
                bid_intent.putExtra("bid_type", singleItem.getBidType());
                bid_intent.putExtra("bid_material_fee", singleItem.getBidMaterialFee());
                bid_intent.putExtra("bid_Service_fee", singleItem.getBidServiceFee());
                mContext.startActivity(bid_intent);
            }
        });
        holder.proName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bid_intent = new Intent(mContext, SingleBidActivity.class);
                bid_intent.putExtra("project_id", singleItem.getProjectId());
                bid_intent.putExtra("bid_id", singleItem.getBidId());
                bid_intent.putExtra("bidder_id", singleItem.getBidderId());
                bid_intent.putExtra("bidder_name", singleItem.getBidderName());
                bid_intent.putExtra("bidder_amount", singleItem.getBidAmount());
                bid_intent.putExtra("bidder_image", singleItem.getBidderImage());
                bid_intent.putExtra("project_title", singleItem.getProjectTitle());
                bid_intent.putExtra("bid_version", singleItem.getBidVersion());
                mContext.startActivity(bid_intent);
            }
        });

        holder.accept_bid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdapBidsInterface.onAcceptClick(singleItem.getBidId(), singleItem.getBidderId());
            }
        });
        holder.decline_bid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdapBidsInterface.onDeclineClick(singleItem.getBidId(), singleItem.getBidderId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public CircularImageView bidder_image, bidder_online_status;
        public RelativeLayout bid_to_chat;
        public ShapeOfView accept_bid_btn, decline_bid_btn, bid_cta_btn;
        public TextView proName, proBid, proMessage, proDate, pro_skill, pro_State;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            bidder_image = itemView.findViewById(R.id.bidder_image);
            proName =  itemView.findViewById(R.id.pro_name);
            proBid =  itemView.findViewById(R.id.bid_amount_txt);
            proMessage =  itemView.findViewById(R.id.bid_message);
//            TODO:: ADD THE BID DATE proDate =  itemView.findViewById(R.id.bid_date);
            pro_skill =  itemView.findViewById(R.id.pro_skill);
            pro_State =  itemView.findViewById(R.id.pro_State);
            bid_to_chat =  itemView.findViewById(R.id.bid_to_chat);
            accept_bid_btn =  itemView.findViewById(R.id.accept_bid_btn);
            bidder_online_status =  itemView.findViewById(R.id.bidder_online_status);
            decline_bid_btn =  itemView.findViewById(R.id.decline_bid_btn);
            bid_cta_btn =  itemView.findViewById(R.id.bid_cta_btn);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
