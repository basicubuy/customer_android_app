package com.ubuyng.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ubuyng.app.R;

public class FragmentTabsFaq extends Fragment {

    public FragmentTabsFaq() {
    }

    public static FragmentTabsFaq newInstance() {
        FragmentTabsFaq fragment = new FragmentTabsFaq();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pro_faq, container, false);




        return root;
    }
}