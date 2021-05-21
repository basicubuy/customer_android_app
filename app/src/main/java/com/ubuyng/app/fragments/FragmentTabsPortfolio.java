package com.ubuyng.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ubuyng.app.R;

public class FragmentTabsPortfolio extends Fragment {

    public FragmentTabsPortfolio() {
    }

    public static FragmentTabsPortfolio newInstance() {
        FragmentTabsPortfolio fragment = new FragmentTabsPortfolio();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pro_portfolio, container, false);




        return root;
    }
}