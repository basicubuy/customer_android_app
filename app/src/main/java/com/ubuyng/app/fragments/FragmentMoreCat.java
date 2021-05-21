package com.ubuyng.app.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.HomeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.SuggestSubCatAdapter;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMoreCat extends Fragment {


    public FragmentMoreCat() {
        // Required empty public constructor
    }
    ArrayList<ItemCat> mCategory;
    private RecyclerView rvSuggest;
    HomeCatAdapter mCatAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View MoreView = inflater.inflate(R.layout.fragment_search_subcat, container, false);

        mCategory = new ArrayList<>();

        rvSuggest = (RecyclerView) MoreView.findViewById(R.id.rvSuggest);
        rvSuggest.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvSuggest.setHasFixedSize(true);
        mShimmerViewContainer = MoreView.findViewById(R.id.shimmer_view_container);


        if (JsonUtils.isNetworkAvailable(getActivity())) {
            new LoadSearch().execute(Constant.SEARCH_CAT);
        } else{
            Intent intent = new Intent(getActivity() , InternetActivity.class);
            startActivity(intent);

        }
        return MoreView;
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
                        ItemCat objItem = new ItemCat();
                        objItem.setCatId(objJson.getString(Constant.CAT_ID));
                        objItem.setCatTitle(objJson.getString(Constant.CAT_NAME));
                        objItem.setCatPic(objJson.getString(Constant.CAT_PIC));
                        mCategory.add(objItem);
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
        mCatAdapter = new HomeCatAdapter(getActivity(), mCategory);
        rvSuggest.setAdapter(mCatAdapter);
    }

}
