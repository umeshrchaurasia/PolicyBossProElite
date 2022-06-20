package com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.fragment.UltraApplicationDetailFragment;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.fragment.UltraQuoteDetailFragment;

import java.util.ArrayList;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UltraMainEntity;

/**
 * Created by Rajeev Ranjan on 19/02/2019.
 */

public class UltraTabsPagerAdapter extends FragmentStatePagerAdapter {

    private static final int TOTAL = 2;
    public final static String QUOTE_LIST = "LIST_QUOTE";
    public final static String APPLICATION_LIST = "LIST_APPLICATION";

    UltraMainEntity ultraMainEntity;

    private String[] tabTitles = new String[]{"QUOTES", "APPLICATION"};

    public UltraTabsPagerAdapter(FragmentManager fm , UltraMainEntity temUltraMainEntity) {
        super(fm);
        this.ultraMainEntity = temUltraMainEntity;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // Salary fragment activity

                UltraQuoteDetailFragment Qfragment = new UltraQuoteDetailFragment();
                Bundle bundle = new Bundle();

                if (ultraMainEntity == null) {
                    bundle.putParcelableArrayList(QUOTE_LIST, null);

                } else {
                    bundle.putParcelableArrayList(QUOTE_LIST, (ArrayList<? extends Parcelable>) ultraMainEntity.getQuote());
                }
                Qfragment.setArguments(bundle);
                return Qfragment;


            case 1:
                // ABN fragment activity
                UltraApplicationDetailFragment Afragment = new UltraApplicationDetailFragment();

                Bundle Abundle = new Bundle();
                if (ultraMainEntity == null) {
                    Abundle.putParcelableArrayList(APPLICATION_LIST, null);
                } else {
                    Abundle.putParcelableArrayList(APPLICATION_LIST, (ArrayList<? extends Parcelable>) ultraMainEntity.getApplication());
                }
                Afragment.setArguments(Abundle);
                return Afragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return TOTAL;
    }
}
