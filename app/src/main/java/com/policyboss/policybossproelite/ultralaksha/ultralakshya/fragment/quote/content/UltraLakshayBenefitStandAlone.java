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
public class UltraLakshayBenefitStandAlone extends BaseFragment {


    TextView txtAnnualPrem1yearAmnt ,txtAnnualPrem2to7Amnt, txtAnnualPrem2to7Text,
            txtMaturityAfterYearText,txtMaturityAfterYearAmnt,
            txtLumpsumAmnt ,txtAnnualEndOfTermAmnt,
            txtMonthlyforYearText ,txtMonthlyforYearAmnt,
            txtOnMaturityDateAmnt,txtPremTerm;

    UltraLakshaFacade ultraLakshaFacade;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_ultra_lakshay_benefit_stand_alone, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        ultraLakshaFacade = new UltraLakshaFacade(getActivity());

        binData();
    }

    private void initialize(View view)
    {
        txtAnnualPrem1yearAmnt = view.findViewById(R.id.txtAnnualPrem1yearAmnt);
        txtAnnualPrem2to7Amnt = view.findViewById(R.id.txtAnnualPrem2to7Amnt);
        txtAnnualPrem2to7Text = view.findViewById(R.id.txtAnnualPrem2to7Text);

        txtMaturityAfterYearText = view.findViewById(R.id.txtMaturityAfterYearText);
        txtMaturityAfterYearAmnt = view.findViewById(R.id.txtMaturityAfterYearAmnt);

        txtLumpsumAmnt = view.findViewById(R.id.txtLumpsumAmnt);
        txtAnnualEndOfTermAmnt = view.findViewById(R.id.txtAnnualEndOfTermAmnt);

        txtMonthlyforYearText = view.findViewById(R.id.txtMonthlyforYearText);
        txtMonthlyforYearAmnt = view.findViewById(R.id.txtMonthlyforYearAmnt);

        txtOnMaturityDateAmnt = view.findViewById(R.id.txtOnMaturityDateAmnt);
        txtPremTerm = view.findViewById(R.id.txtPremTerm);

    }


    private void binData()
    {
        if(ultraLakshaFacade.getPageTwoStandAloneList() != null)
        {

            UltraLakshaIllustrationResponseNew.MasterDataBean.PageTwoStandAloneBean objStandAlone = ultraLakshaFacade.getPageTwoStandAloneList().get(0);

            txtAnnualPrem1yearAmnt.setText(""+getNumbeFormatCommaRuppee(objStandAlone.getAnnualPremiumFirstYr()));
            txtAnnualPrem2to7Amnt.setText(""+getNumbeFormatCommaRuppee(objStandAlone.getAnnualPremiumOtherYrs()));
            txtAnnualPrem2to7Text.setText(""+objStandAlone.getOtherYrs());

            txtMaturityAfterYearText.setText(""+objStandAlone.getMaturityYear());
            txtMaturityAfterYearAmnt.setText(""+getNumbeFormatCommaRuppee(objStandAlone.getMaturityDateValue()));

            txtLumpsumAmnt.setText(""+getNumbeFormatCommaRuppee(objStandAlone.getLumpsumpDeath()));
            txtAnnualEndOfTermAmnt.setText(""+getNumbeFormatCommaRuppee(objStandAlone.getAnnualTillEOT()));

            txtMonthlyforYearText.setText(""+objStandAlone.getMonthlyterm());
            txtMonthlyforYearAmnt.setText(""+getNumbeFormatCommaRuppee(objStandAlone.getMonthlytermValue()));

            txtOnMaturityDateAmnt.setText(""+getNumbeFormatCommaRuppee(objStandAlone.getMaturityDateValue()));
            txtPremTerm.setText(""+(objStandAlone.getPremTerm()));
        }

    }


}
