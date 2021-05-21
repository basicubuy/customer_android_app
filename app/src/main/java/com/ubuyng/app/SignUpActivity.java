package com.ubuyng.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

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

    @Required(order = 8)
    @Password(order = 9, message = "Enter a Valid Password")
    @TextRule(order = 10, minLength = 6, message = "Enter a Password Correctly")
    EditText password;

    MyApplication MyApp;


    Button btnSignUp, auth_google_btn;
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "googleauth_checker";
    String gEmail, gId, gDisplayname, strFirst_name, strFcmToken, success_code, strLast_name, strEmail, strPassword, strMobi, strMessage, strPassengerId, strUserType, strUserPhoneVerify, strUserEmailVerify;
    Uri gUri;
    private Validator validator;

    TextView txtLogin;
    JsonUtils jsonUtils;
    public static final String mypreference = "mypref";
    public static final String pref_email = "pref_email";
    public static final String pref_password = "pref_password";
    public static final String pref_check = "pref_check";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    //    for google auth
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount g_account;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
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
        jsonUtils = new JsonUtils(this);
        pref = getSharedPreferences(mypreference, 0); // 0 - for private mode
        editor = pref.edit();
        MyApp = MyApplication.getInstance();

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone_number = findViewById(R.id.phone_number);
        btnSignUp = findViewById(R.id.btnSingUp);
        txtLogin = findViewById(R.id.txtLogin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FixMe change this to validator after validateAsync
//                validator.validateAsync();
                Intent intent = new Intent(getApplicationContext(), UserOTPActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    /*FOR GOOGLE AUTH API*/
    private void GSignIn() {

        pDialog = new ProgressDialog(SignUpActivity.this);
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
//            TODO:: SEND THIS DATA TO THE BACKEND
        }else{
            Toast.makeText(SignUpActivity.this," Sorry an error occurred, please try again",Toast.LENGTH_LONG).show();



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
        strFirst_name = first_name.getText().toString();
        strLast_name = last_name.getText().toString();
        strEmail = email.getText().toString();
        strPassword = password.getText().toString();
        strMobi = phone_number.getText().toString();
//
//        if (JsonUtils.isNetworkAvailable(SignUpActivity.this)) {
//            new MyTaskRegister().execute(Constant.REGISTER_URL + strFirst_name + "&last_name="+ strLast_name + "&email=" + strEmail + "&password=" + strPassword + "&phone=" + strMobi);
//        } else {
//            Intent intent = new Intent(this , InternetActivity.class);
//            startActivity(intent);
//        }

        RegisterUser();
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


    private void RegisterUser() {
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
        Call<String> call = ubuyapi.createProfile(strFirst_name, strLast_name, strEmail, strPassword, strMobi);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();





        pDialog = new ProgressDialog(SignUpActivity.this);
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
                Intent intent = new Intent(SignUpActivity.this , InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parseRegisterResponse(String jsonresponse) {
        Log.i("onSuccess", "json gotten");


        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME_V1);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                strMessage = objJson.getString(Constant.MSG);
                strFirst_name = objJson.getString(Constant.FIRST_NAME);
                strLast_name = objJson.getString(Constant.LAST_NAME);
                strEmail = objJson.getString(Constant.USER_EMAIL);
                strMobi = objJson.getString(Constant.USER_PHONE);
                strPassengerId = objJson.getString(Constant.USER_ID);
                strUserType = objJson.getString(Constant.USER_TYPE);
                strUserEmailVerify = objJson.getString(Constant.USER_EMAIL_VERIFY);
                strUserPhoneVerify = objJson.getString(Constant.USER_PHONE_VERIFY);
                success_code = objJson.getString(Constant.SUCCESS);
                Log.i("REGISTER_CALLBACK", strMessage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResult();

    }



    public void setResult() {

        if (success_code.equals("0")) {
            showToast(strMessage);
        } else {
            showToast(strMessage);
            MyApp.saveIsLogin(true);
            MyApp.saveLogin(strPassengerId, strFcmToken, strFirst_name, strLast_name,  strEmail, strMobi, strUserType, strUserPhoneVerify, strUserEmailVerify);
            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(i);
            finish();
//            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ExperienceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
