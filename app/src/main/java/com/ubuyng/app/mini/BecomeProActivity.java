/*
 * Copyright (c) 2020. All content and codes belong to Ubuy Nigeria And should not be reproduced or used without written consent from the author
 *
 */

package com.ubuyng.app.mini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.github.florent37.shapeofview.ShapeOfView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ubuyng.app.AboutActivity;
import com.ubuyng.app.MyApplication;
import com.ubuyng.app.R;

public class BecomeProActivity extends AppCompatActivity  {
    Toolbar toolbar;
    MyApplication App;
    ShapeOfView get_app_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_pro);

        App = MyApplication.getInstance();

//       finders
        get_app_btn = findViewById(R.id.get_app_btn);



    }

}
