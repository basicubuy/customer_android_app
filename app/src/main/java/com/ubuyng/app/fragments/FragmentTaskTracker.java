package com.ubuyng.app.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.Models.ItemTracker;
import com.ubuyng.app.ubuyapi.adapters.SuggestSubCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.TimelineAdapter;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class FragmentTaskTracker extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    private TextView task_id;
    ArrayList<ItemTracker> mTracker;
    private RecyclerView rvTimeline;
    private TimelineAdapter mTimelineAdapter;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


//        bundle to get pass ids
        assert getArguments() != null;
        String taskName = getArguments().getString("taskName");
        String task_id = getArguments().getString("task_id");
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_sheet_task_tracker, null);

//        getting the data from server
        mTracker = new ArrayList<>();
        rvTimeline = (RecyclerView) view.findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTimeline.setHasFixedSize(true);


        if (JsonUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            new LoadTimeline().execute(Constant.PROJECT_TIMELINE+task_id);
            Log.i("network_tester", Constant.PROJECT_TIMELINE+task_id);
        } else{
            Intent intent = new Intent(Objects.requireNonNull(getActivity()), InternetActivity.class);
            startActivity(intent);
        }

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        app_bar_layout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);

        // set data to view
        ((TextView) view.findViewById(R.id.name_toolbar)).setText(taskName);
        ((View) view.findViewById(R.id.lyt_spacer)).setMinimumHeight(Tools.getScreenHeight() / 2);

        hideView(app_bar_layout);
        Log.i("frag_task", "fragment details is clicked");

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(app_bar_layout, getActionBarSize());
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(app_bar_layout);
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ((ImageButton) view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog;
    }


    @SuppressLint("StaticFieldLeak")
    private class LoadTimeline extends AsyncTask<String, Void, String> {

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
                        ItemTracker objItem = new ItemTracker();
                        objItem.setId(objJson.getString(Constant.TRACKER_ID));
                        objItem.setBidId(objJson.getString(Constant.TRACKER_BID_ID));
                        objItem.setProjectId(objJson.getString(Constant.TRACKER_PROJ_ID));
                        objItem.setUserId(objJson.getString(Constant.TRACKER_USER_ID));
                        objItem.setProId(objJson.getString(Constant.TRACKER_PRO_ID));
                        objItem.setTrackType(objJson.getString(Constant.TRACKER_TYPE));
                        objItem.setProName(objJson.getString(Constant.TRACKER_PRO));
                        objItem.setMessage(objJson.getString(Constant.TRACKER_MESSAGE));
                        objItem.setCreatedAt(objJson.getString(Constant.TRACKER_DATE));
                        mTracker.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("network_tester", " json loaded");
                displayTimeline();
            }
        }
    }
    private void displayTimeline() {
        rvTimeline.setVisibility(View.VISIBLE);
        mTimelineAdapter = new TimelineAdapter(getActivity(), mTracker);
        rvTimeline.setAdapter(mTimelineAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }

    public void show(FragmentManager supportFragmentManager) {
    }
}
