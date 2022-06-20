package com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.Offline_Term;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.TermQuoteApplicationResponse;


public class TermActivityTabsPagerAdapter_offline extends FragmentStatePagerAdapter {

    public final static String TERM_QUOTE_LIST = "TERM_LIST_QUOTE";
  //  public final static String TERM_APPLICATION_LIST = "TERM_LIST_APPLICATION";
    TermQuoteApplicationResponse mMasterData;
    private String[] pageTitle = new String[]{"QUOTES"};

    public TermActivityTabsPagerAdapter_offline(FragmentManager fm, TermQuoteApplicationResponse masterData) {
        super(fm);
        mMasterData = masterData;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:

                TermQuoteListFragment_offline fragment = new TermQuoteListFragment_offline();
                Bundle bundle = new Bundle();
                if (mMasterData == null) {
                    bundle.putParcelableArrayList(TERM_QUOTE_LIST, null);
                } else {
                    bundle.putParcelableArrayList(TERM_QUOTE_LIST, (ArrayList<? extends Parcelable>) mMasterData.getMasterData().getQuote());
                }
                fragment.setArguments(bundle);
                return fragment;

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 1;
    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}