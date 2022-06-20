package com.policyboss.policybossproelite.offline_quotes;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;

import com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.Offline_GoodsCarrying_Motor.Good_OfflineMotorListActivity;
import com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.Offline_PassengerCarrying_Motor.Passenger_OfflineMotorListActivity;
import com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.health.OfflineHealthListActivity;

import com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.Offline_Term.TermQuoteApplicationActivity_offline;




import com.policyboss.policybossproelite.offline_quotes.OfflineQuoteForm.offline_motor.OfflineMotorListActivity;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.masters.MasterController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.CarMasterResponse;

public class AddNewOfflineQuotesActivity extends BaseActivity implements View.OnClickListener , IResponseSubcriber {
    CardView MotorPrivate, MotorGoods, MotorPassenger, Health, life,offlineQuote;
    DBPersistanceController dbPersistanceController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_offline_quotes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbPersistanceController = new DBPersistanceController(this);
        init_views();
        setListener();

        if (dbPersistanceController.getRTOListNames() != null && dbPersistanceController.getRTOListNames().size() <= 0) {
            showDialog();
            new MasterController(this).getRTOMaster(this);
        }

    }

    private void setListener() {
        MotorPrivate.setOnClickListener(this);
        MotorGoods.setOnClickListener(this);
        MotorPassenger.setOnClickListener(this);
        Health.setOnClickListener(this);
        life.setOnClickListener(this);
        offlineQuote.setOnClickListener(this);
    }

    private void init_views() {
        MotorPrivate = (CardView) findViewById(R.id.MotorPrivate);
        MotorGoods = (CardView) findViewById(R.id.MotorGoods);
        MotorPassenger = (CardView) findViewById(R.id.MotorPassenger);
        Health = (CardView) findViewById(R.id.Health);
        life = (CardView) findViewById(R.id.life);
        offlineQuote= (CardView) findViewById(R.id.offlineQuote);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MotorPrivate:
                startActivity(new Intent(this, OfflineMotorListActivity.class));
                break;
            case R.id.MotorGoods:
                startActivity(new Intent(this, Good_OfflineMotorListActivity.class));
                break;
            case R.id.MotorPassenger:
                startActivity(new Intent(this, Passenger_OfflineMotorListActivity.class));
                break;
            case R.id.Health:
                startActivity(new Intent(this, OfflineHealthListActivity.class));
                break;
            case R.id.life:
                startActivity(new Intent(this, TermQuoteApplicationActivity_offline.class));

                break;
            case R.id.offlineQuote:
                startActivity(new Intent(this, OfflineQuotesListActivity.class));

                break;

        }
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

    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof CarMasterResponse) {
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();

    }
//    @Override
//    public void onBackPressed() {
//
//        supportFinishAfterTransition();
//        super.onBackPressed();
//    }

}
