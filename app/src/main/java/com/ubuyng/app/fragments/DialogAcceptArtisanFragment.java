package com.ubuyng.app.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ubuyng.app.PaymentDebugActivity;
import com.ubuyng.app.R;

public class DialogAcceptArtisanFragment extends DialogFragment {

    private View root_view;
    private String bid_id, bidder_id;
    private ShapeOfView continue_sh, cancel_sh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.accept_bid_artisan_modal, container, false);

//         real_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//         calendar = Calendar.getInstance();
//        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
//        strTime = mdformat.format(calendar.getTime());

//        bundle to get pass ids
        assert getArguments() != null;
        bidder_id = getArguments().getString("bidder_id");
        bid_id = getArguments().getString("bid_id");



        continue_sh = root_view.findViewById(R.id.continue_sh);
        cancel_sh = root_view.findViewById(R.id.cancel_sh);

        cancel_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancel_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        continue_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PaymentIntent = new Intent(getActivity(), PaymentDebugActivity.class);
                startActivity(PaymentIntent);
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