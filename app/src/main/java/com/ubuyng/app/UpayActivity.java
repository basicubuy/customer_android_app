package com.ubuyng.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ubuyng.app.ubuyapi.Models.ItemUpay;
import com.ubuyng.app.ubuyapi.adapters.UpayAdapter;
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

public class UpayActivity extends AppCompatActivity {
    WebView webView;
    String htmlText;
    RelativeLayout loading_webview;
    TextView activity_name;
    ArrayList<ItemUpay> mUpay;
    public RecyclerView upay_history_rv;
    UpayAdapter mUpayAdapter;
    private LinearLayout lyt_not_found, no_projects_found;
    private TextView loading_text, upay_balance_txt;
    private Button btn_retry;
    MyApplication App;
    String user_id, upay_total_balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upay_activity);
        initToolbar();
        mUpay = new ArrayList<>();
        activity_name = findViewById(R.id.activity_name);
        activity_name.setText("Upay");

        App = MyApplication.getInstance();
        user_id = App.getUserId();
        upay_history_rv = findViewById(R.id.upay_history);
        upay_history_rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        upay_history_rv.setHasFixedSize(true);

        upay_balance_txt = findViewById(R.id.upay_balance);
        if (JsonUtils.isNetworkAvailable(UpayActivity.this)) {
            loadUpay();
        } else{
            Intent intent = new Intent(UpayActivity.this , InternetActivity.class);
            startActivity(intent);
        }
    }
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("Terms and Privacy policy");
        }

    }

    private void  loadUpay(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        ProgressDialog pDialog;
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.getUpayHistory(user_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(UpayActivity.this);
        pDialog.setMessage("Loading Please wait...");
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
                    getUpayJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(UpayActivity.this , InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }


    private void getUpayJson(String jsonresponse){
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);
//                    recent tasks model
            JSONArray jsonHistory = jsonArray.getJSONArray(Constant.UPAY_HISTORY);
            JSONObject objJsonHistory;
            for (int i = 0; i < jsonHistory.length(); i++) {
                objJsonHistory = jsonHistory.getJSONObject(i);
                ItemUpay objItem = new ItemUpay();
                objItem.setUpayId(objJsonHistory.getString(Constant.UPAY_ID));
                objItem.setUpayProjectName(objJsonHistory.getString(Constant.UPAY_PROJECT_NAME));
                objItem.setUpayProName(objJsonHistory.getString(Constant.UPAY_PRO_NAME));
                objItem.setUpayProjectId(objJsonHistory.getString(Constant.UPAY_PROJECT_ID));
                objItem.setUpayAmount(objJsonHistory.getString(Constant.UPAY_AMOUNT));
                objItem.setUpayDate(objJsonHistory.getString(Constant.UPAY_DATE));
                mUpay.add(objItem);
            }
//                    completed tasks model
            JSONArray jsonBalance = jsonArray.getJSONArray(Constant.UPAY_BALANCE);
            JSONObject objJsonBalance;
            for (int i = 0; i < jsonBalance.length(); i++) {
                objJsonBalance = jsonBalance.getJSONObject(i);
                upay_total_balance = objJsonBalance.getString(Constant.UPAY_BALANCE_AMOUNT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }
    private void displayData() {

        upay_balance_txt.setText(upay_total_balance);
//       ADAPTER
        mUpayAdapter = new UpayAdapter(UpayActivity.this, mUpay);
        upay_history_rv.setAdapter(mUpayAdapter);

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
