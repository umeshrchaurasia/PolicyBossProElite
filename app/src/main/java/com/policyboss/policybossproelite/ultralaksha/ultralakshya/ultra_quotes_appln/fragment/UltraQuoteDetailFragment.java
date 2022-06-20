package com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.fragment;


import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.MyApplication;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.UltraLakshyaTermBottmActivity;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.adapter.UltraTabsPagerAdapter;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.adapter.Ultra_QuoteDetailAdapter;
import com.policyboss.policybossproelite.utility.Constants;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.tracking.TrackingController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TrackingData;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TrackingRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.UltralakshaRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.loan_fm.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.loan_fm.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.loan_fm.requestentity.FmHomeLoanRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class UltraQuoteDetailFragment extends BaseFragment implements View.OnClickListener,IResponseSubcriber {


    public static final String ULTRA_FOR_INPUT_FRAGMENT = "Ultra_for_input";
    FloatingActionButton UltraAddQuote;
    RecyclerView rvQuoteList;
    Ultra_QuoteDetailAdapter mAapter;
    List<UltralakshaRequestEntity> mQuoteList;

    FmHomeLoanRequest removeQuoteEntity;

    ImageView ivSearch, ivAdd;
    TextView tvAdd, tvSearch;
    EditText etSearch;
    boolean isHit = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_ultra_quote_detail, container, false);
        initView(view);
        setListener();
        setTextWatcher();
        mQuoteList = new ArrayList<>();
        if (getArguments().getParcelableArrayList(UltraTabsPagerAdapter.QUOTE_LIST) != null) {
            mQuoteList = getArguments().getParcelableArrayList(UltraTabsPagerAdapter.QUOTE_LIST);

        }
        mAapter = new Ultra_QuoteDetailAdapter(UltraQuoteDetailFragment.this, mQuoteList);
        rvQuoteList.setAdapter(mAapter);

        return view;

    }

    private void initView(View view) {

        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);
        ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
        tvAdd = (TextView) view.findViewById(R.id.tvAdd);
        tvSearch = (TextView) view.findViewById(R.id.tvSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setVisibility(View.INVISIBLE);

        UltraAddQuote = (FloatingActionButton) view.findViewById(R.id.UltraAddQuote);

        rvQuoteList = (RecyclerView) view.findViewById(R.id.rvQuoteList);
        rvQuoteList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvQuoteList.setLayoutManager(layoutManager);


        UltraAddQuote.setOnClickListener(this);
    }

    private void setListener() {
        ivSearch.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }


    private void setTextWatcher() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch ((view.getId())) {

            case  R.id.UltraAddQuote:
                new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("ULTRA COMBO : Ultra Combo QUOTES ADD WITH FLAOTING BUTTON"), Constants.ULTRALAKSHA_COMBO), null);
                MyApplication.getInstance().trackEvent( Constants.HOME_LOAN,"Clicked","ULTRA COMBO QUOTES ADD WITH FLAOTING BUTTON");

                startActivity(new Intent(getActivity(), UltraLakshyaTermBottmActivity.class));
                break;

            case R.id.tvSearch:
            case R.id.ivSearch:

                new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("ULTRA COMBO : HOME LOAN QUOTES  SEARCH"), Constants.HOME_LOAN), null);

                MyApplication.getInstance().trackEvent( Constants.HOME_LOAN,"Clicked","ULTRA COMBO QUOTES SEARCH");


                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        etSearch.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                if (etSearch.getVisibility() == View.INVISIBLE) {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }

                break;
            case R.id.ivAdd:
            case R.id.tvAdd:
                new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("HOME LOAN : HOME LOAN QUOTES WITH TEXT BUTTON"), Constants.HOME_LOAN), null);

                MyApplication.getInstance().trackEvent( Constants.HOME_LOAN,"Clicked","ULTRA COMBO QUOTES WITH TEXT BUTTON");

                startActivity(new Intent(getActivity(), UltraLakshyaTermBottmActivity.class));
               break;
        }

    }


    public void redirectQuote(UltralakshaRequestEntity request) {


        Intent intent = new Intent(getActivity(), UltraLakshyaTermBottmActivity.class);
        intent.putExtra(ULTRA_FOR_INPUT_FRAGMENT, request);
        startActivity(intent);

    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

    }

    @Override
    public void OnFailure(Throwable t) {

    }
}
