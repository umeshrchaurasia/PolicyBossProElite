package com.policyboss.policybossproelite.motor.privatecar.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorApplicationFragment;
import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorLeadFragment;
import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorQuoteFragment;

import java.util.ArrayList;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.QuoteApplicationEntity;


public class ActivityTabsPagerAdapter extends FragmentStatePagerAdapter {

    private static final int TOTAL = 3;
    public final static String LEAD_LIST = "LIST_LEAD";
    public final static String QUOTE_LIST = "LIST_QUOTE";
    public final static String APPLICATION_LIST = "LIST_APPLICATION";
    QuoteApplicationEntity mMasterData;

    private String[] pageTitle = new String[]{"LEAD", "QUOTE", "APPLICATION"};


    public ActivityTabsPagerAdapter(FragmentManager fm, QuoteApplicationEntity masterData) {
        super(fm);
        mMasterData = masterData;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Salary fragment activity
                MotorLeadFragment Lfragment = new MotorLeadFragment();
                Bundle leadBundle = new Bundle();
                if (mMasterData == null) {
                    leadBundle.putParcelableArrayList(LEAD_LIST, null);
                } else {
                    leadBundle.putParcelableArrayList(LEAD_LIST, (ArrayList<? extends Parcelable>) mMasterData.getMyleads());
                }
                Lfragment.setArguments(leadBundle);
                return Lfragment;
            case 1:
                // Salary fragment activity
                MotorQuoteFragment Qfragment = new MotorQuoteFragment();
                Bundle bundle = new Bundle();
                if (mMasterData == null) {
                    bundle.putParcelableArrayList(QUOTE_LIST, null);
                } else {
                    bundle.putParcelableArrayList(QUOTE_LIST, (ArrayList<? extends Parcelable>) mMasterData.getQuote());
                }
                Qfragment.setArguments(bundle);
                return Qfragment;
            case 2:
                // ABN fragment activity
                MotorApplicationFragment Afragment = new MotorApplicationFragment();
                Bundle Abundle = new Bundle();
                if (mMasterData == null) {
                    Abundle.putParcelableArrayList(APPLICATION_LIST, null);
                } else {
                    Abundle.putParcelableArrayList(APPLICATION_LIST, (ArrayList<? extends Parcelable>) mMasterData.getApplication());
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