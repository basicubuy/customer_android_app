package com.ubuyng.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemSlider;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;
import com.ubuyng.app.ubuyapi.adapters.HomeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomeSliderAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomeTopProsAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroActivity extends AppCompatActivity {
    private TextView mTextMessage;
    ArrayList<ItemCat> mCat;
    ArrayList<ItemSlider> mSlider;
    ArrayList<ItemSubcat> mRecommend, mDesign, mPersonal, mBusiness, mHome;
    public RecyclerView recommend_rv, cat_rv, design_rv, personal_rv, business_rv,  home_rv, slider_rv;
    AVLoadingIndicatorView avi;
    HomeTopProsAdapter mRecommendAdapter, mDesignAdapter, mPersonalAdapter, mBusinessAdapter, mHomeAdapter;
    HomeCatAdapter mCatAdapter;
    HomeSliderAdapter mSlideAdapter;
    private LinearLayout lyt_not_found;
    private RelativeLayout overlay_loader;
    private LinearLayout search_btn_main, feed_lyt;
    private TextView loading_text, hello_text, tool_name,  personal_explore, home_explore, web_explore, business_explore;
    private Button btn_retry;
    NestedScrollView scrollview;
    MyApplication App;
    CardView add_task_card, add_task_quick;
    RelativeLayout loading_data;
    private ImageView tool_image;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retro);
        App = MyApplication.getInstance();




        mSlider = new ArrayList<>();
        mCat = new ArrayList<>();
        mRecommend = new ArrayList<>();
        mDesign = new ArrayList<>();
        mPersonal = new ArrayList<>();
        mBusiness = new ArrayList<>();
        mHome = new ArrayList<>();

        lyt_not_found = findViewById(R.id.lyt_not_found);
        loading_data = findViewById(R.id.loading_data);
        avi = findViewById(R.id.avi);
        loading_text = findViewById(R.id.loading_text);

        add_task_card = findViewById(R.id.add_task_card);
        add_task_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetroActivity.this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


//        explore texts
        home_explore = findViewById(R.id.home_explore);
        personal_explore = findViewById(R.id.personal_explore);
        web_explore = findViewById(R.id.web_explore);
        business_explore = findViewById(R.id.business_explore);

        home_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetroActivity.this, SingleCatActivity.class);
                intent.putExtra("Sub_Id", "1");
                intent.putExtra("Sub_Title", "Home");
                startActivity(intent);
            }
        });
        personal_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetroActivity.this, SingleCatActivity.class);
                intent.putExtra("Sub_Id", "15");
                intent.putExtra("Sub_Title", "Personal");
                startActivity(intent);
            }
        });
        web_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetroActivity.this, SingleCatActivity.class);
                intent.putExtra("Sub_Id", "8");
                intent.putExtra("Sub_Title", "Design");
                startActivity(intent);
            }
        });
        business_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetroActivity.this, SingleCatActivity.class);
                intent.putExtra("Sub_Id", "14");
                intent.putExtra("Sub_Title", "Business");
                startActivity(intent);
            }
        });

        add_task_quick = findViewById(R.id.add_task_quick);
        add_task_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetroActivity.this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        hello_text = findViewById(R.id.hello_text);
        hello_text.setText("Hello "+App.getUserFirstName());


        btn_retry = findViewById(R.id.btn_retry);
        scrollview = findViewById(R.id.scrollview);
        feed_lyt = findViewById(R.id.feed_lyt);

        cat_rv = findViewById(R.id.category_rv);
        cat_rv.setLayoutManager(new LinearLayoutManager(RetroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        cat_rv.setHasFixedSize(true);

        slider_rv = findViewById(R.id.slider_rv);
        slider_rv.setLayoutManager(new LinearLayoutManager(RetroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        slider_rv.setHasFixedSize(true);

        home_rv = findViewById(R.id.home_rv);
        home_rv.setLayoutManager(new LinearLayoutManager(RetroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        home_rv.setHasFixedSize(true);

        design_rv = findViewById(R.id.design_rv);
        design_rv.setLayoutManager(new LinearLayoutManager(RetroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        design_rv.setHasFixedSize(true);

        business_rv = findViewById(R.id.business_rv);
        business_rv.setLayoutManager(new LinearLayoutManager(RetroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        business_rv.setHasFixedSize(true);

        recommend_rv = findViewById(R.id.recommend_rv);
        recommend_rv.setLayoutManager(new LinearLayoutManager(RetroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recommend_rv.setHasFixedSize(true);

        personal_rv = findViewById(R.id.personal_rv);
        personal_rv.setLayoutManager(new LinearLayoutManager(RetroActivity.this, LinearLayoutManager.HORIZONTAL, false));
        personal_rv.setHasFixedSize(true);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.getHomeFeeds();
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        startAnim();
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                stopAnim();
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });

    }


    private void startAnim(){
        avi.show();
        loading_data.setVisibility(View.VISIBLE);
        scrollview.setVisibility(View.GONE);
//        lyt_not_found.setVisibility(View.GONE);
    }

    private void stopAnim(){
        avi.hide();
        scrollview.setVisibility(View.VISIBLE);
        loading_data.setVisibility(View.GONE);
    }

}
