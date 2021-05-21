package com.ubuyng.app.fragments.projects;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.CompletedTasksAdapter;
import com.ubuyng.app.ubuyapi.adapters.ExpiredTasksAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.TasksAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

// preloader

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedProjects extends Fragment {


    ArrayList<ItemProject> mProject, mCompleted, mExpired;
    ArrayList<ItemCat> mCat;
    public RecyclerView projects_rv, completed_rv, expired_rv;
    AVLoadingIndicatorView avi;
    TasksAdapter mProjectAdapter;
    CompletedTasksAdapter mCompletedAdapter;
    ExpiredTasksAdapter mExpiredAdapter;
    HomeCatAdapter mCatAdapter;
    private LinearLayout lyt_not_found, no_projects_found;
    private RelativeLayout overlay_loader;
    private TextView loading_text, tool_name;
    private Button btn_retry;
    NestedScrollView scrollview;
    MyApplication App;
    Toolbar main_toolbar;
    private FloatingActionButton floatingActionButton;
    private ImageView tool_image;
    String user_id;

    public CompletedProjects() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootProject = inflater.inflate(R.layout.fragment_pending_tasks, container, false);

        return rootProject;

    }


  private void  loadProjects(){
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
              .addInterceptor(logging)
              .build();

      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
              .addConverterFactory(ScalarsConverterFactory.create())
              .build();

      UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
      Call<String> call = ubuyapi.getProjects(user_id);
      Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
      startAnim();
      call.enqueue(new Callback<String>() {


          @Override
          public void onResponse(Call<String> call, Response<String> response) {
              stopAnim();
              HttpUrl main_url = response.raw().request().url();
              Log.i("debugger", String.valueOf(main_url));
              if (response.body() != null) {
                  Log.i("onSuccess", response.body().toString());

                  String jsonresponse = response.body().toString();
                  getProjectJson(jsonresponse);

              } else {
                  Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
              }
              if (response.isSuccessful()){
                  Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

              }


          }

          @Override
          public void onFailure(Call<String> call, Throwable t) {
              Intent intent = new Intent(getActivity() , InternetActivity.class);
              startActivity(intent);
              Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
          }
      });


  }


private void getProjectJson(String jsonresponse){
    stopAnim();
    try {
        JSONObject mainJson = new JSONObject(jsonresponse);
        JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);
//                    recent tasks model
        JSONArray jsonTasks = jsonArray.getJSONArray(Constant.PROJECT_PENDING);
        JSONObject objJsonTasks;
        for (int i = 0; i < jsonTasks.length(); i++) {
            objJsonTasks = jsonTasks.getJSONObject(i);
            ItemProject objItem = new ItemProject();
            objItem.setProjectId(objJsonTasks.getString(Constant.PROJECT_ID));
            objItem.setSubCatName(objJsonTasks.getString(Constant.PROJECT_NAME));
            objItem.setSubCatId(objJsonTasks.getString(Constant.PROJECT_SUB_ID));
            objItem.setProjectDate(objJsonTasks.getString(Constant.PROJECT_DATE));
            objItem.setProjectMsg(objJsonTasks.getString(Constant.PROJECT_MESSAGE));
            objItem.setUserId(objJsonTasks.getString(Constant.PROJECT_USER_ID));
            objItem.setProjectBidCount(objJsonTasks.getString(Constant.PROJECT_BID_COUNT));
            objItem.setProjectBidStatus(objJsonTasks.getString(Constant.PROJECT_BID_STATUS));
            objItem.setProjectProgress(objJsonTasks.getString(Constant.PROJECT_PROGRESS));
            mProject.add(objItem);
        }
//                    completed tasks model
        JSONArray jsonCompleted = jsonArray.getJSONArray(Constant.PROJECT_COMPLETED);
        JSONObject objJsonCompleted;
        for (int i = 0; i < jsonCompleted.length(); i++) {
            objJsonCompleted = jsonCompleted.getJSONObject(i);
            ItemProject objItem = new ItemProject();
            objItem.setProjectId(objJsonCompleted.getString(Constant.PROJECT_ID));
            objItem.setSubCatName(objJsonCompleted.getString(Constant.PROJECT_NAME));
            objItem.setSubCatId(objJsonCompleted.getString(Constant.PROJECT_SUB_ID));
            objItem.setProjectMsg(objJsonCompleted.getString(Constant.PROJECT_MESSAGE));
            objItem.setProjectDate(objJsonCompleted.getString(Constant.PROJECT_DATE));
            objItem.setUserId(objJsonCompleted.getString(Constant.PROJECT_USER_ID));
            objItem.setProjectBidCount(objJsonCompleted.getString(Constant.PROJECT_BID_COUNT));
            objItem.setProjectBidStatus(objJsonCompleted.getString(Constant.PROJECT_BID_STATUS));
            objItem.setProjectProgress(objJsonCompleted.getString(Constant.PROJECT_PROGRESS));
            mCompleted.add(objItem);
        }
        // expired tasks model
        JSONArray jsonExpired = jsonArray.getJSONArray(Constant.PROJECT_EXPIRED);
        JSONObject objJsonExpired;
        for (int i = 0; i < jsonExpired.length(); i++) {
            objJsonExpired = jsonExpired.getJSONObject(i);
            ItemProject objItem = new ItemProject();
            objItem.setProjectId(objJsonExpired.getString(Constant.PROJECT_ID));
            objItem.setSubCatName(objJsonExpired.getString(Constant.PROJECT_NAME));
            objItem.setSubCatId(objJsonExpired.getString(Constant.PROJECT_SUB_ID));
            objItem.setProjectDate(objJsonExpired.getString(Constant.PROJECT_DATE));
//                        objItem.setProjectAdd(objJsonExpired.getString(Constant.PROJECT_ADDRESS));
            objItem.setProjectMsg(objJsonExpired.getString(Constant.PROJECT_MESSAGE));
            objItem.setUserId(objJsonExpired.getString(Constant.PROJECT_USER_ID));
            objItem.setProjectBidCount(objJsonExpired.getString(Constant.PROJECT_BID_COUNT));
            objItem.setProjectBidStatus(objJsonExpired.getString(Constant.PROJECT_BID_STATUS));
            objItem.setProjectProgress(objJsonExpired.getString(Constant.PROJECT_PROGRESS));
            mExpired.add(objItem);
        }


    } catch (JSONException e) {
        e.printStackTrace();
    }
    displayData();
}
    private void displayData() {

//          tasks Adapter ADAPTER
        mProjectAdapter = new TasksAdapter(getActivity(), mProject);
        projects_rv.setAdapter(mProjectAdapter);
//          completed Adapter ADAPTER
        mCompletedAdapter = new CompletedTasksAdapter(getActivity(), mCompleted);
        completed_rv.setAdapter(mCompletedAdapter);
//          expired Adapter ADAPTER
        mExpiredAdapter = new ExpiredTasksAdapter(getActivity(), mExpired);
        expired_rv.setAdapter(mExpiredAdapter);

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



}

