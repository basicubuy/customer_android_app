package com.ubuyng.app.fragments.projects;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.projects.InProgressTasksAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.PendingTasksAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.V3InProgressTasksAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.V3PendingTasksAdapter;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

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
public class InProgressProjects extends Fragment {

    ArrayList<ItemProject> mProjectV3;
    ArrayList<ItemBids> mBids;
    public RecyclerView pending_rv3 ;
    V3InProgressTasksAdapter mProjectAdapterV3;
    private LinearLayout lyt_not_found;
    private RelativeLayout overlay_loader, no_projects_found;
    NestedScrollView scrollview;
    MyApplication App;
    String user_id;
    private DatabaseHelper databaseHelper;


    public InProgressProjects() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootProject = inflater.inflate(R.layout.fragment_inprogress_tasks, container, false);

        mProjectV3 = new ArrayList<>();
        mBids = new ArrayList<>();
        App = MyApplication.getInstance();
        databaseHelper = new DatabaseHelper(getActivity());
        Log.i("DEBUG_USER_ID", "user id is"+ App.getUserId());

        lyt_not_found = rootProject.findViewById(R.id.lyt_not_found);
        no_projects_found = rootProject.findViewById(R.id.no_projects_found);
        overlay_loader = rootProject.findViewById(R.id.overlay_loader);

        scrollview = rootProject.findViewById(R.id.scrollview);

        user_id = App.getUserId();//        projects ITEMS
        pending_rv3 = rootProject.findViewById(R.id.pending_rv3);
        pending_rv3.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        pending_rv3.setHasFixedSize(true);


//     TODO::   btn_retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (JsonUtils.isNetworkAvailable(getActivity())) {
//                    loadProjects();
//                } else{
//                    Intent intent = new Intent(getActivity() , InternetActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });


        if (JsonUtils.isNetworkAvailable(getActivity())) {
            loadProjects();
        } else{
            Intent intent = new Intent(getActivity() , InternetActivity.class);
            startActivity(intent);
        }
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
      Call<String> call = ubuyapi.getInProjects(user_id);
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
                  Log.i("onEmptyResponse", "Returned empty ");
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
//                    v3 pending tasks model
        JSONArray jsonPendingV3 = jsonArray.getJSONArray(Constant.V3_PROJECT_INPROGRESS);
        JSONObject objJsonTasksV3;
        for (int i = 0; i < jsonPendingV3.length(); i++) {
            objJsonTasksV3 = jsonPendingV3.getJSONObject(i);
            ItemProject objItem = new ItemProject();
            objItem.setProjectId(objJsonTasksV3.getString(Constant.PROJECT_ID));
            objItem.setSubCatName(objJsonTasksV3.getString(Constant.PROJECT_TITLE));
            objItem.setSubCatId(objJsonTasksV3.getString(Constant.PROJECT_SUB_ID));
            objItem.setProjectStartDate(objJsonTasksV3.getString(Constant.PROJECT_STARTED_DATE));
            objItem.setProjectEndDate(objJsonTasksV3.getString(Constant.PROJECT_DEADLINE_DATE));
            objItem.setProjectMsg(objJsonTasksV3.getString(Constant.PROJECT_MESSAGE));
            objItem.setUserId(objJsonTasksV3.getString(Constant.PROJECT_USER_ID));
            objItem.setProjectBidStatus(objJsonTasksV3.getString(Constant.TASK_STATUS));
            objItem.setProjectBudget(objJsonTasksV3.getString(Constant.PROJECT_AMOUNT));
            objItem.setProName(objJsonTasksV3.getString(Constant.PROJECT_PRO_NAME));
            objItem.setBidId(objJsonTasksV3.getString(Constant.BID_ID));
            objItem.setSelectedPro(objJsonTasksV3.getString(Constant.PROJECT_PRO_IMAGE));
            mProjectV3.add(objItem);
        }


    } catch (JSONException e) {
        e.printStackTrace();
    }
    displayData();
}
    private void displayData() {

//          PENDING TASK V3 ADAPTER
        mProjectAdapterV3 = new V3InProgressTasksAdapter(getActivity(), mProjectV3);
        pending_rv3.setAdapter(mProjectAdapterV3);
    }

    private void startAnim(){
        overlay_loader.setVisibility(View.VISIBLE);
        no_projects_found.setVisibility(View.GONE);
    }

    private void stopAnim(){

        overlay_loader.setVisibility(View.GONE);

    }



}

