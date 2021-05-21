package com.ubuyng.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ubuyng.app.ubuyapi.util.Tools;

public class UpdateTaskActivity extends AppCompatActivity {
    RadioGroup updateGroup;
    RadioButton project_complete, in_progress, selected_pro, hold;
    String project_id, project_title;
    MyApplication App;
    Button Update_project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_update);
        initToolbar();

        Intent intent = getIntent();
        project_id = intent.getStringExtra("project_id");
        project_title = intent.getStringExtra("project_title");
        App = MyApplication.getInstance();

    }
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("Update project");
        }
    }

    public void onRadioClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.project_complete:
                if (checked) {
                    Toast.makeText(this, "task completed "+project_title, Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.in_progress:
                if (checked) {
                    Toast.makeText(this, "task in progress " + project_title, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.selected_pro:
                if (checked) {
                    Toast.makeText(this, "pro selected for task " + project_title, Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.hold:
                if (checked) {
                    Toast.makeText(this, "task on hold " + project_title, Toast.LENGTH_SHORT).show();
                    break;
                }
        }
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



}
