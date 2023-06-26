package com.policyboss.policybossproelite.motor.privatecar.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.motor.privatecar.fragment.InputFragment;
import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorApplicationFragment;
import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorLeadFragment;
import com.policyboss.policybossproelite.motor.privatecar.fragment.MotorQuoteFragment;
import com.policyboss.policybossproelite.motor.privatecar.fragment.QuoteFragment;
import com.policyboss.policybossproelite.webviews.CommonWebViewActivity;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.ApplicationListEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.QuoteListEntity;
import magicfinmart.datacomp.com.finmartserviceapi.motor.controller.MotorController;
import magicfinmart.datacomp.com.finmartserviceapi.motor.model.ResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.motor.model.SummaryEntity;
import magicfinmart.datacomp.com.finmartserviceapi.motor.requestentity.MotorRequestEntity;

public class InputQuoteBottmActivity extends BaseActivity {


    private static String INPUT_FRAGMENT = "input";
    private static String QUOTE_FRAGMENT = "quote";
    private static String BUY_FRAGMENT = "buy";


    public static String MOTOR_INPUT_REQUEST = "input_request_entity";
    public static String MOTOR_QUOTE_REQUEST = "quote_request_entity";
    public static String MOTOR_LEAD_ID = "motor_lead_id";

    BottomNavigationView bottomNavigationView;
    Bundle quoteBundle;
    Fragment tabFragment = null;
    FragmentTransaction transactionSim;
    MotorRequestEntity motorRequestEntity;
    boolean isQuoteVisible = true;
    ImageView ivHdrInput, ivHdrQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_quote_bottm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivHdrInput = (ImageView) findViewById(R.id.ivHdrInput);
        ivHdrQuote = (ImageView) findViewById(R.id.ivHdrQuote);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (getIntent().getParcelableExtra(MotorApplicationFragment.FROM_APPLICATION) != null) {
            ApplicationListEntity entity = getIntent().getParcelableExtra(MotorApplicationFragment.FROM_APPLICATION);
            if (entity.getMotorRequestEntity().getIsTwentyfour() == 0) {

                //update counetr to hit  two times only to manage multiple hits
                Utility.getSharedPreferenceEditor(this).putInt(Utility.QUOTE_COUNTER,
                        MotorController.NO_OF_SERVER_HITS - 1)
                        .commit();
                //1. update srn in preference
                Utility.getSharedPreferenceEditor(this).
                        putString(Utility.CARQUOTE_UNIQUEID, entity.getSRN()).commit();


                //2. create bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable(MOTOR_QUOTE_REQUEST, entity.getMotorRequestEntity());
                quoteBundle = bundle;

                bottomNavigationView.setSelectedItemId(R.id.navigation_quote);
            } else {
                //send to Input
                //modify
                quoteBundle = new Bundle();
                quoteBundle.putParcelable(MOTOR_INPUT_REQUEST, entity.getMotorRequestEntity());

                bottomNavigationView.setSelectedItemId(R.id.navigation_input);
            }

        }  // Note : when we click on Lead tab

        else if (getIntent().getParcelableExtra(MotorQuoteFragment.FROM_QUOTE) != null) {

            QuoteListEntity mQuoteListEntity = getIntent().getParcelableExtra(MotorQuoteFragment.FROM_QUOTE);

            if (mQuoteListEntity.getMotorRequestEntity().getIsTwentyfour() == 0) {

                //update counetr to hit  two times only to manage multiple hits
                Utility.getSharedPreferenceEditor(this).putInt(Utility.QUOTE_COUNTER,
                        MotorController.NO_OF_SERVER_HITS - 1)
                        .commit();
                //1. update srn in preference
                Utility.getSharedPreferenceEditor(this).
                        putString(Utility.CARQUOTE_UNIQUEID, mQuoteListEntity.getSRN()).commit();


                //2. create bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable(MOTOR_QUOTE_REQUEST, mQuoteListEntity.getMotorRequestEntity());

                quoteBundle = bundle;

                bottomNavigationView.setSelectedItemId(R.id.navigation_quote);
            } else {
                //send to Input
                //modify
                quoteBundle = new Bundle();
                quoteBundle.putParcelable(MOTOR_INPUT_REQUEST, mQuoteListEntity.getMotorRequestEntity());

                bottomNavigationView.setSelectedItemId(R.id.navigation_input);
            }

        } else if (getIntent().getParcelableExtra(MotorLeadFragment.FROM_LEAD) != null) {

            QuoteListEntity entity = getIntent().getParcelableExtra(MotorLeadFragment.FROM_LEAD);
            if (entity.getMotorRequestEntity().getIsTwentyfour() == 0) {

                //update counetr to hit  two times only to manage multiple hits
                Utility.getSharedPreferenceEditor(this).putInt(Utility.QUOTE_COUNTER,
                        MotorController.NO_OF_SERVER_HITS - 1)
                        .commit();
                //1. update srn in preference
                Utility.getSharedPreferenceEditor(this).
                        putString(Utility.CARQUOTE_UNIQUEID, entity.getSRN()).commit();


                //2. create bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable(MOTOR_QUOTE_REQUEST, entity);
                bundle.putString(MOTOR_LEAD_ID, String.valueOf(entity.getLeadId()));
                quoteBundle = bundle;


                bottomNavigationView.setSelectedItemId(R.id.navigation_quote);
            } else {
                //send to Input
                //modify
                quoteBundle = new Bundle();
                quoteBundle.putParcelable(MOTOR_INPUT_REQUEST, entity.getMotorRequestEntity());
                quoteBundle.putString(MOTOR_LEAD_ID, String.valueOf(entity.getLeadId()));

                bottomNavigationView.setSelectedItemId(R.id.navigation_input);
            }
        } else {
            //first input fragment load
            bottomNavigationView.setSelectedItemId(R.id.navigation_input);
        }

        quoteBundle = null;


    }


    private void loadFragment(Fragment fragment, String TAG) {
        transactionSim = getSupportFragmentManager().beginTransaction();
        transactionSim.replace(R.id.frame_layout, fragment, TAG);
        transactionSim.addToBackStack(TAG);
        transactionSim.show(fragment);
        transactionSim.commit();
        //transactionSim.commitAllowingStateLoss();
    }

    public void highlighInput() {
        ivHdrInput.setVisibility(View.VISIBLE);
        ivHdrQuote.setVisibility(View.GONE);
    }

    public void highlighQuote() {
        ivHdrQuote.setVisibility(View.VISIBLE);
        ivHdrInput.setVisibility(View.GONE);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    //if (isQuoteVisible) {
                    if (true) {
                        tabFragment = getSupportFragmentManager().findFragmentByTag(INPUT_FRAGMENT);
                        if (motorRequestEntity != null) {
                            quoteBundle = new Bundle();
                            quoteBundle.putParcelable(InputQuoteBottmActivity.MOTOR_INPUT_REQUEST, motorRequestEntity);
                        }

                        if (tabFragment != null) {
                            highlighInput();
                            tabFragment.setArguments(quoteBundle);
                            loadFragment(tabFragment, INPUT_FRAGMENT);

                        } else {
                            highlighInput();
                            InputFragment inputFragment = new InputFragment();
                            inputFragment.setArguments(quoteBundle);
                            loadFragment(inputFragment, INPUT_FRAGMENT);
                        }
                    } else {
                        Toast.makeText(InputQuoteBottmActivity.this, "Please wait.., Fetching all quotes", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                case R.id.navigation_quote:

                    tabFragment = getSupportFragmentManager().findFragmentByTag(QUOTE_FRAGMENT);
                    if (tabFragment != null) {
                        highlighQuote();
                        loadFragment(tabFragment, QUOTE_FRAGMENT);

                    } else {
                        if (quoteBundle != null) {
                            highlighQuote();
                            QuoteFragment quoteFragment = new QuoteFragment();
                            quoteFragment.setArguments(quoteBundle);
                            loadFragment(quoteFragment, QUOTE_FRAGMENT);
                        } else {
                            InputFragment fragment = (InputFragment) getSupportFragmentManager().findFragmentByTag(INPUT_FRAGMENT);
                            if (fragment != null)
                                fragment.getQuote();
                            // Toast.makeText(InputQuoteBottmActivity.this, "Tap get Quotes ", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }

                    return true;
                case R.id.navigation_buy:

//                    tabFragment = getSupportFragmentManager().findFragmentByTag("BUY");
//                    if (tabFragment != null) {
//                        loadFragment(tabFragment, INPUT_FRAGMENT);
//
//                    } else {
//                        loadFragment(new BuyFragment(), INPUT_FRAGMENT);
//                    }

                    return false;
            }

            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (isQuoteVisible) {
            if (R.id.navigation_quote == bottomNavigationView.getSelectedItemId()) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_input);
            } else {
                InputQuoteBottmActivity.this.finish();
            }
        } else {
            Toast.makeText(InputQuoteBottmActivity.this, "Please Wait fetching Quotes!!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void modifyQuote(MotorRequestEntity entity) {
        motorRequestEntity = entity;
        /*startActivityForResult(new Intent(this, ModifyQuoteActivity.class)
                .putExtra("CAR_REQUEST", motorRequestEntity), 1000);*/

    }

    public void redirectInput(MotorRequestEntity entity) {

        //if (isQuoteVisible) {
        if (true) {
            motorRequestEntity = entity;
            quoteBundle = new Bundle();
            quoteBundle.putParcelable(InputQuoteBottmActivity.MOTOR_INPUT_REQUEST, motorRequestEntity);

            if (motorRequestEntity == null)
                Toast.makeText(InputQuoteBottmActivity.this, "Please fill all inputs", Toast.LENGTH_SHORT).show();
            else
                bottomNavigationView.setSelectedItemId(R.id.navigation_input);

        } else {
            Toast.makeText(InputQuoteBottmActivity.this, "Fetching all quotes", Toast.LENGTH_SHORT).show();
        }
    }

    public void getQuoteParameterBundle(MotorRequestEntity entity, String MOTOR_LEAD_ID) {

        motorRequestEntity = entity;
        quoteBundle = new Bundle();
        quoteBundle.putParcelable(InputQuoteBottmActivity.MOTOR_QUOTE_REQUEST, motorRequestEntity);
        quoteBundle.putString(InputQuoteBottmActivity.MOTOR_LEAD_ID, MOTOR_LEAD_ID);

        if (motorRequestEntity == null)
            Toast.makeText(InputQuoteBottmActivity.this, "Please fill all inputs", Toast.LENGTH_SHORT).show();
        else
            bottomNavigationView.setSelectedItemId(R.id.navigation_quote);

    }

    public void updateRequest(MotorRequestEntity entity, boolean isQuoteVisible) {
        motorRequestEntity = entity;
        this.isQuoteVisible = isQuoteVisible;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isQuoteVisible) {
                    finish();
                    return true;
                } else {
                    Toast.makeText(InputQuoteBottmActivity.this, "Please Wait fetching Quotes!!!", Toast.LENGTH_SHORT).show();
                    return false;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //region unUsed code

    public interface ActivityCallback            // Interface creation
    {
        void onMethodCallback(String strTyp);
    }

    private void unCheckAllBottomMenu() {
        int size = bottomNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomNavigationView.getMenu().getItem(i).setCheckable(false);
        }
    }


    public void redirectToBuy(ResponseEntity entity) {

        int fbaID = new DBPersistanceController(this).getUserData().getFBAId();
        String url = "https://policyboss.com/";
        //String url = "http://policyboss.com/";
        String title = "";
        String name = "";
        url = url + "buynowprivatecar/4/" + entity.getService_Log_Unique_Id() + "/nonposp/" + fbaID;
        title = "Motor Insurance";

        //new QuoteApplicationController(this).convertQuoteToApp(entity.);

        startActivity(new Intent(this, CommonWebViewActivity.class)
                .putExtra("URL", url)
                .putExtra("NAME", name)
                .putExtra("TITLE", title));
    }

    public void redirectToPopUpPremium(ResponseEntity entity, SummaryEntity summaryEntity, String IDV) {
        startActivity(new Intent(this, PremiumBreakUpActivity.class)
                .putExtra("RESPONSE", entity));


    }

    //endregion


}
