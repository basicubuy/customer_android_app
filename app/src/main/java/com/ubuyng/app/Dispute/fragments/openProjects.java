package com.ubuyng.app.Dispute.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemDispute;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.disputes.DisputesAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.PendingTasksAdapter;
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
public class openProjects extends Fragment {


    ArrayList<ItemDispute> mDisputes;
    public RecyclerView open_rv;
    DisputesAdapter mOpenDispute;
    private LinearLayout lyt_not_found;
    private RelativeLayout overlay_loader, no_disputes_found;
    NestedScrollView scrollview;
    MyApplication App;
    String user_id;
    private DatabaseHelper databaseHelper;


    public openProjects() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootProject = inflater.inflate(R.layout.fragment_open_disputes, container, false);

        mDisputes = new ArrayList<>();
        App = MyApplication.getInstance();
        databaseHelper = new DatabaseHelper(getActivity());

        overlay_loader = rootProject.findViewById(R.id.overlay_loader);

        scrollview = rootProject.findViewById(R.id.scrollview);

        user_id = App.getUserId();//        projects ITEMS
        open_rv = rootProject.findViewById(R.id.open_rv);
        open_rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        open_rv.setHasFixedSize(true);





        if (JsonUtils.isNetworkAvailable(getActivity())) {
            loadDisputes();
        } else{
            Intent intent = new Intent(getActivity() , InternetActivity.class);
            startActivity(intent);
        }
        return rootProject;

    }


  private void  loadDisputes(){
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
      Call<String> call = ubuyapi.getDisputes(user_id);
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
//                    getting disputes model
        JSONArray jsonDisputes = jsonArray.getJSONArray(Constant.DISPUTE_OPEN);
        JSONObject objJsonDisputes;
        for (int i = 0; i < jsonDisputes.length(); i++) {
            objJsonDisputes = jsonDisputes.getJSONObject(i);
            ItemDispute objItem = new ItemDispute();
            objItem.setDisputeId(objJsonDisputes.getString(Constant.DISPUTE_ID));
            objItem.setDisputeCat(objJsonDisputes.getString(Constant.DISPUTE_CAT));
            objItem.setTaskName(objJsonDisputes.getString(Constant.DISPUTE_DES));
            objItem.setTaskRef(objJsonDisputes.getString(Constant.DISPUTE_REF));
            objItem.setDisputeDate(objJsonDisputes.getString(Constant.DISPUTE_DATE));
            objItem.setDisputeStatus(objJsonDisputes.getString(Constant.DISPUTE_STATUS));
            mDisputes.add(objItem);
        }

    } catch (JSONException e) {
        e.printStackTrace();
    }
    displayData();
}
    private void displayData() {

//          PENDING TASK V3 ADAPTER
        mOpenDispute = new DisputesAdapter(getActivity(), mDisputes);
        open_rv.setAdapter(mOpenDispute);

    }

    private void startAnim(){
        overlay_loader.setVisibility(View.VISIBLE);
    }

    private void stopAnim(){

        overlay_loader.setVisibility(View.GONE);

    }



}

