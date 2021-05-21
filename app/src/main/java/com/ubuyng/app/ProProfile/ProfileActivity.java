/*
 * Copyright (c) 2020. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.ProProfile;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.fragments.DialogAcceptArtisanFragment;
import com.ubuyng.app.fragments.DialogAcceptBidFragment;
import com.ubuyng.app.fragments.DialogDeclineBidFragment;
import com.ubuyng.app.ubuyapi.Models.ItemPortfolio;
import com.ubuyng.app.ubuyapi.Models.ItemRatings;
import com.ubuyng.app.ubuyapi.Models.ItemServices;
import com.ubuyng.app.ubuyapi.adapters.PortfolioAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.ServicesAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;
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

public class ProfileActivity extends AppCompatActivity {
    ImageView pro_image, pro_user_verified_badge;
    ArrayList<ItemServices> mServices;
    ArrayList<ItemRatings> mRating;
    ArrayList<ItemPortfolio> mPortfolio;
    LinearLayout show_data, bid_freelance, bid_artisan;
    TextView proname_toolbar_txt, pro_username, about_professional, fr_amount_txt, service_amount_txt, material_amount_txt, ar_amount_txt,task_num, pro_rating_text, pro_address, bid_des_txt, job_done_txt, rating_rate_txt, joined_date_txt, number_verify_txt, id_verify_txt, email_verify_txt;
    MyApplication App;
    AVLoadingIndicatorView avi;
    RelativeLayout loading_data;
    ServicesAdapter mServiceAdap;
    PortfolioAdapter mPortfolioAdap;
    CircularImageView pro_user_image;
    ShapeOfView no_services, no_portfolio;
    RecyclerView services_rv, reviews_rv, hours_rv, port_rv;
    Button invite_to_job;
    FloatingActionButton chat_pro_fab;
    Toolbar main_toolbar;
    String  pro_id, pro_name, about_pro, premium_pro, profile_img,  str_email_badge, str_task_done, str_pro_rating, str_profile_date, str_number_badge, str_id_badge, str_user_verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pro_profile);

        Intent bid_intent = getIntent();
        pro_id = bid_intent.getStringExtra("pro_id");
        pro_name = bid_intent.getStringExtra("pro_name");
        premium_pro = bid_intent.getStringExtra("premium_pro");
        profile_img = bid_intent.getStringExtra("profile_img");
        str_task_done = bid_intent.getStringExtra("task_done");
        about_pro = bid_intent.getStringExtra("about_pro");

        App = MyApplication.getInstance();

        mServices = new ArrayList<>();
        mRating = new ArrayList<>();
        mPortfolio = new ArrayList<>();

        loading_data = findViewById(R.id.loading_data);
        show_data = findViewById(R.id.show_data);
        avi = findViewById(R.id.avi);

        proname_toolbar_txt = findViewById(R.id.proname_toolbar_txt);
        proname_toolbar_txt.setText(pro_name);

        pro_username = findViewById(R.id.pro_username);
        pro_username.setText(pro_name);

        joined_date_txt = findViewById(R.id.joined_date_txt);
        rating_rate_txt = findViewById(R.id.rating_rate_txt);
        job_done_txt = findViewById(R.id.job_done_txt);
        number_verify_txt = findViewById(R.id.number_verify_txt);
        email_verify_txt = findViewById(R.id.email_verify_txt);
        id_verify_txt = findViewById(R.id.id_verify_txt);
        pro_user_verified_badge = findViewById(R.id.pro_user_verified_badge);
        invite_to_job = findViewById(R.id.invite_to_job);

        invite_to_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pro_user_image = findViewById(R.id.pro_user_image);
        Picasso.get().load(profile_img).fit().into(pro_user_image);

        /*breaker*/
        services_rv = findViewById(R.id.services_rv);
        services_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        services_rv.setHasFixedSize(true);

        reviews_rv = findViewById(R.id.reviews_rv);
        reviews_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        reviews_rv.setHasFixedSize(true);

        hours_rv = findViewById(R.id.hours_rv);
        hours_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        hours_rv.setHasFixedSize(true);

        port_rv = findViewById(R.id.port_rv);
        port_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        port_rv.setHasFixedSize(true);

//      shape of view finders
        no_services = findViewById(R.id.no_services);
        no_portfolio = findViewById(R.id.no_portfolio);

        if (JsonUtils.isNetworkAvailable(this)) {
            loadBidProfile();
        } else {
            Intent intent = new Intent(this , InternetActivity.class);
            startActivity(intent);
        }
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.textColorDark), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarLight(this);
    }

    private void loadBidProfile() {
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
        Call<String> call = ubuyapi.getSingleProProfile(pro_id, App.getUserId());
        startAnim();
        Log.i("onEmptyResponse", "test network");
        //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                stopAnim();
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getBidsJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");

//                    Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                    finding_bids.setVisibility(View.INVISIBLE);
//                    no_bids.setVisibility(View.VISIBLE);
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(ProfileActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");
                //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBidsJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);

            JSONArray jsonProfile = jsonArray.getJSONArray(Constant.PROFILE_HEADER);
            JSONObject objJson;
            for (int i = 0; i < jsonProfile.length(); i++) {
                objJson = jsonProfile.getJSONObject(i);
                str_task_done = objJson.getString(Constant.TASK_DONE);
                str_email_badge = objJson.getString(Constant.BADGE_EMAIL);
                str_id_badge = objJson.getString(Constant.BADGE_ID);
                str_number_badge = objJson.getString(Constant.BADGE_PHONE);
                str_user_verified = objJson.getString(Constant.USER_VERIFIED);
                about_pro = objJson.getString(Constant.ABOUT_PRO);
//              str_pro_rating = objJson.getString(Constant.RA);
                str_profile_date = objJson.getString(Constant.PRO_JOINED);
            }


//          services model
            JSONArray JsonServices = jsonArray.getJSONArray(Constant.SERVICE_HEADER);
            JSONObject objjsonServices;
            for (int i = 0; i < JsonServices.length(); i++) {
                objjsonServices = JsonServices.getJSONObject(i);
                ItemServices objItem = new ItemServices();
                objItem.setServiceId(objjsonServices.getString(Constant.SERVICE_ID));
                objItem.setServiceImage(objjsonServices.getString(Constant.SERVICE_IMAGE));
                objItem.setServiceName(objjsonServices.getString(Constant.SERVICE_NAME));
                mServices.add(objItem);
            }

//          services model
            JSONArray JsonPortfolio = jsonArray.getJSONArray(Constant.PORTFOLIO_HEADER);
            JSONObject objJsonPortfolio;
            for (int i = 0; i < JsonPortfolio.length(); i++) {
                objJsonPortfolio = JsonPortfolio.getJSONObject(i);
                ItemPortfolio objItem = new ItemPortfolio();
                objItem.setPortId(objJsonPortfolio.getString(Constant.PORTFOLIO_ID));
                objItem.setPortImg(objJsonPortfolio.getString(Constant.PORTFOLIO_file));
                objItem.setPortTitle(objJsonPortfolio.getString(Constant.PORTFOLIO_title));
                objItem.setPortComments(objJsonPortfolio.getString(Constant.PORTFOLIO_comments));
                objItem.setPortLikes(objJsonPortfolio.getString(Constant.PORTFOLIO_likes));
                mPortfolio.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }

    private void displayData() {
        if (mServices.isEmpty()){
            no_services.setVisibility(View.VISIBLE);
        }
        if (mPortfolio.isEmpty()){
            no_portfolio.setVisibility(View.VISIBLE);
        }
        mServiceAdap = new ServicesAdapter(this, mServices);
        services_rv.setAdapter(mServiceAdap);

        mPortfolioAdap = new PortfolioAdapter(this, mPortfolio);
        port_rv.setAdapter(mPortfolioAdap);

        if (str_user_verified.equals("1")){
            pro_user_verified_badge.setVisibility(View.VISIBLE);
        }else{
            pro_user_verified_badge.setVisibility(View.GONE);
        }
//        now we set the data's from the
        if (str_email_badge.equals("1")){
            email_verify_txt.setTextColor(getResources().getColor(R.color.textColorDark));
        }else{
            email_verify_txt.setTextColor(getResources().getColor(R.color.progress_bg));
            email_verify_txt.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.progress_bg));
        }

        if (str_id_badge.equals("1")){
            id_verify_txt.setTextColor(getResources().getColor(R.color.textColorDark));
        }else{
            id_verify_txt.setTextColor(getResources().getColor(R.color.progress_bg));
            id_verify_txt.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.progress_bg));
        }

        if (str_number_badge.equals("1")){
            number_verify_txt.setTextColor(getResources().getColor(R.color.textColorDark));
        }else{
            number_verify_txt.setTextColor(getResources().getColor(R.color.progress_bg));
            number_verify_txt.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.progress_bg));

        }
        job_done_txt.setText(str_task_done);

        joined_date_txt.setText(str_profile_date);

    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void startAnim() {
        avi.show();
        loading_data.setVisibility(View.VISIBLE);
        show_data.setVisibility(View.GONE);
    }

    private void stopAnim() {
        avi.hide();
        loading_data.setVisibility(View.GONE);
        show_data.setVisibility(View.VISIBLE);
    }

    private void showDialogProjectList() {
        Bundle bundle = new Bundle();
        bundle.putString("userID", App.getUserId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogAcceptBidFragment newFragment = new DialogAcceptBidFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

}
