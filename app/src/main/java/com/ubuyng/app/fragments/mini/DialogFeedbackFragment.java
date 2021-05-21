package com.ubuyng.app.fragments.mini;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ubuyng.app.PaymentDebugActivity;
import com.ubuyng.app.R;

public class DialogFeedbackFragment extends DialogFragment {

    private View root_view;
    private Integer app_rating;
    private ShapeOfView continue_sh, cancel_sh;
    private ImageView rate1, rate2, rate3, rate4, rate5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.feedback_modal, container, false);

//         real_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//         calendar = Calendar.getInstance();
//        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
//        strTime = mdformat.format(calendar.getTime());

//        bundle to get pass ids
        assert getArguments() != null;

        app_rating = 0;
//        finder
        rate1 = root_view.findViewById(R.id.rate1);
        rate2 = root_view.findViewById(R.id.rate2);
        rate3 = root_view.findViewById(R.id.rate3);
        rate4 = root_view.findViewById(R.id.rate4);
        rate5 = root_view.findViewById(R.id.rate5);

        rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app_rating = 1;
                ratingSwitcher(app_rating);
            }
        });

        rate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app_rating = 2;
                ratingSwitcher(app_rating);
            }
        });

        rate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app_rating = 3;
                ratingSwitcher(app_rating);
            }
        });

        rate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app_rating = 4;
                ratingSwitcher(app_rating);
            }
        });

        rate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app_rating = 5;
                ratingSwitcher(app_rating);
            }
        });

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
                dismiss();
            }
        });

        return root_view;
    }

  private void ratingSwitcher(Integer app_rating){
        switch (app_rating){
            case 0:
                rate1.setImageResource(R.drawable.angry2);
                rate2.setImageResource(R.drawable.sarcastic2);
                rate3.setImageResource(R.drawable.neutra2);
                rate4.setImageResource(R.drawable.smile2);
                rate5.setImageResource(R.drawable.wow2);
                break;
            case 1:
                rate1.setImageResource(R.drawable.angry1);
                rate2.setImageResource(R.drawable.sarcastic2);
                rate3.setImageResource(R.drawable.neutra2);
                rate4.setImageResource(R.drawable.smile2);
                rate5.setImageResource(R.drawable.wow2);
                break;
            case 2:
                rate1.setImageResource(R.drawable.angry2);
                rate2.setImageResource(R.drawable.sarcastic1);
                rate3.setImageResource(R.drawable.neutra2);
                rate4.setImageResource(R.drawable.smile2);
                rate5.setImageResource(R.drawable.wow2);
                break;
            case 3:
                rate1.setImageResource(R.drawable.angry2);
                rate2.setImageResource(R.drawable.sarcastic2);
                rate3.setImageResource(R.drawable.neutral1);
                rate4.setImageResource(R.drawable.smile2);
                rate5.setImageResource(R.drawable.wow2);
                break;
            case 4:
                rate1.setImageResource(R.drawable.angry2);
                rate2.setImageResource(R.drawable.sarcastic2);
                rate3.setImageResource(R.drawable.neutra2);
                rate4.setImageResource(R.drawable.smile1);
                rate5.setImageResource(R.drawable.wow2);
                break;
            case 5:
                rate1.setImageResource(R.drawable.angry2);
                rate2.setImageResource(R.drawable.sarcastic2);
                rate3.setImageResource(R.drawable.neutra2);
                rate4.setImageResource(R.drawable.smile2);
                rate5.setImageResource(R.drawable.wow1);
                break;
        }
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