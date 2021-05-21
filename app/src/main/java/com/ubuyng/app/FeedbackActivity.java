package com.ubuyng.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FeedbackActivity extends AppCompatActivity implements Validator.ValidationListener{
    @Required(order = 1)
    @TextRule(order = 2, message = "Please enter your feedback", minLength = 0)
    EditText feedback;




    MyApplication MyApp;


    Button btn_submit;

    String strFirst_name, strMessage, strFcmToken, strLast_name, strEmail, strPassword, strMobi, strPassengerId, strUserType, strUserPhoneVerify, strUserEmailVerify;

    private Validator validator;

    JsonUtils jsonUtils;
    public static final String mypreference = "mypref";
    public static final String pref_email = "pref_email";
    public static final String pref_check = "pref_check";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feedback);
        jsonUtils = new JsonUtils(this);
        pref = getSharedPreferences(mypreference, 0); // 0 - for private mode
        editor = pref.edit();
        MyApp = MyApplication.getInstance();
        Intent edit_intent = getIntent();
        strPassengerId = edit_intent.getStringExtra("user_id");
        strFirst_name = edit_intent.getStringExtra("first_name");
        strLast_name = edit_intent.getStringExtra("last_name");
        strEmail = edit_intent.getStringExtra("email");
        strMobi = edit_intent.getStringExtra("number");



        btn_submit = findViewById(R.id.btn_submit);
        feedback = findViewById(R.id.feedback);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                validator.validateAsync();
            }
        });



        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        strMessage = feedback.getText().toString();



        sendFeedback();
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, "Record Not Saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendFeedback() {
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
        Call<String> call = ubuyapi.submitFeedback(strPassengerId, strMessage);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();





        pDialog = new ProgressDialog(FeedbackActivity.this);
        pDialog.setMessage("Updating ...");
        pDialog.setCancelable(false);
        pDialog.show();
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (null != pDialog && pDialog.isShowing()) {
                    pDialog.dismiss();
                    HttpUrl main_url = response.raw().request().url();
                    Log.i("debugger", String.valueOf(main_url));
                }

                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    parseRegisterResponse(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(FeedbackActivity.this , InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseRegisterResponse(String jsonresponse) {
        Log.i("onSuccess", "json gotten");
        Toast.makeText(this, "Thanks for the Feedback.", Toast.LENGTH_SHORT).show();


    }




    public void showToast(String msg) {
        Toast.makeText(FeedbackActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ExperienceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
