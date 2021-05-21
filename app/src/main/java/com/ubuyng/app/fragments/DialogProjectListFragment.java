package com.ubuyng.app.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.PaymentDebugActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemPortfolio;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.Models.ItemServices;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DialogProjectListFragment extends DialogFragment {

    private View root_view;
    private String  user_id;
    private ShapeOfView continue_sh, cancel_sh;
    ArrayList<ItemProject> mProject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.accept_bid_modal, container, false);

//         real_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//         calendar = Calendar.getInstance();
//        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
//        strTime = mdformat.format(calendar.getTime());

//        bundle to get pass ids
        assert getArguments() != null;
        user_id = getArguments().getString("userID");
        mProject = new ArrayList<>();


        loadProjects();

        continue_sh = root_view.findViewById(R.id.continue_sh);
        cancel_sh = root_view.findViewById(R.id.cancel_sh);

        cancel_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancel_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        continue_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PaymentIntent = new Intent(getActivity(), PaymentDebugActivity.class);
                startActivity(PaymentIntent);
            }
        });

        return root_view;
    }

    private void loadProjects() {
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
        Call<String> call = ubuyapi.getInviteProjects(user_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
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
                Intent intent = new Intent(getActivity(), InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getProjectJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);

//           projects model
            JSONArray JsonProjects = jsonArray.getJSONArray(Constant.CUS_PROJECTS);
            JSONObject objjsonProjects;
            for (int i = 0; i < JsonProjects.length(); i++) {
                objjsonProjects = JsonProjects.getJSONObject(i);
                ItemProject objItem = new ItemProject();
                objItem.setProjectId(objjsonProjects.getString(Constant.PROJECT_ID));
                objItem.setProjectTitle(objjsonProjects.getString(Constant.PROJECT_TITLE));
                mProject.add(objItem);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }
    private void displayData() {
        Objects.requireNonNull(getActivity()).onBackPressed();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}