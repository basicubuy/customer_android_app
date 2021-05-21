package com.ubuyng.app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ubuyng.app.HelperClasses.BottomSheetListenersRoot;
import com.ubuyng.app.mini.ProfileActivity;
import com.ubuyng.app.ProjectActivity;
import com.ubuyng.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSixBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListenersRoot bottomSheetListeners;
    private RelativeLayout rlHolder, rlProject, rlProfile, rlShare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_six_bottom_sheet, container, false);


        //GET THE NAME[OF ITEM CLICKED] FROM THE RecyclerViewGridAdapter.java at Line 68. TO SHOW IN TOP OF BOTTOM SHEET
        final String name = this.getArguments().getString("name");
        TextView head1 = view.findViewById(R.id.head1);
        head1.setText(name);

        rlHolder = view.findViewById(R.id.rlHolder);
        rlProject = view.findViewById(R.id.rlProject);
        rlProfile = view.findViewById(R.id.rlProfile);
        rlShare = view.findViewById(R.id.rlShare);

        onClickListeners();

        rlHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListeners.onClicked(name);
                dismiss();
            }
        });
        return view;
    }

    // IT DEFINES WHAT SHOULD HAPPEN WHEN "ITEM INSIDE BOTTOM SHEET DIALOG" IS CLICKED.
    private void onClickListeners() {
        rlProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProjectActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismiss();
            }
        });

        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismiss();
            }
        });

        rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListeners.onClicked("Copy clicked");
                dismiss();
            }
        });

    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            bottomSheetListeners = (BottomSheetListenersRoot) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement bottom sheet listener");
//        }
//    }

}
