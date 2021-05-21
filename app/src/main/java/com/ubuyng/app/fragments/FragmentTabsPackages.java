package com.ubuyng.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ubuyng.app.R;

public class FragmentTabsPackages extends Fragment {

    public FragmentTabsPackages() {
    }

    public static FragmentTabsPackages newInstance() {
        FragmentTabsPackages fragment = new FragmentTabsPackages();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pro_packages, container, false);




        return root;
    }
}