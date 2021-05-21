package com.ubuyng.app;
/*
 * All content and codes belong to Andrew Ben Richard
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ubuyng.app.Chat.ChatterActivity;
import com.ubuyng.app.fragments.FragmentSixBottomSheet;
import com.ubuyng.app.mini.ProfileActivity;
import com.ubuyng.app.ubuyapi.Models.ItemCat;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;
import com.ubuyng.app.ubuyapi.adapters.HomeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.HomeTopProsAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class MainDebugActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener  {

    private TextView mTextMessage;
    FragmentManager fragmentManager;
    private FrameLayout frameLayout;
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabAdd;
    private FragmentSixBottomSheet fragmentSixBottomSheet;
    private boolean isFabAttachedAtCenter;
    ArrayList<ItemCat> mCat;
    ArrayList<ItemSubcat> mRecommend, mDesign, mPersonal, mBusiness, mHome;
    public RecyclerView recommend_rv, cat_rv, design_rv, personal_rv, business_rv,  home_rv;
    AVLoadingIndicatorView avi;
    HomeTopProsAdapter mRecommendAdapter, mDesignAdapter, mPersonalAdapter, mBusinessAdapter, mHomeAdapter;
    HomeCatAdapter mCatAdapter;
    private LinearLayout lyt_not_found;
    private RelativeLayout overlay_loader;
    private LinearLayout search_btn_main, feed_lyt;
    private TextView loading_text;
    private Button btn_retry;
    NestedScrollView scrollview;
    private ShimmerFrameLayout mShimmerViewContainer;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

//        searchView = findViewById(R.id.search_main);

        bottomAppBar = findViewById(R.id.bar);
        setSupportActionBar(bottomAppBar);

        feed_lyt = findViewById(R.id.feed_lyt);

        fabAdd = findViewById(R.id.fab);
        fragmentSixBottomSheet = new FragmentSixBottomSheet();
//        appBarAdapter = new AppBarAdapter(RecyclerData.recyclerViewData());
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(appBarAdapter);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDebugActivity.this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        initToolbar();

        mCat = new ArrayList<>();
        mRecommend = new ArrayList<>();
        mDesign = new ArrayList<>();
        mPersonal = new ArrayList<>();
        mBusiness = new ArrayList<>();
        mHome = new ArrayList<>();

//        search_btn_main = findViewById(R.id.search_btn_main);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        overlay_loader = findViewById(R.id.overlay_loader);
        avi = findViewById(R.id.avi);
        loading_text = findViewById(R.id.loading_text);


        btn_retry = findViewById(R.id.btn_retry);
        scrollview = findViewById(R.id.scrollview);

        cat_rv = findViewById(R.id.category_rv);
        cat_rv.setLayoutManager(new LinearLayoutManager(MainDebugActivity.this, LinearLayoutManager.HORIZONTAL, false));
        cat_rv.setHasFixedSize(true);

        home_rv = findViewById(R.id.home_rv);
        home_rv.setLayoutManager(new LinearLayoutManager(MainDebugActivity.this, LinearLayoutManager.HORIZONTAL, false));
        home_rv.setHasFixedSize(true);

        design_rv = findViewById(R.id.design_rv);
        design_rv.setLayoutManager(new LinearLayoutManager(MainDebugActivity.this, LinearLayoutManager.HORIZONTAL, false));
        design_rv.setHasFixedSize(true);

        business_rv = findViewById(R.id.business_rv);
        business_rv.setLayoutManager(new LinearLayoutManager(MainDebugActivity.this, LinearLayoutManager.HORIZONTAL, false));
        business_rv.setHasFixedSize(true);

        recommend_rv = findViewById(R.id.recommend_rv);
        recommend_rv.setLayoutManager(new LinearLayoutManager(MainDebugActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recommend_rv.setHasFixedSize(true);

        personal_rv = findViewById(R.id.personal_rv);
        personal_rv.setLayoutManager(new LinearLayoutManager(MainDebugActivity.this, LinearLayoutManager.HORIZONTAL, false));
        personal_rv.setHasFixedSize(true);


//        btn_retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (JsonUtils.isNetworkAvailable(MainActivity.this)) {
//                    new Home().execute(Constant.HOME_FEEDS);
//                } else{
////            TODO: DEVELOP INTERNET ERROR ACTIVITY
//                    lyt_not_found.setVisibility(View.VISIBLE);
//
//                }            }
//        });
//
//        btn_retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
        // create a function for the first load


    }


    private void startAnim(){
//        avi.show();
        avi.smoothToShow();
        overlay_loader.setVisibility(View.VISIBLE);
        lyt_not_found.setVisibility(View.GONE);
    }

    private void stopAnim(){
//        avi.hide();
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
        feed_lyt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_post) {
            // Handle the add project fuction
            Intent intent = new Intent(MainDebugActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        else if (id == R.id.navigation_projects) {
            // Handle to show all users projects
            Intent intent = new Intent(MainDebugActivity.this, ProjectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        else if (id == R.id.navigation_inbox) {
            // Handle to show the user inbox
            Intent intent = new Intent(MainDebugActivity.this, ChatterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        else if (id == R.id.navigation_profile) {
            // Handle to show the user profile
            Intent intent = new Intent(MainDebugActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        else if (id == R.id.navigation_locations) {
            // Handle to show the user address
            Intent intent = new Intent(MainDebugActivity.this, myAddressActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        else if (id == R.id.navigation_refer) {
            // Handle the referal share function
            shareUbuy();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    share ubuy
    private void shareUbuy(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String sAux = "\n" + getResources().getString(R.string.share_msg) +  "\n" + getResources().getString(R.string.download_msg)
                    + "\n" + getString(R.string.app_name) + " "  + "http://play.google.com/store/apps/details?id=" + MainDebugActivity.this.getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
        }

    }
}
