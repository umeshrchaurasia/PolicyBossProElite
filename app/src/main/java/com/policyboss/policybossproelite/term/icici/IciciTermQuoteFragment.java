package com.policyboss.policybossproelite.term.icici;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.webviews.CommonWebViewActivity;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.term.TermInsuranceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TermCompareResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TermFinmartRequest;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TermRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.TermCompareQuoteResponse;

/**
 * Created by Rajeev Ranjan on 06/04/2018.
 */

public class IciciTermQuoteFragment extends BaseFragment implements View.OnClickListener, BaseFragment.PopUpListener, IResponseSubcriber {
    TermFinmartRequest termFinmartRequest;
    TermRequestEntity termRequestEntity;
    TextView tvSum, tvGender, tvSmoker, tvAge, tvPolicyTerm, tvCrn, filter;
    ImageView ivEdit;
    //TermQuoteAdapter mAdapter;
    TermCompareQuoteResponse termCompareQuoteResponse;
    // RecyclerView rvTerm;

    private final Handler handler = new Handler();
    private Runnable runPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_term_icici_quote, container, false);
        registerPopUp(this);
        initView(view);
        setListener();
        if (getArguments() != null) {
            if (getArguments().getParcelable(IciciTermActivity.QUOTE_DATA) != null) {
                termFinmartRequest = new TermFinmartRequest();
                termFinmartRequest = getArguments().getParcelable(IciciTermActivity.QUOTE_DATA);
                termRequestEntity = termFinmartRequest.getTermRequestEntity();
                bindHeaders();
                fetchQuotes();
            }
        }

        return view;
    }

    private void initView(View view) {
        //rvTerm = (RecyclerView) view.findViewById(R.id.rvTerm);
        //rvTerm.setLayoutManager(new LinearLayoutManager(getActivity()));
        //rvTerm.setHasFixedSize(true);
        tvSum = (TextView) view.findViewById(R.id.tvSum);
        tvGender = (TextView) view.findViewById(R.id.tvGender);
        tvSmoker = (TextView) view.findViewById(R.id.tvSmoker);
        tvAge = (TextView) view.findViewById(R.id.tvAge);
        tvPolicyTerm = (TextView) view.findViewById(R.id.tvPolicyTerm);
        tvCrn = (TextView) view.findViewById(R.id.tvCrn);
        ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        filter = (TextView) view.findViewById(R.id.filter);
    }

    private void setListener() {
        ivEdit.setOnClickListener(this);
        filter.setOnClickListener(this);
    }

    private void bindHeaders() {
        if (termRequestEntity != null) {
            tvSum.setText("" + termRequestEntity.getSumAssured());
            if (termRequestEntity.getInsuredGender().equals("M"))
                tvGender.setText("MALE");
            else
                tvGender.setText("FEMALE");
            if (termRequestEntity.getIs_TabaccoUser().equals("true"))
                tvSmoker.setText("SMOKER");
            else
                tvSmoker.setText("NON-SMOKER");
            tvAge.setText("" + termRequestEntity.getInsuredDOB());
            tvPolicyTerm.setText("" + termRequestEntity.getPolicyTerm() + " YEARS");
            tvCrn.setText("---");
        }
    }

    private void fetchQuotes() {
        showDialog();
        new TermInsuranceController(getActivity()).getTermInsurer(termFinmartRequest, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter:
            case R.id.ivEdit:
                ((IciciTermActivity) getActivity()).redirectToInput(termFinmartRequest);
                break;
        }
    }

    @Override
    public void onPositiveButtonClick(Dialog dialog, View view) {
        dialog.cancel();
    }

    @Override
    public void onCancelButtonClick(Dialog dialog, View view) {
        dialog.cancel();
    }

    public void redirectToBuy(TermCompareResponseEntity entity) {
        if (Utility.checkShareStatus(getActivity()) == 1) {

            //convert quote to application server
               /* new QuoteApplicationController(getActivity()).convertQuoteToApp(
                        "" + saveQuoteEntity.getVehicleRequestID(),
                        entity.getInsurer_Id(), imgPath,
                        this);*/

            startActivity(new Intent(getActivity(), CommonWebViewActivity.class)
                    .putExtra("URL", entity.getProposerPageUrl())
                    .putExtra("NAME", "" + entity.getInsurerName())
                    .putExtra("TITLE", "" + entity.getInsurerName()));

        } else {
            openPopUp(ivEdit, "Message", "Your POSP status is INACTIVE", "OK", true);
        }


    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof TermCompareQuoteResponse) {
            cancelDialog();
            this.termCompareQuoteResponse = (TermCompareQuoteResponse) response;
            //mAdapter = new TermQuoteAdapter(IciciTermQuoteFragment.this, termCompareQuoteResponse);
            //rvTerm.setAdapter(mAdapter);
            tvCrn.setText("" + termCompareQuoteResponse.getMasterData().getResponse().get(0).getCustomerReferenceID());
        }

    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

}
