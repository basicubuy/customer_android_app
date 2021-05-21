package com.ubuyng.app;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.florent37.shapeofview.ShapeOfView;
import com.ubuyng.app.ubuyapi.util.Tools;


public class StepperActivity extends AppCompatActivity {

    private static final int MAX_STEP = 3;
    MyApplication App;
    private PrefManager prefManager;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Button btn_join_sibro;
    private TextView skip_txt, signin_txt2;
    private String title_array[] = {
            "Find Pros & Artisans",
            "Post Tasks for Free",
            "Compare Job Bids",
    };

    private Integer int_array[] = {
           1,
            2,
          3,
    };
    private String description_array[] = {
            "Access thousands of skilled \n Nigerian on Ubuy for home \n and office needs",
            "List tasks on Ubuy without \n commissions",
            "Choose among multiple bids \n that suit your budget",
    };
    private int about_images_array[] = {
            R.drawable.find_pro_wizard,
            R.drawable.post_task_wizard,
            R.drawable.compare_bids_wizard,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_wizard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        }


        App = MyApplication.getInstance();

        initComponent();

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        Tools.setSystemBarTransparent(this);
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        if (App.getIsLogin()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        }


    }
    private void initComponent() {
        viewPager =  findViewById(R.id.view_pager);
        skip_txt = findViewById(R.id.skip_txt);

        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        skip_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setFirstTimeLaunch(false);
                Intent intent = new Intent(StepperActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
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
            dots[i].setColorFilter(getResources().getColor(R.color.overlay_light_90), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            int height = 15;
            int weight = 40;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(weight, height));
            params.setMargins(10, 10, 10, 10);
            dots[current_index].setLayoutParams(params);
            dots[current_index].setImageResource(R.drawable.btn_corner_white);
            dots[current_index].setColorFilter(getResources().getColor(R.color.overlay_light_90), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
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
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_stepper_wiz, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(description_array[position]);
            ((ShapeOfView) view.findViewById(R.id.fab_next_wiz)).setVisibility(View.GONE);
            ((Button) view.findViewById(R.id.get_started_btn)).setVisibility(View.GONE);
            ShapeOfView btnNext = (ShapeOfView) view.findViewById(R.id.fab_next_wiz);
            Button getStartedbtn = (Button) view.findViewById(R.id.get_started_btn);

            if (int_array[position] != 3){
                ((ShapeOfView) view.findViewById(R.id.fab_next_wiz)).setVisibility(View.VISIBLE);
            }else if(int_array[position] == 3){
                ((Button) view.findViewById(R.id.get_started_btn)).setVisibility(View.VISIBLE);

            }

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

            getStartedbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefManager.setFirstTimeLaunch(false);
                    Intent intent = new Intent(StepperActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            });

//            ((Button) view.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (App.getIsLogin()) {
//                        prefManager.setFirstTimeLaunch(false);
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        prefManager.setFirstTimeLaunch(false);
//                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            });

            ((ImageView) view.findViewById(R.id.wizard_img)).setImageDrawable(getResources().getDrawable(about_images_array[position]));
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}