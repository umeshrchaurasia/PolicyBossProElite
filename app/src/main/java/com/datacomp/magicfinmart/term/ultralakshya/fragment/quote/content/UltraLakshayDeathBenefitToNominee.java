package com.datacomp.magicfinmart.term.ultralakshya.fragment.quote.content;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datacomp.magicfinmart.BaseFragment;
import com.datacomp.magicfinmart.R;
import com.datacomp.magicfinmart.term.ultralakshya.fragment.quote.content.adapter.UltraLakshyaDeathNomineeAdapter;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.database.UltraLakshaFacade;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.DeathBenefitEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;

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
        }else{
            mAdapter = new UltraLakshyaDeathNomineeAdapter(UltraLakshayDeathBenefitToNominee.this, getDeathNomineeLst());
            rvDeathBenefit.setAdapter(mAdapter);  //temp data

          //  Toast.makeText(getActivity(),"No data found",Toast.LENGTH_SHORT).show();
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
            obj.setJeevanBenefitsPayable("" +500 +i);
            obj.setJeevanPremiumPaid(""+700 +i);
            obj.setLakshyaBenefitsPayable(""+178900 +i);
            obj.setLakshyaPremiumPaid(""+3347900 +i);
            DeathBenefitLst.add(obj);
        }

        return DeathBenefitLst;
    }


    public void showDeathPayableAlert() {

        if (alertDialog != null && alertDialog.isShowing()) {

            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);

        AppCompatImageView ivClose;
        TextView txtHdr ,
                txtJeevanImmediately ,txtUltraImmediately,
                txtJeevanAnnualPayoutfor9 ,txtUltraAnnualPayoutfor9,
                txtJeevanMonthlyPayoutfor9,txtUltraMonthlyPayoutfor9,
                txtJeevanMaturityDate ,txtUltraMaturityDate,
                txtJeevanMonthlyPayoutfor5 ,txtUltraMonthlyPayoutfor5,
                txtJeevanTotal ,txtUltraTotal;


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_benefit_payable_to_nominee, null);


        builder.setView(dialogView);
        alertDialog = builder.create();
        // set the custom dialog components - text, image and button
        ivClose =  dialogView.findViewById(R.id.ivClose);
        txtHdr =  dialogView.findViewById(R.id.txtHdr);
        txtJeevanImmediately  =  dialogView.findViewById(R.id.txtJeevanImmediately);
        txtUltraImmediately  =  dialogView.findViewById(R.id.txtUltraImmediately);

        txtJeevanAnnualPayoutfor9  =  dialogView.findViewById(R.id.txtJeevanAnnualPayoutfor9);
        txtUltraAnnualPayoutfor9  =  dialogView.findViewById(R.id.txtUltraAnnualPayoutfor9);

        txtJeevanMonthlyPayoutfor9  =  dialogView.findViewById(R.id.txtJeevanMonthlyPayoutfor9);
        txtUltraMonthlyPayoutfor9  =  dialogView.findViewById(R.id.txtUltraMonthlyPayoutfor9);

        txtJeevanMaturityDate  =  dialogView.findViewById(R.id.txtJeevanMaturityDate);
        txtUltraMaturityDate  =  dialogView.findViewById(R.id.txtUltraMaturityDate);

        txtJeevanMonthlyPayoutfor5  =  dialogView.findViewById(R.id.txtJeevanMonthlyPayoutfor5);
        txtUltraMonthlyPayoutfor5  =  dialogView.findViewById(R.id.txtUltraMonthlyPayoutfor5);

        txtJeevanTotal  =  dialogView.findViewById(R.id.txtJeevanTotal);
        txtUltraTotal  =  dialogView.findViewById(R.id.txtUltraTotal);


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