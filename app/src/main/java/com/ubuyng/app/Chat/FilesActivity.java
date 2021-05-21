package com.ubuyng.app.Chat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexvasilkov.gestures.commons.circle.CircleImageView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MainActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.PaymentActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Dependants.SpacingItemDecoration;
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

public class FilesActivity extends AppCompatActivity  {
    FilesAdapter mFilesAdapter;
    ArrayList<FilesModel> mFiles;
    RecyclerView rView;
    String project_id, bid_id, bidder_id, bidder_name, bidder_image, bidder_amount, newMessage, bidder_message, strBidHasChat, sender_id, strMessage, strBidStatus, project_title;
    MyApplication App;
    TextView pro_name, bid_amount, task_name, bid_pro_name, bid_pro_amount, bid_pro_msg;
    ImageView image_view;
    CircleImageView pro_image;
    LinearLayout selected_image_lty, no_files;
   private  CardView  attach;
    ImageButton bt_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_files);
        mFiles = new ArrayList<>();

        App = MyApplication.getInstance();
        Intent intent = getIntent();
        project_id = intent.getStringExtra("project_id");
        bid_id = intent.getStringExtra("bid_id");
        bidder_id = intent.getStringExtra("bidder_id");
        bidder_name = intent.getStringExtra("bidder_name");
        bidder_image = intent.getStringExtra("bidder_image");
        bidder_amount = intent.getStringExtra("bidder_amount");
        project_title = intent.getStringExtra("project_title");
        bidder_message = intent.getStringExtra("bidder_message");
        sender_id = App.getUserId();

//        header ids
        pro_image =  findViewById(R.id.pro_image);
        Picasso.get().load(bidder_image)
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(pro_image);

        bt_close =  findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pro_name =  findViewById(R.id.pro_name);
        pro_name.setText(bidder_name);


        bid_amount =  findViewById(R.id.bid_amount);
        bid_amount.setText("₦"+ bidder_amount);

        task_name =  findViewById(R.id.task_name);
        task_name.setText(project_title);

        // chat input and fab
        attach =  (CardView) findViewById(R.id.attach_card);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            needPermission();
        } else {
            gotPrmission();
        }
//        video.setOnClickListener(this);
//        pdf.setOnClickListener(this);


        no_files =  findViewById(R.id.no_files);

        rView =  findViewById(R.id.rView);
        rView.setLayoutManager(new GridLayoutManager(this, 2));
        rView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 3), true));
        rView.setHasFixedSize(true);

        Toolbar toolbar = findViewById(R.id.toolbar_main);

        if (JsonUtils.isNetworkAvailable(FilesActivity.this)) {
            loadFiles();
        } else{
//            TODO: DEV ELOP INTERNET ERROR ACTIVITY
            Log.i("NETWORK_TESTER", "NO INTERNET DETECTED");
        }


    }

    private void gotPrmission() {
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img_intent = new Intent(FilesActivity.this, ImageActivity.class);
                img_intent.putExtra("project_id", project_id);
                img_intent.putExtra("bid_id", bid_id);
                img_intent.putExtra("bidder_id", bidder_id);
                img_intent.putExtra("bidder_name", bidder_name);
                img_intent.putExtra("bidder_amount", bidder_amount);
                img_intent.putExtra("bidder_image", bidder_image);
                img_intent.putExtra("project_title", project_title);
                startActivity(img_intent);
            }
        });
    }

    private void needPermission() {
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast("Opps. \n we need permission to do that");
            }
        });
    }

    private void openFiles() {
        }

    private void loadFiles() {
        ProgressDialog pDialog;
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
        Call<String> call = ubuyapi.getFiles(project_id, bid_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(FilesActivity.this);
        pDialog.setMessage("Loading Chat...");
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
                    Filesjson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(FilesActivity.this , InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void Filesjson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                FilesModel objItem = new FilesModel();
                objItem.setFile_id(objJson.getString(Constant.FILE_ID));
                objItem.setBid_id(objJson.getString(Constant.BID_ID));
                objItem.setProject_id(objJson.getString(Constant.PROJECT_ID));
                objItem.setIs_image(objJson.getString(Constant.FILE_IS_IMAGE));
                objItem.setFile_url(objJson.getString(Constant.FILE_URL));
                objItem.setFile_type(objJson.getString(Constant.FILE_TYPE));
                objItem.setFile_name(objJson.getString(Constant.FILE_NAME));
                objItem.setFile_date(objJson.getString(Constant.FILE_DATE));
                mFiles.add(objItem);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        setResult();
    }

    private void setResult() {

        //          SLIDER ADAPTER
        mFilesAdapter = new FilesAdapter(FilesActivity.this, mFiles);
        rView.setAdapter(mFilesAdapter);
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




    public void showToast(String msg) {
        Toast.makeText(FilesActivity.this, msg, Toast.LENGTH_LONG).show();
    }



}
