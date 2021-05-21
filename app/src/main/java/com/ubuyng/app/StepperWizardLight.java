package com.ubuyng.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.ubuyng.app.fragments.FragmentMore;
import com.ubuyng.app.ubuyapi.Common.Common;
import com.ubuyng.app.ubuyapi.Models.ItemChoices;
import com.ubuyng.app.ubuyapi.Models.ItemQuestions;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;
import com.ubuyng.app.ubuyapi.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import static com.ubuyng.app.fragments.FragmentMore.newInstance;

public class StepperWizardLight extends AppCompatActivity {

    private Common callback;
    private int MAX_STEP = 1;
    int index=0, thisQuestion=0, totalQuestion,correctQuestion;
    private ViewPager viewPager;
    private SectionsPagerAdapterNews SectionsPagerAdapterNews;
    private Button btnNext;
    private ImageView question_into;
    ArrayList<ItemQuestions> mQuestions, mAllQuestions;
    ArrayList<ItemChoices> mChoices, mAllChoices;
    private DatabaseHelper databaseHelper;
//    public static SQLiteDatabase db;
    private String sub_id, sub_title, locate_string, user_address, user_city, user_lat, user_lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_wizard);
//     databaseHelper.getWritableDatabase();
//        databaseHelper.delete(TABLE_RESPONSES_NAME, null,null);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        sub_id = intent.getStringExtra("sub_id");
        sub_title = intent.getStringExtra("sub_title");
        user_address = intent.getStringExtra("user_address");
        user_city = intent.getStringExtra("user_city");
        user_lat = intent.getStringExtra("user_lat");
        user_lng = intent.getStringExtra("user_lng");
        Log.d("Debug_call", "The subcategory id is "+sub_id);

        initComponent();
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
        if (Common.Checklist.size() > 0)
            Common.Checklist.clear();

    }
    private void onUpgrade(SQLiteDatabase db) {
        db = databaseHelper.getWritableDatabase();
        // Create tables again
        db.close();
    }
    private void initComponent() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btnNext = (Button) findViewById(R.id.btn_next);
        question_into = findViewById(R.id.question_into);
        mQuestions = new ArrayList<>();
        mAllQuestions = new ArrayList<>();
        mChoices = new ArrayList<>();
        mAllChoices = new ArrayList<>();



        // adding bottom dots
        bottomProgressDots(0);
        if (JsonUtils.isNetworkAvailable(StepperWizardLight.this)) {
            new LoadQuestions().execute(Constant.SINGLESUBCAT+sub_id);
            Log.d("Debug_call", Constant.SINGLESUBCAT+sub_id);
        } else{
            Intent intent = new Intent(this , InternetActivity.class);
            startActivity(intent);

        }

        String intro_text[] = {" We're asking you a few questions so we can bring you the right pros",
                " Description",
                " Please confirm your location "};
        String intro_type[] = {"0", "3", "4"};

        for (int i = 0; i < intro_text.length; i++) {
            ItemQuestions ItemQuestions = new ItemQuestions();
            ItemQuestions.setQuesText(intro_text[i]);
            ItemQuestions.setQuesType(intro_type[i]);
            mQuestions.add(ItemQuestions);
        }

        String choice1[] = {"1 ",
                "1",
                " 1"};
        for (int i = 0; i < intro_text.length; i++) {
            ItemChoices ItemChoices = new ItemChoices();
            ItemChoices.setChoiceText(choice1[i]);
            mChoices.add(ItemChoices);
        }
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem() + 1;
                if (current < MAX_STEP) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    finish();

                }
            }
        });

        ((ImageButton)findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    @SuppressLint("StaticFieldLeak")
    private class LoadQuestions extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startAnim();

        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            stopAnim();
            if (null == result || result.length() == 0) {
                Log.i("LOADERS_CALL", "NO ITEM FOUND");
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        ItemQuestions objItem = new ItemQuestions();
                        objItem.setQuesID(objJson.getString(Constant.QUES_ID));
                        objItem.setCatId(objJson.getString(Constant.QUES_SUB_ID));
                        objItem.setQuesText(objJson.getString(Constant.QUES_TEXT));
                        objItem.setQuesRequired(objJson.getString(Constant.QUES_REQUIRED));
                        objItem.setQuesType(objJson.getString(Constant.QUES_TYPE));
                        mAllQuestions.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayData();
            }
        }
    }


    private void displayData() {
        int insertIndex = 1;
        mQuestions.addAll(insertIndex, mAllQuestions);
        MAX_STEP = mQuestions.size();
        SectionsPagerAdapterNews = new SectionsPagerAdapterNews(mQuestions, getSupportFragmentManager());
        viewPager.setAdapter(SectionsPagerAdapterNews);

    }


    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);

            if (position == mQuestions.size() - 1) {
                btnNext.setText(getString(R.string.GOT_IT));
                btnNext.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnNext.setTextColor(Color.WHITE);

                Intent intent = new Intent(getBaseContext(), SubmitTaskActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
               else
                {
                    btnNext.setText(getString(R.string.NEXT));
                    btnNext.setBackgroundColor(getResources().getColor(R.color.grey_10));
                    btnNext.setTextColor(getResources().getColor(R.color.grey_90));
                }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */



    /**
     * FRAGEMENT VIEW PAGER
     */
    private class SectionsPagerAdapterNews extends FragmentPagerAdapter {
        private ArrayList<ItemQuestions> modelList;

        public SectionsPagerAdapterNews(ArrayList<ItemQuestions> modelList, FragmentManager fm) {
            super(fm);
            this.modelList = modelList;
            viewPager.setOffscreenPageLimit(mQuestions.size());


            if (Common.choice_tag == null){
                Log.i("CALL_BACK", "not found");
            } else {
                Log.i("CALL_BACK", Common.choice_tag);
            }
        }
        //Button method to truncate table rows

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("ques_id", modelList.get(position).getQuesID());
            bundle.putString("ques_type", modelList.get(position).getQuesType());
            bundle.putString("ques_text", modelList.get(position).getQuesText());
            bundle.putString("sub_id", sub_id);
            bundle.putString("sub_title", sub_title);
            bundle.putString("user_address", user_address);
            bundle.putString("user_city", user_city);
            bundle.putString("user_lat", user_lat);
            bundle.putString("user_lng", user_lng);
//            Log.i("FRAGMENT_CALL", "The question id is "+modelList.get(position).getQuesText());

// set Fragmentclass Arguments
            FragmentMore fragobj = new FragmentMore();
            fragobj.setArguments(bundle);
            return fragobj;
        }

        @Override
        public int getCount() {
            return modelList.size();
        }

    }

}