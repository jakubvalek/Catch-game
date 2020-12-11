package com.example.catchgame.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.catchgame.R;
import com.example.catchgame.views.fragments.MainFragment;
import com.example.catchgame.views.fragments.ScoresFragment;
import com.example.catchgame.views.fragments.SettingsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_settings, R.string.tab_text_menu, R.string.tab_text_high_score};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new SettingsFragment();
                break;
            case 1:
                fragment = MainFragment.newInstance();
                break;
            case 2:
                fragment = ScoresFragment.newInstance();
                break;
        }
        // will never return null -> assert to calm down the IDE
        assert fragment != null;
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}