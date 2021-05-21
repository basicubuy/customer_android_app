package com.ubuyng.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SignInActivity extends AppCompatActivity implements Validator.ValidationListener{

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "googleauth_checker";
    String   gEmail, gId, gDisplayname, strEmail, strFcmToken, strPassword, strMessage, strFirstame, strPassengerId, strMobile, strLastName, strUserType, strUserPhoneVerify, strUserEmailVerify;
    Uri gUri;
    @Required(order = 1)
    @Email(order = 2, message = "Please Check and Enter a valid Email Address")
    EditText edtEmail;

    @Required(order = 3)
    @Password(order = 4, message = "Enter a Valid Password")
    @TextRule(order = 5, minLength = 6, message = "Enter a Password Correctly")
    EditText edtPassword;
    private Validator validator;
    Button btnSingIn, auth_google_btn;
    Button auth_facebook_btn;
    MyApplication MyApp = MyApplication.getInstance();
    TextView textForgot, signup_btn;
    JsonUtils jsonUtils;
    public static final String mypreference = "mypref";
    public static final String pref_email = "pref_email";
    public static final String pref_password = "pref_password";
    public static final String pref_check = "pref_check";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    ProgressDialog pDialog;

//    for google auth
GoogleSignInClient mGoogleSignInClient;
GoogleSignInAccount g_account;
//FOR FACEBOOK AUTH
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        /*SECTOR :: ADDING OF FACEBOOK AUTH STARTS HERE*/
        auth_facebook_btn = findViewById(R.id.auth_facebook_btn);
        callbackManager = CallbackManager.Factory.create();
//        auth_facebook_btn.setReadPermissions(Arrays.asList(EMAIL));


        /*SECTOR :: ADDING OF FACEBOOK AUTH ENDS HERE*/

        /*SECTOR :: ADDING OF GOOGLE AUTHS STARTS HERE*/
        auth_google_btn = findViewById(R.id.auth_google_btn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestProfile().requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        
//        check if the user has already signed in
        g_account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(updateUIg_account);
        auth_google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                GSignIn();
            }
        });
        
        /*SECTOR :: ADDING OF GOOGLE AUTHS ENDS HERE*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        }

        jsonUtils = new JsonUtils(this);
        pref = getSharedPreferences(mypreference, 0); // 0 - for private mode
        editor = pref.edit();

        MyApp = MyApplication.getInstance();

        edtEmail = findViewById(R.id.editText_email);
        edtPassword = findViewById(R.id.editText_password);
        btnSingIn = findViewById(R.id.signin_btn);

        signup_btn = findViewById(R.id.signup_btn);

        textForgot = findViewById(R.id.textForgot);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

//        textForgot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validateAsync();
            }
        });


        validator = new Validator(this);
        validator.setValidationListener(this);



    }

    /*FOR GOOGLE AUTH API*/
    private void GSignIn() {
        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(GoogleSignInAccount g_account) {
        pDialog.dismiss();

        if (g_account != null){
        gEmail = g_account.getEmail();
        gDisplayname = g_account.getDisplayName();
        gUri = g_account.getPhotoUrl();
        gId = g_account.getId();
        }else{
            Toast.makeText(SignInActivity.this," Sorry an error occurred, please try again",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
//        result retured from launching the intent from googlle signin
        if (requestCode == RC_SIGN_IN){
//            the task reurned from this call is always completed
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGsignInResult(task);
        }
    }

    private void handleGsignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUI(account);
        }catch (ApiException e){
            Log.w(TAG, "signInResult:failed code=" +e.getStatusCode());
            updateUI(null);
        }
    }
    /*FOR GOOGLE AUTH FUNCTIONS ENDS HERE*/

    @Override
    public void onValidationSucceeded() {
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();

        AuthUser();
//        if (JsonUtils.isNetworkAvailable(SignInActivity.this)) {
//            new MyTaskLogin().execute(Constant.LOGIN_URL + strEmail + "&password=" + strPassword);
//        } else {
//            Intent intent = new Intent(this , InternetActivity.class);
//            startActivity(intent);
//        }
    }

    private void AuthUser() {
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
        Call<String> call = ubuyapi.userAuth(strEmail, strPassword);
        Log.i("onEmptyResponse", "test network");//;





        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage("Please wait...");
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
                    parseAuthResponse(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(SignInActivity.this , InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseAuthResponse(String jsonresponse) {
        Log.i("onSuccess", "json gotten");


        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                if (objJson.has(Constant.MSG)) {
                    strMessage = objJson.getString(Constant.MSG);
                    Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                } else {
                    Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                    strPassengerId = objJson.getString(Constant.USER_ID);
//                            Log.i("DEBUG_USER_ID", "user id is"+ objJson.getString(Constant.USER_ID));
                    strFirstame = objJson.getString(Constant.FIRST_NAME);
                    strLastName = objJson.getString(Constant.LAST_NAME);
                    strMobile = objJson.getString(Constant.USER_PHONE);
                    strEmail = objJson.getString(Constant.USER_EMAIL);
                    strUserType = objJson.getString(Constant.USER_TYPE);
                    strUserEmailVerify   = objJson.getString(Constant.USER_EMAIL_VERIFY);
                    strUserPhoneVerify = objJson.getString(Constant.USER_PHONE_VERIFY);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResult();
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


    public void setResult() {

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<InstanceIdResult> task) {
//
//                    }
//
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
//                        Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });

        if (Constant.GET_SUCCESS_MSG == 0) {
            showToast("Opps. \n" + strMessage);

        } else {
            MyApp.saveIsLogin(true);
            MyApp.saveLogin(strPassengerId, strFcmToken, strFirstame, strLastName,  strEmail, strMobile, strUserType, strUserPhoneVerify, strUserEmailVerify);
            Intent i = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ExperienceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
