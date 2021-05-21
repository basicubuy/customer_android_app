package com.ubuyng.app.Categories;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.PosterActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.SingleCatActivity;
import com.ubuyng.app.ubuyapi.Dependants.SpacingItemDecoration;
import com.ubuyng.app.ubuyapi.Dependants.ViewAnimation;
import com.ubuyng.app.ubuyapi.Interface.AdapBidsInterface;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.Models.ItemPros;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;
import com.ubuyng.app.ubuyapi.adapters.CatAdapter;
import com.ubuyng.app.ubuyapi.adapters.Categories.SubTopProsAdapter;
import com.ubuyng.app.ubuyapi.adapters.Categories.SubTrendingProsAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomeTopProsAdapter;
import com.ubuyng.app.ubuyapi.adapters.SearchSubCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.SuggestSubCatAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;

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

public class AllCategoryActivity extends AppCompatActivity{
    private EditText et_search;
    HomeTopProsAdapter topProsAdapter;
    private ImageButton bt_clear;
    CatAdapter allCatAdapter;
    ArrayList<ItemPros> mTopPros;
    ArrayList<ItemCat> mCats;
    ArrayList<ItemSubcat> mSubcategory;
    RecyclerView top_pros_rv, recyclerSuggestion, rvSuggest, cats_rv;
    MyApplication App;
    private LinearLayout lyt_suggestion;
    ImageView back_btn, sub_image;
    String search_text, strSubId, strSubName, strSubImage;
    TextView top_pros_txt, trending_pros_txt, sub_name_txt;
    RelativeLayout overlay_loader;
    CoordinatorLayout main_lyt;
    FloatingActionButton post_task_fab;
    private SearchSubCatAdapter mSubCatAdapter;
//    private SuggestSubCatAdapter mSubSuggestAdapter;
    //    private Timer timer = new Timer();
    private boolean loadingData;
    private final long DELAY = 2000; // in ms
    private AdapBidsInterface AdapBidsInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        /*get the bidders array list*/
        mCats = new ArrayList<>();
        mSubcategory = new ArrayList<>();
        mTopPros = new ArrayList<>();

        /*get the following details from the intent called*/
        Intent intent = getIntent();
        strSubId = intent.getStringExtra("Sub_Id");
        strSubName = intent.getStringExtra("Sub_Title");
        strSubImage = intent.getStringExtra("Sub_image");

        /*get the user details*/
        App = MyApplication.getInstance();

        top_pros_txt = findViewById(R.id.top_pros_txt);
        //top_pros_txt.setText("Top "+strSubName+" Pros");

        top_pros_rv = findViewById(R.id.top_pros_rv);
        top_pros_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        top_pros_rv.setHasFixedSize(true);

        cats_rv = findViewById(R.id.cats_rv);
        cats_rv.setLayoutManager(new GridLayoutManager(AllCategoryActivity.this, 2));
        cats_rv.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(AllCategoryActivity.this, 4), true));
        cats_rv.setHasFixedSize(true);

        
        /*for loader animation*/
        overlay_loader = findViewById(R.id.overlay_loader);

        /* connect to server and get bids for the project*/
        if (JsonUtils.isNetworkAvailable(this)) {
           loadSubPros();
        } else{
            intent = new Intent(AllCategoryActivity.this , InternetActivity.class);
            startActivity(intent);
        }

        /*for preloader*/

        lyt_suggestion = (LinearLayout) findViewById(R.id.lyt_suggestion);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(textWatcher);
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

    private void showSuggestionSearch() {
        ViewAnimation.expand(lyt_suggestion);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence c, int i, int i1, int i2) {
            if (c.toString().trim().length() == 0) {
                bt_clear.setVisibility(View.GONE);
            } else {
                bt_clear.setVisibility(View.VISIBLE);
                recyclerSuggestion.setVisibility(View.VISIBLE);
            }
            search_text  = et_search.getText().toString();
        }

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() >= 3) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runSearch();
                    }
                }, 1000);
            }


        }
    };

    private void searchAction() {
        ViewAnimation.collapse(lyt_suggestion);

        final String query = et_search.getText().toString().trim();
        if (!query.equals("")) {
            // create a function for the first load
            runSearch();
        } else {
            Toast.makeText(this, "Please fill search input", Toast.LENGTH_SHORT).show();
        }
    }

    private void runSearch(){
        if (loadingData){
            Log.i("SEARHCQUERY", " SOMETHING IS ALREADY LOADING");
        } else{
            Log.i("SEARHCQUERY", " search url is "+ Constant.SEARCH_SUBCAT+search_text);
            if (JsonUtils.isNetworkAvailable(AllCategoryActivity.this)) {
                loadSearch();
            } else{
                Intent intent = new Intent(this , InternetActivity.class);
                startActivity(intent);
            }
        }
    }

    private void loadSearch() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        loadingData = true;

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.getSearchPar(search_text);
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

                    String Searchresponse = response.body().toString();
                    getSearchJson(Searchresponse);
                    loadingData = false;

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(AllCategoryActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSearchJson(String Searchresponse) {
        mSubcategory.clear();
        try {
            JSONObject mainJson = new JSONObject(Searchresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                ItemSubcat objItem = new ItemSubcat();
                objItem.setSubId(objJson.getString(Constant.SUB_ID));
                objItem.setSubTitle(objJson.getString(Constant.SUB_TITLE));
                objItem.setSubSecName(objJson.getString(Constant.SUB_SEC));
                mSubcategory.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displaySuggest();
    }

    private void displaySuggest() {
        rvSuggest.setVisibility(View.VISIBLE);
        mSubCatAdapter = new SearchSubCatAdapter(this, mSubcategory);
        rvSuggest.setAdapter(mSubCatAdapter);
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
        Call<String> call = ubuyapi.getHomeFeeds();
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
                Intent intent = new Intent(AllCategoryActivity.this, InternetActivity.class);
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
            JSONArray jsonPremium = jsonArray.getJSONArray(Constant.HOME_PRO);
            JSONObject objJsonPremium;
            for (int i = 0; i < jsonPremium.length(); i++) {
                objJsonPremium = jsonPremium.getJSONObject(i);
                ItemPros objItem = new ItemPros();
                objItem.setProId(objJsonPremium.getString(Constant.PRO_ID));
                objItem.setProName(objJsonPremium.getString(Constant.PRO_NAME));
                objItem.setProjectCount(objJsonPremium.getString(Constant.TASK_DONE));
                objItem.setProfileImage(objJsonPremium.getString(Constant.PRO_IMAGE));
                objItem.setPremiumPro(objJsonPremium.getString(Constant.PREMIUM_PRO));
                objItem.setProService(objJsonPremium.getString(Constant.PRO_SERVICE));
                mTopPros.add(objItem);
            }

            JSONArray jsonCat = jsonArray.getJSONArray(Constant.HOME_CAT);
            JSONObject objJsonCat;
            for (int i = 0; i < jsonCat.length(); i++) {
                objJsonCat = jsonCat.getJSONObject(i);
                ItemCat objItem = new ItemCat();
                objItem.setCatId(objJsonCat.getString(Constant.CAT_ID));
                objItem.setCatTitle(objJsonCat.getString(Constant.CAT_NAME));
                objItem.setCatDes(objJsonCat.getString(Constant.CAT_DES));
                objItem.setCatPic(objJsonCat.getString(Constant.CAT_PIC));
                objItem.setCatColor(objJsonCat.getString(Constant.CAT_COLOR));
                mCats.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }

    private void displayData() {
//        topProsAdapter = new SubTopProsAdapter(AllCategoryActivity.this, mTopPros);
//        top_pros_rv.setAdapter(topProsAdapter);

        topProsAdapter = new HomeTopProsAdapter(AllCategoryActivity.this, mTopPros);
        top_pros_rv.setAdapter(topProsAdapter);

        allCatAdapter = new CatAdapter(AllCategoryActivity.this, mCats);
        cats_rv.setAdapter(allCatAdapter);
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
