package com.ubuyng.app;
/*
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmActivity extends AppCompatActivity  {

    MyApplication App;
    String UserEmail, UserPhone, UserEmailConfirm, UserPhoneConfirm;
    TextView myEmail, edit_number;
    EditText myNumber;
    Button verify_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        App = MyApplication.getInstance();

        UserEmailConfirm = App.getEmailVerify();
        UserPhoneConfirm = App.getNumberVerify();
        UserEmail = App.getUserEmail();
        UserPhone = App.getUserMobile();

        myEmail = findViewById(R.id.myEmail);
        edit_number = findViewById(R.id.edit_number);
        myNumber = findViewById(R.id.myNumber);
        verify_btn = findViewById(R.id.verify_btn);

        myNumber.setText(UserPhone);
        myEmail.setText(UserEmail);


        Toast.makeText(this, "TAP ON IMAGES AND MENU ICONS", Toast.LENGTH_SHORT).show();

//        initToolbar();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }



}
