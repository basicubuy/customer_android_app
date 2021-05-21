package com.ubuyng.app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionPagerAdapterRoot extends FragmentPagerAdapter {

    private Fragment fragment1, fragment2, fragment3, fragment4;
    private int fragmentCount;

    public SectionPagerAdapterRoot(FragmentManager fm, Fragment fragment1, Fragment fragment2, Fragment fragment3, Fragment fragment4, int fragmentCount) {
        super(fm);
        this.fragment1 = fragment1;
        this.fragment2 = fragment2;
        this.fragment3 = fragment3;
        this.fragment4 = fragment4;
        this.fragmentCount = fragmentCount;
    }

    public SectionPagerAdapterRoot(FragmentManager fm, Fragment fragment1, Fragment fragment2, int fragmentCount) {
        super(fm);
        this.fragment1 = fragment1;
        this.fragment2 = fragment2;
        this.fragmentCount = fragmentCount;
    }
    public SectionPagerAdapterRoot(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment1;
            case 1:
                return fragment2;
            case 2:
                return fragment3;
                case 3:
                return fragment4;

        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentCount;
    }
}
