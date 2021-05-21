package com.ubuyng.app.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.Chat.ChatterActivity;
import com.ubuyng.app.ProjectBidInProgressActivity;
import com.ubuyng.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DialogPaymentSuccessFragment extends DialogFragment {

    private View root_view;
    private String taskName, project_id, pro_name, pro_amount, pro_image, bid_id, real_date, strTime;
    private ShapeOfView continue_sh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.payment_successful_modal, container, false);

//        bundle to get pass ids
        assert getArguments() != null;
        taskName = getArguments().getString("taskName");
        project_id = getArguments().getString("task_id");
        pro_name = getArguments().getString("bidder_name");
        pro_amount = getArguments().getString("bidder_amount");
        pro_image = getArguments().getString("bidder_image");
        bid_id = getArguments().getString("bid_id");
        continue_sh = root_view.findViewById(R.id.continue_sh);

        continue_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bid_intent = new Intent(getContext(), ProjectBidInProgressActivity.class);
                bid_intent.putExtra("project_id", project_id);
                bid_intent.putExtra("bid_id", bid_id);
                bid_intent.putExtra("bidder_name", pro_name);
                bid_intent.putExtra("bidder_amount", pro_amount);
                bid_intent.putExtra("bidder_image", pro_image);
                bid_intent.putExtra("project_title", taskName);
                startActivity(bid_intent);
            }
        });

        return root_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}