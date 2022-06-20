package com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.Offline_Term;


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


import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.offline_quotes.OfflineQuotesController;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TermFinmartRequest;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.DeleteTermQuoteResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.TermQuoteApplicationResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermQuoteListFragment_offline extends BaseFragment implements View.OnClickListener, IResponseSubcriber {
    public static final String TERM_INPUT_FRAGMENT = "input_term_fragment_bottom";
    public static final String TERM_FOR_INPUT_FRAGMENT = "for_term_input";
    FloatingActionButton fbAddQuote;
    int compId = 28;

    List<TermFinmartRequest> listQuote;
    TermQuoteAdapter_offline mAdapter;
    RecyclerView rvTermQuote;

    TextView tvAdd, tvSearch;
    EditText etSearch;
    ImageView ivSearch;
    boolean isHit = false;
    TermFinmartRequest removeQuoteEntity;

    public TermQuoteListFragment_offline() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_term_quote_tab, container, false);
        init(view);
        setListener();
        setTextWatcher();
        listQuote = new ArrayList<>();
        if (getArguments().getParcelableArrayList(TermActivityTabsPagerAdapter_offline.TERM_QUOTE_LIST) != null) {
            listQuote = getArguments().getParcelableArrayList(TermActivityTabsPagerAdapter_offline.TERM_QUOTE_LIST);
        }
        compId = ((TermQuoteApplicationActivity_offline) getActivity()).getCompId();
        mAdapter = new TermQuoteAdapter_offline(this, listQuote);
        rvTermQuote.setAdapter(mAdapter);

        rvTermQuote.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastCompletelyVisibleItemPosition = 0;

                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();


                if (lastCompletelyVisibleItemPosition == listQuote.size() - 1 && listQuote.size() > 6) {
                    if (!isHit) {
                        isHit = true;
                        fetchMoreQuotes(listQuote.size());

                    }
                }
            }
        });
        return view;
    }

    private void setListener() {
        ivSearch.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        fbAddQuote.setOnClickListener(this);
    }

    private void init(View view) {
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);
        tvAdd = (TextView) view.findViewById(R.id.tvAdd);
        tvSearch = (TextView) view.findViewById(R.id.tvSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setVisibility(View.INVISIBLE);
        rvTermQuote = (RecyclerView) view.findViewById(R.id.rvTermQuote);
        rvTermQuote.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvTermQuote.setLayoutManager(layoutManager);
        fbAddQuote = (FloatingActionButton) view.findViewById(R.id.fbAddQuote);
    }


    private void setTextWatcher() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        compId = ((TermQuoteApplicationActivity_offline) getActivity()).getCompId();
        if (v.getId() == R.id.fbAddQuote || v.getId() == R.id.tvAdd) {
            Intent intentHdfc = new Intent(getActivity(), HdfcTermActivity_offline.class);
            intentHdfc.putExtra(TERM_FOR_INPUT_FRAGMENT, 28);
            //intent.putExtra(TERM_INPUT_FRAGMENT, null);
            startActivity(intentHdfc);
            /*
            switch (compId) {
                case 0://compare term
                    Intent intentCompare = new Intent(getActivity(), CompareTermActivity.class);
                    intentCompare.putExtra(TERM_FOR_INPUT_FRAGMENT, 0);
                    //intent.putExtra(TERM_INPUT_FRAGMENT, null);
                    startActivity(intentCompare);
                    break;
                case 43://edelwise
                case 28://hdfc
                    Intent intentHdfc = new Intent(getActivity(), HdfcTermActivity.class);
                    intentHdfc.putExtra(TERM_FOR_INPUT_FRAGMENT, 28);
                    //intent.putExtra(TERM_INPUT_FRAGMENT, null);
                    startActivity(intentHdfc);
                    break;
                case 1://tata aig
                    callInputTerm(compId, null);
                    break;
                case 39://icici
                    Intent intent = new Intent(getActivity(), IciciTermActivity.class);
                    intent.putExtra(TERM_FOR_INPUT_FRAGMENT, 39);
                    //intent.putExtra(TERM_INPUT_FRAGMENT, null);
                    startActivity(intent);
                    break;
            }*/
        } else if (v.getId() == R.id.tvSearch
                || v.getId() == R.id.ivSearch) {
            if (etSearch.getVisibility() == View.INVISIBLE) {
                etSearch.setVisibility(View.VISIBLE);
                etSearch.requestFocus();
            }
        }

    }

    public void callInputTerm(int whichTerm, TermFinmartRequest termFinmartRequest) {
        /*Intent intent = new Intent(getActivity(), CompareTermActivity.class);
        intent.putExtra(TERM_FOR_INPUT_FRAGMENT, whichTerm);
        intent.putExtra(TERM_INPUT_FRAGMENT, termFinmartRequest);
        startActivity(intent);*/

        Intent intent = new Intent(getActivity(), HdfcTermActivity_offline.class);
        intent.putExtra(TERM_FOR_INPUT_FRAGMENT, whichTerm);
        intent.putExtra(TERM_INPUT_FRAGMENT, termFinmartRequest);
        startActivity(intent);

    }

    public void removeQuote(TermFinmartRequest entity) {

        removeQuoteEntity = entity;
        showDialog("Please wait.. removing quote");
//        new TermInsuranceController(getActivity()).deleteTermQuote("" + entity.getTermRequestId(),
//                this);
    }

    public void fetchMoreQuotes(int count) {
        //showDialog("Fetching.., Please wait.!");

        new OfflineQuotesController(getActivity()).getTermQuoteApplicationList_offline(28, count, "1", this);


    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof TermQuoteApplicationResponse) {
            List<TermFinmartRequest> list = ((TermQuoteApplicationResponse) response).getMasterData().getQuote();
            if (list.size() > 0) {
                isHit = false;
                //Toast.makeText(getActivity(), "fetching more...", Toast.LENGTH_SHORT).show();

                for (TermFinmartRequest entity : list) {
                    if (!listQuote.contains(entity)) {
                        listQuote.add(entity);
                    }
                }
            }


        } else if (response instanceof DeleteTermQuoteResponse) {
            cancelDialog();
            if (response.getStatusNo() == 0) {
                listQuote.remove(removeQuoteEntity);

            }
        }

        mAdapter.refreshAdapter(listQuote);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
    }
}
