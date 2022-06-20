package com.policyboss.policybossproelite.motor.privatecar.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.motor.privatecar.activity.InputQuoteBottmActivity;
import com.policyboss.policybossproelite.motor.privatecar.adapter.ActivityTabsPagerAdapter;
import com.policyboss.policybossproelite.motor.privatecar.adapter.MotorLeadAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.quoteapplication.QuoteApplicationController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LeadDispositionEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.MotorMyLeadEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.QuoteListEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.LeadDispositionResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.LeadDispositionSaveResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.MotorLeadResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.MotorViewLeadResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.QuoteApplicationResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class MotorLeadFragment extends BaseFragment implements View.OnClickListener, IResponseSubcriber {

    public static final String FROM_LEAD = "from_lead";

    RecyclerView rvLeadList;
    MotorLeadAdapter motorLeadAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    List<MotorMyLeadEntity> mLeadList;
    List<LeadDispositionEntity> leadDispoList;
    QuoteListEntity removeQuoteEntity;
    ImageView ivSearch;
    TextView tvSearch;
    EditText etSearch;
    RecyclerView.LayoutManager layoutManager;
    boolean isHit = false;
    String Type = "";
    AlertDialog finmartCommentDialog;

    ArrayAdapter<String> leadDispositAdapter;
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    ArrayList<String> arrayLeadDisp;

    MotorMyLeadEntity myLeadEntityEdit;

    public MotorLeadFragment() {
        // Required empty public constructor
    }

    //LEAD_LIST

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_motor_lead, container, false);
        arrayLeadDisp = new ArrayList<String>();
        initView(view);
        setListener();
        setTextWatcher();

        mLeadList = new ArrayList<>();
        if (getArguments().getParcelableArrayList(ActivityTabsPagerAdapter.LEAD_LIST) != null) {
            mLeadList = getArguments().getParcelableArrayList(ActivityTabsPagerAdapter.LEAD_LIST);
        }

        fetchLeadDisposition();
        rvLeadList.setAdapter(null);
        motorLeadAdapter = new MotorLeadAdapter(MotorLeadFragment.this, mLeadList);
        rvLeadList.setAdapter(motorLeadAdapter);

        rvLeadList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastCompletelyVisibleItemPosition = 0;

                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();


                if (lastCompletelyVisibleItemPosition == mLeadList.size() - 1) {
                    if (!isHit) {
                        isHit = true;
                        fetchMoreLeads(mLeadList.size());

                    }
                }
            }
        });


        return view;
    }

    public void fetchMoreLeads(int count) {
        //showDialog("Fetching.., Please wait.!");


        new QuoteApplicationController(getActivity()).getQuoteAppList(count, 3, "", "",
                new DBPersistanceController(getActivity()).getUserData().getFBAId(),
                1,
                "",
                this);
    }

    public void fetchLeadDisposition() {
        new QuoteApplicationController(getActivity()).getLeadDispositionMaster(this);
    }

    private void setListener() {
        ivSearch.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }

    private void setTextWatcher() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                motorLeadAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initView(View view) {
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);

        tvSearch = (TextView) view.findViewById(R.id.tvSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setVisibility(View.INVISIBLE);

        rvLeadList = (RecyclerView) view.findViewById(R.id.rvLeadList);
        rvLeadList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvLeadList.setLayoutManager(layoutManager);
        leadDispoList = new ArrayList<LeadDispositionEntity>();

    }


    //redirect to input quote bottom
    public void redirectToInputQuote(MotorMyLeadEntity entity) {

        showDialog();
        new QuoteApplicationController(getActivity()).ModifyLead(entity, this);

    }

    public void redirectViewToInputQuote(MotorMyLeadEntity entity) {

        showDialog();
        new QuoteApplicationController(getActivity()).ViewLead(entity.getVehicleRequestID(), String.valueOf(entity.getLeadId()), this);

    }

    public void redirectEditToInputQuote(MotorMyLeadEntity entity) {

        showDialog();
        new QuoteApplicationController(getActivity()).EditLead(entity.getVehicleRequestID(), String.valueOf(entity.getLeadId()), this);

    }

    public void redirectToCommenttAlert(MotorMyLeadEntity entity) {


        if (finmartCommentDialog != null && finmartCommentDialog.isShowing()) {

            return;
        } else if (leadDispoList.size() > 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
            myLeadEntityEdit = entity;

            // LeadDispositionEntity leadDispositionEntity;
            Button btnAllow;
            EditText etRemark;
            Spinner spDisp;
            TextView txtTile, txtBody, txtMob;
            ImageView ivCross;


            LayoutInflater inflater = this.getLayoutInflater();

            final View dialogView = inflater.inflate(R.layout.layout_lead_comment_popup, null);

            builder.setView(dialogView);
            finmartCommentDialog = builder.create();
            // set the custom dialog components - text, image and button
            txtTile = dialogView.findViewById(R.id.txtTile);
            ivCross = dialogView.findViewById(R.id.ivCross);

            btnAllow = dialogView.findViewById(R.id.btnAllow);
            spDisp = dialogView.findViewById(R.id.spDisp);
            etRemark = dialogView.findViewById(R.id.etRemark);


            leadDispositAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_custom_text, arrayLeadDisp);
            leadDispositAdapter.setDropDownViewResource(R.layout.layout_custom_text);
            spDisp.setAdapter(leadDispositAdapter);


            btnAllow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //map.get(spDisp.getSelectedItem().toString()
                    if (map.get(spDisp.getSelectedItem().toString()).equals("0")) {
                        Toast.makeText(getActivity(), "Select Disposition..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (etRemark.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Enter Comment..", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        finmartCommentDialog.dismiss();
                        showDialog();
                        new QuoteApplicationController(getActivity()).saveLeadDisposition(
                                String.valueOf(entity.getLeadId()),
                                map.get(spDisp.getSelectedItem().toString()),
                                entity.getVehicleRequestID(),
                                etRemark.getText().toString(), MotorLeadFragment.this);


                    }

                }
            });


            ivCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finmartCommentDialog.dismiss();

                }
            });
            finmartCommentDialog.setCancelable(false);
            finmartCommentDialog.show();
        } else {
            Toast.makeText(getActivity(), "No Lead Disposition Found..", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvSearch:
            case R.id.ivSearch:
                if (etSearch.getVisibility() == View.INVISIBLE) {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }

                break;

        }

    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();
        if (response instanceof QuoteApplicationResponse) {
            List<MotorMyLeadEntity> list = ((QuoteApplicationResponse) response).getMasterData().getMyleads();
            if (list.size() > 0) {
                isHit = false;
                for (MotorMyLeadEntity entity : list) {
                    if (!mLeadList.contains(entity)) {
                        mLeadList.add(entity);
                    }
                }


            }
            motorLeadAdapter.refreshAdapter(mLeadList);
            motorLeadAdapter.notifyDataSetChanged();

        } else if (response instanceof MotorLeadResponse) {

            QuoteListEntity entity = ((MotorLeadResponse) response).getMasterData();


            startActivity(new Intent(getActivity(), InputQuoteBottmActivity.class).putExtra(FROM_LEAD, entity));
        } else if (response instanceof MotorViewLeadResponse) {

            QuoteListEntity entity = ((MotorViewLeadResponse) response).getMasterData();

            startActivity(new Intent(getActivity(), InputQuoteBottmActivity.class).putExtra(FROM_LEAD, entity));
        } else if (response instanceof LeadDispositionResponse) {

            leadDispoList = ((LeadDispositionResponse) response).getMasterData();

            arrayLeadDisp = getLeadDispList(leadDispoList);

        } else if (response instanceof LeadDispositionSaveResponse) {


            motorLeadAdapter.updateDispostion(myLeadEntityEdit);
            Toast.makeText(getActivity(), ((LeadDispositionSaveResponse) response).getMessage(), Toast.LENGTH_SHORT).show();


        }


    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getLeadDispList(List<LeadDispositionEntity> leadDispoList) {
        map.clear();
        map.put("SELECT", "0");
        for (LeadDispositionEntity entity : leadDispoList) {
            map.put(entity.getDisposition().toUpperCase(), String.valueOf(entity.getId()));    // adding in Map
        }
        arrayLeadDisp.clear();
        arrayLeadDisp = new ArrayList<String>(map.keySet());
        return arrayLeadDisp;
    }


}
