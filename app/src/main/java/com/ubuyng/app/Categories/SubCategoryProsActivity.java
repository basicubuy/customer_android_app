package com.ubuyng.app.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.PosterActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.fragments.DialogAcceptBidFragment;
import com.ubuyng.app.fragments.DialogDeclineBidFragment;
import com.ubuyng.app.ubuyapi.Interface.AdapBidsInterface;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemPros;
import com.ubuyng.app.ubuyapi.adapters.BiddersAdapter;
import com.ubuyng.app.ubuyapi.adapters.Categories.SubTopProsAdapter;
import com.ubuyng.app.ubuyapi.adapters.Categories.SubTrendingProsAdapter;
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

public class SubCategoryProsActivity extends AppCompatActivity implements AdapBidsInterface{
    SubTopProsAdapter topProsAdapter;
    SubTrendingProsAdapter trendingProsAdapter;
    ArrayList<ItemPros> mTrendingPros, mTopPros;
    RecyclerView top_pros_rv, trending_pros_rv;
    MyApplication App;
    ImageView back_btn, sub_image;
    String strSubId, strSubName, strSubImage;
    TextView top_pros_txt, trending_pros_txt, sub_name_txt;
    RelativeLayout overlay_loader;
    CoordinatorLayout main_lyt;
    FloatingActionButton post_task_fab;
//
    private AdapBidsInterface AdapBidsInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        /*get the bidders array list*/
        mTrendingPros = new ArrayList<>();
        mTopPros = new ArrayList<>();

        /*get the following details from the intent called*/
        Intent intent = getIntent();
        strSubId = intent.getStringExtra("Sub_Id");
        strSubName = intent.getStringExtra("Sub_Title");
        strSubImage = intent.getStringExtra("Sub_image");
   


        /*get the user details*/
        App = MyApplication.getInstance();

        /*finder called here*/
        post_task_fab = findViewById(R.id.post_task_fab);
        post_task_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent postTask = new Intent(SubCategoryProsActivity.this, PosterActivity.class);
                postTask.putExtra("sub_id", strSubId);
                postTask.putExtra("sub_name", strSubName);
                startActivity(postTask);
            }
        });


        sub_image = findViewById(R.id.sub_image);
        Picasso.get().load(strSubImage)
                .resize(180, 150)
                .error(R.drawable.loading_placeholder)
                .into(sub_image);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        top_pros_txt = findViewById(R.id.top_pros_txt);
        top_pros_txt.setText("Top "+strSubName+" Pros");

         sub_name_txt = findViewById(R.id.sub_name_txt);
        sub_name_txt.setText(strSubName);

        trending_pros_txt = findViewById(R.id.trending_pros_txt);
        trending_pros_txt.setText("Trending "+strSubName+" Pros");

        top_pros_rv = findViewById(R.id.top_pros_rv);
        top_pros_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        top_pros_rv.setHasFixedSize(true);

         trending_pros_rv = findViewById(R.id.trending_pros_rv);
        trending_pros_rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,  false));
        trending_pros_rv.setHasFixedSize(true);

        
        /*for loader animation*/
        overlay_loader = findViewById(R.id.overlay_loader);

        /* connect to server and get bids for the project*/
        if (JsonUtils.isNetworkAvailable(this)) {
           loadSubPros();
        } else{
            intent = new Intent(SubCategoryProsActivity.this , InternetActivity.class);
            startActivity(intent);

        }

      

    }

    @Override
    public void onAcceptClick( String bidID, String BidderId){


    }
  @Override
    public void onDeclineClick( String bidId, String Bidderid){

    }

    private void loadSubPros() {
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
        Call<String> call = ubuyapi.getSubPros(strSubId);
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
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                    finding_bids.setVisibility(View.INVISIBLE);
//                    no_bids.setVisibility(View.VISIBLE);
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(SubCategoryProsActivity.this, InternetActivity.class);
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
//           premium_PROS model
            JSONArray jsonPremium = jsonArray.getJSONArray(Constant.PREMIUM_PROS);
            JSONObject objJsonPremium;
            for (int i = 0; i < jsonPremium.length(); i++) {
                objJsonPremium = jsonPremium.getJSONObject(i);
                ItemPros objItem = new ItemPros();
                objItem.setProId(objJsonPremium.getString(Constant.PRO_ID));
                objItem.setPremiumPro(objJsonPremium.getString(Constant.PREMIUM_PRO));
                objItem.setProfileImage(objJsonPremium.getString(Constant.PRO_IMAGE));
                objItem.setProjectCount(objJsonPremium.getString(Constant.TASK_DONE));
                objItem.setProService(objJsonPremium.getString(Constant.PRO_SERVICE));
                objItem.setPremiumPro(objJsonPremium.getString(Constant.PREMIUM_PRO));
                objItem.setProName(objJsonPremium.getString(Constant.PRO_NAME));
                mTopPros.add(objItem);

            }
//           TRENDING_PROS model
            JSONArray jsonTrending = jsonArray.getJSONArray(Constant.TRENDING_PROS);
            JSONObject objjsonTrending;
            for (int i = 0; i < jsonTrending.length(); i++) {
                objjsonTrending = jsonTrending.getJSONObject(i);
                ItemPros objItem = new ItemPros();
                objItem.setProId(objjsonTrending.getString(Constant.PRO_ID));
                objItem.setPremiumPro(objjsonTrending.getString(Constant.PREMIUM_PRO));
                objItem.setProfileImage(objjsonTrending.getString(Constant.PRO_IMAGE));
                objItem.setProjectCount(objjsonTrending.getString(Constant.TASK_DONE));
                objItem.setProService(objjsonTrending.getString(Constant.PRO_SERVICE));
                objItem.setPremiumPro(objjsonTrending.getString(Constant.PREMIUM_PRO));
                objItem.setProName(objjsonTrending.getString(Constant.PRO_NAME));
                mTrendingPros.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }

    private void displayData() {
        topProsAdapter = new SubTopProsAdapter(SubCategoryProsActivity.this, mTopPros);
        top_pros_rv.setAdapter(topProsAdapter);

        trendingProsAdapter = new SubTrendingProsAdapter(SubCategoryProsActivity.this, mTrendingPros);
        trending_pros_rv.setAdapter(trendingProsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    private void startAnim(){
//        overlay_loader.setVisibility(View.VISIBLE);
//        no_projects_found.setVisibility(View.GONE);
    }

    private void stopAnim(){

//        overlay_loader.setVisibility(View.GONE);

    }


}
