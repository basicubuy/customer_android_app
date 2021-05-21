/*
 * Copyright (c) 2020. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.mini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ubuyng.app.AboutActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;

public class UrewardActivity extends AppCompatActivity  {
    Toolbar toolbar;
    MyApplication App;
    TextView user_points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ureward);

        App = MyApplication.getInstance();

//       finders
        user_points = findViewById(R.id.user_points);

//        if (JsonUtils.isNetworkAvailable(ProfileActivity.this)) {
//            new MyProfile().execute(Constant.PROFILE_URL + strID);
//        } else {
//            Intent intent = new Intent(this , InternetActivity.class);
//            startActivity(intent);
//        }

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
