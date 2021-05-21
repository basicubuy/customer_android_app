package com.ubuyng.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ubuyng.app.ubuyapi.util.Tools;
import com.ubuyng.app.R;
public class DebugActivity extends AppCompatActivity {


    private BottomAppBar bottomAppBar;
    private FloatingActionButton floatingActionButton;
    private boolean isFabAttachedAtCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);


        bottomAppBar = findViewById(R.id.bar);
        setSupportActionBar(bottomAppBar);


    }




}
