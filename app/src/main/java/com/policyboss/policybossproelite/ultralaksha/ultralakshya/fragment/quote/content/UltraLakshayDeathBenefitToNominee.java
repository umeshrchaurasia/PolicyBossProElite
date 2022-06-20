package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.adapter.UltraLakshyaDeathNomineeAdapter;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.database.UltraLakshaFacade;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.DeathBenefitEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshaIllustrationResponseNew;

/**
 * A simple {@link Fragment} subclass.
 */
public class UltraLakshayDeathBenefitToNominee extends BaseFragment {


    RecyclerView rvDeathBenefit;
    List<DeathBenefitEntity> DeathBenefitLst;
    UltraLakshyaDeathNomineeAdapter mAdapter;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginEntity;
    PrefManager prefManager;
    UltraLakshaFacade ultraLakshaFacade;
    UltraLakshaFacade ultraLakshaFacadeDialog;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ultra_lakshay_death_nominee, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        ultraLakshaFacade = new UltraLakshaFacade(getActivity());

        if(ultraLakshaFacade.getDeathBenefitList() != null) {
            mAdapter = new UltraLakshyaDeathNomineeAdapter(UltraLakshayDeathBenefitToNominee.this, ultraLakshaFacade.getDeathBenefitList());
            rvDeathBenefit.setAdapter(mAdapter);
        }
        else{
//            mAdapter = new UltraLakshyaDeathNomineeAdapter(UltraLakshayDeathBenefitToNominee.this, getDeathNomineeLst());
//            rvDeathBenefit.setAdapter(mAdapter);  //temp data

            Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize(View view) {

        prefManager = new PrefManager(getActivity());
        DeathBenefitLst = new ArrayList<DeathBenefitEntity>();

        prefManager.setNotificationCounter(0);

        rvDeathBenefit = (RecyclerView) view.findViewById(R.id.rvDeathBenefit);
        rvDeathBenefit.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvDeathBenefit.setLayoutManager(layoutManager);


    }

    private  List<DeathBenefitEntity>  getDeathNomineeLst()
    {
        DeathBenefitLst.clear();
        for(int i =1; i <20 ; i++) {
            DeathBenefitEntity obj = new DeathBenefitEntity();
            obj.setYear(""+i);
            obj.setJeevanBenefitsPayable("" +500000 +i);
            obj.setJeevanPremiumPaid(""+700 +i);
            obj.setLakshyaBenefitsPayable(""+178900 +i);
            obj.setLakshyaPremiumPaid(""+3347900 +i);
            DeathBenefitLst.add(obj);
        }

        return DeathBenefitLst;
    }


    public void showDeathPayableAlert(UltraLakshaIllustrationResponseNew.MasterDataBean.DeathBenefitBean deathBenefitEntity) {

        if (alertDialog != null && alertDialog.isShowing()) {

            return;
        }


        UltraLakshaIllustrationResponseNew.MasterDataBean.BenefitsPopupBean  benefitsPopupEntity = ultraLakshaFacade.getBenefitPopupList().get(0);

        int period = Integer.valueOf(benefitsPopupEntity.getTerm()) -  Integer.valueOf(deathBenefitEntity.getYear());
      //  long JeevanMaturityDate  =    Long.valueOf(deathBenefitEntity.getJeevanBenefitsPayable()) -  (period * Long.valueOf(benefitsPopupEntity.getAnnualPayout()));
       // long UltraMaturityDate   =    Long.valueOf(deathBenefitEntity.getLakshyaBenefitsPayable()) -  (period * Long.valueOf(benefitsPopupEntity.getAnnualPayout()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);

        AppCompatImageView ivClose;
        TextView txtHdr , txtJeevanImmediately ,txtUltraImmediately, txtJeevanMaturityDate ,txtUltraMaturityDate, txtJeevanTotal ,txtUltraTotal, lblAnnualPayout, lblMonthlyPayout ,
                txtUltraMonthlyPayoutforFirst,txtUltraMonthlyPayoutfor5,txtJeevanAnnualPayoutfor ,txtUltraAnnualPayoutfor;
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_benefit_payable_to_nominee, null);


        builder.setView(dialogView);
        alertDialog = builder.create();
        // set the custom dialog components - text, image and button

        //region declaration
        ivClose =  dialogView.findViewById(R.id.ivClose);
        txtHdr =  dialogView.findViewById(R.id.txtHdr);

        lblAnnualPayout  =  dialogView.findViewById(R.id.lblAnnualPayout);
        lblMonthlyPayout  =  dialogView.findViewById(R.id.lblMonthlyPayout);

        txtUltraImmediately  =  dialogView.findViewById(R.id.txtUltraImmediately);

        txtJeevanMaturityDate  =  dialogView.findViewById(R.id.txtJeevanMaturityDate);
        txtUltraMaturityDate  =  dialogView.findViewById(R.id.txtUltraMaturityDate);

        txtJeevanTotal  =  dialogView.findViewById(R.id.txtJeevanTotal);
        txtUltraTotal  =  dialogView.findViewById(R.id.txtUltraTotal);

        txtUltraMonthlyPayoutforFirst  =  dialogView.findViewById(R.id.txtUltraMonthlyPayoutforFirst);
        txtUltraMonthlyPayoutfor5  =  dialogView.findViewById(R.id.txtUltraMonthlyPayoutfor5);

        txtJeevanAnnualPayoutfor  = dialogView.findViewById(R.id.txtJeevanAnnualPayoutfor);
        txtUltraAnnualPayoutfor = dialogView.findViewById(R.id.txtUltraAnnualPayoutfor);

        //endregion

        txtHdr.setText("Benefits payable to nominee in case of death in year "+ deathBenefitEntity.getYear());
        lblAnnualPayout.setText("Annual payout for years "+ period);
        lblMonthlyPayout.setText("Monthly payout for years "+ period);
        txtUltraImmediately.setText("" + getNumbeFormatComma(benefitsPopupEntity.getULOnDeath()));
        txtJeevanTotal.setText(""+getNumbeFormatComma(deathBenefitEntity.getJeevanBenefitsPayable()));
        txtUltraTotal.setText(""+ getNumbeFormatComma(deathBenefitEntity.getLakshyaBenefitsPayable()));
        txtUltraMonthlyPayoutforFirst.setText(""+ getNumbeFormatComma(benefitsPopupEntity.getMonthlyPayFirst()) );

        txtJeevanMaturityDate.setText("" + getNumbeFormatComma(benefitsPopupEntity.getMatDateJL()));
        txtUltraMaturityDate.setText("" + getNumbeFormatComma(benefitsPopupEntity.getMatDateJL()));  // temp 05
        txtUltraMonthlyPayoutfor5.setText("" + getNumbeFormatComma(benefitsPopupEntity.getMonthlyPayFiveYears()));

        txtJeevanAnnualPayoutfor.setText("" +getNumbeFormatComma(benefitsPopupEntity.getAnnualPayout()));
        txtUltraAnnualPayoutfor.setText("" + getNumbeFormatComma(benefitsPopupEntity.getAnnualPayout()));



        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });


        alertDialog.setCancelable(true);

        alertDialog.show();
        //  alertDialog.getWindow().setLayout(900, 600);


    }
}
