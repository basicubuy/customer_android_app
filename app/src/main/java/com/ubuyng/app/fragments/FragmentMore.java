package com.ubuyng.app.fragments;

/**
 * This is a demo project for EveryFarm,farmer
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Common.Common;
import com.ubuyng.app.ubuyapi.Models.ItemCheckSelect;
import com.ubuyng.app.ubuyapi.Models.ItemChoices;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// preloader

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMore extends Fragment {


    public FragmentMore() {
        // Required empty public constructor
    }
//
//    public static FragmentMore newInstance(int val) {
//        FragmentMore fragment = new FragmentMore();
//        Bundle args = new Bundle();
//        args.putInt("someInt", val);
//        fragment.setArguments(args);
//        return fragment;
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        return rootView;

    }




//    @Override
//    public void onResume() {
//        super.onResume();
//        mShimmerViewContainer.startShimmer();
//    }
//
//    @Override
//    public void onPause() {
//        mShimmerViewContainer.stopShimmer();
//        super.onPause();
//    }
}

