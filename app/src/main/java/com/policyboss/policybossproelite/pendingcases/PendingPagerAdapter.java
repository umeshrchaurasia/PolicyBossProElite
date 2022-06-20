package com.policyboss.policybossproelite.pendingcases;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PendingCaseInsLoanResponse;


public class PendingPagerAdapter extends FragmentStatePagerAdapter {

    private static int TOTAL = 2;
    public final static String INSURANCE_LIST = "LIST_INSURANCE";
    public final static String LOAN_LIST = "LIST_LOAN";
    PendingCaseInsLoanResponse.MasterDataBean mMasterData;

    private String[] pageTitle = new String[]{"INSURANCE", "LOAN"};


    public PendingPagerAdapter(FragmentManager fm, Context mContext, PendingCaseInsLoanResponse.MasterDataBean masterData) {
        super(fm);
        mMasterData = masterData;
        if (new DBPersistanceController(mContext).isHideLoan()){
            TOTAL = 1;
        }
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
                PendingCaseFragment Qfragment = new PendingCaseFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PendingCaseFragment.TYPE, 1);
                if (mMasterData == null) {
                    bundle.putParcelableArrayList(INSURANCE_LIST, null);
                } else {
                    bundle.putParcelableArrayList(INSURANCE_LIST, (ArrayList<? extends Parcelable>) mMasterData.getInsurance());
                }
                Qfragment.setArguments(bundle);
                return Qfragment;
            case 1:
                // ABN fragment activity
                PendingCaseFragment Afragment = new PendingCaseFragment();
                Bundle Abundle = new Bundle();
                Abundle.putInt(PendingCaseFragment.TYPE, 2);
                if (mMasterData == null) {
                    Abundle.putParcelableArrayList(LOAN_LIST, null);
                } else {
                    Abundle.putParcelableArrayList(LOAN_LIST, (ArrayList<? extends Parcelable>) mMasterData.getLoan());
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