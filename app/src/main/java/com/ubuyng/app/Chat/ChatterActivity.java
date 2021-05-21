package com.ubuyng.app.Chat;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexvasilkov.gestures.commons.circle.CircleImageView;
import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MainActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.PaymentActivity;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.SignInActivity;
import com.ubuyng.app.fragments.CompleteDialogFragment;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.adapters.BiddersAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

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

public class ChatterActivity extends AppCompatActivity  {
    ChatterAdapter rcAdapter;
    ArrayList<ChatterModel> mChatter, mChatterNew;
    RecyclerView chat_rv;
    String chat_sender_type, project_id, bid_id, bidder_id, bidder_name,  bidder_chat_active, bidder_image, bidder_amount, newMessage, bidder_message, strBidHasChat, sender_id, strMessage, strBidStatus, project_title;
    MyApplication App;
    TextView pro_name;
    EditText EditMessage;
    CircularImageView pro_image;
    TextInputEditText msg_input;
    FloatingActionButton fab_send, fab_files;
    LinearLayout disabled_chat_input, accepted_chat_input, chat_body;
    ShapeOfView disagree_sh, accept_sh, chat_agreement_sh;
    private Handler handler;
    private Runnable checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        mChatter = new ArrayList<>();
        mChatterNew = new ArrayList<>();

        App = MyApplication.getInstance();
        Intent intent = getIntent();
        project_id = intent.getStringExtra("project_id");
        bid_id = intent.getStringExtra("bid_id");
        bidder_id = intent.getStringExtra("bidder_id");
        bidder_name = intent.getStringExtra("bidder_name");
        bidder_chat_active = intent.getStringExtra("bidder_chat_active");
        bidder_image = intent.getStringExtra("bidder_image");
        bidder_amount = intent.getStringExtra("bidder_amount");
        project_title = intent.getStringExtra("project_title");
        bidder_message = intent.getStringExtra("bidder_message");
        sender_id = App.getUserId();
        
        bidder_chat_active = "1";

//        header ids
        pro_image =  findViewById(R.id.pro_image);
        Picasso.get().load(bidder_image)
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(pro_image);

        disagree_sh =  findViewById(R.id.disagree_sh);
        disagree_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });  
        
        chat_agreement_sh =  findViewById(R.id.chat_agreement_sh);
        disabled_chat_input = findViewById(R.id.disabled_chat_input);
        accepted_chat_input= findViewById(R.id.accepted_chat_input);
        chat_body = findViewById(R.id.chat_body);
        accept_sh =  findViewById(R.id.accept_sh);
        accept_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatActive();
            }
        });

        pro_name =  findViewById(R.id.pro_name);
        pro_name.setText(bidder_name);


        chat_rv = (RecyclerView) findViewById(R.id.chat_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        chat_rv.setHasFixedSize(false);
        chat_rv.setLayoutManager(layoutManager);

        // chat input and fab
        msg_input =  findViewById(R.id.msg_input);
        fab_send =  findViewById(R.id.fab_send);
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intSendChat();
            }
        });
        fab_files =  findViewById(R.id.fab_files);
        fab_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent files_intent = new Intent(ChatterActivity.this , FilesActivity.class);
                files_intent.putExtra("project_id", project_id);
                files_intent.putExtra("bid_id", bid_id);
                files_intent.putExtra("bidder_id", bidder_id);
                files_intent.putExtra("bidder_name", bidder_name);
                files_intent.putExtra("bidder_amount", bidder_amount);
                files_intent.putExtra("bidder_image", bidder_image);
                files_intent.putExtra("project_title", project_title);
                startActivity(files_intent);            }
        });

        if (bidder_chat_active.equals("1")){
            chatActive();
        }
        if (JsonUtils.isNetworkAvailable(ChatterActivity.this)) {
            loadChat();
        } else{
            intent = new Intent(ChatterActivity.this , InternetActivity.class);
            startActivity(intent);

        }

          handler = new Handler();
          checker = new Runnable() {
            @Override
            public void run() {

                runChecker();
                handler.postDelayed(this, 15000);
            }
        };

        handler.postDelayed(checker, 15000);


    }

    private void chatActive() {
        chat_agreement_sh.setVisibility(View.GONE);
        disabled_chat_input.setVisibility(View.GONE);
        accepted_chat_input.setVisibility(View.VISIBLE);
        chat_body.setVisibility(View.VISIBLE);
    }

   private void intSendChat(){
       chat_sender_type = "1";
       newMessage = String.valueOf(msg_input.getText());
       /*added the data to offline view */
       ChatterModel objItem = new ChatterModel();
       objItem.setSender(chat_sender_type);
       objItem.setMessage(newMessage);
       mChatter.add(objItem);
       rcAdapter.notifyItemInserted(mChatter.size() - 1);
       chat_rv.scrollToPosition(rcAdapter.getItemCount() - 1);
       msg_input.getText().clear();
       /*now send to the server*/
       postMessage();
    }

        private void loadChat() {
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
            Call<String> call = ubuyapi.getBidMessages("2216", "7846");
            Log.i("onEmptyResponse", "test network");
            //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    HttpUrl main_url = response.raw().request().url();
                    Log.i("debugger", String.valueOf(main_url));
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        getChatsJson(jsonresponse);

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
                    Intent intent = new Intent(ChatterActivity.this, InternetActivity.class);
                    startActivity(intent);
                    Log.i("onEmptyResponse", "error occured");
                    //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            });
        }
   private void runChecker() {
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
            Call<String> call = ubuyapi.getBidMessagesChecker("2216", "7846");
            Log.i("onEmptyResponse", "test network");
            //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    HttpUrl main_url = response.raw().request().url();
                    Log.i("debugger", String.valueOf(main_url));
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        getCheckerChatsJson(jsonresponse);

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
                    Intent intent = new Intent(ChatterActivity.this, InternetActivity.class);
                    startActivity(intent);
                    Log.i("onEmptyResponse", "error occured");
                    //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            });
        }

    private void getChatsJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);

//           messages model
            JSONArray jsonChats = jsonArray.getJSONArray(Constant.CHATTER_HEADER);
            JSONObject objjsonChats;
            for (int i = 0; i < jsonChats.length(); i++) {
                objjsonChats = jsonChats.getJSONObject(i);
                ChatterModel objItem = new ChatterModel();
                objItem.setSender(objjsonChats.getString(Constant.CHATTER_SENDER));
                objItem.setMessage(objjsonChats.getString(Constant.CHATTER_MESSAGE));
                mChatter.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }
    private void getCheckerChatsJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);

//           messages model
            JSONArray jsonChats = jsonArray.getJSONArray(Constant.CHATTER_HEADER);
            JSONObject objjsonChats;
            for (int i = 0; i < jsonChats.length(); i++) {
                objjsonChats = jsonChats.getJSONObject(i);
                ChatterModel objItem = new ChatterModel();
                objItem.setSender(objjsonChats.getString(Constant.CHATTER_SENDER));
                objItem.setMessage(objjsonChats.getString(Constant.CHATTER_MESSAGE));
                mChatter.add(objItem);
                rcAdapter.notifyItemInserted(mChatter.size() - 1);
                chat_rv.scrollToPosition(rcAdapter.getItemCount() - 1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        runSeenMessages();
    }

    private void displayData() {
        rcAdapter = new ChatterAdapter(ChatterActivity.this, mChatter);
        chat_rv.setAdapter(rcAdapter);
        chatActive();
    }

    private void postMessage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        // finally, execute the request
        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.sendMessage("7846", "832", "2216", "3782", newMessage);
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
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
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

    private void runSeenMessages() {
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
        Call<String> call = ubuyapi.getBidMessagesSeen("2216", "7846");
        Log.i("onEmptyResponse", "test network");
        //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getCheckerChatsJson(jsonresponse);

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
                Intent intent = new Intent(ChatterActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");
                //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
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



}
