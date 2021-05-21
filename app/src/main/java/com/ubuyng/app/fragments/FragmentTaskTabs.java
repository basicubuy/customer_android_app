package com.ubuyng.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ubuyng.app.R;
import com.ubuyng.app.fragments.projects.ArchiveProjects;
import com.ubuyng.app.fragments.projects.CompletedProjects;
import com.ubuyng.app.fragments.projects.InProgressProjects;
import com.ubuyng.app.fragments.projects.PendingProjects;

public class FragmentTaskTabs extends Fragment {

    public FragmentTaskTabs() {
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SectionsPagerAdapterTasks sectionsPagerAdapterTasks;

    public static FragmentTaskTabs newInstance() {
        FragmentTaskTabs fragment = new FragmentTaskTabs();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks_tabs, container, false);


        tabLayout = root.findViewById(R.id.tasks_tab);
        viewPager = root.findViewById(R.id.tasks_pager);



        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        sectionsPagerAdapterTasks = new SectionsPagerAdapterTasks(getFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapterTasks);


        return root;
    }

    private class SectionsPagerAdapterTasks extends FragmentPagerAdapter {

        public SectionsPagerAdapterTasks(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                // replace with different fragments
                case 0:
                    return new PendingProjects();

                case 1:
                    return new InProgressProjects();

                case 2:
                    return new CompletedProjects();

                case 3:
                    return new ArchiveProjects();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}