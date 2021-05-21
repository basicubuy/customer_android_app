package com.ubuyng.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TaskerActivity extends AppCompatActivity  {

    private String sub_id, sub_title, locate_string, user_address, user_city, user_lat, user_lng, htmlText;
    MyApplication App;
    TextView pro_name;
    CircularImageView pro_image;
    EditText EditMessage;
    WebView webView;
    MaterialCardView accept_pro;
    ImageView buttonSend;
    RelativeLayout finding_pro_layout, finding_ques;
    Button view_tasker;
    boolean loadingFinished = true;
    boolean redirect = false;
    ArrayList<ItemCat> mCategory;
    AVLoadingIndicatorView avi;
    ImageButton bt_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasker);

        mCategory = new ArrayList<>();
        App = MyApplication.getInstance();
        accept_pro = findViewById(R.id.accept_pro);
        Intent intent = getIntent();
        sub_id = intent.getStringExtra("sub_id");
        sub_title = intent.getStringExtra("sub_title");
        user_address = intent.getStringExtra("user_address");
        user_city = intent.getStringExtra("user_city");
        user_lat = intent.getStringExtra("user_lat");
        user_lng = intent.getStringExtra("user_lng");
        Log.d("Debug_call", "The subcategory id is "+sub_id);
         htmlText = "https://ubuy.ng/mtasker/?sub_id="+sub_id+"&user_id="+App.getUserId()+"&user_city="+user_city+"&user_address="+user_address+"&user_lat="+user_lat+"&user_lng="+user_lng;
        finding_ques = findViewById(R.id.finding_ques);
        webView = findViewById(R.id.webview);
        avi = findViewById(R.id.avi);
        avi.smoothToShow();
        finding_pro_layout = findViewById(R.id.finding_pro);
        view_tasker = findViewById(R.id.view_tasker);
        loadTasker();

        bt_close = findViewById(R.id.bt_close);
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (JsonUtils.isNetworkAvailable(this)) {
            new LoadNetTester().execute(Constant.SEARCH_CAT);
        } else{
            intent = new Intent(this , InternetActivity.class);
            startActivity(intent);
            Log.i("PROFILE", "LOADER");

        }

    }



    @SuppressLint("StaticFieldLeak")
    private class LoadNetTester extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null == result || result.length() == 0) {
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemCat objItem = new ItemCat();
                        objItem.setCatId(objJson.getString(Constant.CAT_ID));
                        objItem.setCatTitle(objJson.getString(Constant.CAT_NAME));
                        objItem.setCatPic(objJson.getString(Constant.CAT_PIC));
                        mCategory.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
                finding_ques.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void loadTasker(){
        webView.setVisibility(View.VISIBLE);

        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.equals(htmlText)) {
                    webView.loadUrl(url);
                    Log.i("URLCHECKER", "On mtasker");

                    return true;
                }
                else{
                    finding_pro_layout.setVisibility(View.VISIBLE);
                    view_tasker.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.INVISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(TaskerActivity.this, ProjectBidActivity.class);
                            startActivity(intent);
                        }
                    }, 5000);
//                    Intent intent = new Intent(TaskerActivity.this, MainActivity.class);
//                    startActivity(intent);
                    return true; // Handle By application itself
                }


            }

        });



        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("MyApplication", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(htmlText);

        webView.setWebViewClient(new WebViewClient() {
                                     private boolean error;

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         Log.i("URLCHECKER", url);
                                         if (url.contains("mtasker")) {
//                                             webView.loadUrl(url);
                                             Log.i("URLCHECKER", "On mtasker");
                                         }
                                         else{
                                             finding_pro_layout.setVisibility(View.VISIBLE);
                                             view_tasker.setVisibility(View.VISIBLE);
                                             webView.setVisibility(View.INVISIBLE);
                                             Log.i("URLCHECKER", "Not mtasker");

                                             new Handler().postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     Intent intent = new Intent(TaskerActivity.this, ProjectBidActivity.class);
                                                     startActivity(intent);
                                                 }
                                             }, 2000);
//                    Intent intent = new Intent(TaskerActivity.this, MainActivity.class);
//                    startActivity(intent);
                                         }

                                     }

                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         finding_ques.setVisibility(View.INVISIBLE);


                                     }
                                 }
        );
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



    private class MyWebChromeClient extends WebChromeClient {
        Context context;



        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

}
