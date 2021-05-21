package com.ubuyng.app.fragments;

/**
 * This is a demo project for EveryFarm,farmer
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ubuyng.app.R;

// preloader

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMessage extends Fragment {

  TextView email, privacy_policy, terms;

    public FragmentMessage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pending_tasks, container, false);

//        email = rootView.findViewById(R.id.email);


        return rootView;

    }

    private void fetchData() {

    }

}

