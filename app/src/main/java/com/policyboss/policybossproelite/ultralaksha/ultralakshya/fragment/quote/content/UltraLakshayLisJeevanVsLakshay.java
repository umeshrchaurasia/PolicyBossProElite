package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.R;

import magicfinmart.datacomp.com.finmartserviceapi.database.UltraLakshaFacade;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshaIllustrationResponseNew;

/**
 * A simple {@link Fragment} subclass.
 */
public class UltraLakshayLisJeevanVsLakshay extends BaseFragment {

    TextView txtBasicSum,txtLicAnnualPremium,txtUlAnnualPremium,txtUlAnnualPremiumPercentage,

    txtLicMaturityAmt,txtUlMaturityAmt,txtLicAnnualIcomeOndeath, txtUlAnnualIcomeOndeath;

    UltraLakshaFacade ultraLakshaFacade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ultra_lakshay_lis_jeevan_vs_lakshay, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        ultraLakshaFacade = new UltraLakshaFacade(getActivity());

        setUnmatchedUI();
    }

    private void initialize(View view)
    {
        txtBasicSum = view.findViewById(R.id.txtBasicSum);
        txtLicAnnualPremium = view.findViewById(R.id.txtLicAnnualPremium);
        txtUlAnnualPremium = view.findViewById(R.id.txtUlAnnualPremium);

        txtUlAnnualPremiumPercentage = view.findViewById(R.id.txtUlAnnualPremiumPercentage);
        txtLicMaturityAmt = view.findViewById(R.id.txtLicMaturityAmt);

        txtUlMaturityAmt = view.findViewById(R.id.txtUlMaturityAmt);
        txtLicAnnualIcomeOndeath = view.findViewById(R.id.txtLicAnnualIcomeOndeath);

        txtUlAnnualIcomeOndeath = view.findViewById(R.id.txtUlAnnualIcomeOndeath);


    }


    private void setUnmatchedUI()
    {
        if(ultraLakshaFacade.getLicVrs() != null)
        {

            UltraLakshaIllustrationResponseNew.MasterDataBean.LicVrsBean objLicVsUltra = ultraLakshaFacade.getLicVrs().get(0);

            txtBasicSum.setText(""+objLicVsUltra.getBasicSum());
            txtLicAnnualPremium.setText(""+objLicVsUltra.getLicAnnualPremium());
            txtUlAnnualPremium.setText(""+objLicVsUltra.getUlAnnualPremium());

           // txtUlAnnualPremiumPercentage.setText("");
            txtLicMaturityAmt.setText(""+objLicVsUltra.getLicMaturityAmt());

            txtUlMaturityAmt.setText(""+objLicVsUltra.getUlMaturityAmt());
            txtLicAnnualIcomeOndeath.setText(""+objLicVsUltra.getLicAnnualIcomeOndeath());

            txtUlAnnualIcomeOndeath.setText(""+objLicVsUltra.getUlAnnualIcomeOndeath());

        }
    }


}
