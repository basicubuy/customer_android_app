package com.ubuyng.app.fragments;

/**
 * This is a demo project for EveryFarm,farmer
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.BecomeProActivity;
import com.ubuyng.app.EditProfileActivity;
import com.ubuyng.app.FeedbackActivity;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.UpayActivity;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class FragmentProfile extends Fragment {

  TextView pro_name, edit_profile;
  CircularImageView profile_image;
  LinearLayout coupons, invite, upay, become_pro, help, feedback;
    MyApplication App;
    String user_id, user_full_name, user_image, first_name, last_name, number, email;

    public FragmentProfile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.i("PROFILE", "LOADER");

        App = MyApplication.getInstance();
        user_id = App.getUserId();
        user_full_name = App.getUserFirstName()+' '+App.getUserLastName();
        pro_name = rootView.findViewById(R.id.pro_name);
        profile_image = rootView.findViewById(R.id.profile_image);

        edit_profile = rootView.findViewById(R.id.edit_profile);
//        todo:: edit profile
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_intent = new Intent(getActivity() , EditProfileActivity.class);
                edit_intent.putExtra("user_id", user_id);
                edit_intent.putExtra("first_name", first_name);
                edit_intent.putExtra("last_name", last_name);
                edit_intent.putExtra("number", number);
                edit_intent.putExtra("email", email);
                startActivity(edit_intent);
            }
        });
//        coupons = rootView.findViewById(R.id.coupons);
////        todo:: coupons
//        coupons.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), CouponActivity.class);
//                startActivity(intent);
//            }
//        });

//

        invite = rootView.findViewById(R.id.invite);
        //        todo:: invite
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUbuy();
            }
        });

        upay = rootView.findViewById(R.id.upay);
        //        todo:: upay
        upay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpayActivity.class);
                startActivity(intent);
            }
        });

        become_pro = rootView.findViewById(R.id.become_pro);
        //        todo:: become_pro
        become_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BecomeProActivity.class);
                startActivity(intent);
            }
        });

//        help = rootView.findViewById(R.id.help);
//        //        todo:: help
//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), HelpActivity.class);
//                startActivity(intent);
//            }
//        });

        feedback = rootView.findViewById(R.id.feedback);
        //        todo:: feedback
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
            }
        });


        if (JsonUtils.isNetworkAvailable(getActivity())) {
            loadingProfile();
        } else{
//            TODO: DEVELOP INTERNET ERROR ACTIVITY
            Log.i("NETWORK_TESTER", "NO INTERNET DETECTED");
        }
        return rootView;

    }

    private void loadingProfile() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        ProgressDialog pDialog;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.getMyProfile(user_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                pDialog.dismiss();
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    parseStatusResponse(jsonresponse);

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



    private void parseStatusResponse(String jsonresponse) {
        if (null == jsonresponse || jsonresponse.length() == 0) {
            showToast(getString(R.string.nodata));
        } else {
            try {
                JSONObject mainJson = new JSONObject(jsonresponse);
                JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                JSONObject objJson;
                for (int i = 0; i < jsonArray.length(); i++) {
                    objJson = jsonArray.getJSONObject(i);
                    user_id = objJson.getString(Constant.USER_ID);
                    first_name = objJson.getString(Constant.FIRST_NAME);
                    last_name = objJson.getString(Constant.LAST_NAME);
                    number = objJson.getString(Constant.USER_PHONE);
                    email = objJson.getString(Constant.USER_EMAIL);
                    user_image = objJson.getString(Constant.USER_PHOTO);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        updateDisplay();

    }

    private void updateDisplay() {
        pro_name.setText(App.getUserFirstName()+' '+App.getUserLastName());
        Picasso.get().load(user_image)
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(profile_image);
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
}
//    share ubuy
    private void shareUbuy(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = "\n" + getResources().getString(R.string.share_msg) +  "\n" + getResources().getString(R.string.download_msg)
                    + "\n" + getString(R.string.app_name) + " "  + "http://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
        }

    }
    }


