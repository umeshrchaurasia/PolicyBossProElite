package com.policyboss.policybossproelite.term.termselection;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.helpfeedback.raiseticketDialog.RaiseTicketDialogActivity;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserConstantEntity;

public class TermSelectionActivity extends BaseActivity {
    RecyclerView rvTermSelection;
    TermSelectionItemAdapter mAdapter;
    DBPersistanceController db;
    UserConstantEntity userConstantEntity;

    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DBPersistanceController(this);
        userConstantEntity = db.getUserConstantsData();
        prefManager = new PrefManager(this);
        init();
    }

    private void init() {
        rvTermSelection = (RecyclerView) findViewById(R.id.rvTermSelection);
        rvTermSelection.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvTermSelection.setLayoutManager(layoutManager);
        mAdapter = new TermSelectionItemAdapter(this);
        rvTermSelection.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insurance_menu, menu);
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

            case R.id.action_raise:
                // Toast.makeText(this,"Popup",Toast.LENGTH_SHORT).show();

                String url = userConstantEntity.getRaiseTickitUrl() + "&mobile_no=" + userConstantEntity.getMangMobile()
                        + "&UDID=" + userConstantEntity.getUserid()+"&app_version="+prefManager.getAppVersion()
                        +"&device_code="+prefManager.getDeviceID()+"&ssid="+userConstantEntity.getPOSPNo()+"&fbaid="+userConstantEntity.getFBAId();



                Log.d("URL", "Raise Ticket URL: "+url);

                startActivity(new Intent(this, RaiseTicketDialogActivity.class)
                             .putExtra("URL", url));
                return true;
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
