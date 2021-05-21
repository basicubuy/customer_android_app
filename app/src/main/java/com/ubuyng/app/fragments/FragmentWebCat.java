package com.ubuyng.app.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.SuggestSubCatAdapter;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWebCat extends Fragment {


    public FragmentWebCat() {
        // Required empty public constructor
    }
    ArrayList<ItemProject> mSubcategory;
    private RecyclerView rvSuggest;
    private SuggestSubCatAdapter mSubSuggestAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View webView = inflater.inflate(R.layout.fragment_search_subcat, container, false);

        mSubcategory = new ArrayList<>();

        rvSuggest = (RecyclerView) webView.findViewById(R.id.rvSuggest);
        rvSuggest.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSuggest.setHasFixedSize(true);
        mShimmerViewContainer = webView.findViewById(R.id.shimmer_view_container);


        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new LoadSearch().execute(Constant.SEARCH_SUGGEST+"8");
        } else{
            Intent intent = new Intent(getActivity() , InternetActivity.class);
            startActivity(intent);

        }
        return webView;

    }
    @SuppressLint("StaticFieldLeak")
    private class LoadSearch extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null == result || result.length() == 0) {
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemProject objItem = new ItemProject();
                        objItem.setSubCatId(objJson.getString(Constant.SUB_ID));
                        objItem.setCatId(objJson.getString(Constant.SUB_CAT_ID));
                        objItem.setSubCatName(objJson.getString(Constant.SUB_TITLE));
                        objItem.setSubCatDes(objJson.getString(Constant.SUB_DESCRIPTION));
                        objItem.setSubCatIcon(objJson.getString(Constant.SUB_PIC));
                        mSubcategory.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displaySuggest();
            }
        }
    }
    private void displaySuggest() {
        rvSuggest.setVisibility(View.VISIBLE);
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
        mSubSuggestAdapter = new SuggestSubCatAdapter(getActivity(), mSubcategory);
        rvSuggest.setAdapter(mSubSuggestAdapter);
    }
}
