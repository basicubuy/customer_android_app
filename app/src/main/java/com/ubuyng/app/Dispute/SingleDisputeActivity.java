package com.ubuyng.app.Dispute;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Interface.AdapDisputeInterface;
import com.ubuyng.app.ubuyapi.Models.ItemDispute;
import com.ubuyng.app.ubuyapi.Models.ItemDisputeCat;
import com.ubuyng.app.ubuyapi.Models.ItemDisputeTask;
import com.ubuyng.app.ubuyapi.adapters.disputes.DisputeCatAdapter;
import com.ubuyng.app.ubuyapi.adapters.disputes.DisputeTaskAdapter;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.Tools;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SingleDisputeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    ArrayList<ItemDispute> mDisputes;
    private DisputeCatAdapter mAdapterDispute;
    private EditText editText_msg;
    private TextView task_ref, dispute_status, dispute_task, details_txt, pro_name, pro_skill, total_jobs;
    private ShapeOfView send_sh;
    private AVLoadingIndicatorView avisave_draft;
    private Integer saving_checker;
    private ImageView pro_image;
    private LinearLayout cat_li_lyt, task_li_lyt;
    private NestedScrollView scroll;
    private String user_id, str_disputeId, str_taskId, str_task, str_CatID, str_disputeCat, str_disputeDate, str_disputeStatus, str_disputeRef;
    MyApplication App;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_dispute);

        mDisputes = new ArrayList<>();
        App = MyApplication.getInstance();
        user_id = App.getUserId();

        /*get the following details from the intent called*/
        Intent intent = getIntent();
        str_disputeId = intent.getStringExtra("dispute_id");
        str_disputeCat = intent.getStringExtra("dispute_cat");
        str_disputeDate = intent.getStringExtra("dispute_date");
        str_disputeStatus = intent.getStringExtra("dispute_status");
        str_task = intent.getStringExtra("dispute_task");
        str_disputeRef = intent.getStringExtra("dispute_ref");


        initToolbar();

        task_ref = findViewById(R.id.task_ref);
        dispute_status = findViewById(R.id.dispute_status);
        dispute_task = findViewById(R.id.dispute_task);
        details_txt = findViewById(R.id.details_txt);
        pro_name = findViewById(R.id.pro_name);
        pro_skill = findViewById(R.id.pro_skill);
        total_jobs = findViewById(R.id.total_jobs);

//        now we add the data
        task_ref.setText(str_disputeRef);
        if (str_disputeStatus.equals("0")){
        dispute_status.setText("Open");
            dispute_status.setTextColor(getResources().getColor(R.color.warning_notify));
        }else{
        dispute_status.setText("Resolved");
            dispute_status.setTextColor(getResources().getColor(R.color.success_notify));

        }
        dispute_task.setText(str_task);
//        todo:::  details_txt.setText(str_task);



        recyclerView = findViewById(R.id.tasks_rv);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        saving_checker = null;



//        send_sh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               str_Dispute = editText_msg.getText().toString();
////                QValidator();
//            }
//        });





//        attach_sh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
//            }
//        });
    }

//    private void QValidator() {
//
//        if (str_CatName == null){
//            cat_li_lyt.setBackground(ContextCompat.getDrawable(SingleDisputeActivity.this, R.drawable.edit_text_select_error_bg_outline));
//            select_cat_text.setTextColor(getResources().getColor(R.color.error_form));
//            select_cat_drop.setColorFilter(ContextCompat.getColor(this, R.color.error_form));
//            cat_error_txt.setVisibility(View.VISIBLE);
//        }
//
//        if (str_taskName == null){
//            task_li_lyt.setBackground(ContextCompat.getDrawable(SingleDisputeActivity.this, R.drawable.edit_text_select_error_bg_outline));
//            select_task_text.setTextColor(getResources().getColor(R.color.error_form));
//            select_task_drop.setColorFilter(ContextCompat.getColor(this, R.color.error_form));
//            task_error_txt.setVisibility(View.VISIBLE);
//        }
//        if (str_Dispute.trim().length() == 0 || str_Dispute.trim().length() <= 6 &&  str_CatName.trim().length() == 0 && str_taskName.trim().length() == 0 ) {
//            dispute_error_txt.setVisibility(View.VISIBLE);
//            editText_msg.setBackground(ContextCompat.getDrawable(SingleDisputeActivity.this, R.drawable.edit_text_error_bg_outline));
//        }else{
//            postDispute();
//        }
//    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.textColorDark), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarLight(this);
    }




    private void parseStatusResponse(String jsonresponse) {
    }

//
//    private void displayData() {
//        cats_rv.setVisibility(View.VISIBLE);
//        mAdapterDispute = new DisputeCatAdapter(SingleDisputeActivity.this, mDisputes, this);
//        cats_rv.setAdapter(mAdapterDispute);
//
//        tasks_rv.setVisibility(View.VISIBLE);
//        mAdapterTasks = new DisputeTaskAdapter(SingleDisputeActivity.this, mTasks, this);
//        tasks_rv.setAdapter(mAdapterTasks);
//    }

}
