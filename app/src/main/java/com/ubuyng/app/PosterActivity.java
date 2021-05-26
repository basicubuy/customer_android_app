package com.ubuyng.app;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ubuyng.app.Responses.TaskResponse;
import com.ubuyng.app.ubuyapi.Interface.AdapCatInterface;
import com.ubuyng.app.ubuyapi.Models.ItemDropDown;
import com.ubuyng.app.ubuyapi.Models.ItemDropSub;
import com.ubuyng.app.ubuyapi.Models.ItemSkill;
import com.ubuyng.app.ubuyapi.Models.ItemState;
import com.ubuyng.app.ubuyapi.Models.TaskRequest;
import com.ubuyng.app.ubuyapi.adapters.projects.ListDropdownAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.SkillDropdownAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.StateDropdownAdapter;
import com.ubuyng.app.ubuyapi.adapters.projects.SubDropdownAdapter;
import com.ubuyng.app.ubuyapi.retrofit.ApiClient;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//google places


public class PosterActivity extends AppCompatActivity implements AdapCatInterface {

    private Toolbar toolbar;
    ArrayList<ItemDropDown> mDropDown;
    ArrayList<ItemDropSub> mSubsDropDown;
    ArrayList<ItemSkill> mSkills;
    ArrayList<ItemState> mState;
    private RecyclerView cats_rv, subcats_rv, skills_rv, state_rv;
    private ListDropdownAdapter mAdapterList;
    private StateDropdownAdapter mStateAdapter;
    private SubDropdownAdapter mSubAdapterList;
    private SkillDropdownAdapter mSkillAdapter;
    private ShapeOfView cats_view_lyt, cats_sh, subCats_sh, sub_cats_view_lyt, sub_skills_view_lyt, subSkill_sh, state_sh, state_view_lyt, post_task_sh;
    private AdapCatInterface AdapCatInterface;
    private RelativeLayout nested_scroller;
    private EditText editText_title, editText_des, editText_lga, editText_address, editText_budget;
    private TextView task_sub_title, select_cat_txt, select_sub_text, select_state_txt, sub_cat_error_txt, budget_error_txt, title_error_txt, cat_error_txt, lga_error_txt, address_error_txt, state_error_txt, draft_post_txt;
    private ImageView cat_dropdown_img, select_sub_drop, state_dropdown_img;
    private ChipGroup skills_chip_grp;
    private LinearLayout post_lyt, cat_li_lyt, subcat_li_lyt, skill_li_lyt, state_li_lyt, location_lyt, address_lyt;
    private String intentSubId, intentSubName, intentProName, user_id, str_saveStatus,strTask_id, str_taskTitle, str_taskDes, str_taskBudget, str_taskCategory, str_taskCategoryId, str_taskSubCategory, str_taskSubCategoryId,str_taskState, str_taskLga, str_taskAddress;
    private Integer sub_type_checker, saving_checker;
    private AVLoadingIndicatorView avisave_draft;
    MyApplication App;
    private TextView editText_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_debug);

        /*external datas from previous activities*/
        /*get the following details from the intent called*/
        Intent extraIntent = getIntent();
        intentSubId = extraIntent.getStringExtra("sub_Id");
        intentSubName = extraIntent.getStringExtra("sub_name");
        intentProName = extraIntent.getStringExtra("pro_name");

        mDropDown = new ArrayList<>();
        mSubsDropDown = new ArrayList<>();
        mSkills = new ArrayList<>();
        mState = new ArrayList<>();
        App = MyApplication.getInstance();
        user_id = App.getUserId();
        //SECTION:: breakers area

        post_lyt = findViewById(R.id.post_lyt);
//        task_sub_title = findViewById(R.id.task_sub_title);
        post_lyt.setVisibility(View.VISIBLE);
//        categories
        cats_sh = findViewById(R.id.cats_sh);
        cats_rv =  findViewById(R.id.cats_rv);
        cats_view_lyt = findViewById(R.id.cats_view_lyt);
        cat_dropdown_img = findViewById(R.id.cat_dropdown_img);
        select_cat_txt = findViewById(R.id.select_cat_txt);
        cats_rv.setLayoutManager(new LinearLayoutManager(this));
        cats_rv.setHasFixedSize(true);

//       sub categories
        subcats_rv = findViewById(R.id.subcats_rv);
        subCats_sh = findViewById(R.id.subCats_sh);
        sub_cats_view_lyt = findViewById(R.id.sub_cats_view_lyt);
        select_sub_drop = findViewById(R.id.select_sub_drop);
        select_sub_text = findViewById(R.id.select_sub_text);
        subcats_rv.setLayoutManager(new LinearLayoutManager(this));
        subcats_rv.setHasFixedSize(true);


        //        skills
        skills_rv = findViewById(R.id.skills_rv);
        subSkill_sh = findViewById(R.id.subSkill_sh);
        sub_skills_view_lyt = findViewById(R.id.sub_skills_view_lyt);
        skills_chip_grp = findViewById(R.id.skills_chip_grp);
        skills_rv.setLayoutManager(new LinearLayoutManager(this));
        skills_rv.setHasFixedSize(true);


//        states
        state_rv = findViewById(R.id.state_rv);
        state_sh = findViewById(R.id.state_sh);
        state_view_lyt = findViewById(R.id.state_view_lyt);
        select_state_txt = findViewById(R.id.select_state_txt);
        state_dropdown_img = findViewById(R.id.state_dropdown_img);
        state_rv.setLayoutManager(new LinearLayoutManager(this));
        state_rv.setHasFixedSize(true);


        /*loaders*/

        avisave_draft = findViewById(R.id.avisave_draft);
        draft_post_txt = findViewById(R.id.draft_post_txt);

        /*post task functions for validation before task is posted*/
        post_task_sh = findViewById(R.id.post_task_sh);
        editText_title = findViewById(R.id.editText_title);
        editText_address = findViewById(R.id.editText_address);
//        check if the edittext has left focus

        editText_city = findViewById(R.id.editText_city);

//        find_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=");
//                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                        mapIntent.setPackage("com.google.android.apps.maps");
//                        startActivity(mapIntent);
//                    }
//                }, 1000);
//            }
//        });

       editText_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean e) {
               if (!e){
//                   QDraftSave();
               }
           }
       });

        editText_budget = findViewById(R.id.editText_budget);
        editText_des = findViewById(R.id.editText_des);

//        editText_lga = findViewById(R.id.editText_lga);
        editText_address = findViewById(R.id.editText_address);

//        errors
        sub_cat_error_txt = findViewById(R.id.sub_cat_error_txt);
        budget_error_txt = findViewById(R.id.budget_error_txt);
        title_error_txt = findViewById(R.id.title_error_txt);
        cat_error_txt = findViewById(R.id.cat_error_txt);
        state_error_txt = findViewById(R.id.state_error_text);
        address_error_txt = findViewById(R.id.address_error_txt);
        lga_error_txt = findViewById(R.id.lga_error_txt);
        cat_li_lyt = findViewById(R.id.cat_li_lyt);
        subcat_li_lyt = findViewById(R.id.subcat_li_lyt);
        skill_li_lyt = findViewById(R.id.skill_li_lyt);
        state_li_lyt = findViewById(R.id.state_li_lyt);
        location_lyt = findViewById(R.id.location_lyt);
        address_lyt = findViewById(R.id.address_lyt);

//        setting the default null call for the selectors
        str_taskState = null;
        str_taskCategory = null;
        str_taskSubCategory = null;
        sub_type_checker = null;
        strTask_id = null;
        str_taskCategoryId = null;
        str_taskSubCategoryId = null;
        saving_checker = null;

        /*here we update the data for categories, subcategories and pros data for a selected pro, subcategory or category*/


        if (intentSubId == null){
//                task_sub_title.setVisibility(View.VISIBLE);
//            if(intentProName == null) {
//                task_sub_title.setText("Hire "+intentProName+" for a " +intentSubName+" job");
//            }
//                task_sub_title.setText("Post a " +intentSubName+" job");
            select_sub_text.setText(intentSubName);
            select_sub_text.setTextColor(getResources().getColor(R.color.colorPrimary));
            select_sub_drop.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            loadSkills(intentSubId);
            subcat_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_active_bg_outline));
            str_taskSubCategory = intentSubName;
            str_taskSubCategoryId = intentSubId;
        }
        /*end of intent cats and pros function*/


        post_task_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QValidator();
            }
        });

        /*post task validator ends here*/
        nested_scroller = findViewById(R.id.nested_scroller);
        if (JsonUtils.isNetworkAvailable(this)) {
            loadCategories();
        } else{
            Intent intent = new Intent(this, InternetActivity.class);
            startActivity(intent);
        }

        cats_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cats_view_lyt.getVisibility() == View.VISIBLE){
                    cats_view_lyt.setVisibility(View.GONE);
                }else{
                    cats_view_lyt.setVisibility(View.VISIBLE);
                }

            }
        });
        subSkill_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sub_skills_view_lyt.getVisibility() == View.VISIBLE){
                    sub_skills_view_lyt.setVisibility(View.GONE);
                }else{
                    sub_skills_view_lyt.setVisibility(View.VISIBLE);
                }

            }
        });
        subCats_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sub_cats_view_lyt.getVisibility() == View.VISIBLE){
                    sub_cats_view_lyt.setVisibility(View.GONE);
                }else{
                    sub_cats_view_lyt.setVisibility(View.VISIBLE);
                }

            }
        });
        state_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (state_view_lyt.getVisibility() == View.VISIBLE){
                    state_view_lyt.setVisibility(View.GONE);
                }else{
                    state_view_lyt.setVisibility(View.VISIBLE);
                }

            }
        });
        nested_scroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cats_view_lyt.getVisibility() == View.VISIBLE){
                    cats_view_lyt.setVisibility(View.GONE);
                }
                if (sub_cats_view_lyt.getVisibility() == View.VISIBLE){
                    sub_cats_view_lyt.setVisibility(View.GONE);
                }

                if (sub_skills_view_lyt.getVisibility() == View.VISIBLE){
                    sub_skills_view_lyt.setVisibility(View.GONE);
                }

                if (state_view_lyt.getVisibility() == View.VISIBLE){
                    state_view_lyt.setVisibility(View.GONE);
                }
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyDkWm5A_WGqdfePi7iT1xAS1UdD748Mrk8");
        Button find_location = findViewById(R.id.find_location);
        find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete
                        .IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(PosterActivity.this);
                startActivityForResult(intent, 100);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            editText_address.setText(place.getAddress());

            try {
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                String address1 = addresses.get(0).getAddressLine(0);
                String address2 = addresses.get(0).getAddressLine(1);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();

//                Log.i("Locations", address1 + " |  " + address2 + " | " + city + " | " + state + " | " + country + " | " + postalCode);

                editText_city.setText(city);
                select_state_txt.setText(state);

            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void validateChecker(){

    }
    @Override
    public void onCatClick( String catData, String catID){
        if (cats_view_lyt.getVisibility() == View.VISIBLE){
            cats_view_lyt.setVisibility(View.GONE);
            select_cat_txt.setText(catData);
            select_cat_txt.setTextColor(getResources().getColor(R.color.colorPrimary));
            cat_dropdown_img.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            loadSubCategories(catID);
            cat_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_active_bg_outline));
            str_taskCategory = catData;
            str_taskCategoryId = catID;
        }else{
            cats_view_lyt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSubCatClick( String subCatData, String subCatID, String subCatType){
        if (sub_cats_view_lyt.getVisibility() == View.VISIBLE){
            sub_cats_view_lyt.setVisibility(View.GONE);
            select_sub_text.setText(subCatData);
            select_sub_text.setTextColor(getResources().getColor(R.color.colorPrimary));
            select_sub_drop.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            loadSkills(subCatID);
            subcat_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_active_bg_outline));
            str_taskSubCategory = subCatData;
            str_taskSubCategoryId = subCatID;
            sub_type_checker = Integer.valueOf(subCatType);
//            if (sub_type_checker == 0){
//                location_lyt.setVisibility(View.VISIBLE);
//                address_lyt.setVisibility(View.VISIBLE);
//            }else{
//                location_lyt.setVisibility(View.GONE);
//                address_lyt.setVisibility(View.GONE);
//            }
        }else{
            sub_cats_view_lyt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSubSkillClick( String skillData, String subCatID, String skillId){
        if (sub_skills_view_lyt.getVisibility() == View.VISIBLE){
            sub_skills_view_lyt.setVisibility(View.GONE);
//      TODO:// ADD SKILLS AS CHIPS      loadSkills(skillId);
//            now set the if conditions
            ArrayList<String> Sskills = new ArrayList<String>();
            Sskills.add(skillData);
//            first set the skills strings
            QDraftSave(skillId);
            setSkillsChip(Sskills);

        }else{
            sub_skills_view_lyt.setVisibility(View.VISIBLE);
        }
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
           mChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    QDeleteSkill(mChip.getText().toString());
                    skills_chip_grp.removeView(mChip);
                }
            });
            mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    QDeleteSkill(mChip.getText().toString());
                    skills_chip_grp.removeView(mChip);
                }
            });
            skills_chip_grp.addView(mChip);


        }
    }

    @Override
    public void onStateClick( String stateData, String stateID){
        Toast.makeText(PosterActivity.this,stateData,Toast.LENGTH_LONG).show();
        if (state_view_lyt.getVisibility() == View.VISIBLE){
            state_view_lyt.setVisibility(View.GONE);
            select_state_txt.setText(stateData);
            select_state_txt.setTextColor(getResources().getColor(R.color.colorPrimary));
            state_dropdown_img.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            loadSubCategories(stateID);
            state_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_active_bg_outline));
            str_taskState = stateData;
        }else{
            state_view_lyt.setVisibility(View.VISIBLE);
        }
    }

    private void QValidator() {
        str_taskTitle = editText_title.getText().toString();
        if (str_taskTitle.trim().length() == 0 || str_taskTitle.trim().length() <= 6 ){
            title_error_txt.setVisibility(View.VISIBLE);
            editText_title.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_error_bg_outline));
        }
        else{
            title_error_txt.setVisibility(View.GONE);
            editText_title.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_round_bg_outline));
            ActivateSave();
        }

//        not validated
        str_taskDes = editText_des.getText().toString();

        str_taskBudget = editText_budget.getText().toString();
        if (str_taskBudget != null){
            budget_error_txt.setVisibility(View.GONE);
            editText_budget.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_round_bg_outline));
            ActivateSave();
        }
        else{

            budget_error_txt.setVisibility(View.VISIBLE);
            editText_budget.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_error_bg_outline));

        }

//        for address when artisan
        if (sub_type_checker != null){
            if (sub_type_checker == 0){
                str_taskAddress = editText_address.getText().toString();
                if (str_taskAddress.length() <= 0 || str_taskAddress.length() <= 6 ){
                    address_error_txt.setVisibility(View.VISIBLE);
                    editText_address.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_error_bg_outline));
                }else{
                    address_error_txt.setVisibility(View.GONE);
                    editText_address.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_round_bg_outline));
                    ActivateSave();
                }

//                str_taskLga = editText_lga.getText().toString();
//                if (str_taskLga.length() <= 0 || str_taskLga.length() <= 6 ){
//                    lga_error_txt.setVisibility(View.VISIBLE);
//                    editText_lga.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_error_bg_outline));
//                }else{
//                    lga_error_txt.setVisibility(View.GONE);
//                    editText_lga.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_round_bg_outline));
//                    ActivateSave();
//                }

                if (str_taskState == null){
                    state_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_error_bg_outline));
                    select_state_txt.setTextColor(getResources().getColor(R.color.error_form));
                    state_dropdown_img.setColorFilter(ContextCompat.getColor(this, R.color.error_form));
                    state_error_txt.setVisibility(View.VISIBLE);

                }else{
                    ActivateSave();
                }
            }
        }


        //        for categories use conditions

        if (str_taskCategory == null){
            cat_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_error_bg_outline));
            select_cat_txt.setTextColor(getResources().getColor(R.color.error_form));
            cat_dropdown_img.setColorFilter(ContextCompat.getColor(this, R.color.error_form));
            cat_error_txt.setVisibility(View.VISIBLE);
        }else{
            ActivateSave();
        }

        if (str_taskSubCategory == null){
            subcat_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_error_bg_outline));
            select_sub_text.setTextColor(getResources().getColor(R.color.error_form));
            select_sub_drop.setColorFilter(ContextCompat.getColor(this, R.color.error_form));
            sub_cat_error_txt.setVisibility(View.VISIBLE);
        }else{
            ActivateSave();
        }


    }

    private void ActivateSave(){
        if (sub_type_checker != null){
            if (sub_type_checker == 1){
                if (str_taskTitle.length()  >= 6 && str_taskBudget != null && str_taskCategory != null && str_taskSubCategory != null){
                    if (saving_checker == null){
                    postTester();
                    }else if(saving_checker == 1){
                        Toast.makeText(PosterActivity.this,"saving a task please wait", Toast.LENGTH_LONG).show();
                    }
                }

            }
            else if(sub_type_checker == 0){
                if (str_taskTitle != null && str_taskBudget != null && str_taskCategory != null && str_taskSubCategory != null && str_taskState != null & str_taskLga != null && str_taskAddress != null){
                    if (saving_checker == null){
                        postTester();
                    }else if(saving_checker == 1){
                        Toast.makeText(PosterActivity.this,"saving a task please wait", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }

    private void QDraftSave(String skillId) {
        draft_post_txt.setVisibility(View.GONE);
        avisave_draft.setVisibility(View.VISIBLE);
        StartAnim();
        qUploadDraft(skillId);
    }

    private void QDeleteSkill(String skillTitle) {
        draft_post_txt.setVisibility(View.GONE);
        avisave_draft.setVisibility(View.VISIBLE);
        StartAnim();
        qDeleteDraft(skillTitle);
    }


//    private void QDraftSave() {
//        str_taskTitle = editText_title.getText().toString();
//        if (str_taskTitle.trim().length() >= 3) {
//            Toast.makeText(DebugPagerActivity.this,"Entered text is "+str_taskTitle, Toast.LENGTH_LONG).show();
//        }
//    }

    public TaskRequest createRequest(){
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTask_title(editText_title.getText().toString());
        taskRequest.setDes(editText_des.getText().toString());
        taskRequest.setBudget(editText_budget.getText().toString());
        taskRequest.setCat_id(str_taskCategoryId);
        taskRequest.setAddress(editText_address.getText().toString());
        taskRequest.setCity(editText_city.getText().toString());
        taskRequest.setState(select_state_txt.getText().toString());
        taskRequest.setSub_id(str_taskSubCategoryId);
        taskRequest.setUser_id(App.getUserId());
        return taskRequest;
    }

    public void saveTask(TaskRequest taskRequest){
        Call<TaskResponse> taskResponseCall = ApiClient.getTaskService().saveTask(taskRequest);
        taskResponseCall.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                Log.i("Response: ", response.toString());
                if(response.isSuccessful()){
                    Toast.makeText(PosterActivity.this, "Saved successfully!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(PosterActivity.this, "Request Failed!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Toast.makeText(PosterActivity.this, "Request Failed!" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Poster
    private void postTester() {
        saveTask(createRequest());
//        saving_checker = 0;
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
//        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
//        Call<String> call = ubuyapi.postDebug(App.getUserId(), str_taskTitle, str_taskDes, str_taskBudget, str_taskCategoryId, str_taskSubCategoryId);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call,
//                                   Response<String> response) {
//                HttpUrl main_url = response.raw().request().url();
//                Log.i("debugger", String.valueOf(main_url));
//                if (response.body() != null) {
//                    Log.i("onSuccess", response.body().toString());
//                    String jsonresponse = response.body().toString();
//                    parseStatusResponse(jsonresponse);
//                } else {
//                    saving_checker = null;
//                    Log.i("onEmptyResponse", "Returned empty ");
//                    //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                }
//                if (response.isSuccessful()){
//                    saving_checker = null;
//                    Log.i("onEmptyResponse", "Returned success");
//                    //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                Log.e("Upload error:", Objects.requireNonNull(t.getMessage()));
//            }
//        });
    }

    private void parseStatusResponse(String jsonresponse) {
        saving_checker = null;
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                strTask_id = objJson.getString(Constant.PROJECT_ID);
                str_saveStatus = objJson.getString(Constant.SUCCESS);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        FinalSaveDisplay();
    }

    private void FinalSaveDisplay() {
        if (Integer.parseInt(str_saveStatus) == 1){
//            goto single task page with the new set of data
            Intent intent = new Intent(PosterActivity.this, ProjectBidActivity.class);
            intent.putExtra("project_id", strTask_id);
            intent.putExtra("project_title", str_taskTitle);
            intent.putExtra("project_brief", str_taskDes);
            intent.putExtra("project_budget", str_taskBudget);
            intent.putExtra("project_sub_id", str_taskSubCategoryId);
            startActivity(intent);
        }
    }

    private void loadCategories() {
        LoadingAnimate();
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
        Call<String> call = ubuyapi.getCatState();
        Log.i("onEmptyResponse", "test network");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getCatJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PosterActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getCatJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONObject jsonArray = mainJson.getJSONObject(Constant.ARRAY_NAME);
//           Cat model
            JSONArray jsonCat = jsonArray.getJSONArray(Constant.HOME_CAT);
            JSONObject objJsonCat;
            mSubsDropDown.clear();
            mSkills.clear();
            for (int i = 0; i < jsonCat.length(); i++) {
                objJsonCat = jsonCat.getJSONObject(i);
                ItemDropDown objItem = new ItemDropDown();
                objItem.setItemId(objJsonCat.getString(Constant.CAT_ID));
                objItem.setItemName(objJsonCat.getString(Constant.CAT_NAME));
                mDropDown.add(objItem);
            }
//           state model
            JSONArray jsonState = jsonArray.getJSONArray(Constant.STATE_LIST);
            JSONObject objjsonState;
            for (int i = 0; i < jsonState.length(); i++) {
                objjsonState = jsonState.getJSONObject(i);
                ItemState objItem = new ItemState();
                objItem.setStateId(objjsonState.getString(Constant.STATE_ID));
                objItem.setState(objjsonState.getString(Constant.STATE_NAME));
                objItem.setStateCode(objjsonState.getString(Constant.STATE_CODE));
                mState.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displaySuggest();
        ChangeDraftDisplay();
    }

    private void loadSubCategories(String sub_id) {
        LoadingAnimate();
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
        Call<String> call = ubuyapi.getsubcats(sub_id);
        Log.i("onEmptyResponse", "test network");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getSubCatJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PosterActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");
            }
        });


    }

    private void getSubCatJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            mSubsDropDown.clear();
            select_sub_text.setText("Select subcategory");
            select_sub_text.setTextColor(getResources().getColor(R.color.textColorDark));
            select_sub_drop.setColorFilter(ContextCompat.getColor(this, R.color.textColorDark));
            subcat_li_lyt.setBackground(ContextCompat.getDrawable(PosterActivity.this, R.drawable.edit_text_select_bg_outline));
            mSkills.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                ItemDropSub objItem = new ItemDropSub();
                objItem.setItemId(objJson.getString(Constant.CAT_ID));
                objItem.setItemName(objJson.getString(Constant.CAT_NAME));
                objItem.setItemType(objJson.getString(Constant.SUB_TYPE));
                mSubsDropDown.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displaySubSuggest();
        ChangeDraftDisplay();
    }

    private void loadSkills(String sub_id) {
        LoadingAnimate();
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
        Call<String> call = ubuyapi.getSkills(sub_id);
        Log.i("onEmptyResponse", "test network");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getSubSkillsJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PosterActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");
            }
        });


    }

    private void getSubSkillsJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            mSkills.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                ItemSkill objItem = new ItemSkill();
                objItem.setSkillId(objJson.getString(Constant.SKILL_ID));
                objItem.setSkillTitle(objJson.getString(Constant.SKILL_TITLE));
                objItem.setSubId(objJson.getString(Constant.SUB_REC_ID));
                mSkills.add(objItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displaySubSkill();
        ChangeDraftDisplay();
    }

    private void qUploadDraft(String skillId) {
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
        Call<String> call = ubuyapi.saveDraftTask(user_id, str_taskTitle, skillId);
        Log.i("onEmptyResponse", "test network");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getDraftData(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PosterActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");
            }
        });


    }

    private void getDraftData(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                strTask_id = objJson.getString(Constant.PROJECT_ID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ChangeDraftDisplay();
    }

    private void qDeleteDraft(String skillTitle) {
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
        Call<String> call = ubuyapi.DeleteSkill(strTask_id, skillTitle);
        Log.i("onEmptyResponse", "test network");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getDraftData(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(PosterActivity.this, InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");
            }
        });


    }

    private void getDeleteData(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
                strTask_id = objJson.getString(Constant.PROJECT_ID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ChangeDraftDisplay();
    }

    private void ChangeDraftDisplay() {
        draft_post_txt.setVisibility(View.VISIBLE);
        avisave_draft.setVisibility(View.GONE);
        StopAnim();
    }

    private void LoadingAnimate() {
        draft_post_txt.setVisibility(View.GONE);
        avisave_draft.setVisibility(View.VISIBLE);
        StartAnim();
    }

    private void displaySuggest() {
        cats_rv.setVisibility(View.VISIBLE);
        mAdapterList = new ListDropdownAdapter(PosterActivity.this, mDropDown, this);
        cats_rv.setAdapter(mAdapterList);

        state_rv.setVisibility(View.VISIBLE);
        mStateAdapter = new StateDropdownAdapter(PosterActivity.this, mState, this);
        state_rv.setAdapter(mStateAdapter);
    }

    private void displaySubSuggest() {
        Toast.makeText(PosterActivity.this,"Nothing returned",Toast.LENGTH_LONG).show();
        subcats_rv.setVisibility(View.VISIBLE);
        mSubAdapterList = new SubDropdownAdapter(PosterActivity.this, mSubsDropDown, this);
        subcats_rv.setAdapter(mSubAdapterList);
    }

    private void displaySubSkill() {
        Toast.makeText(PosterActivity.this,"Nothing returned",Toast.LENGTH_LONG).show();
        skills_rv.setVisibility(View.VISIBLE);
        mSkillAdapter = new SkillDropdownAdapter(PosterActivity.this, mSkills, this);
        skills_rv.setAdapter(mSkillAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    void StartAnim(){
        avisave_draft.show();
    }

    void StopAnim(){
        avisave_draft.hide();
    }

}
