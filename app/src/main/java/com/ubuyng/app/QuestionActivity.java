package com.ubuyng.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ubuyng.app.ubuyapi.Common.Common;
import com.ubuyng.app.ubuyapi.Models.ItemChoices;
import com.ubuyng.app.ubuyapi.Models.ItemQuestions;
import com.ubuyng.app.ubuyapi.db.DatabaseHelper;
import com.ubuyng.app.ubuyapi.util.Constant;
import com.ubuyng.app.ubuyapi.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {
    private Common callback;
    private int MAX_STEP = 1;
    int index=0, thisQuestion=0, totalQuestion,correctQuestion;
    private ViewPager viewPager;
    private Button btnNext;
    private ImageView question_into;
    ArrayList<ItemQuestions> mQuestions, mAllQuestions;
    ArrayList<ItemChoices> mChoices, mAllChoices;
    private DatabaseHelper databaseHelper;
    private String sub_id, sub_Title;
//    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
//        intent to call subcat_id and cat_id
        Intent intent = getIntent();
        sub_id = intent.getStringExtra("Sub_Id");
        sub_Title = intent.getStringExtra("Sub_Title");

        ((ImageButton)findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (JsonUtils.isNetworkAvailable(QuestionActivity.this)) {
            new LoadQuestions().execute(Constant.SINGLESUBCAT+sub_id);
        } else{
            intent = new Intent(this , InternetActivity.class);
            startActivity(intent);

        }
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
            }
        }
    }



}
