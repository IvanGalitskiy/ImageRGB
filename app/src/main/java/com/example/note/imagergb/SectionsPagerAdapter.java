package com.example.note.imagergb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by NOTE on 17.09.2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> list = new ArrayList();
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragmnet(Fragment f){
        list.add(f);
        notifyDataSetChanged();
    }
    public Fragment getFragment(int index){
        return list.get(index);
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }

    public ArrayList<Fragment> getList() {
        return list;
    }
}
