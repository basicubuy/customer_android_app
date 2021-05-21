package com.ubuyng.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ubuyng.app.mini.ProfileActivity;

public class CouponActivity extends AppCompatActivity {
    WebView webView;
    String htmlText;
    RelativeLayout loading_webview;
    TextView activity_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        initToolbar();
        webView = findViewById(R.id.webview);
        loading_webview = findViewById(R.id.loading_webview);
        htmlText = "https://ubuy.ng/about/terms-of-use";
        loadHTW();
        activity_name = findViewById(R.id.activity_name);
        activity_name.setText("Contact us");
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


    private void loadHTW(){
        webView.setVisibility(View.VISIBLE);

        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("about")) {
                    webView.loadUrl(url);
                    return true;
                }
                else{
                    Intent intent = new Intent(CouponActivity.this, ProfileActivity.class);
                    startActivity(intent);
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
        Log.i("NETWORK_TESTER", htmlText);

        webView.loadUrl(htmlText);

        webView.setWebViewClient(new WebViewClient() {
                                     private boolean error;

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {

                                     }

                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         loading_webview.setVisibility(View.INVISIBLE);
                                     }
                                 }
        );
    }
    private class MyWebChromeClient extends WebChromeClient {
        Context context;



        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
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
