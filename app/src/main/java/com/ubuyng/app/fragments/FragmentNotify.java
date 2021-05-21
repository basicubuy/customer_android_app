package com.ubuyng.app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemNotify;
import com.ubuyng.app.ubuyapi.adapters.NotifiesAdapter;
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
public class FragmentNotify extends Fragment {


    ArrayList<ItemNotify> mNotify;
    public RecyclerView notify_rv;
    AVLoadingIndicatorView avi;
    NotifiesAdapter mNotifyAdapter;
    private LinearLayout lyt_not_found, no_notify_found;
    private RelativeLayout overlay_loader;
    private TextView loading_text, tool_name;
    private Button btn_retry;
    NestedScrollView scrollview;
    MyApplication App;
    Toolbar main_toolbar;
    String user_id;

    public FragmentNotify() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootProject = inflater.inflate(R.layout.fragment_notify, container, false);

        mNotify = new ArrayList<>();
        App = MyApplication.getInstance();
        user_id = App.getUserId();//        projects ITEMS

        Log.i("DEBUG_USER_ID", "user id is"+ App.getUserId());

        lyt_not_found = rootProject.findViewById(R.id.lyt_not_found);
        no_notify_found = rootProject.findViewById(R.id.no_notify_found);
        overlay_loader = rootProject.findViewById(R.id.overlay_loader);
        avi = rootProject.findViewById(R.id.avi);
        loading_text = rootProject.findViewById(R.id.loading_text);

        btn_retry = rootProject.findViewById(R.id.btn_retry);
        scrollview = rootProject.findViewById(R.id.scrollview);

//        projects ITEMS 
        notify_rv = rootProject.findViewById(R.id.notify_rv);
        notify_rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        notify_rv.setHasFixedSize(true);


        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JsonUtils.isNetworkAvailable(getActivity())) {
                    loadNotify();
                } else{
                    Intent intent = new Intent(getActivity() , InternetActivity.class);
                    startActivity(intent);
                }
            }
        });

        // create a function for the first load
        if (JsonUtils.isNetworkAvailable(getActivity())) {
            loadNotify();
        } else{
            Intent intent = new Intent(getActivity() , InternetActivity.class);
            startActivity(intent);
        }


        return rootProject;

    }

    private void loadNotify() {
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
        Call<String> call = ubuyapi.getNotify(user_id);
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
                    getNotifyJson(jsonresponse);

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

    private void getNotifyJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                ItemNotify objItem = new ItemNotify();
                objItem.setProjectId(objJson.getString(Constant.PROJECT_ID));
                objItem.setBidId(objJson.getString(Constant.BID_ID));
                objItem.setNotifyMsg(objJson.getString(Constant.NOTIFY_MSG));
                objItem.setNotifyType(objJson.getString(Constant.NOTIFY_TYPE));
                objItem.setNotifyUrl(objJson.getString(Constant.NOTIFY_URL));
                objItem.setNotifyDate(objJson.getString(Constant.PROJECT_DATE));
                mNotify.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();

    }


    private void displayData() {

//          Fact Adapter ADAPTER
        mNotifyAdapter = new NotifiesAdapter(getActivity(), mNotify);
        notify_rv.setAdapter(mNotifyAdapter);

    }

    private void startAnim(){
//        avi.show();
        avi.smoothToShow();
        overlay_loader.setVisibility(View.VISIBLE);
        lyt_not_found.setVisibility(View.GONE);
        no_notify_found.setVisibility(View.GONE);
    }

    private void stopAnim(){
//        avi.hide();
        avi.smoothToHide();
        overlay_loader.setVisibility(View.GONE);

    }



}

