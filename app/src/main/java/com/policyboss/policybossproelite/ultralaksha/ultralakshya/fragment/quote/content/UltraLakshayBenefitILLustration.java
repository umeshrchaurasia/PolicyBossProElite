package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content.adapter.UltraLakshyaBenefitIllustratorAdapter;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.database.UltraLakshaFacade;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LakshyaBenefitIllustratorEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshaIllustrationResponseNew;

/**
 * A simple {@link Fragment} subclass.
 */
public class UltraLakshayBenefitILLustration extends BaseFragment {


    RecyclerView rvDeathBenefit;
    TextView txtCashFlow;
    List<LakshyaBenefitIllustratorEntity> BenefitsIllustratorLst;
    UltraLakshyaBenefitIllustratorAdapter mAdapter;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginEntity;
    PrefManager prefManager;
    UltraLakshaFacade ultraLakshaFacade;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ultra_lakshay_illutrator, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);

        ultraLakshaFacade = new UltraLakshaFacade(getActivity());

        if (ultraLakshaFacade.getBenefitList() != null) {

            UltraLakshaIllustrationResponseNew.MasterDataBean.BenefitsBean benefitsEntity = ultraLakshaFacade.getBenefitList().get(ultraLakshaFacade.getBenefitList().size() - 1);
            txtCashFlow.setText("" + benefitsEntity.getCashFlow());
            mAdapter = new UltraLakshyaBenefitIllustratorAdapter(UltraLakshayBenefitILLustration.this, ultraLakshaFacade.getBenefitList());
            rvDeathBenefit.setAdapter(mAdapter);
        } else {
            txtCashFlow.setText("");
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize(View view) {

        prefManager = new PrefManager(getActivity());
        BenefitsIllustratorLst = new ArrayList<LakshyaBenefitIllustratorEntity>();

        prefManager.setNotificationCounter(0);
        txtCashFlow = (TextView) view.findViewById(R.id.txtCashFlow);
        rvDeathBenefit = (RecyclerView) view.findViewById(R.id.rvDeathBenefit);
        rvDeathBenefit.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvDeathBenefit.setLayoutManager(layoutManager);


    }

    private List<LakshyaBenefitIllustratorEntity> getDeathNomineeLst() {
        BenefitsIllustratorLst.clear();
        for (int i = 1; i < 20; i++) {
            LakshyaBenefitIllustratorEntity obj = new LakshyaBenefitIllustratorEntity();
            obj.setYear("" + i);
            obj.setAge("" + 19 + i);
            obj.setAnnualPremium("" + 700 + i);
            obj.setCashFlow("" + 178900 + i);
            obj.setLicCover("" + 3347900 + i);
            obj.setLoanAvailable("" + 6747900 + i);
            BenefitsIllustratorLst.add(obj);
        }

        return BenefitsIllustratorLst;
    }

}
