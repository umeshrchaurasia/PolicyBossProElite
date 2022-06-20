package com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.activity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.UltraLakshyaTermBottmActivity;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.adapter.UltraTabsPagerAdapter;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.term.TermInsuranceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UltraMainEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshyaQuoteApplnResponse;


public class UltraLakshyaDetailActivity extends BaseActivity implements IResponseSubcriber {

    Toolbar toolbar;
    ViewPager viewPager;
    UltraTabsPagerAdapter mAdapter;
    LoginResponseEntity loginEntity;

    UltraMainEntity ultraMainEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultra_lakshya_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        setSupportActionBar(toolbar);
        tabLayout.setupWithViewPager(viewPager, true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        loginEntity = new DBPersistanceController(this).getUserData();


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        fetchQuoteApplication();

    }


    private void fetchQuoteApplication() {

        showDialog();

        new TermInsuranceController(this).getUltraQualeAppList(UltraLakshyaDetailActivity.this);


    }


    @Override
    public void OnSuccess(APIResponse response, String message) {


        cancelDialog();
        if (response instanceof UltraLakshyaQuoteApplnResponse) {
            if (((UltraLakshyaQuoteApplnResponse) response).getMasterData() != null) {

                ultraMainEntity = ((UltraLakshyaQuoteApplnResponse) response).getMasterData();
                if (ultraMainEntity.getQuote().size() != 0
                        || ultraMainEntity.getApplication().size() != 0) {

                    mAdapter = new UltraTabsPagerAdapter(getSupportFragmentManager(), ultraMainEntity);
                    viewPager.setAdapter(mAdapter);

                } else {
                    finish();
                    startActivity(new Intent(this, UltraLakshyaTermBottmActivity.class));
                }
            }

        }
    }

    @Override
    public void OnFailure(Throwable t) {

        cancelDialog();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
        // return super.onSupportNavigateUp();
    }
}
