package com.example.patja2r.test1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by patja2r on 5/26/2016.
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new QuestionOneFragment();
        Bundle bundle = new Bundle();
        int i=0;
        bundle.putInt("count", i+1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 10;
    }
}
