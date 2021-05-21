package com.ubuyng.app.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ubuyng.app.EditProfileActivity;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemRatings;
import com.ubuyng.app.ubuyapi.Models.ItemServices;
import com.ubuyng.app.ubuyapi.adapters.ProfileReviewsAdapter;
import com.ubuyng.app.ubuyapi.adapters.ProfileServicesAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentTabsAboutPro extends Fragment {

    ArrayList<ItemRatings> mRating;
    ArrayList<ItemServices> mServices;
    ArrayList<ItemServices> ItemBadges;
    public RecyclerView badges_rv, services_rv, reviews_rv;
    ProfileServicesAdapter mServiceAdapter;
    ProfileReviewsAdapter mReviewAdapter;
    String badge_email, badge_phone, badge_id, pro_id, badge_pro_id, about_str;
    ImageView email_badge_img, phone_badge_img, id_badge_img;
    CardView email_badge_card, phone_badge_card, id_badge_card;
    TextView about_txt;



    public FragmentTabsAboutPro() {
    }

    public static FragmentTabsAboutPro newInstance() {
        FragmentTabsAboutPro fragment = new FragmentTabsAboutPro();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_pro, container, false);



//        bundle to get pass ids
        assert getArguments() != null;
         pro_id = getArguments().getString("bidder_id");

        mServices = new ArrayList<>();
        mRating = new ArrayList<>();

         email_badge_img = rootView.findViewById(R.id.email_badge_img);
         email_badge_card = rootView.findViewById(R.id.email_badge_card);
         phone_badge_img = rootView.findViewById(R.id.phone_badge_img);
         phone_badge_card = rootView.findViewById(R.id.phone_badge_card);
         id_badge_img = rootView.findViewById(R.id.id_badge_img);
         id_badge_card = rootView.findViewById(R.id.id_badge_card);
         about_txt = rootView.findViewById(R.id.about_txt);



        services_rv = rootView.findViewById(R.id.services_rv);
        services_rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        services_rv.setHasFixedSize(true);

        reviews_rv = rootView.findViewById(R.id.reviews_rv);
        reviews_rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        reviews_rv.setHasFixedSize(true);

        if (JsonUtils.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
          AboutMeLoader();
        } else{
            Intent intent = new Intent(getActivity() , InternetActivity.class);
            startActivity(intent);

        }
        return rootView;
    }

    private void AboutMeLoader() {
        ProgressDialog pDialog;
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
        Call<String> call = ubuyapi.getAboutProfile(pro_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading ...");
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
                    parseFeedData(jsonresponse);

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

    private void parseFeedData(String jsonresponse) {
        if (null == jsonresponse || jsonresponse.length() == 0) {
//                lyt_not_found.setVisibility(View.VISIBLE);
        } else {

            try {
                JSONObject mainJson = new JSONObject(jsonresponse);
                JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);

//                    badge model
                JSONArray jsonBadge = jsonArray.getJSONArray(Constant.PROFILE_BADGES);
                JSONObject objJsonBadge;
                for (int i = 0; i < jsonBadge.length(); i++) {
                    objJsonBadge = jsonBadge.getJSONObject(i);
                    badge_pro_id = objJsonBadge.getString(Constant.BADGE_PRO_ID);
                    badge_email = objJsonBadge.getString(Constant.BADGE_EMAIL);
                    badge_phone = objJsonBadge.getString(Constant.BADGE_PHONE);
                    badge_id = objJsonBadge.getString(Constant.BADGE_ID);
                }
//                    about model
                JSONArray jsonAbout = jsonArray.getJSONArray(Constant.PROFILE_ABOUT);
                JSONObject objJsonAbout;
                for (int i = 0; i < jsonAbout.length(); i++) {
                    objJsonAbout = jsonAbout.getJSONObject(i);
                    about_str = objJsonAbout.getString(Constant.PRO_ABOUT);

                }

                //                    services model
                JSONArray jsonService = jsonArray.getJSONArray(Constant.PROFILE_SERVICES);
                JSONObject objJsonService;
                for (int i = 0; i < jsonService.length(); i++) {
                    objJsonService = jsonService.getJSONObject(i);
                    ItemServices objItem = new ItemServices();
                    objItem.setServiceId(objJsonService.getString(Constant.SERVICE_ID));
                    objItem.setServiceCategory(objJsonService.getString(Constant.SERVICE_CATEGORY));
                    objItem.setServiceName(objJsonService.getString(Constant.SERVICE_NAME));
                    objItem.setServiceProjects(objJsonService.getString(Constant.SERVICE_PROJECTS));
                    objItem.setServiceImage(objJsonService.getString(Constant.SERVICE_IMAGE));
                    mServices.add(objItem);
                }
//                    rating
                JSONArray jsonRate = jsonArray.getJSONArray(Constant.PROFILE_RATING);
                JSONObject objJsonRate;
                for (int i = 0; i < jsonRate.length(); i++) {
                    objJsonRate = jsonRate.getJSONObject(i);
                    ItemRatings objItem = new ItemRatings();
                    objItem.setRatingId(objJsonRate.getString(Constant.RATING_ID));
                    objItem.setRatings(objJsonRate.getString(Constant.RATINGS));
                    objItem.setRatingComment(objJsonRate.getString(Constant.RATINGS_COMMENT));
                    objItem.setRatingTitle(objJsonRate.getString(Constant.RATINGS_TITLE));
                    objItem.setCusId(objJsonRate.getString(Constant.RATINGS_CUS_ID));
                    objItem.setProjectName(objJsonRate.getString(Constant.RATINGS_PROJECT_NAME));
                    objItem.setRateType(objJsonRate.getString(Constant.RATINGS_TYPE));
                    objItem.setCusName(objJsonRate.getString(Constant.RATINGS_CUS_NAME));
                    objItem.setCusImage(objJsonRate.getString(Constant.RATINGS_IMAGE));
                    objItem.setRateDate(objJsonRate.getString(Constant.RATINGS_DATE));
                    mRating.add(objItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            displayData();
        }
    }



    private void displayData() {
        about_txt.setText(about_str);
//email checker
        if (badge_email != null && !badge_email.equals("")) {
            Picasso.get().load(badge_email)
                    .error(R.drawable.loading_placeholder)
                    .into(email_badge_img);

        }else{
            email_badge_card.setVisibility(View.GONE);
            email_badge_img.setVisibility(View.GONE);
        }
//        phone checker
        if (badge_phone != null && !badge_phone.equals("")) {
            Picasso.get().load(badge_phone)
                    .error(R.drawable.loading_placeholder)
                    .into(phone_badge_img);

        }else{
            phone_badge_card.setVisibility(View.GONE);
            phone_badge_img.setVisibility(View.GONE);
        }
//        id checker
        if (badge_id != null && !badge_id.equals("")) {
            Picasso.get().load(badge_id)
                    .error(R.drawable.loading_placeholder)
                    .into(id_badge_img);

        }else{
            id_badge_card.setVisibility(View.GONE);
            id_badge_img.setVisibility(View.GONE);
        }
        //          SERVICES ADAPTER
        mServiceAdapter = new ProfileServicesAdapter(getActivity(), mServices);
        services_rv.setAdapter(mServiceAdapter);

        //          Rating ADAPTER
        mReviewAdapter = new ProfileReviewsAdapter(getActivity(), mRating);
        reviews_rv.setAdapter(mReviewAdapter);

    }

}