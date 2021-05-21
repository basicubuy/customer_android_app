/*
 * Copyright (c) 2020. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.mini;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.florent37.shapeofview.ShapeOfView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ubuyng.app.AboutActivity;
import com.ubuyng.app.Dispute.DisputeResoulutionsActivity;
import com.ubuyng.app.MainActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.NotificationActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.fragments.DialogAcceptBidFragment;
import com.ubuyng.app.fragments.mini.DialogFeedbackFragment;
import com.ubuyng.app.ubuyapi.util.Tools;

public class ProfileActivity extends AppCompatActivity  {
    Toolbar toolbar;
    MyApplication App;
    String strFullname, strFirst_name, strLast_name, strPhone_number, strEmail, strID, strUserType, strUserPhoneVerify, strUserEmailVerify, project_no, fav_pro;
    CircularImageView  user_image;
    ImageView upay_btn;
    TextView username, user_email, user_balance;
    CardView reward_lyt,  settings_lyt, dispute_lyt, feedback_lyt, support_lyt, refer_lyt, faq_lyt, pro_lyt ;
    ShapeOfView logout_lyt;
    ImageView notification_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        App = MyApplication.getInstance();

//       finders
        username = findViewById(R.id.username);
        user_email = findViewById(R.id.user_email);
        user_balance = findViewById(R.id.user_balance);
//        images
        upay_btn = findViewById(R.id.upay_btn);
        user_image = findViewById(R.id.user_image);
//        cards
        reward_lyt = findViewById(R.id.reward_lyt);
        settings_lyt = findViewById(R.id.settings_lyt);
        dispute_lyt = findViewById(R.id.dispute_lyt);
        feedback_lyt = findViewById(R.id.feedback_lyt);
        support_lyt = findViewById(R.id.support_lyt);
        refer_lyt = findViewById(R.id.refer_lyt);
        faq_lyt = findViewById(R.id.faq_lyt);
        pro_lyt = findViewById(R.id.pro_lyt);
//        shape of views
        logout_lyt = findViewById(R.id.logout_lyt);

        strFullname = App.getUserFirstName() + " " + App.getUserLastName();
        username.setText(strFullname);
        strEmail = App.getUserEmail();
        user_email.setText(strEmail);
        strID = App.getUserId();

        notification_btn = findViewById(R.id.notification_btn);
        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
                startActivity(intent);
                finish();
            }
        });
        upay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpayActivity.class);
                startActivity(intent);
                finish();
            }
        });
        reward_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UrewardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        settings_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dispute_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, DisputeResoulutionsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        feedback_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog();
            }
        });
        support_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
            }
        });
        refer_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUbuy();
            }
        });
        faq_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        pro_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, BecomeProActivity.class);
                startActivity(intent);
                finish();
            }
        });
        logout_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        if (JsonUtils.isNetworkAvailable(ProfileActivity.this)) {
//            new MyProfile().execute(Constant.PROFILE_URL + strID);
//        } else {
//            Intent intent = new Intent(this , InternetActivity.class);
//            startActivity(intent);
//        }
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.textColorDark), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarLight(this);
    }

    private void showFeedbackDialog() {
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFeedbackFragment newFragment = new DialogFeedbackFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }


    //    share ubuy
    private void shareUbuy(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = "\n" + getResources().getString(R.string.share_msg) +  "\n" + getResources().getString(R.string.download_msg)
                    + "\n" + getString(R.string.app_name) + " "  + "http://play.google.com/store/apps/details?id=" + ProfileActivity.this.getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
        }

    }

//    private class MyProfile extends AsyncTask<String, Void, String> {
//
//        ProgressDialog pDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pDialog = new ProgressDialog(ProfileActivity.this);
//            pDialog.setMessage("Getting  your profile...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            return JsonUtils.getJSONString(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            if (null != pDialog && pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
//
//            if (null == result || result.length() == 0) {
//                showToast(getString(R.string.nodata));
//            } else {
//                try {
//                    JSONObject mainJson = new JSONObject(result);
//                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
//                    JSONObject objJson;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        objJson = jsonArray.getJSONObject(i);
//                        strFirst_name = objJson.getString(Constant.FIRST_NAME);
//                        strLast_name = objJson.getString(Constant.LAST_NAME);
//                        strEmail = objJson.getString(Constant.USER_EMAIL);
//                        strPhone_number = objJson.getString(Constant.USER_PHONE);
//                        strUserType = objJson.getString(Constant.USER_TYPE);
//                        strUserEmailVerify = objJson.getString(Constant.USER_EMAIL_VERIFY);
//                        strUserPhoneVerify = objJson.getString(Constant.USER_PHONE_VERIFY);
//                        project_no = objJson.getString(Constant.USER_ALL_PROJECT);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            display();
//        }
//    }

//    private void display() {
//
//        fullname.setText(strFirst_name + " " + strLast_name );
//        email.setText(strEmail);
////        phone_number.setText(strPhone_number);
////        all_project.setText(project_no);
//    }



}
