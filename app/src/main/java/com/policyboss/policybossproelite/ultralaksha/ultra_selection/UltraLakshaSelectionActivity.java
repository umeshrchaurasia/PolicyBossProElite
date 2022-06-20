package com.policyboss.policybossproelite.ultralaksha.ultra_selection;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.activity.UltraLakshyaDetailActivity;
import com.policyboss.policybossproelite.utility.Constants;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.tracking.TrackingController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TrackingData;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TrackingRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.model.TermSelectionEntity;

public class UltraLakshaSelectionActivity extends BaseActivity {
    RecyclerView rvUltraLakshSelection;
    UltraSelectionItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultralaksha_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {
        rvUltraLakshSelection = (RecyclerView) findViewById(R.id.rvUltraLakshSelection);
        rvUltraLakshSelection.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUltraLakshSelection.setLayoutManager(layoutManager);
        mAdapter = new UltraSelectionItemAdapter(this);
        rvUltraLakshSelection.setAdapter(mAdapter);
    }

    public void planClick(TermSelectionEntity entity) {

//        startActivity(new Intent(this, UltraLakshyaTermBottmActivity.class).putExtra(Constants.LIFE_INS, entity.getCompantID()));
//        new TrackingController(this).sendData(new TrackingRequestEntity(new TrackingData(entity.getCompanyName() + " Selection"), Constants.ULTRALAKSHA_INS), null);


        startActivity(new Intent(this, UltraLakshyaDetailActivity.class));
        new TrackingController(this).sendData(new TrackingRequestEntity(new TrackingData(entity.getCompanyName() + " Selection"), Constants.ULTRALAKSHA_INS), null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:
                onBackPressed();
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("MarkTYPE", "FROM_HOME");
//                startActivity(intent);
//
//                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public void onBackPressed() {
//
//        supportFinishAfterTransition();
//        super.onBackPressed();
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
        // return super.onSupportNavigateUp();
    }
}
