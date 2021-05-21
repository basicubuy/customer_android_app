package com.ubuyng.app.ubuyapi.adapters.projects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

//note: this code is a dependants for this app and all right strictly belongs to The author (Andrew Ben Richard)
public class PendingTasksAdapter extends RecyclerView.Adapter<PendingTasksAdapter.ItemRowHolder> {

    private ArrayList<ItemProject> dataList;
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private String project_date, project_id;
    ArrayList<ItemBids> mBids;
    public RecyclerView pending_rv;
    public Integer bid_counter, deduct_avater_view;

    public PendingTasksAdapter(Context context, ArrayList<ItemProject> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        databaseHelper = new DatabaseHelper(mContext);


    }

    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_receiving_project, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ItemProject singleItem = dataList.get(position);
//        final ItemRowHolder holder = ItemRowHolder;

        holder.projectName.setText(singleItem.getSubCatName());
        holder.bid_count_text.setText(singleItem.getProjectBidCount()+" Bids Recieved");

        bid_counter = Integer.valueOf(singleItem.getProjectBidCount());
//        here we check if the bid count is greater 0 and hide bid avatars images
        if (bid_counter == 0){
            holder.bidder_avater_showcase.setVisibility(View.GONE);
        }else{
//            now if the data is greater than 0 we would now display bid images
//            now we check if bidder 1 has image else hide the view
            if (singleItem.getBidderImage1() == "null"){
                holder.bidder_image_1.setVisibility(View.GONE);
            }else{
                Picasso.get().load(singleItem.getBidderImage1())
                        .fit()
                        .error(R.drawable.loading_placeholder)
                        .into(holder.bidder_image_1);
            }

            //            now we check if bidder 2 has image else hide the view
            if (singleItem.getBidderImage2()== "null"){
                holder.bidder_image_2.setVisibility(View.GONE);
            }else{
                Picasso.get().load(singleItem.getBidderImage2())
                        .fit()
                        .error(R.drawable.loading_placeholder)
                        .into(holder.bidder_image_2);
            }

            //            now we check if bidder 3 has image else hide the view
            if (singleItem.getBidderImage3() == "null"){
                holder.bidder_image_3.setVisibility(View.GONE);
            }else{
                Picasso.get().load(singleItem.getBidderImage3())
                        .fit()
                        .error(R.drawable.loading_placeholder)
                        .into(holder.bidder_image_3);
            }


//            now we check if the bid count is lesser than 4

            if (bid_counter >= 4){
                holder.bid_count_holder.setVisibility(View.VISIBLE);
                deduct_avater_view = bid_counter - 3;
                holder.left_bid_counts.setText("+"+deduct_avater_view);
            }
        }

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ProjectBidActivity.class);
                intent.putExtra("project_id", singleItem.getProjectId());
                intent.putExtra("project_title", singleItem.getSubCatName());
                intent.putExtra("project_brief", singleItem.getProjectMsg());
                intent.putExtra("project_version", singleItem.getProjectVersion());
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
        public TextView projectName, project_budget, bid_count_text, left_bid_counts;
        public LinearLayout lyt_parent;
        RelativeLayout bidder_avater_showcase;
        public ShapeOfView bid_count_holder;
        public CircularImageView bidder_image_1, bidder_image_2, bidder_image_3;

        public ItemRowHolder(View itemView) {
            super(itemView);
            projectName =  itemView.findViewById(R.id.project_name);
            bid_count_text =  itemView.findViewById(R.id.bid_count_text);
            left_bid_counts =  itemView.findViewById(R.id.left_bid_counts);
            bid_count_holder =  itemView.findViewById(R.id.bid_count_holder);
            bidder_image_1 =  itemView.findViewById(R.id.bidder_image_1);
            bidder_image_2 =  itemView.findViewById(R.id.bidder_image_2);
            bidder_image_3 =  itemView.findViewById(R.id.bidder_image_3);
            bidder_avater_showcase =  itemView.findViewById(R.id.bidder_avater_showcase);

            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }



}
