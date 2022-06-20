package com.policyboss.policybossproelite.ultralaksha.ultralakshya;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.home.HomeActivity;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.input.UltraLakshyaTermInputFragment;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.UltraLakshayQuoteFragment;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.ultra_quotes_appln.fragment.UltraQuoteDetailFragment;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.UltralakshaRequestEntity;

public class UltraLakshyaTermBottmActivity extends BaseActivity {


    private static String INPUT_FRAGMENT = "input_term_ultralakshya";
    private static String QUOTE_FRAGMENT = "quote_term_ultralakshya";


    public static String INPUT_DATA = "input_term_data_ultralakshya";
    public static String QUOTE_DATA = "quote_term_data_ultralakshya";

    private static String BUY_FRAGMENT = "buy";

    BottomNavigationView bottomNavigationView;
    Bundle quoteBundle;
    Fragment tabFragment = null;
    FragmentTransaction transactionSim;
    UltralakshaRequestEntity termFinmartRequest;
    ImageView ivHdrInput, ivHdrQuote;
    RelativeLayout RlbottonHeader;
    View viewHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ultra_lakshya_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RlbottonHeader = (RelativeLayout) findViewById(R.id.bottomHeader);
        viewHeader = (View) findViewById(R.id.viewHeader);

        ivHdrInput = (ImageView) findViewById(R.id.ivHdrInput);
        ivHdrQuote = (ImageView) findViewById(R.id.ivHdrQuote);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //1. which insurer for enable input
        //2, check request
        hideBoth();
        quoteBundle = new Bundle();


        //1. if UltraTermRequest entity found
        //2. send to input page and bind data
        //3. if no entity data send to input page

        if (getIntent().getParcelableExtra(UltraQuoteDetailFragment.ULTRA_FOR_INPUT_FRAGMENT) != null) {
            termFinmartRequest = getIntent().getParcelableExtra(UltraQuoteDetailFragment.ULTRA_FOR_INPUT_FRAGMENT);
            quoteBundle.putParcelable(INPUT_DATA, termFinmartRequest);

        }

        //
        bottomNavigationView.setSelectedItemId(R.id.navigation_input);

    }

    //region navigation click

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input:
                    if (ivHdrInput.getVisibility() != View.VISIBLE) {
                        tabFragment = getSupportFragmentManager().findFragmentByTag(INPUT_FRAGMENT);
                        if (termFinmartRequest != null) {
                            quoteBundle.putParcelable(QUOTE_DATA, null);
                            quoteBundle.putParcelable(INPUT_DATA, termFinmartRequest);
                        }

                        UltraLakshyaTermInputFragment inputFragment = new UltraLakshyaTermInputFragment();
                        inputFragment.setArguments(quoteBundle);
                        loadFragment(inputFragment, INPUT_FRAGMENT);
                        // highlighInput();
                    }
                    return true;
                case R.id.navigation_quote:
                    if (ivHdrQuote.getVisibility() != View.VISIBLE) {
                        tabFragment = getSupportFragmentManager().findFragmentByTag(QUOTE_FRAGMENT);
                        if (termFinmartRequest != null) {
                            quoteBundle.putString(INPUT_DATA, termFinmartRequest.getContactName());
                            //quoteBundle.putParcelable(QUOTE_DATA, termFinmartRequest);
                            UltraLakshayQuoteFragment quoteFragment = new UltraLakshayQuoteFragment();
                            quoteFragment.setArguments(quoteBundle);
                            loadFragment(quoteFragment, INPUT_FRAGMENT);
                            // highlighQuote();
                        } else {
                            Toast.makeText(UltraLakshyaTermBottmActivity.this, "Please fill all inputs", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                case R.id.navigation_buy:
                    return false;
            }

            return false;
        }
    };
    //endregion

    private void loadFragment(Fragment fragment, String TAG) {
        transactionSim = getSupportFragmentManager().beginTransaction();
        transactionSim.replace(R.id.frame_layout, fragment, TAG);
        transactionSim.addToBackStack(TAG);
        transactionSim.show(fragment);
        transactionSim.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_quote) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_input);
        } else {
            UltraLakshyaTermBottmActivity.this.finish();
        }


    }

    public void manageHeader(boolean bln) {
        if (bln) {
            RlbottonHeader.setVisibility(View.VISIBLE);
            viewHeader.setVisibility(View.VISIBLE);
        } else {
            RlbottonHeader.setVisibility(View.GONE);
            viewHeader.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void redirectToQuote() {
        // this.termFinmartRequest = termFinmartRequest;
        quoteBundle = new Bundle();
        quoteBundle.putParcelable(QUOTE_DATA, termFinmartRequest);
        bottomNavigationView.setSelectedItemId(R.id.navigation_quote);

        //highlighQuote();
    }

    public void redirectToInput(UltralakshaRequestEntity termFinmartRequest) {
        this.termFinmartRequest = termFinmartRequest;
        quoteBundle = new Bundle();
        quoteBundle.putParcelable(QUOTE_DATA, null);
        quoteBundle.putParcelable(INPUT_DATA, termFinmartRequest);
        bottomNavigationView.setSelectedItemId(R.id.navigation_input);
    }

    public void updateRequest(UltralakshaRequestEntity termFinmartRequest) {
        this.termFinmartRequest = termFinmartRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
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

    public void highlighInput() {
        ivHdrInput.setVisibility(View.VISIBLE);
        ivHdrQuote.setVisibility(View.GONE);
    }

    public void highlighQuote() {
        ivHdrQuote.setVisibility(View.VISIBLE);
        ivHdrInput.setVisibility(View.GONE);

    }

    public void hideBoth() {
        ivHdrQuote.setVisibility(View.GONE);
        ivHdrInput.setVisibility(View.GONE);
    }


}
