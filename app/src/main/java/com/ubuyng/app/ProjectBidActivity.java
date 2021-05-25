package com.ubuyng.app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ubuyng.app.fragments.DialogAcceptBidFragment;
import com.ubuyng.app.fragments.DialogDeclineBidFragment;
import com.ubuyng.app.fragments.DialogPaymentSuccessFragment;
import com.ubuyng.app.fragments.FragmentTaskDetails;
import com.ubuyng.app.fragments.FragmentTaskTracker;
import com.ubuyng.app.ubuyapi.Interface.AdapBidsInterface;
import com.ubuyng.app.ubuyapi.Interface.AdapCatInterface;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.Models.ItemDropDown;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.Models.ItemSkill;
import com.ubuyng.app.ubuyapi.Models.ItemState;
import com.ubuyng.app.ubuyapi.adapters.BiddersAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProjectBidActivity extends AppCompatActivity implements AdapBidsInterface{
    BiddersAdapter rcAdapter;
    ArrayList<ItemBids> mBidders;
    ArrayList<String> Sskills = new ArrayList<String>();
    RecyclerView skills_rv, bids_rv;
    MyApplication App;
    String strProject_id, strProject_title, strSubCategoryName, strProject_brief, strProject_amount, strSubId;
    TextView project_name, project_brief, project_amount;
    RelativeLayout no_bids, overlay_loader;
    LinearLayout menu_edit_task, menu_delete_task, menu_timeline_task, menu_share_task, menu_support_task;
    ImageView toggle_nav_menu;
    ShapeOfView top_nav_menu;
    private ChipGroup skills_chip_grp;
    Integer menu_toggle = 0;
    CoordinatorLayout main_lyt;
//
    private AdapBidsInterface AdapBidsInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        /*get the bidders array list*/
        mBidders = new ArrayList<>();
        initToolbar();
        /*get the following details from the intent called*/
        Intent intent = getIntent();
        strProject_id = intent.getStringExtra("project_id");
        strProject_title = intent.getStringExtra("project_title");
        strProject_brief = intent.getStringExtra("project_brief");
        strProject_amount = intent.getStringExtra("project_budget");



        /*get the user details*/
        App = MyApplication.getInstance();

        /*finder called here*/
        project_name = findViewById(R.id.project_name);
//        project_name.setText(strProject_title);

        project_brief = findViewById(R.id.project_brief);
        project_brief.setText(strProject_brief);

        project_amount = findViewById(R.id.project_amount);
//        project_amount.setText(strSubCategoryName);

//        chips
        skills_chip_grp = findViewById(R.id.skills_chip_grp);


        /*toogle switcher class*/
        toggle_nav_menu = findViewById(R.id.toggle_nav_menu);
        top_nav_menu = findViewById(R.id.top_nav_menu);
        main_lyt = findViewById(R.id.main_lyt);
        toggle_nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toogleSwitcher();

            }
        });
        main_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toogleSwitcher();

            }
        });

        /*finder for menu items*/
        menu_edit_task = findViewById(R.id.menu_edit_task);
        menu_delete_task = findViewById(R.id.menu_delete_task);
        menu_timeline_task = findViewById(R.id.menu_timeline_task);
        menu_share_task = findViewById(R.id.menu_share_task);
        menu_support_task = findViewById(R.id.menu_support_task);


        /*for loader animation*/
        overlay_loader = findViewById(R.id.overlay_loader);

        /* connect to server and get bids for the project*/
        if (JsonUtils.isNetworkAvailable(this)) {
           loadProject();
        } else{
            intent = new Intent(ProjectBidActivity.this , InternetActivity.class);
            startActivity(intent);

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

         bids_rv = (RecyclerView) findViewById(R.id.bids_rv);
        bids_rv.setHasFixedSize(false);
        bids_rv.setLayoutManager(layoutManager);
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

    @Override
    public void onAcceptClick( String bidID, String BidderId){
        showDialogAccept(bidID, BidderId);

    }
  @Override
    public void onDeclineClick( String bidId, String Bidderid){
      Toast.makeText(this,"popup decline",Toast.LENGTH_LONG).show();
      showDialogDeclined(bidId, Bidderid);
    }

    private void showDialogAccept(String bidId, String Bidderid) {
        Bundle bundle = new Bundle();
        bundle.putString("bid_id", bidId);
        bundle.putString("bidder_id", Bidderid);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogAcceptBidFragment newFragment = new DialogAcceptBidFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }
    private void showDialogDeclined(String bidId, String Bidderid) {
        Bundle bundle = new Bundle();
        bundle.putString("bid_id", bidId);
        bundle.putString("bidder_id", Bidderid);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogDeclineBidFragment newFragment = new DialogDeclineBidFragment();
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    private void toogleSwitcher() {
        switch (menu_toggle){
            case 0 :
                top_nav_menu.setVisibility(View.VISIBLE);
                menu_toggle = 1;
                break;
            case 1 :
                toggle_nav_menu.setVisibility(View.GONE);
                menu_toggle = 0;
                break;
        }
    }


    private void loadProject() {
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
        Call<String> call = ubuyapi.getProjectBids(strProject_id);
        startAnim();
        Log.i("onEmptyResponse", "test network");
        //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                stopAnim();
                HttpUrl main_url = response.raw().request().url();
                Log.i("Details", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());
//                    project_amount.setText();
                    String jsonresponse = response.body().toString();
                    getBidsJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                    finding_bids.setVisibility(View.INVISIBLE);
//                    no_bids.setVisibility(View.VISIBLE);
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(ProjectBidActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");
                //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getBidsJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);
//           Cat model
            JSONArray jsonCat = jsonArray.getJSONArray(Constant.SKILL_HEADER);
            JSONObject objJsonCat;
            for (int i = 0; i < jsonCat.length(); i++) {
                objJsonCat = jsonCat.getJSONObject(i);
                Sskills.add(objJsonCat.getString(Constant.SKILL_TITLE));
                setSkillsChip(Sskills);
            }
//           state model
            JSONArray jsonState = jsonArray.getJSONArray(Constant.BID_HEADER);
            JSONObject objjsonState;
            for (int i = 0; i < jsonState.length(); i++) {
                objjsonState = jsonState.getJSONObject(i);
                ItemBids objItem = new ItemBids();
                objItem.setBidId(objjsonState.getString(Constant.BID_ID));
                objItem.setBidderId(objjsonState.getString(Constant.BID_PRO_ID));
                objItem.setBidderImage(objjsonState.getString(Constant.BIDDER_IMAGE));
                objItem.setBidderName(objjsonState.getString(Constant.PRO_NAME));
                objItem.setBidMessage(objjsonState.getString(Constant.BID_MESSAGE));
                objItem.setBidAmount(objjsonState.getString(Constant.BID_AMOUNT));
                objItem.setProjectId(objjsonState.getString(Constant.PROJECT_ID));
                objItem.setBidStatus(objjsonState.getString(Constant.BID_STATUS));
                objItem.setBidDate(objjsonState.getString(Constant.BID_DATE));
                objItem.setBidType(objjsonState.getString(Constant.BID_TYPE));
                objItem.setBidMaterialFee(objjsonState.getString(Constant.BID_MATERIAL_FEE));
                objItem.setBidServiceFee(objjsonState.getString(Constant.BID_SERVICE_FEE));

                mBidders.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }

    public void setSkillsChip(ArrayList<String> Sskills){
        for (String skill : Sskills){
            Chip mChip =(Chip) this.getLayoutInflater().inflate(R.layout.item_chip_skill, null, false);
            mChip.setText(skill);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            mChip.setPadding(paddingDp, 0, paddingDp, 0);
//            mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                    QDeleteSkill(mChip.getText().toString());
//                    skills_chip_grp.removeView(mChip);
//                }
//            });
            skills_chip_grp.addView(mChip);
        }
    }


    private void displayData() {
        rcAdapter = new BiddersAdapter(ProjectBidActivity.this, mBidders, this);
        bids_rv.setAdapter(rcAdapter);
        bids_rv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    private void startAnim(){
        overlay_loader.setVisibility(View.VISIBLE);
//        no_projects_found.setVisibility(View.GONE);
    }

    private void stopAnim(){

        overlay_loader.setVisibility(View.GONE);

    }


//    private void showDetailsDialog() {
//        Bundle bundle = new Bundle();
//        bundle.putString("taskName", project_title);
//        bundle.putString("taskDes", project_brief);
//        Log.i("brief_task", "task details is clicked");
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTaskDetails customDialogFragment = new FragmentTaskDetails();
//        customDialogFragment.setArguments(bundle);
//
//
//        if (mIsLarge) {
//            customDialogFragment.show(fragmentManager, "dialog");
//        } else {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//
//            fragmentTransaction.add(android.R.id.content, customDialogFragment)
//                    .addToBackStack(null)
//                    .commit();
//        }
//
//    }
//    private void showTrackerDialog() {
//        Bundle bundle = new Bundle();
//        bundle.putString("taskName", project_title);
//        bundle.putString("task_id", project_id);
//        Log.i("brief_task", "task details is clicked");
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTaskTracker customTrackerDialog = new FragmentTaskTracker();
//        customTrackerDialog.setArguments(bundle);
//
//
//        if (mIsLarge) {
//            customTrackerDialog.show(fragmentManager, "dialog");
//        } else {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//
//            fragmentTransaction.add(android.R.id.content, customTrackerDialog)
//                    .addToBackStack(null)
//                    .commit();
//        }
//
//    }
//
//    private void showBottomSheetDialog() {
//        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
//
//        final View view = getLayoutInflater().inflate(R.layout.sheet_update_task, null);
//        (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
////        (view.findViewById(R.id.bt_details)).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(getApplicationContext(), "Details clicked", Toast.LENGTH_SHORT).show();
////            }
////        });
//
//        mBottomSheetDialog = new BottomSheetDialog(this);
//        mBottomSheetDialog.setContentView(view);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//
//        mBottomSheetDialog.show();
//        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mBottomSheetDialog = null;
//            }
//        });
//    }

}
