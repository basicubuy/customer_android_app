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
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

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

public class EditProfileActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Required(order = 1)
    @TextRule(order = 2, minLength = 3, maxLength = 35, trim = true, message = "Enter Valid First Name")
    EditText first_name;

    @Required(order = 3)
    @TextRule(order = 4, minLength = 3, maxLength = 35, trim = true, message = "Enter Valid Last Name")
    EditText last_name;

    @TextRule(order = 5, message = "Enter valid Phone Number", minLength = 0, maxLength = 11)
    EditText phone_number;

    @Required(order = 6)
    @Email(order = 7, message = "Please Check and Enter a valid Email Address")
    EditText email;


    MyApplication MyApp;


    Button btn_update;

    String strFirst_name, strFcmToken, strLast_name, strEmail, strPassword, strMobi, strMessage, strPassengerId, strUserType, strUserPhoneVerify, strUserEmailVerify;

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
        setContentView(R.layout.activity_edit_profile);
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


        first_name = findViewById(R.id.first_name);
        first_name.setText(strFirst_name);

        last_name = findViewById(R.id.last_name);
        last_name.setText(strLast_name);

        email = findViewById(R.id.email);
        email.setText(strEmail);

        phone_number = findViewById(R.id.phone_number);
        phone_number.setText(strMobi);
//
//        btn_update = findViewById(R.id.update_info_btn);
//
//        update_info_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                validator.validateAsync();
//            }
//        });



        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        strFirst_name = first_name.getText().toString();
        strLast_name = last_name.getText().toString();
        strEmail = email.getText().toString();
        strMobi = phone_number.getText().toString();


        updateUser();
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


    private void updateUser() {
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
        Call<String> call = ubuyapi.editProfile(strFirst_name, strLast_name, strEmail, strMobi);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();





        pDialog = new ProgressDialog(EditProfileActivity.this);
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
                Intent intent = new Intent(EditProfileActivity.this , InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseRegisterResponse(String jsonresponse) {
        Log.i("onSuccess", "json gotten");


        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                strMessage = objJson.getString(Constant.MSG);
                strFirst_name = objJson.getString(Constant.FIRST_NAME);
                strLast_name = objJson.getString(Constant.LAST_NAME);
                strEmail = objJson.getString(Constant.USER_EMAIL);
                strMobi = objJson.getString(Constant.USER_PHONE);
                strPassengerId = objJson.getString(Constant.USER_ID);
                Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                Log.i("REGISTER_CALLBACK", strMessage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResult();

    }


    public void setResult() {

        MyApp.saveLogin(strPassengerId, MyApp.getFcmToken(), strFirst_name, strLast_name,  strEmail, strMobi, MyApp.getUserType(), MyApp.getNumberVerify(), MyApp.getEmailVerify());
        showToast("Profile updated");
    }

    public void showToast(String msg) {
        Toast.makeText(EditProfileActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ExperienceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
