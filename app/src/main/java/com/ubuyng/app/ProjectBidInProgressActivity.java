package com.ubuyng.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.chip.ChipGroup;
import com.ubuyng.app.Dispute.DisputeResoulutionsActivity;
import com.ubuyng.app.ubuyapi.Interface.AdapBidsInterface;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.adapters.BiddersAdapter;

import java.util.ArrayList;

public class ProjectBidInProgressActivity extends AppCompatActivity{
    BiddersAdapter rcAdapter;
    ArrayList<ItemBids> mBidders;
    ArrayList<String> Sskills = new ArrayList<String>();
    RecyclerView skills_rv, bids_rv;
    MyApplication App;
    String strProject_id, strProject_title, strProject_brief, strProject_amount, strSubId, strProjectVersion;
    TextView project_name, project_brief, project_amount;
    RelativeLayout no_bids, overlay_loader;
    LinearLayout safety_toolkit_lyt, dispute_lyt, menu_edit_task, menu_delete_task, menu_timeline_task, menu_share_task, menu_support_task;
    ImageView toggle_nav_menu;
    ShapeOfView top_nav_menu;
    Integer menu_toggle = 0;
    CoordinatorLayout main_lyt;
//
    private AdapBidsInterface AdapBidsInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details_inprogress);
        /*get the bidders array list*/
        mBidders = new ArrayList<>();

        /*get the following details from the intent called*/
        Intent intent = getIntent();
        strProject_id = intent.getStringExtra("project_id");
        strProject_title = intent.getStringExtra("project_title");
        strProject_brief = intent.getStringExtra("project_brief");
        strProject_amount = intent.getStringExtra("project_budget");
        strSubId = intent.getStringExtra("project_sub_id");
        strProjectVersion = intent.getStringExtra("project_version");

        /*get the user details*/
        App = MyApplication.getInstance();

        /*finder called here*/
        project_name = findViewById(R.id.project_name);
        project_name.setText(strProject_title);

        project_brief = findViewById(R.id.project_brief);
        project_brief.setText(strProject_brief);

        project_amount = findViewById(R.id.project_amount);
        if (strProjectVersion.equals(String.valueOf(strProjectVersion))){
            project_amount.setVisibility(View.GONE);
        }
        project_amount.setText(strProject_amount);


        /*for loader animation*/
        overlay_loader = findViewById(R.id.overlay_loader);
        safety_toolkit_lyt = findViewById(R.id.safety_toolkit_lyt);
        safety_toolkit_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SafetyIntent = new Intent(ProjectBidInProgressActivity.this, SafetyToolKitActivity.class);
                startActivity(SafetyIntent);
            }
        });


        dispute_lyt = findViewById(R.id.dispute_lyt);
        dispute_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DisputeIntent = new Intent(ProjectBidInProgressActivity.this, DisputeResoulutionsActivity.class);
                startActivity(DisputeIntent);
            }
        });

        /* connect to server and get bids for the project*/
//        if (JsonUtils.isNetworkAvailable(this)) {
//           loadProject();
//        } else{
//            intent = new Intent(ProjectBidInProgressActivity.this , InternetActivity.class);
//            startActivity(intent);
//
//        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

    }


//    private void loadProject() {
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(logging)
//                .build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//
//        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
//        Call<String> call = ubuyapi.getProjectBids(strProject_id, strProjectVersion);
//        startAnim();
//        Log.i("onEmptyResponse", "test network");
//        //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//        call.enqueue(new Callback<String>() {
//
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                stopAnim();
//                HttpUrl main_url = response.raw().request().url();
//                Log.i("debugger", String.valueOf(main_url));
//                if (response.body() != null) {
//                    Log.i("onSuccess", response.body().toString());
//
//                    String jsonresponse = response.body().toString();
//                    getBidsJson(jsonresponse);
//
//                } else {
//                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
////                    finding_bids.setVisibility(View.INVISIBLE);
////                    no_bids.setVisibility(View.VISIBLE);
//                }
//                if (response.isSuccessful()){
//                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Intent intent = new Intent(ProjectBidInProgressActivity.this, InternetActivity.class);
//                startActivity(intent);
//                Log.i("onEmptyResponse", "error occured");
//                //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//
//    private void getBidsJson(String jsonresponse) {
//        try {
//            JSONObject mainJson = new JSONObject(jsonresponse);
//            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);
////           Cat model
//            JSONArray jsonCat = jsonArray.getJSONArray(Constant.SKILL_HEADER);
//            JSONObject objJsonCat;
//            for (int i = 0; i < jsonCat.length(); i++) {
//                objJsonCat = jsonCat.getJSONObject(i);
//                Sskills.add(objJsonCat.getString(Constant.SKILL_TITLE));
//                setSkillsChip(Sskills);
//            }
////           state model
//            JSONArray jsonState = jsonArray.getJSONArray(Constant.BID_HEADER);
//            JSONObject objjsonState;
//            for (int i = 0; i < jsonState.length(); i++) {
//                objjsonState = jsonState.getJSONObject(i);
//                ItemBids objItem = new ItemBids();
//                objItem.setBidId(objjsonState.getString(Constant.BID_ID));
//                objItem.setBidderId(objjsonState.getString(Constant.BID_PRO_ID));
//                objItem.setBidderImage(objjsonState.getString(Constant.BIDDER_IMAGE));
//                objItem.setBidderName(objjsonState.getString(Constant.PRO_NAME));
//                objItem.setBidMessage(objjsonState.getString(Constant.BID_MESSAGE));
//                objItem.setBidAmount(objjsonState.getString(Constant.BID_AMOUNT));
//                objItem.setProjectId(objjsonState.getString(Constant.PROJECT_ID));
//                objItem.setBidStatus(objjsonState.getString(Constant.BID_STATUS));
//                objItem.setBidDate(objjsonState.getString(Constant.BID_DATE));
//                objItem.setBidType(objjsonState.getString(Constant.BID_TYPE));
//                objItem.setBidMaterialFee(objjsonState.getString(Constant.BID_MATERIAL_FEE));
//                objItem.setBidServiceFee(objjsonState.getString(Constant.BID_SERVICE_FEE));
//                objItem.setBidVersion(objjsonState.getString(Constant.SIMPLE_VERSION_BREAKER));
//                mBidders.add(objItem);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        displayData();
//    }

//    private void displayData() {
//        rcAdapter = new BiddersAdapter(ProjectBidInProgressActivity.this, mBidders, this);
//        bids_rv.setAdapter(rcAdapter);
//        bids_rv.setVisibility(View.VISIBLE);
//    }

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


}
