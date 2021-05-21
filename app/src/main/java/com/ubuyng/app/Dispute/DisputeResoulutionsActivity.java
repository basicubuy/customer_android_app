package com.ubuyng.app.Dispute;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.florent37.shapeofview.ShapeOfView;
import com.google.android.material.tabs.TabLayout;
import com.ubuyng.app.Dispute.fragments.openProjects;
import com.ubuyng.app.R;
import com.ubuyng.app.fragments.FragmentTaskTabs;
import com.ubuyng.app.fragments.projects.ArchiveProjects;
import com.ubuyng.app.fragments.projects.CompletedProjects;
import com.ubuyng.app.fragments.projects.InProgressProjects;
import com.ubuyng.app.fragments.projects.PendingProjects;
import com.ubuyng.app.ubuyapi.util.Tools;

public class DisputeResoulutionsActivity extends AppCompatActivity {

    private ShapeOfView submit_disput_sh;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapterTasks sectionsPagerAdapterTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute_resolution);

        initToolbar();


        tabLayout = findViewById(R.id.tasks_tab);
        viewPager = findViewById(R.id.tasks_pager);



        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        sectionsPagerAdapterTasks = new SectionsPagerAdapterTasks(getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapterTasks);


        submit_disput_sh = findViewById(R.id.submit_disput_sh);
        submit_disput_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddDisputeIntent = new Intent(DisputeResoulutionsActivity.this, AddDisputeActivity.class);
                startActivity(AddDisputeIntent);
            }
        });
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

    private static class SectionsPagerAdapterTasks extends FragmentPagerAdapter {

        public SectionsPagerAdapterTasks(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // replace with different fragments
                case 0:
                    return new openProjects();

                case 1:
                    return new openProjects();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
