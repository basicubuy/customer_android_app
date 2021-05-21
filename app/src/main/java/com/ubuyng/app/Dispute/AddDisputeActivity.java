package com.ubuyng.app.Dispute;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Interface.AdapDisputeInterface;
import com.ubuyng.app.ubuyapi.Models.ItemDisputeCat;
import com.ubuyng.app.ubuyapi.Models.ItemDisputeTask;
import com.ubuyng.app.ubuyapi.adapters.disputes.DisputeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.disputes.DisputeTaskAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.Tools;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddDisputeActivity extends AppCompatActivity implements AdapDisputeInterface {

    private RecyclerView tasks_rv, cats_rv;
    private Toolbar toolbar;
    ArrayList<ItemDisputeCat> mCats;
    ArrayList<ItemDisputeTask> mTasks;
    private DisputeCatAdapter mAdapterCat;
    private DisputeTaskAdapter mAdapterTasks;
    private EditText editText_des;
    private TextView select_task_text, cat_error_txt, task_error_txt,dispute_error_txt, select_cat_text, file_title_txt, failed_txt;
    private ShapeOfView send_sh, attach_sh, single_attach_sh, Cats_sh,  task_sh, cats_view_lyt, task_view_lyt;
    private AVLoadingIndicatorView avisave_draft;
    private String user_id, str_taskName, str_taskId, str_taskRef, str_CatID, str_CatName, str_Dispute;
    private Integer cat_checker, saving_checker;
    private ImageView select_cat_drop;
    private ImageView select_task_drop;
    private LinearLayout cat_li_lyt, task_li_lyt;
    private NestedScrollView scroll;
    MyApplication App;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dispute_resolution);

        mCats = new ArrayList<>();
        mTasks = new ArrayList<>();
        App = MyApplication.getInstance();
        user_id = App.getUserId();
 
        initToolbar();

        send_sh = findViewById(R.id.send_sh);
        scroll = findViewById(R.id.scroll);

        editText_des = findViewById(R.id.editText_des);
        cat_error_txt = findViewById(R.id.cat_error_txt);
        select_task_text = findViewById(R.id.select_task_text);
        task_error_txt = findViewById(R.id.task_error_txt);
        dispute_error_txt = findViewById(R.id.dispute_error_txt);
        task_li_lyt = findViewById(R.id.task_li_lyt);

        tasks_rv = findViewById(R.id.tasks_rv);
        select_task_drop = findViewById(R.id.select_task_drop);
        task_sh = findViewById(R.id.task_sh);
        task_view_lyt = findViewById(R.id.task_view_lyt);
        tasks_rv = findViewById(R.id.tasks_rv);
        tasks_rv.setLayoutManager(new LinearLayoutManager(this));
        tasks_rv.setHasFixedSize(true);

        cats_rv = findViewById(R.id.cats_rv);
        Cats_sh = findViewById(R.id.Cats_sh);
        select_cat_text = findViewById(R.id.select_cat_text);
        select_cat_drop = findViewById(R.id.select_cat_drop);
        cat_li_lyt = findViewById(R.id.cat_li_lyt);
        cats_view_lyt = findViewById(R.id.cats_view_lyt);
        cats_rv.setLayoutManager(new LinearLayoutManager(this));
        cats_rv.setHasFixedSize(true);

        str_taskName = null;
        str_taskId = null;
        str_taskRef = null;
        str_CatID = null;
        str_CatName = null;
        saving_checker = null;


        getCat();

        send_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               str_Dispute = editText_des.getText().toString();
                QValidator();
            }
        });

        scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cats_view_lyt.getVisibility() == View.VISIBLE){
                    cats_view_lyt.setVisibility(View.GONE);
                }
                if (task_view_lyt.getVisibility() == View.VISIBLE){
                    task_view_lyt.setVisibility(View.GONE);
                }
            }
        });

        Cats_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cats_view_lyt.getVisibility() == View.VISIBLE){
                    cats_view_lyt.setVisibility(View.GONE);
                }else{
                    cats_view_lyt.setVisibility(View.VISIBLE);
                }

            }
        });

        task_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (task_view_lyt.getVisibility() == View.VISIBLE){
                    task_view_lyt.setVisibility(View.GONE);
                }else{
                    task_view_lyt.setVisibility(View.VISIBLE);
                }
            }
        });

//        attach_sh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
//            }
//        });
    }

    private void QValidator() {

        if (str_CatName == null){
            cat_li_lyt.setBackground(ContextCompat.getDrawable(AddDisputeActivity.this, R.drawable.edit_text_select_error_bg_outline));
            select_cat_text.setTextColor(getResources().getColor(R.color.error_form));
            select_cat_drop.setColorFilter(ContextCompat.getColor(this, R.color.error_form));
            cat_error_txt.setVisibility(View.VISIBLE);
        }

        if (str_taskName == null){
            task_li_lyt.setBackground(ContextCompat.getDrawable(AddDisputeActivity.this, R.drawable.edit_text_select_error_bg_outline));
            select_task_text.setTextColor(getResources().getColor(R.color.error_form));
            select_task_drop.setColorFilter(ContextCompat.getColor(this, R.color.error_form));
            task_error_txt.setVisibility(View.VISIBLE);
        }
        if (str_Dispute.trim().length() == 0 || str_Dispute.trim().length() <= 6 &&  str_CatName.trim().length() == 0 && str_taskName.trim().length() == 0 ) {
            dispute_error_txt.setVisibility(View.VISIBLE);
            editText_des.setBackground(ContextCompat.getDrawable(AddDisputeActivity.this, R.drawable.edit_text_error_bg_outline));
        }else{
            postDispute();
        }
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
    public void onCatClick( String catData, String catID){
        if (cats_view_lyt.getVisibility() == View.VISIBLE){
            cats_view_lyt.setVisibility(View.GONE);
            select_cat_text.setText(catData);
            select_cat_text.setTextColor(getResources().getColor(R.color.colorPrimary));
            select_cat_drop.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            cat_li_lyt.setBackground(ContextCompat.getDrawable(AddDisputeActivity.this, R.drawable.edit_text_select_active_bg_outline));
            str_CatName = catData;
            str_CatID = catID;
        }else{
            cats_view_lyt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTaskClick( String taskData, String taskId, String taskRef){
        if (task_view_lyt.getVisibility() == View.VISIBLE){
            task_view_lyt.setVisibility(View.GONE);
            select_task_text.setText(taskData);
            select_task_text.setTextColor(getResources().getColor(R.color.colorPrimary));
            select_task_drop.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            task_li_lyt.setBackground(ContextCompat.getDrawable(AddDisputeActivity.this, R.drawable.edit_text_select_active_bg_outline));
            str_taskName = taskData;
            str_taskId = taskId;
            str_taskRef = taskRef;
        }else{
            task_view_lyt.setVisibility(View.VISIBLE);
        }
    }


    private void postDispute() {
        saving_checker = 0;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        // finally, execute the request
        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.postDispute(App.getUserId(), str_taskId, str_taskRef, str_Dispute, str_CatID);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());
                    String jsonresponse = response.body().toString();
                    parseStatusResponse(jsonresponse);
                } else {
                    saving_checker = null;
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    saving_checker = null;
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("Upload error:", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void parseStatusResponse(String jsonresponse) {
    }

    private void getCat() {

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
        Call<String> call = ubuyapi.getDisputeCat(user_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    parseFeedData(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(AddDisputeActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void parseFeedData(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);
//           Cat model
            JSONArray jsonCat = jsonArray.getJSONArray(Constant.HOME_CAT);
            JSONObject objJsonCat;
            for (int i = 0; i < jsonCat.length(); i++) {
                objJsonCat = jsonCat.getJSONObject(i);
                ItemDisputeCat objItem = new ItemDisputeCat();
                objItem.setCatId(objJsonCat.getString(Constant.CAT_ID));
                objItem.setCatName(objJsonCat.getString(Constant.CAT_NAME));
                mCats.add(objItem);
            }
//           state model
            JSONArray jsonState = jsonArray.getJSONArray(Constant.PROJECTS_HEADER);
            JSONObject objjsonState;
            for (int i = 0; i < jsonState.length(); i++) {
                objjsonState = jsonState.getJSONObject(i);
                ItemDisputeTask objItem = new ItemDisputeTask();
                objItem.setTaskId(objjsonState.getString(Constant.PROJECT_ID));
                objItem.setTaskName(objjsonState.getString(Constant.PROJECT_TITLE));
                objItem.setTaskRef(objjsonState.getString(Constant.PROJECT_REF));
                mTasks.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }

    private void displayData() {
        cats_rv.setVisibility(View.VISIBLE);
        mAdapterCat = new DisputeCatAdapter(AddDisputeActivity.this, mCats, this);
        cats_rv.setAdapter(mAdapterCat);

        tasks_rv.setVisibility(View.VISIBLE);
        mAdapterTasks = new DisputeTaskAdapter(AddDisputeActivity.this, mTasks, this);
        tasks_rv.setAdapter(mAdapterTasks);
    }

}
