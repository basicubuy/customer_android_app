package com.ubuyng.app;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.ubuyapi.Models.ItemNotify;
import com.ubuyng.app.ubuyapi.adapters.NotifiesAdapter;
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

public class NotificationActivity extends AppCompatActivity {

    ArrayList<ItemNotify> mNotify;
    public RecyclerView notify_rv;
    AVLoadingIndicatorView avi;
    NotifiesAdapter mNotifyAdapter;
    private LinearLayout lyt_not_found, no_notify_found;
    private RelativeLayout overlay_loader;
    private TextView loading_text, tool_name;
    private Button btn_retry;
    NestedScrollView scrollview;
    MyApplication App;
    Toolbar main_toolbar;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mNotify = new ArrayList<>();
        App = MyApplication.getInstance();
        user_id = App.getUserId();//        projects ITEMS

        Log.i("DEBUG_USER_ID", "user id is"+ App.getUserId());

        lyt_not_found = findViewById(R.id.lyt_not_found);
        no_notify_found = findViewById(R.id.no_notify_found);
        overlay_loader = findViewById(R.id.overlay_loader);
        avi = findViewById(R.id.avi);
        loading_text = findViewById(R.id.loading_text);

        btn_retry = findViewById(R.id.btn_retry);
        scrollview = findViewById(R.id.scrollview);

//        projects ITEMS
        notify_rv = findViewById(R.id.notify_rv);
        notify_rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        notify_rv.setHasFixedSize(true);


        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JsonUtils.isNetworkAvailable(NotificationActivity.this)) {
                    loadNotify();
                } else{
                    Intent intent = new Intent(NotificationActivity.this, InternetActivity.class);
                    startActivity(intent);
                }
            }
        });

        // create a function for the first load
        if (JsonUtils.isNetworkAvailable(NotificationActivity.this)) {
            loadNotify();
        } else{
            Intent intent = new Intent(NotificationActivity.this, InternetActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
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

    private void loadNotify() {
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
        Call<String> call = ubuyapi.getNotify(user_id);
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
                    getNotifyJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(NotificationActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getNotifyJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                ItemNotify objItem = new ItemNotify();
                objItem.setProjectId(objJson.getString(Constant.PROJECT_ID));
                objItem.setBidId(objJson.getString(Constant.BID_ID));
                objItem.setNotifyMsg(objJson.getString(Constant.NOTIFY_MSG));
                objItem.setNotifyType(objJson.getString(Constant.NOTIFY_TYPE));
                objItem.setNotifyUrl(objJson.getString(Constant.NOTIFY_URL));
                objItem.setNotifyDate(objJson.getString(Constant.PROJECT_DATE));
                mNotify.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();

    }


    private void displayData() {
//          Fact Adapter ADAPTER
        mNotifyAdapter = new NotifiesAdapter(NotificationActivity.this, mNotify);
        notify_rv.setAdapter(mNotifyAdapter);

    }

    private void startAnim(){
//        avi.show();
        avi.smoothToShow();
        overlay_loader.setVisibility(View.VISIBLE);
        lyt_not_found.setVisibility(View.GONE);
        no_notify_found.setVisibility(View.GONE);
    }

    private void stopAnim(){
//        avi.hide();
        avi.smoothToHide();
        overlay_loader.setVisibility(View.GONE);

    }
}
