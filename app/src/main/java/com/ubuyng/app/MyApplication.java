package com.ubuyng.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import com.uxcam.UXCam;

//import com.example.util.TypefaceUtil;
//import com.onesignal.OSNotificationOpenResult;
//import com.onesignal.OneSignal;


public class MyApplication extends Application {

    private static MyApplication mInstance;
    public SharedPreferences preferences, LivesPref;
    public String prefName = "UbuyNG";

    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Sentry.init("https://949bba361bca4218ab8a63c3fb6d1b6f@sentry.io/1778737", new AndroidSentryClientFactory(this));
        UXCam.startWithKey("utjzss3yxn9nlkz");

//        OneSignal.startInit(this)
//                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .init();
//        mInstance = this;
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/custom.ttf");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void saveIsNotification(boolean flag) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IsNotification", flag);
        editor.apply();
    }
    public boolean getNotification() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getBoolean("IsNotification", true);
    }

    public void saveIsLogin(boolean flag) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IsLoggedIn", flag);
        editor.apply();
    }

    public boolean getIsLogin() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getBoolean("IsLoggedIn", false);
    }

    public void saveLogin(String user_id, String fcm_token, String first_name,  String last_name, String email, String mobile, String user_type, String number_verify, String email_verify) {
        preferences = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("fcm_token", fcm_token);
        editor.putString("first_name", first_name);
        editor.putString("last_name", last_name);
        editor.putString("email", email);
        editor.putString("mobile", mobile);
        editor.putString("user_type", user_type);
        editor.putString("number_verify", number_verify);
        editor.putString("email_verify", email_verify);
        editor.apply();
    }

    public void SaveLives(int user_lives) {
        LivesPref = this.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = LivesPref.edit();
        editor.putInt("user_lives", user_lives);
        editor.apply();
    }


    public String getUserType() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("user_type", "");
    }

    public String getUserId() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("user_id", "");
    }

    public String getUserFirstName() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("first_name", "");
    }

    public String getFcmToken() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("fcm_token", "");
    }
    public String getUserLastName() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("last_name", "");
    }

    public String getUserEmail() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("email", "");
    }
    public String getUserMobile() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("mobile", "");
    }
    public String getNumberVerify() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("number_verify", "");
    }
    public String getEmailVerify() {
        preferences = this.getSharedPreferences(prefName, 0);
        return preferences.getString("email_verify", "");
    }

//    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
//        @Override
//        public void notificationOpened(OSNotificationOpenResult result) {
//            JSONObject data = result.notification.payload.additionalData;
//            String customKey;
//            String isExternalLink;
//            if (data != null) {
//                customKey = data.optString("property_id", null);
//                isExternalLink = data.optString("external_link", null);
//                if (customKey != null) {
//                    if (!customKey.equals("0")) {
//                        Intent intent = new Intent(MyApplication.this, PropertyDetailsActivity.class);
//                        intent.putExtra("Id", customKey);
//                        intent.putExtra("isNotification", true);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    } else {
//                        if (!isExternalLink.equals("false")) {
//                            Intent intent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(isExternalLink));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(MyApplication.this, SplashActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    }
//                }
//            }
//        }
//    }
}