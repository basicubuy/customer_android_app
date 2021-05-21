package com.ubuyng.app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.Chat.ChatterActivity;
import com.ubuyng.app.fragments.FragmentTabsAboutPro;
import com.ubuyng.app.fragments.FragmentTabsFaq;
import com.ubuyng.app.fragments.FragmentTabsPackages;
import com.ubuyng.app.fragments.FragmentTabsPortfolio;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BidProfileActivity extends AppCompatActivity {
    String project_id, bidder_name, bidder_image, project_title,bid_id, bidder_id, bidder_amount, bid_message, bid_status, pro_id, cus_id, pro_city, task_done, pro_rating, bid_date;
    ImageView pro_image;
    LinearLayout profile_lyt;
    CardView profile_to_chat, share_profile;
    Button hire_pro;
    TextView pro_name, bid_amount, task_num, pro_rating_text, pro_address, bid_message_text;
    ProgressDialog progressDialog;
    private View parent_view;
    AVLoadingIndicatorView avi, avi_rec;
    TabLayout tab_layout;ViewPager view_pager;
    RelativeLayout overlay_loader;
    MyApplication App;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_profile);

        Intent bid_intent = getIntent();
        project_id = bid_intent.getStringExtra("project_id");
        bid_id = bid_intent.getStringExtra("bid_id");
        bidder_id = bid_intent.getStringExtra("bidder_id");
        bidder_name = bid_intent.getStringExtra("bidder_name");
        bidder_amount = bid_intent.getStringExtra("bidder_amount");
        bidder_image = bid_intent.getStringExtra("bidder_image");
        project_title = bid_intent.getStringExtra("project_title");
        App = MyApplication.getInstance();
//        profile_lyt = findViewById(R.id.profile_lyt);
        pro_image = findViewById(R.id.pro_image);
        task_num = findViewById(R.id.task_num);
        pro_name = findViewById(R.id.pro_name);
        pro_address = findViewById(R.id.pro_address);
        overlay_loader = findViewById(R.id.overlay_loader);
        avi = findViewById(R.id.avi);
        profile_to_chat = findViewById(R.id.profile_to_chat);

        profile_to_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat_intent = new Intent(BidProfileActivity.this , ChatterActivity.class);
                chat_intent.putExtra("project_id", project_id);
                chat_intent.putExtra("bid_id", bid_id);
                chat_intent.putExtra("bidder_id", bidder_id);
                chat_intent.putExtra("bidder_name", bidder_name);
                chat_intent.putExtra("bidder_amount", bidder_amount);
                chat_intent.putExtra("bidder_image", bidder_image);
                chat_intent.putExtra("project_title", project_title);
                startActivity(chat_intent);
            }
        });
        share_profile   = findViewById(R.id.share_profile);
        share_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareProfile();
            }
        });
        hire_pro   = findViewById(R.id.hire_pro);
        hire_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accept_intent = new Intent(BidProfileActivity.this , PaymentActivity.class);
                accept_intent.putExtra("project_id", project_id);
                accept_intent.putExtra("bid_id", bid_id);
                accept_intent.putExtra("bidder_id", bidder_id);
                accept_intent.putExtra("bidder_name", bidder_name);
                accept_intent.putExtra("bidder_amount", bidder_amount);
                accept_intent.putExtra("bidder_image", bidder_image);
                accept_intent.putExtra("project_title", project_title);
                startActivity(accept_intent);
            }
        });

//        profile tabs

        view_pager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);


        if (JsonUtils.isNetworkAvailable(this)) {
            loadProProfile();
        } else {
            Intent intent = new Intent(this , InternetActivity.class);
            startActivity(intent);
        }
    }

    private void ShareProfile() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = "\n" + getResources().getString(R.string.share_pro_msg) + bidder_name +" profile on Ubuy Nigeria using "+ "\n"
                  + "http:/ubuy.ng/";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
        }

    }

    private void loadProProfile() {
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
        Call<String> call = ubuyapi.getProProfile(bidder_id);
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
                    getProfileJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(BidProfileActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getProfileJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                bidder_name = objJson.getString(Constant.PRO_NAME);
                bidder_image = objJson.getString(Constant.PRO_IMAGE);
                task_done = objJson.getString(Constant.TASK_DONE);
                pro_city = objJson.getString(Constant.USER_PHONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    display();
    }



    private void display() {
        pro_name.setText(bidder_name);
        task_num.setText(task_done);
        Picasso.get().load(bidder_image)
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(pro_image);

        pro_address.setText(pro_city);
//        bid_message_text.setText(bid_message);
    }

    private void showDeclineConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.decline_bid);
        builder.setMessage(R.string.confirm_bid_decline);
        builder.setPositiveButton(R.string.AGREE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Snackbar.make(parent_view, "Bid Declined", Snackbar.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        builder.setNegativeButton(R.string.CANCEL, null);
        builder.show();
    }

    private void loadingBidDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setTitle("Loading Bid");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                progressDialog.dismiss();
//            }
//        }).start();
    }
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FragmentTabsAboutPro.newInstance(), "About Me");
//        adapter.addFragment(FragmentTabsPortfolio.newInstance(), "Portfolio");
//        adapter.addFragment(FragmentTabsPackages.newInstance(), "Reviews");
//        adapter.addFragment(FragmentTabsFaq.newInstance(), "FAQ");
        viewPager.setAdapter(adapter);
        Log.i("viewpager_checker", "setupViewPager: checker");
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            Bundle bundle = new Bundle();
            bundle.putString("bidder_id", bidder_id);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            fragment.setArguments(bundle);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void startAnim(){
//        avi.show();
        avi.smoothToShow();
        overlay_loader.setVisibility(View.VISIBLE);
//        lyt_not_found.setVisibility(View.GONE);
    }

    private void stopAnim(){
//        avi.hide();
        avi.smoothToHide();
        overlay_loader.setVisibility(View.GONE);

    }

}
