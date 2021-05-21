package com.ubuyng.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alexvasilkov.gestures.commons.circle.CircleImageView;
import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;
import com.github.florent37.shapeofview.ShapeOfView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.fragments.DialogPaymentSuccessFragment;
import com.ubuyng.app.ubuyapi.Dependants.ViewAnimation;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.flutterwave.raveandroid.RaveConstants.NG;
import static com.flutterwave.raveandroid.RaveConstants.NGN;
import static com.flutterwave.raveandroid.RaveConstants.RAVE_PARAMS;

public class PaymentActivity extends AppCompatActivity {


    String project_id, project_brief,upay_type, project_deadline, bidder_task_done, bid_id, bidder_id, bidder_name, bidder_image, bidder_amount, trans_fee, trans_total, trans_amount, trans_percent, trans_duration, project_title, user_id, txRef;
    MyApplication App;
    TextView pro_name, task_brief, pro_task_done, bid_amount, task_name, bid_duration, payment_bid, transact_fee, payment_total, tans_percent_cal;
    ImageView pro_image;
    LinearLayout bid_duration_lyt;
    CardView pay_btn;
    ImageButton bt_close, bt_toggle_text, bt_toggle_input;;
//    Integer flutter_total;
     double doubleValue;
     int flutter_total;
    private View parent_view;
    private NestedScrollView nested_scroll_view;
    private Button pay_now, bt_hide_text, bt_save_input, bt_hide_input;
    private View lyt_expand_text, lyt_expand_input;
    private ShapeOfView coupon_sh;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pro);

        initToolbar();
        App = MyApplication.getInstance();
        Intent intent = getIntent();
        bid_id = intent.getStringExtra("bid_id");
        user_id = App.getUserId();


        pro_image =  findViewById(R.id.pro_image);


        pro_name =  findViewById(R.id.pro_name);
        pro_name.setText(bidder_name);

        pro_task_done =  findViewById(R.id.pro_task_done);
        task_name =  findViewById(R.id.task_name);
        bid_amount =  findViewById(R.id.bid_amount);
        task_brief =  findViewById(R.id.task_brief);
        payment_bid =  findViewById(R.id.payment_bid);
        tans_percent_cal =  findViewById(R.id.tans_percent_cal);


        bid_duration =  findViewById(R.id.task_deadline);
        transact_fee =  findViewById(R.id.transact_fee);
        payment_total =  findViewById(R.id.payment_total);
        pay_btn =  findViewById(R.id.pay_btn);
        bt_close =  findViewById(R.id.bt_close);
        pay_now =  findViewById(R.id.pay_now);
        pay_now.setVisibility(View.INVISIBLE);


//        bt_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        if (JsonUtils.isNetworkAvailable(PaymentActivity.this)) {
            loadingPaymentEnv();
        } else{
//            TODO: DEVELOP INTERNET ERROR ACTIVITY
            Log.i("NETWORK_TESTER", "NO INTERNET DETECTED");
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
        initComponent();
    }

    private void initComponent() {
        coupon_sh = findViewById(R.id.coupon_sh);
        coupon_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogPaymentSuccess();

            }
        });
//
//        // text section
        bt_toggle_text = (ImageButton) findViewById(R.id.bt_toggle_text);
        lyt_expand_text = (View) findViewById(R.id.lyt_expand_text);
        lyt_expand_text.setVisibility(View.GONE);
//
        bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

//        bt_hide_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggleSectionText(bt_toggle_text);
//            }
//        });

        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
    }


    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


    private void showDialogPaymentSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString("taskName", project_title);
        bundle.putString("task_id", project_id);
        bundle.putString("bid_id", bid_id);
        bundle.putString("bidder_name", bidder_name);
        bundle.putString("bidder_amount", bidder_amount);
        bundle.putString("bidder_image", bidder_image);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogPaymentSuccessFragment newFragment = new DialogPaymentSuccessFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    private void loadingPaymentEnv() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        ProgressDialog pDialog;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.getBidPayment(bid_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(PaymentActivity.this);
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
                    parseStatusResponse(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PaymentActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }



    private void parseStatusResponse(String jsonresponse) {
        if (null == jsonresponse || jsonresponse.length() == 0) {
            showToast(getString(R.string.nodata));
        } else {
            try {
                JSONObject mainJson = new JSONObject(jsonresponse);
                JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                JSONObject objJson;
                for (int i = 0; i < jsonArray.length(); i++) {
                    objJson = jsonArray.getJSONObject(i);
                    project_title = objJson.getString(Constant.PROJECT_TITLE);
                    project_brief = objJson.getString(Constant.PROJECT_MESSAGE);
                    project_deadline = objJson.getString(Constant.PROJECT_DEADLINE);
                    bidder_image = objJson.getString(Constant.PRO_IMAGE);
                    bidder_name = objJson.getString(Constant.PRO_NAME);
                    bidder_task_done = objJson.getString(Constant.TASK_DONE);
                    bidder_amount = objJson.getString(Constant.BID_AMOUNT);
                    txRef = objJson.getString(Constant.TEXT_REF);
                    trans_amount = objJson.getString(Constant.TRANSACT_AMOUNT);
                    trans_fee = objJson.getString(Constant.TRANSACT_FEE);
                    trans_percent = objJson.getString(Constant.TRANSACT_PERCENT);
                    trans_total = objJson.getString(Constant.TRANSACT_TOTAL);
                    trans_duration = objJson.getString(Constant.TRANSACT_DURATION);
                     upay_type = objJson.getString(Constant.UPAY_TYPE);
//                    flutter_total = Integer.valueOf();
                    final String f_total = objJson.getString(Constant.TRANSACT_FLUTTER);
                    try{
                        doubleValue = Double.parseDouble(f_total.trim());
                        flutter_total = (int) doubleValue;
                    }catch(NumberFormatException exception){

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        updateDisplay();

    }

    private void updateDisplay() {
        pro_name.setText(bidder_name);
        task_brief.setText(project_brief);
        task_name.setText(project_title);
        bid_duration.setText("Deadline: "+project_deadline);
        pro_task_done.setText(bidder_task_done+" Jobs completed");
        Picasso.get().load(bidder_image)
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(pro_image);
        bid_amount.setText('₦'+bidder_amount);

        /*payment calculations*/
        payment_bid.setText('₦'+trans_amount);
        payment_total.setText('₦'+trans_total);
        tans_percent_cal.setText("ransaction Fees ("+trans_percent+")");
        transact_fee.setText('₦'+trans_fee);
        switch (upay_type) {

            case "0":
                pay_now.setVisibility(View.VISIBLE);
                pay_now.setText("Hire pro");
                pay_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArtisanHire();
                    }
                });
                break;
            case "1":
                pay_now.setVisibility(View.VISIBLE);
                pay_now.setText("Pay Now");

                pay_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        paymentRave();
                    }
                });
                break;
        }

    }

    private void paymentRave() {
        new RavePayManager(PaymentActivity.this).setAmount(Double.parseDouble(String.valueOf(flutter_total)))
                .setCountry(NG)
                .setCurrency(NGN)
                .setEmail(App.getUserEmail())
                .setfName(App.getUserFirstName())
                .setlName(App.getUserLastName())
                .setPublicKey("FLWPUBK-4b136db5c3f70524249a430ea819243b-X")
                .setEncryptionKey("61396918b329defd49059766")
                .setTxRef(txRef)
                .setPhoneNumber(App.getUserMobile())
                .acceptAccountPayments(true)
                    .acceptCardPayments(true)
                    .acceptMpesaPayments(false)
                    .acceptAchPayments(false)
                    .acceptGHMobileMoneyPayments(false)
                    .acceptUgMobileMoneyPayments(false)
                    .acceptZmMobileMoneyPayments(false)
                    .acceptRwfMobileMoneyPayments(false)
                    .acceptSaBankPayments(false)
                    .acceptUkPayments(false)
                    .acceptBankTransferPayments(true)
                    .acceptUssdPayments(true)
                    .acceptBarterPayments(true)
                    .acceptFrancMobileMoneyPayments(false)
                    .allowSaveCardFeature(true)
                    .onStagingEnv(false)
                .shouldDisplayFee(false)
                    .showStagingLabel(false)
                    .initialize();
    }

    /********---------- HANDLING RAVE RESPONSE  ----------****/
    /*
     *  We advise you to do a further verification of transaction's details on your server to be
     *  sure everything checks out before providing service or goods.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {

            String message = data.getStringExtra("response");

            if (message != null) {
                Log.d("rave response", message);
            }

            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                PaymentSuccess();
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
//              todo  PaymentFailed();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /********---------- RAVE RESPONSE ENDS HERE  ----------****/

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void PaymentSuccess() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        ProgressDialog pDialog;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONSITEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.sendpaymentSuccess(bid_id, txRef);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(PaymentActivity.this);
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
                    showDialogPaymentSuccess();
                    String jsonresponse = response.body().toString();
                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PaymentActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }
    private void ArtisanHire() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        ProgressDialog pDialog;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONSITEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.HireProArtisan(bid_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        pDialog = new ProgressDialog(PaymentActivity.this);
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
                    onBackPressed();
                    String jsonresponse = response.body().toString();
                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PaymentActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }


}
