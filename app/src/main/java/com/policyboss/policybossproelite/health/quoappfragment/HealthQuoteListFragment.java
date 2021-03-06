package com.policyboss.policybossproelite.health.quoappfragment;


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
import com.policyboss.policybossproelite.health.HealthActivityTabsPagerAdapter;
import com.policyboss.policybossproelite.health.healthquotetabs.HealthQuoteBottomTabsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.health.HealthController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.HealthQuote;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.HealthDeleteResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.HealthQuoteAppResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthQuoteListFragment extends BaseFragment implements View.OnClickListener, IResponseSubcriber {

    public static final String HEALTH_INPUT_FRAGMENT = "input_fragment_bottom";
    public static final String FROM_QUOTE = "from_quote";

    FloatingActionButton btnAddQuote;
    RecyclerView rvHealthQuoteList;
    HealthQuoteAdapter healthQuoteAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    List<HealthQuote> mQuoteList;
    HealthQuote removeQuoteEntity;
    TextView tvAdd, tvSearch;
    EditText etSearch;
    ImageView ivSearch;
    boolean isHit = false;
    RecyclerView.LayoutManager layoutManager;

    public HealthQuoteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_quote, container, false);
        initView(view);
        setListener();
        setTextWatcher();
        mQuoteList = new ArrayList<>();
        if (getArguments().getParcelableArrayList(HealthActivityTabsPagerAdapter.HEALTH_QUOTE_LIST) != null) {
            mQuoteList = getArguments().getParcelableArrayList(HealthActivityTabsPagerAdapter.HEALTH_QUOTE_LIST);
        }
        healthQuoteAdapter = new HealthQuoteAdapter(HealthQuoteListFragment.this, mQuoteList);
        rvHealthQuoteList.setAdapter(healthQuoteAdapter);

        rvHealthQuoteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void fetchMoreQuotes(int size) {

        //  showDialog("Fetching.., Please wait.!");
        new HealthController(getActivity()).getHealthQuoteApplicationList(size, 1,
                String.valueOf(new DBPersistanceController(getActivity()).getUserData().getFBAId()),
                this);

    }

    /*
        Redirect to health quote to show all quote
     */
    public void quoteItemClick(HealthQuote healthQuote) {
        Intent intent = new Intent(getActivity(), HealthQuoteBottomTabsActivity.class);
        intent.putExtra(HEALTH_INPUT_FRAGMENT, healthQuote);
        startActivity(intent);
    }

    //redirect to input quote bottom
    public void redirectToInputQuote(HealthQuote healthQuote) {
        startActivity(new Intent(getActivity(), HealthQuoteBottomTabsActivity.class).putExtra(FROM_QUOTE, healthQuote));
    }


    private void initView(View view) {
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);
        tvAdd = (TextView) view.findViewById(R.id.tvAdd);
        tvSearch = (TextView) view.findViewById(R.id.tvSearch);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setVisibility(View.INVISIBLE);
        btnAddQuote = (FloatingActionButton) view.findViewById(R.id.fbAddHealthQuote);
        rvHealthQuoteList = (RecyclerView) view.findViewById(R.id.rvHealthQuoteList);
        rvHealthQuoteList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvHealthQuoteList.setLayoutManager(layoutManager);
        btnAddQuote.setOnClickListener(this);

    }

    private void setListener() {
        ivSearch.setOnClickListener(this);
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
                healthQuoteAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void removeQuote(HealthQuote entity) {

        removeQuoteEntity = entity;
        showDialog("Please wait.. removing quote");
        new HealthController(getContext()).deleteQuote("" + entity.getHealthRequestId(),
                this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvAdd:
            case R.id.fbAddHealthQuote:
                startActivity(new Intent(getActivity(), HealthQuoteBottomTabsActivity.class));
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


    //region server response

    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();
        if (response instanceof HealthDeleteResponse) {
            if (response.getStatusNo() == 0) {
                mQuoteList.remove(removeQuoteEntity);

            }
        } else if (response instanceof HealthQuoteAppResponse) {
            if (((HealthQuoteAppResponse) response).getMasterData() != null) {

                List<HealthQuote> list = ((HealthQuoteAppResponse) response).getMasterData().getQuote();
                if (list.size() > 0) {
                    isHit = false;

                    for (HealthQuote entity : list) {
                        if (!mQuoteList.contains(entity)) {
                            mQuoteList.add(entity);
                        }
                    }
                }
            }

        }
        healthQuoteAdapter.refreshAdapter(mQuoteList);
        healthQuoteAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
    }


    //endregion
}
