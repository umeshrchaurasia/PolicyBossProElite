package com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.health;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.health.adapter.OfflineHealthListItemAdapter;
import com.policyboss.policybossproelite.utility.Constants;

import java.util.ArrayList;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.offline_quotes.OfflineQuotesController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.HealthQuote;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.OfflineHealthResponse;

public class OfflineHealthListActivity extends BaseActivity implements View.OnClickListener, IResponseSubcriber {


    FloatingActionButton fbOfflineHealth;
    RecyclerView rbOfflineHealth;
    boolean isHit = false;
    List<HealthQuote> offlineHealthList;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginEntity;
    OfflineHealthListItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_health_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        offlineHealthList = new ArrayList<HealthQuote>();

        dbPersistanceController = new DBPersistanceController(this);
        loginEntity = dbPersistanceController.getUserData();

        init_widgets();
        setListeners();

        offlineHealthList = new ArrayList<HealthQuote>();
        fetchHealthQuotes(0);

    }

    private void init_widgets() {
        fbOfflineHealth = (FloatingActionButton) findViewById(R.id.fbOfflineHealth);
        rbOfflineHealth = (RecyclerView) findViewById(R.id.rbOfflineHealth);
        rbOfflineHealth.setHasFixedSize(true);
        rbOfflineHealth.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setListeners() {
        fbOfflineHealth.setOnClickListener(this);

        rbOfflineHealth.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastCompletelyVisibleItemPosition = 0;

                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();


                if (lastCompletelyVisibleItemPosition == offlineHealthList.size() - 1 && offlineHealthList.size() > 6) {
                    if (!isHit) {
                        isHit = true;
                        fetchHealthQuotes(offlineHealthList.size());

                    }
                }
            }
        });
    }

    private void fetchHealthQuotes(int count) {
        //TODO: Fetch offline quote
        showDialog("Loading offline list");
        new OfflineQuotesController(this).getOfflineHealthList(String.valueOf(loginEntity.getFBAId()), count, this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fbOfflineHealth) {

            //  startActivity(new Intent(this, InputOfflineHealthActivity.class));

            Intent intent = new Intent(this, InputOfflineHealthActivity.class);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();
        if (response instanceof OfflineHealthResponse) {

            List<HealthQuote> list = ((OfflineHealthResponse) response).getMasterData();
            if (list.size() > 0) {

                isHit = false;

                for (HealthQuote entity : list) {

                    if (!offlineHealthList.contains(entity))
                        offlineHealthList.add(entity);

                }
            }

            mAdapter = new OfflineHealthListItemAdapter(this, offlineHealthList);
            rbOfflineHealth.setAdapter(mAdapter);

        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
    }

    public void editOfflineHealth(HealthQuote entity) {


        Intent intent = new Intent(this, InputOfflineHealthActivity.class);
        intent.putExtra(Constants.OFFLINE_HEALTH_EDIT, entity);
        startActivityForResult(intent, 2);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            if (data != null) {
                if (data.hasExtra("MESSAGE")) {
                    offlineHealthList = new ArrayList<HealthQuote>();
                    fetchHealthQuotes(0);
                }
            }
        }
    }
}
