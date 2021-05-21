package com.ubuyng.app;
/*
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.HomeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.TasksAdapter;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity  {
    ArrayList<ItemProject> mProject;
    ArrayList<ItemCat> mCat;
    public RecyclerView projects_rv, cat_rv;
    AVLoadingIndicatorView avi, aviCat;
    TasksAdapter mProjectAdapter;
    HomeCatAdapter mCatAdapter;
    private LinearLayout lyt_not_found, no_projects_found;
    private RelativeLayout overlay_loader;
    private TextView loading_text;
    private Button btn_retry;
    NestedScrollView scrollview;
    MyApplication App;
    Toolbar main_toolbar;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);


//        Toast.makeText(this, "TAP ON IMAGES AND MENU ICONS", Toast.LENGTH_SHORT).show();



        App = MyApplication.getInstance();

        Log.i("DEBUG_USER_ID", "user id is"+ App.getUserId());

        no_projects_found = findViewById(R.id.no_projects_found);





    }


    @SuppressLint("StaticFieldLeak")
    private class LoadProjects extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            stopAnim();
            if (null == result || result.length() == 0) {
                no_projects_found.setVisibility(View.VISIBLE);
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemProject objItem = new ItemProject();
                        objItem.setProjectId(objJson.getString(Constant.PROJECT_ID));
                        objItem.setSubCatName(objJson.getString(Constant.PROJECT_NAME));
                        objItem.setSubCatImage(objJson.getString(Constant.PROJECT_CAT_IMAGE));
                        objItem.setSubCatId(objJson.getString(Constant.PROJECT_SUB_ID));
                        objItem.setProjectAdd(objJson.getString(Constant.PROJECT_ADDRESS));
                        objItem.setProjectDate(objJson.getString(Constant.PROJECT_DATE));
                        objItem.setProjectMsg(objJson.getString(Constant.PROJECT_MESSAGE));
                        objItem.setUserId(objJson.getString(Constant.PROJECT_USER_ID));
                        mProject.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }

    private void displayData() {

//          Fact Adapter ADAPTER
        mProjectAdapter = new TasksAdapter(InboxActivity.this, mProject);
        projects_rv.setAdapter(mProjectAdapter);

    }

    private void startAnim(){
//        avi.show();
        avi.smoothToShow();
        overlay_loader.setVisibility(View.VISIBLE);
        lyt_not_found.setVisibility(View.GONE);
        no_projects_found.setVisibility(View.GONE);
    }

    private void stopAnim(){
//        avi.hide();
        avi.smoothToHide();
        overlay_loader.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}
