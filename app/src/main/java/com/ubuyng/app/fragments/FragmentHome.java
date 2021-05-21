package com.ubuyng.app.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubuyng.app.AllProsActivity;
import com.ubuyng.app.Categories.AllCategoryActivity;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.NotificationActivity;
import com.ubuyng.app.R;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ubuyng.app.ubuyapi.Dependants.SpacingItemDecoration;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemPros;
import com.ubuyng.app.ubuyapi.Models.ItemSlider;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;
import com.ubuyng.app.ubuyapi.adapters.HomeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomePopularAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomeSliderAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomeTopProsAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
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

// preloader

public class FragmentHome extends Fragment {


    private TextView mTextMessage;
    ArrayList<ItemCat> mCat;
    ArrayList<ItemSlider> mSlider;
    ArrayList<ItemSubcat> mPopular, mPersonal, mBusiness, mHome;
    ArrayList<ItemPros> mTopPros;
    public RecyclerView popular_rv, cat_rv, top_pros_rv, personal_rv, business_rv,  home_rv, slider_rv;
    AVLoadingIndicatorView avi;
    HomePopularAdapter mPopularAdapter;
       HomeTopProsAdapter mTopProsAdapter;
    HomeCatAdapter mCatAdapter;
    HomeSliderAdapter mSlideAdapter;
    private LinearLayout lyt_not_found;
    private RelativeLayout overlay_loader;
    private LinearLayout search_btn_main, feed_lyt;
    private TextView loading_text, view_all_cats, tool_name,  personal_explore, home_explore, web_explore, business_explore, view_all_pros;
    private Button btn_retry;
    NestedScrollView scrollview;
    MyApplication App;
    CardView add_task_card, add_task_quick;
    RelativeLayout loading_data;
    private ImageView tool_image, notification_btn;

    public FragmentHome() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        App = MyApplication.getInstance();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        mSlider = new ArrayList<>();
        mPopular = new ArrayList<>();
        mCat = new ArrayList<>();
        mTopPros = new ArrayList<>();

        lyt_not_found = rootView.findViewById(R.id.lyt_not_found);
        view_all_cats = rootView.findViewById(R.id.view_all_cats);
        view_all_pros = rootView.findViewById(R.id.view_all_pros);
        loading_data = rootView.findViewById(R.id.loading_data);
        avi = rootView.findViewById(R.id.avi);
        loading_text = rootView.findViewById(R.id.loading_text);

        btn_retry = rootView.findViewById(R.id.btn_retry);
        scrollview = rootView.findViewById(R.id.scrollview);
        feed_lyt = rootView.findViewById(R.id.feed_lyt);

        view_all_cats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllCategoryActivity.class);
                startActivity(intent);
            }
        });

        view_all_pros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllProsActivity.class);
                startActivity(intent);
            }
        });

        cat_rv = rootView.findViewById(R.id.category_rv);
        cat_rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        cat_rv.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 4), true));
        cat_rv.setHasFixedSize(true);

        slider_rv = rootView.findViewById(R.id.slider_rv);
        slider_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        slider_rv.setHasFixedSize(true);

        top_pros_rv = rootView.findViewById(R.id.top_pros_rv);
        top_pros_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        top_pros_rv.setHasFixedSize(true);

        popular_rv = rootView.findViewById(R.id.popular_rv);
        popular_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popular_rv.setHasFixedSize(true);

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
                    parseFeedData(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");
                    //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(getActivity(), InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });

        return rootView;

    }


    private void parseFeedData(String jsonresponse) {
        Log.i("onEmptyResponse", "json test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);
            //slider model
            JSONArray jsonSlider = jsonArray.getJSONArray(Constant.HOME_SLIDER);
            JSONObject objJsonSlide;
            for (int i = 0; i < jsonSlider.length(); i++) {
                objJsonSlide = jsonSlider.getJSONObject(i);
                ItemSlider objItem = new ItemSlider();
                objItem.setSlideId(objJsonSlide.getString(Constant.SLIDE_ID));
                objItem.setSlideName(objJsonSlide.getString(Constant.SLIDE_NAME));
                objItem.setSlidePic(objJsonSlide.getString(Constant.SLIDE_PIC));
                objItem.setSlideType(objJsonSlide.getString(Constant.SLIDE_TYPE));
                objItem.setSourceId(objJsonSlide.getString(Constant.SLIDE_SOURCE_ID));
                mSlider.add(objItem);
            }
            //cat
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
                mCat.add(objItem);
            }

//                    recommended
            JSONArray jsonRecom = jsonArray.getJSONArray(Constant.HOME_RECOMMEND);
            JSONObject objjsonRecom;
            for (int i = 0; i < jsonRecom.length(); i++) {
                objjsonRecom = jsonRecom.getJSONObject(i);
                ItemSubcat objItem = new ItemSubcat();
                objItem.setSubId(objjsonRecom.getString(Constant.SUB_ID));
                objItem.setSubTitle(objjsonRecom.getString(Constant.SUB_TITLE));
                objItem.setSubPic(objjsonRecom.getString(Constant.SUB_PIC));
                mPopular.add(objItem);
            }

//                PROS LIST
            JSONArray jsonDesign = jsonArray.getJSONArray(Constant.HOME_PRO);
            JSONObject objjsonDesign;
            for (int i = 0; i < jsonDesign.length(); i++) {
                objjsonDesign = jsonDesign.getJSONObject(i);
                ItemPros objItem = new ItemPros();
                objItem.setProId(objjsonDesign.getString(Constant.PRO_ID));
                objItem.setProName(objjsonDesign.getString(Constant.PRO_NAME));
                objItem.setProjectCount(objjsonDesign.getString(Constant.TASK_DONE));
                objItem.setProfileImage(objjsonDesign.getString(Constant.PRO_IMAGE));
                objItem.setPremiumPro(objjsonDesign.getString(Constant.PREMIUM_PRO));
                objItem.setProService(objjsonDesign.getString(Constant.PRO_SERVICE));
                mTopPros.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }

    private void displayData() {

        //          SLIDER ADAPTER
        mSlideAdapter = new HomeSliderAdapter(getActivity(), mSlider);
        slider_rv.setAdapter(mSlideAdapter);

        //          CAT Adapter ADAPTER
        mCatAdapter = new HomeCatAdapter(getActivity(), mCat);
        cat_rv.setAdapter(mCatAdapter);

        //          RECOMMEND Adapter ADAPTER
        mPopularAdapter = new HomePopularAdapter(getActivity(), mPopular);
        popular_rv.setAdapter(mPopularAdapter);

//         DESIGN Spotlight ADAPTER
        mTopProsAdapter = new HomeTopProsAdapter(getActivity(), mTopPros);
        top_pros_rv.setAdapter(mTopProsAdapter);

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

