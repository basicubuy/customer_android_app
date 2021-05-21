package com.ubuyng.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ubuyng.app.fragments.FragmentEventsCat;
import com.ubuyng.app.fragments.FragmentHomeCat;
import com.ubuyng.app.fragments.FragmentMoreCat;
import com.ubuyng.app.fragments.FragmentWebCat;
import com.ubuyng.app.ubuyapi.Dependants.ViewAnimation;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.AdapterSuggestionSearch;
import com.ubuyng.app.ubuyapi.adapters.SearchSubCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.SuggestSubCatAdapter;
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

//google places


public class SingleCatActivity extends AppCompatActivity {

    private EditText et_search;
    private String search_text, sub_id, sub_Title, project_lat, project_lng, project_address, project_city, project_state;
    private ImageButton bt_clear, bt_close;
    ArrayList<ItemProject> mSubcategory, mSearchSuggest;
    AVLoadingIndicatorView avi, avi_rec;
    private ProgressBar progress_bar;
    private LinearLayout lyt_not_found;
    private RelativeLayout overlay_loader, main_loader;
    private TextView loading_text, auto_text;
    private Button btn_retry;

    private RecyclerView recyclerSuggestion, rvSuggest;
    private AdapterSuggestionSearch mAdapterSuggestion;
    private LinearLayout lyt_suggestion, recom_lyt, toolbar;
    private SearchSubCatAdapter mSubCatAdapter;
    private SuggestSubCatAdapter mSubSuggestAdapter;
//    private Timer timer = new Timer();
    private boolean loadingData;
    private final long DELAY = 2000; // in ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlecat);
        mSubcategory = new ArrayList<>();
        mSearchSuggest = new ArrayList<>();

        Intent intent = getIntent();
        sub_id = intent.getStringExtra("Sub_Id");
        sub_Title = intent.getStringExtra("Sub_Title");

//        initToolbar();
        initComponent();

        rvSuggest = (RecyclerView) findViewById(R.id.rvSuggest);
        rvSuggest.setLayoutManager(new LinearLayoutManager(this));
        rvSuggest.setHasFixedSize(true);

        if (JsonUtils.isNetworkAvailable(this)) {
            loadSubCats();
            Log.i("SUB_LIST", Constant.SEARCH_SUGGEST+sub_id);

        } else{
             intent = new Intent(this , InternetActivity.class);
            startActivity(intent);

        }
    }


    private void loadSubCats() {
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
        Call<String> call = ubuyapi.getsubcats(sub_id);
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
                    getSubCatsJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(SingleCatActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }


    private void getSubCatsJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                ItemProject objItem = new ItemProject();
                objItem.setSubCatId(objJson.getString(Constant.SUB_ID));
                objItem.setCatId(objJson.getString(Constant.SUB_CAT_ID));
                objItem.setSubCatName(objJson.getString(Constant.SUB_TITLE));
                objItem.setSubCatDes(objJson.getString(Constant.SUB_DESCRIPTION));
                objItem.setSubCatIcon(objJson.getString(Constant.SUB_PIC));
                mSubcategory.add(objItem);
//                        Log.i("SUB_LIST", objJson.getString(Constant.SUB_TITLE));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displaySuggest();
    }


    private void initComponent() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);

        lyt_suggestion = (LinearLayout) findViewById(R.id.lyt_suggestion);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(textWatcher);

        overlay_loader = findViewById(R.id.overlay_loader);
        main_loader = findViewById(R.id.main_loader);
//        auto_text = findViewById(R.id.auto_text);
        avi = findViewById(R.id.avi);
        loading_text = findViewById(R.id.loading_text);

        btn_retry = findViewById(R.id.btn_retry);


        bt_clear = (ImageButton) findViewById(R.id.bt_clear);
        bt_clear.setVisibility(View.GONE);
        recyclerSuggestion = (RecyclerView) findViewById(R.id.recyclerSuggestion);
        recyclerSuggestion.setLayoutManager(new LinearLayoutManager(this));
        recyclerSuggestion.setHasFixedSize(true);

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
                recyclerSuggestion.setVisibility(View.GONE);
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    hideKeyboard();
                    searchAction();
                    return true;
                }
                return false;
            }
        });

        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showSuggestionSearch();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;
            }
        });
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
        public void afterTextChanged(Editable  s) {
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
                        Log.i("SEARHCQUERY", " SOME THEING IS ALREADY LOADING");
        } else{
            Log.i("SEARHCQUERY", " search url is "+ Constant.SEARCH_SUBCAT+search_text);

            if (JsonUtils.isNetworkAvailable(SingleCatActivity.this)) {
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
                Intent intent = new Intent(SingleCatActivity.this, InternetActivity.class);
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
                ItemProject objItem = new ItemProject();
                objItem.setSubCatId(objJson.getString(Constant.SUB_ID));
                objItem.setSubCatName(objJson.getString(Constant.SUB_TITLE));
                objItem.setSubCatSec(objJson.getString(Constant.SUB_SEC));
                objItem.setProjectAdd(project_address);
                objItem.setProjectLng(project_lng);
                objItem.setProjectLat(project_lat);
                objItem.setProjectCity(project_city);
                objItem.setProjectState(project_state);
                mSubcategory.add(objItem);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }


  private void displaySuggest() {
        rvSuggest.setVisibility(View.VISIBLE);
        main_loader.setVisibility(View.INVISIBLE);
        mSubSuggestAdapter = new SuggestSubCatAdapter(this, mSubcategory);
        rvSuggest.setAdapter(mSubSuggestAdapter);
    }

    private void displayData() {

//        mSubCatAdapter = new SearchSubCatAdapter(this, mSubcategory);
//        recyclerSuggestion.setAdapter(mSubCatAdapter);
//        int insertIndex = 0;
//        mSubcategory.addAll(insertIndex, mSubcategory);
//        mSubCatAdapter.notifyItemRangeInserted(insertIndex, mSubcategory.size());
//        mSubcategory.clear();
//        mSubCatAdapter.notifyDataSetChanged();
//        replaceData();
        showSuggestionSearch();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


}
