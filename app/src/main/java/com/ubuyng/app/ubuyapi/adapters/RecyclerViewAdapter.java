package com.ubuyng.app.ubuyapi.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.RecyclerViewModel;
import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<RecyclerViewModel> modelArrayList;
    private FragmentManager fragmentManager; // THIS IS SUPPORT FRAGMENT MANAGER.

    public RecyclerViewAdapter(ArrayList<RecyclerViewModel> modelArrayList, FragmentManager fragmentManager) {
        this.modelArrayList = modelArrayList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_search_subcats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.circleImageView.setImageResource(modelArrayList.get(position).getImage());
        holder.name.setText(modelArrayList.get(position).getName());
        holder.des.setText(modelArrayList.get(position).getLocation());

        final String msg = modelArrayList.get(position).getName();

        // IT DEFINES WHAT SHOULD HAPPEN WHEN "ITEM IN RECYCLER VIEW" IS CLICKED.
        // IN THIS CASE, IT OPENS A BOTTOM SHEET FRAGMENT, AS YOU CAN SEE INSIDE BELOW METHOD
        // FOR MORE INFO ON WHAT THAT THAT METHOD DOES, JUST HOLD[CTRL] AND CLICK [LEFT] FOR WINDOW USERS.

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    private void openBottomSheetDialog(String message) {
        // @QUESTION -> WHY USE OF "BUNDLE"?
        // @ANSWER -> SUPPOSE YOU WANT TO PASS "SOME DATA" FROM RECYCLER VIEW AND PUT THAT ONTO
        // BOTTOM SHEET DIALOG FRAGMENT, THIS WILL HELP US TO SET THE TEXT OR IMAGE OR
        // ANY OTHER DATA THAT WE WANT TO DISPLAY.

        Bundle bundle = new Bundle();
        bundle.putString("message", message);

    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private CircularImageView circleImageView;
        private TextView name, des;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.cat_icon);
            name = itemView.findViewById(R.id.sub_catname);
            des = itemView.findViewById(R.id.sub_catdes);
        }


    }
}