package com.policyboss.policybossproelite.offline_quotes;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.offline_quotes.OfflineQuoteListAdapter.QuoteListRowAdapter;
import com.policyboss.policybossproelite.webviews.CommonWebViewActivity;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.offline_quotes.OfflineQuotesController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.DocumentsOfflineEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.OfflineQuoteEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.OfflineQuoteResponse;

public class OfflineQuotesListActivity extends BaseActivity implements IResponseSubcriber {
    public static final String OFFLINE_FROM = "offline_from_list";
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginEntity;
    RecyclerView rvOffline;
    List<OfflineQuoteEntity> offlineQuoteList;
    QuoteListRowAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_quotes_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbPersistanceController = new DBPersistanceController(this);
        loginEntity = dbPersistanceController.getUserData();

        initialize();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(OfflineQuotesListActivity.this, AddOfflineQuotesActivity.class));
            }
        });

    }

    private void initialize() {


        rvOffline = (RecyclerView) findViewById(R.id.rvOffline);
        rvOffline.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OfflineQuotesListActivity.this);
        rvOffline.setLayoutManager(layoutManager);


    }

    public void redirectToQuoteList(DocumentsOfflineEntity docuEntity)
    {
      //  Toast.makeText(this,""+strName,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, CommonWebViewActivity.class)
                .putExtra("URL", docuEntity.getDocument_path())
                .putExtra("NAME", "OfflineQuotes")
                .putExtra("TITLE", "" +docuEntity.getDocument_name()));

    }
    public void redirectToEdit(OfflineQuoteEntity quoteEntity )
    {
        Intent intent  = new Intent(this,AddOfflineQuotesActivity.class);
        intent.putExtra(OFFLINE_FROM,quoteEntity);
        startActivity(intent);

    }



    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof OfflineQuoteResponse) {

            if (response.getStatusNo() == 0) {

                offlineQuoteList = ((OfflineQuoteResponse) response).getMasterData();

                mAdapter = new QuoteListRowAdapter(OfflineQuotesListActivity.this, offlineQuoteList);
                rvOffline.setAdapter(mAdapter);

            }
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showDialog();
        new OfflineQuotesController(this).getOfflineQuote(OfflineQuotesListActivity.this);

    }
}
