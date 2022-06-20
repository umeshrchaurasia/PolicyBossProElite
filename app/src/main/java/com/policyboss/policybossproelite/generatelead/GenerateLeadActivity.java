package com.policyboss.policybossproelite.generatelead;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.home.HomeActivity;
import com.policyboss.policybossproelite.scan_vehicle.VehicleScanActivity;
import com.policyboss.policybossproelite.vehicle_details.VehicleDetailFragment;

public class GenerateLeadActivity extends BaseActivity implements View.OnClickListener {

    Fragment fragment = null;
    LinearLayout llShareData, llVehicleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_lead);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init_widgets();

        getSupportActionBar().setTitle("VEHICLE DETAIL");
        fragment = new VehicleDetailFragment();
        loadFragment(fragment);
    }

    private void init_widgets() {
        llShareData = findViewById(R.id.llShareData);
        llVehicleDetails = findViewById(R.id.llVehicleDetails);
        llShareData.setOnClickListener(this);
        llVehicleDetails.setOnClickListener(this);

    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();

            return true;
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llVehicleDetails:
                getSupportActionBar().setTitle("VEHICLE DETAIL");
                fragment = new VehicleDetailFragment();
                loadFragment(fragment);
                break;
            case R.id.llShareData:

                startActivity(new Intent(this, VehicleScanActivity.class));
                /*fragment = new ShareDataFragment();
                getSupportActionBar().setTitle("SHARE DATA");
                loadFragment(fragment);*/
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_home:

                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("MarkTYPE", "FROM_HOME");
                startActivity(intent);

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        supportFinishAfterTransition();
        super.onBackPressed();
    }

}
