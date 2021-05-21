/*
 * Copyright (c) 2020. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.mini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class SettingsActivity extends AppCompatActivity  {
    Toolbar toolbar;
    MyApplication App;
    String strFullname, strFirst_name, strLast_name, strPhone_number, strEmail, strID, strUserType, strUserPhoneVerify, strUserEmailVerify, project_no, fav_pro;
    CircularImageView  user_image;
    ImageView upay_btn;
    EditText edit_firstname, edit_lastname, edit_number, edit_email, edit_oldpass, edit_newpass, edit_repeatpass;
    ShapeOfView deactivate_lyt, change_img_btn, update_info_btn, update_pass_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        App = MyApplication.getInstance();

//       finders
        edit_firstname = findViewById(R.id.edit_firstname);
        edit_lastname = findViewById(R.id.edit_lastname);
        edit_number = findViewById(R.id.edit_number);
        edit_email = findViewById(R.id.edit_email);
        edit_oldpass = findViewById(R.id.edit_oldpass);
        edit_newpass = findViewById(R.id.edit_newpass);
        edit_repeatpass = findViewById(R.id.edit_repeatpass);
//        images
        upay_btn = findViewById(R.id.upay_btn);
        user_image = findViewById(R.id.user_image);
//        shape of views
        deactivate_lyt = findViewById(R.id.deactivate_lyt);
        change_img_btn = findViewById(R.id.change_img_btn);
        update_info_btn = findViewById(R.id.update_info_btn);
        update_pass_btn = findViewById(R.id.update_pass_btn);

        edit_firstname.setText(App.getUserFirstName());
        edit_lastname.setText(App.getUserLastName());
        edit_email.setText(App.getUserEmail());
        edit_number.setText(App.getUserMobile());

        strID = App.getUserId();


        change_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        deactivate_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        update_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        update_pass_btn.setOnClickListener(new View.OnClickListener() {
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
