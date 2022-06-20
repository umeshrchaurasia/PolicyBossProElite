package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.UltraLakshayScenarioOfBenefitsDeath;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.UltraLakshayUnmatchedBenefit;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.UltraLakshayBenefitStandAlone;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.UltraLakshayDeathBenefitToNominee;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.UltraLakshayBenefitILLustration;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.UltraLakshayProductCombo;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.coverPage1Fragment;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.coverPage2Fragment;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.coverPage3Fragment;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;

/**
 * Created by Rajeev Ranjan on 31/01/2019.
 */

public class LakshayQuotePagerAdapter extends FragmentStatePagerAdapter {

    String strUserName;
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public LakshayQuotePagerAdapter(FragmentManager fm, String strName) {
        super(fm);
        this.strUserName = strName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                coverPage1Fragment objFrag = new coverPage1Fragment();
                Bundle bundle = new Bundle();           // use bundle for paasing the data
                bundle.putString(Utility.ULTRA_LAKSHYA_HDR_NAME, strUserName);
                objFrag.setArguments(bundle);
                return objFrag;

            case 1:
                return new coverPage2Fragment();

            case 2:
                return new coverPage3Fragment();

            case 3:
                return new UltraLakshayUnmatchedBenefit();

            case 4:

                return new UltraLakshayBenefitStandAlone();

            case 5:
                return new UltraLakshayScenarioOfBenefitsDeath();


            case 6:

                return new UltraLakshayBenefitILLustration();

            case 7:
                return new UltraLakshayDeathBenefitToNominee();


            case 8:
                return new UltraLakshayProductCombo();


        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);

    }
}
