package com.policyboss.policybossproelite.motor.privatecar.fragment;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.helpfeedback.raiseticket.AddTicketActivity;
import com.policyboss.policybossproelite.motor.privatecar.activity.InputQuoteBottmActivity;
import com.policyboss.policybossproelite.motor.privatecar.adapter.ActivityTabsPagerAdapter;
import com.policyboss.policybossproelite.motor.privatecar.adapter.MotorQuoteAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.quoteapplication.QuoteApplicationController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.QuoteListEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.QuoteAppUpdateDeleteResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.QuoteApplicationResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class MotorQuoteFragment extends BaseFragment implements View.OnClickListener, IResponseSubcriber {

    public static final String FROM_QUOTE = "from_quote";
    FloatingActionButton btnAddQuote;
    RecyclerView rvQuoteList;
    MotorQuoteAdapter motorQuoteAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    List<QuoteListEntity> mQuoteList;
    QuoteListEntity removeQuoteEntity;
    ImageView ivSearch;
    TextView tvAdd, tvSearch;
    EditText etSearch;
    RecyclerView.LayoutManager layoutManager;
    boolean isHit = false;

    public MotorQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motor_quote, container, false);
        initView(view);
        setListener();
        setTextWatcher();
        mQuoteList = new ArrayList<>();
        if (getArguments().getParcelableArrayList(ActivityTabsPagerAdapter.QUOTE_LIST) != null) {
            mQuoteList = getArguments().getParcelableArrayList(ActivityTabsPagerAdapter.QUOTE_LIST);
        }

        rvQuoteList.setAdapter(null);
        motorQuoteAdapter = new MotorQuoteAdapter(MotorQuoteFragment.this, mQuoteList);
        rvQuoteList.setAdapter(motorQuoteAdapter);

        rvQuoteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastCompletelyVisibleItemPosition = 0;

                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();


                if (lastCompletelyVisibleItemPosition == mQuoteList.size() - 1) {
                    if (!isHit) {
                        isHit = true;
                        fetchMoreQuotes(mQuoteList.size());

                    }
                }
            }
        });
        return view;
    }

    private void setTextWatcher() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                motorQuoteAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setListener() {
        ivSearch.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }


    //redirect to input quote bottom
    public void redirectToInputQuote(QuoteListEntity entity) {
        startActivity(new Intent(getActivity(), InputQuoteBottmActivity.class).putExtra(FROM_QUOTE, entity));
    }

    private void initView(View view) {
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);

        tvAdd = (TextView) view.findViewById(R.id.tvAdd);
        tvSearch = (TextView) view.findViewById(R.id.tvSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setVisibility(View.INVISIBLE);

        btnAddQuote = (FloatingActionButton) view.findViewById(R.id.fbAddQuote);
        rvQuoteList = (RecyclerView) view.findViewById(R.id.rvQuoteList);
        rvQuoteList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvQuoteList.setLayoutManager(layoutManager);
        btnAddQuote.setOnClickListener(this);

    }

    public void removeQuote(QuoteListEntity entity) {

        removeQuoteEntity = entity;
        showDialog("Please wait.. removing quote");
        new QuoteApplicationController(getContext()).deleteQuote("" + entity.getVehicleRequestID(),
                this);

    }
    public void raiseticket(QuoteListEntity entity)
    {
//        getActivity().startActivity(new Intent(this.getActivity(), AddTicketActivity.class)
//                .putExtra("ProductType", "Motor")
//                .putExtra("crn",entity.getMotorRequestEntity().getCrn()));

        Intent intent = new Intent(this.getActivity(), AddTicketActivity.class);
        intent.putExtra("ProductType", "MOTOR");
        intent.putExtra("crn",entity.getMotorRequestEntity().getCrn());

       startActivity(intent);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvAdd:
            case R.id.fbAddQuote:
                startActivity(new Intent(getActivity(), InputQuoteBottmActivity.class));
                break;
            case R.id.tvSearch:
            case R.id.ivSearch:
                if (etSearch.getVisibility() == View.INVISIBLE) {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }

                break;

        }
    }


    public void fetchMoreQuotes(int count) {
        //showDialog("Fetching.., Please wait.!");


        new QuoteApplicationController(getActivity()).getQuoteAppList(count, 1, "", "",
                new DBPersistanceController(getActivity()).getUserData().getFBAId(),
                1,
                "",
                this);
    }

    //region server response

    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();
        if (response instanceof QuoteAppUpdateDeleteResponse) {
            if (response.getStatusNo() == 0) {
                mQuoteList.remove(removeQuoteEntity);

            }
        } else if (response instanceof QuoteApplicationResponse) {
            List<QuoteListEntity> list = ((QuoteApplicationResponse) response).getMasterData().getQuote();
            if (list.size() > 0) {
                isHit = false;
                for (QuoteListEntity entity : list) {
                    if (!mQuoteList.contains(entity)) {
                        mQuoteList.add(entity);
                    }
                }
            }


        }

        motorQuoteAdapter.refreshAdapter(mQuoteList);
        motorQuoteAdapter.notifyDataSetChanged();

    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
    }

    //endregion
}
