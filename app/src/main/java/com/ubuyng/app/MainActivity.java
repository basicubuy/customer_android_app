package com.ubuyng.app;
/*
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.ubuyng.app.fragments.FragmentHome;
import com.ubuyng.app.fragments.FragmentNotify;
import com.ubuyng.app.fragments.FragmentProfile;
import com.ubuyng.app.fragments.FragmentTaskTabs;
import com.ubuyng.app.mini.ProfileActivity;

public class MainActivity extends AppCompatActivity implements OSSubscriptionObserver {

    FragmentManager fragmentManager;
    private FrameLayout frameLayout;
    private FloatingActionButton fabAdd;
    private boolean isFabAttachedAtCenter;
    private LinearLayout lyt_not_found;
    private RelativeLayout overlay_loader;
    private LinearLayout search_btn_main, feed_lyt;
    private TextView hello_text;
    private Button btn_retry;
    NestedScrollView scrollview;
    private ShimmerFrameLayout mShimmerViewContainer;
    private FragmentHome fragment2;
    private FragmentTaskTabs fragment3;
    private FragmentNotify fragment4;
    private FragmentProfile fragment5;
    BottomNavigationView mBottom;
    MyApplication App;

    private ImageView notification_btn;

    private androidx.appcompat.widget.Toolbar mToolbar;
    private AppBarLayout appBarLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App = MyApplication.getInstance();


        hello_text = findViewById(R.id.hello_username);
        hello_text.setText(App.getUserFirstName());

        notification_btn = findViewById(R.id.notification_btn);
        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

       mBottom = findViewById(R.id.bottom_navigation);
        floatingActionButton = findViewById(R.id.post_task_fab);

//// FIXME: 20/01/2020  // OneSignal Initialization
        OneSignal.addSubscriptionObserver(this);

        OneSignal.startInit(this)
        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
        .unsubscribeWhenNotificationsAreDisabled(true)
        .init();
//        tool_name = findViewById(R.id.tool_name);
//        tool_image = findViewById(R.id.tool_image);

        fragment2 = new FragmentHome();
        fragment3 = new FragmentTaskTabs();
        fragment4 = new FragmentNotify();

        //default fragment that should be visible on open
        handleFragments(fragment2);
//        mappingToXml();
        //pass fragments that should be visible in following switch case

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PosterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        handleFragments(fragment2);

                        break;
                    case R.id.navigation_projects:
                        Intent projectIntent = new Intent(MainActivity.this, ProjectActivity.class);
                        projectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(projectIntent);


                        break;
                    case R.id.navigation_notify:
                        handleFragments(fragment4);
//                        Intent intenter = new Intent(MainActivity.this, NotificationActivity.class);
//                        intenter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intenter);
                        break;
                    case R.id.navigation_profile:
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void handleFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame1, fragment);
        fragmentTransaction.commit();
    }
//    share ubuy
    private void shareUbuy(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = "\n" + getResources().getString(R.string.share_msg) +  "\n" + getResources().getString(R.string.download_msg)
                    + "\n" + getString(R.string.app_name) + " "  + "http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
            new AlertDialog.Builder(this)
                    .setMessage("You've successfully subscribed to push notifications!")
                    .show();
            // get player ID
            stateChanges.getTo().getUserId();
        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

}
