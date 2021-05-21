/*
 * Copyright (c) 2020. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.ProProfile;

import android.content.Intent;
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

public class SingleBidActivity extends AppCompatActivity {
    String project_id, bidder_name, project_title, bidder_image, bid_id, bidder_id, bidder_amount, bidder_message, bid_type, bid_material_fee,  bid_service_fee, bid_status, pro_id, cus_id, str_email_badge, str_task_done, str_pro_rating, str_profile_date, str_number_badge, str_id_badge, str_user_verified;
    ImageView pro_image, pro_user_verified_badge;
    ArrayList<ItemServices> mServices;
    ArrayList<ItemRatings> mRating;
    ArrayList<ItemPortfolio> mPortfolio;
    LinearLayout show_data, bid_freelance, bid_artisan;
    TextView proname_toolbar_txt, pro_username, fr_amount_txt, service_amount_txt, material_amount_txt, ar_amount_txt,task_num, pro_rating_text, pro_address, bid_des_txt, job_done_txt, rating_rate_txt, joined_date_txt, number_verify_txt, id_verify_txt, email_verify_txt;
    MyApplication App;
    AVLoadingIndicatorView avi;
    RelativeLayout loading_data;
    ServicesAdapter mServiceAdap;
    PortfolioAdapter mPortfolioAdap;
    CircularImageView pro_user_image;
    ShapeOfView no_services, no_portfolio;
    RecyclerView services_rv, reviews_rv, hours_rv, port_rv;
    Button accept_bid_btn, decline_bid_btn;
    FloatingActionButton chat_pro_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bid);

        Intent bid_intent = getIntent();
        project_id = bid_intent.getStringExtra("project_id");
        bid_id = bid_intent.getStringExtra("bid_id");
        bidder_id = bid_intent.getStringExtra("bidder_id");
        bidder_name = bid_intent.getStringExtra("bidder_name");
        bidder_amount = bid_intent.getStringExtra("bidder_amount");
        bidder_message = bid_intent.getStringExtra("bidder_message");
        bidder_image = bid_intent.getStringExtra("bidder_image");
        project_title = bid_intent.getStringExtra("project_title");
        bid_type = bid_intent.getStringExtra("bid_type");
        bid_material_fee = bid_intent.getStringExtra("bid_material_fee");
        bid_service_fee = bid_intent.getStringExtra("bid_Service_fee");
        App = MyApplication.getInstance();


        mServices = new ArrayList<>();
        mRating = new ArrayList<>();
        mPortfolio = new ArrayList<>();

        loading_data = findViewById(R.id.loading_data);
        show_data = findViewById(R.id.show_data);
        avi = findViewById(R.id.avi);
        proname_toolbar_txt = findViewById(R.id.proname_toolbar_txt);
        proname_toolbar_txt.setText(bidder_name);

        pro_username = findViewById(R.id.pro_username);
        pro_username.setText(bidder_name);

        joined_date_txt = findViewById(R.id.joined_date_txt);
        rating_rate_txt = findViewById(R.id.rating_rate_txt);
        job_done_txt = findViewById(R.id.job_done_txt);
        number_verify_txt = findViewById(R.id.number_verify_txt);
        email_verify_txt = findViewById(R.id.email_verify_txt);
        id_verify_txt = findViewById(R.id.id_verify_txt);
        pro_user_verified_badge = findViewById(R.id.pro_user_verified_badge);

        accept_bid_btn = findViewById(R.id.accept_bid_btn);
        decline_bid_btn = findViewById(R.id.decline_bid_btn);
        chat_pro_fab = findViewById(R.id.chat_pro_fab);

        accept_bid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProjectAccept();

            }
        });

        if (bid_type.equals("1")){
            accept_bid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogProjectAccept();
                }
            });
        }else{
            accept_bid_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogArtisanSuccess();
                }
            });
        }
        decline_bid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProjectDecline();

            }
        });

        pro_user_image = findViewById(R.id.pro_user_image);
        Picasso.get().load(bidder_image).fit().into(pro_user_image);

        bid_des_txt = findViewById(R.id.bid_des_txt);
        bid_des_txt.setText(bidder_message);
        fr_amount_txt = findViewById(R.id.fr_amount_txt);


        bid_artisan = findViewById(R.id.bid_artisan);
        bid_freelance = findViewById(R.id.bid_freelance);

        if (bid_type != null){

            if (bid_type.equals("1")){
                bid_artisan.setVisibility(View.GONE);
                bid_freelance.setVisibility(View.VISIBLE);
                fr_amount_txt.setText(bidder_amount);

            }else if(bid_type.equals("2")){
                bid_freelance.setVisibility(View.GONE);
                bid_artisan.setVisibility(View.VISIBLE);
                ar_amount_txt.setText(bidder_amount);
                material_amount_txt.setText(bidder_amount);
                service_amount_txt.setText(bidder_amount);
            }
        }else{
            bid_artisan.setVisibility(View.GONE);
            bid_freelance.setVisibility(View.VISIBLE);
            fr_amount_txt.setText(bidder_amount);
        }
        bid_artisan = findViewById(R.id.bid_artisan);
        bid_artisan = findViewById(R.id.bid_artisan);


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
        port_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        port_rv.setHasFixedSize(true);


//        shape of view finders
        no_services = findViewById(R.id.no_services);
        no_portfolio = findViewById(R.id.no_portfolio);

        if (JsonUtils.isNetworkAvailable(this)) {
                      loadBidProfile();
        } else {
            Intent intent = new Intent(this , InternetActivity.class);
            startActivity(intent);
        }
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
        Call<String> call = ubuyapi.getBidProfile(bid_id, String.valueOf(1));
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

                    //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                    finding_bids.setVisibility(View.INVISIBLE);
//                    no_bids.setVisibility(View.VISIBLE);
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(SingleBidActivity.this, InternetActivity.class);
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
//                str_pro_rating = objJson.getString(Constant.RA);
                str_profile_date = objJson.getString(Constant.PRO_JOINED);
            }


//           services model
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

//            services model
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


    private void showDialogProjectAccept() {
        Bundle bundle = new Bundle();
//        bundle.putString("taskName", project_title);
//        bundle.putString("task_id", project_id);
//        bundle.putString("bid_id", bid_id);
//        bundle.putString("bidder_name", bidder_name);
//        bundle.putString("bidder_amount", bidder_amount);
//        bundle.putString("bidder_image", bidder_image);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogAcceptBidFragment newFragment = new DialogAcceptBidFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    private void showDialogProjectDecline() {
        Bundle bundle = new Bundle();
//        bundle.putString("taskName", project_title);
//        bundle.putString("task_id", project_id);
//        bundle.putString("bid_id", bid_id);
//        bundle.putString("bidder_name", bidder_name);
//        bundle.putString("bidder_amount", bidder_amount);
//        bundle.putString("bidder_image", bidder_image);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogDeclineBidFragment newFragment = new DialogDeclineBidFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }
    private void showDialogArtisanSuccess() {
        Bundle bundle = new Bundle();
//        bundle.putString("taskName", project_title);
//        bundle.putString("task_id", project_id);
//        bundle.putString("bid_id", bid_id);
//        bundle.putString("bidder_name", bidder_name);
//        bundle.putString("bidder_amount", bidder_amount);
//        bundle.putString("bidder_image", bidder_image);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogAcceptArtisanFragment newFragment = new DialogAcceptArtisanFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

}
