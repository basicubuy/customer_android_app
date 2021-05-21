package com.ubuyng.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ExperienceActivity extends AppCompatActivity {
    private Button user_type_pro, user_type_cus;
    private TextView user_type_htw;



    MyApplication App;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        App = MyApplication.getInstance();


        user_type_pro = findViewById(R.id.user_type_pro);
        user_type_cus = findViewById(R.id.user_type_cus);
        user_type_htw = findViewById(R.id.user_type_htw);
        // Checking for first time launch - before calling setContentView()
        launchHomeScreen();

        user_type_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        user_type_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserTypeProActivity.class);
                startActivity(intent);
                finish();
            }
        });

        user_type_htw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HTWActivity.class);
                startActivity(intent);
                finish();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }

    }

    private void launchHomeScreen() {
        if (App.getIsLogin()) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

//                App.getUserId();
//                if (App.getEmailVerify().equals("null")){
//                    Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                }else if (App.getEmailVerify().equals("null")){
//                    Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
//                }
        }

    }
}