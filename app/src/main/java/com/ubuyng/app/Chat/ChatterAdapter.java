package com.ubuyng.app.Chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ubuyng.app.R;

import java.util.ArrayList;

/**
 * Created by Wahyu on 06/08/2015.
 */
public class ChatterAdapter extends RecyclerView.Adapter<ChatterAdapter.ItemViewHolder> {
    private static ArrayList<ChatterModel> dataList;
    private LayoutInflater mInflater;
    private Context context;

    public ChatterAdapter(Context ctx, ArrayList<ChatterModel> data) {
        context = ctx;
        dataList = data;
        mInflater = LayoutInflater.from(context);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ShapeOfView my_chat_sh, pro_chat_sh;
        private TextView pro_chat_txt, my_chat_txt;
        public ItemViewHolder(View itemView) {
            super(itemView);

            my_chat_sh = itemView.findViewById(R.id.my_chat_sh);
            pro_chat_sh = itemView.findViewById(R.id.pro_chat_sh);
            pro_chat_txt = itemView.findViewById(R.id.pro_chat_txt);
            my_chat_txt = itemView.findViewById(R.id.my_chat_txt);
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_activity27, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        if(dataList.get(position).getSender().equals("1")) {
            holder.my_chat_sh.setVisibility(View.VISIBLE);
            holder.pro_chat_sh.setVisibility(View.GONE);
            holder.my_chat_txt.setText(dataList.get(position).getMessage());
        }else{
            holder.pro_chat_sh.setVisibility(View.VISIBLE);
            holder.my_chat_sh.setVisibility(View.GONE);
            holder.pro_chat_txt.setText(dataList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
