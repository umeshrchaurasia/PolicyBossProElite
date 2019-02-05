package com.datacomp.magicfinmart.term.ultralakshya;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.datacomp.magicfinmart.BaseActivity;
import com.datacomp.magicfinmart.R;
import com.datacomp.magicfinmart.home.HomeActivity;

import com.datacomp.magicfinmart.term.quoteapp.TermQuoteListFragment;
import com.datacomp.magicfinmart.term.ultralakshya.fragment.input.UltraLakshyaTermInputFragment;
import com.datacomp.magicfinmart.term.ultralakshya.fragment.quote.UltraLakshayQuoteFragment;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TermFinmartRequest;

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
    TermFinmartRequest termFinmartRequest;
    ImageView ivHdrInput, ivHdrQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultra_lakshya_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivHdrInput = (ImageView) findViewById(R.id.ivHdrInput);
        ivHdrQuote = (ImageView) findViewById(R.id.ivHdrQuote);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //1. which insurer for enable input
        //2, check request
        hideBoth();
        quoteBundle = new Bundle();
        if (getIntent().getIntExtra(TermQuoteListFragment.TERM_FOR_INPUT_FRAGMENT, 0) != 0) {
            int insurerID = getIntent().getIntExtra(TermQuoteListFragment.TERM_FOR_INPUT_FRAGMENT, 0);
            quoteBundle.putInt(TermQuoteListFragment.TERM_FOR_INPUT_FRAGMENT, insurerID);
        }

        if (getIntent().getParcelableExtra(TermQuoteListFragment.TERM_INPUT_FRAGMENT) != null) {
            termFinmartRequest = getIntent().getParcelableExtra(TermQuoteListFragment.TERM_INPUT_FRAGMENT);
            quoteBundle.putParcelable(INPUT_DATA, null);
            quoteBundle.putParcelable(QUOTE_DATA, termFinmartRequest);
          //  bottomNavigationView.setSelectedItemId(R.id.navigation_quote);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_input);
        }
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
                        highlighInput();
                    }
                    return true;
                case R.id.navigation_quote:
                    if (ivHdrQuote.getVisibility() != View.VISIBLE) {
                        tabFragment = getSupportFragmentManager().findFragmentByTag(INPUT_FRAGMENT);
                        if (termFinmartRequest != null) {
                            quoteBundle.putParcelable(INPUT_DATA, null);
                            quoteBundle.putParcelable(QUOTE_DATA, termFinmartRequest);
                            UltraLakshayQuoteFragment quoteFragment = new UltraLakshayQuoteFragment();
                            quoteFragment.setArguments(quoteBundle);
                            loadFragment(quoteFragment, INPUT_FRAGMENT);
                            highlighQuote();
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
        //super.onBackPressed();
        if (ivHdrQuote.getVisibility() == View.VISIBLE) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_input);
        } else {
            UltraLakshyaTermBottmActivity.this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void redirectToQuote(TermFinmartRequest termFinmartRequest) {
        this.termFinmartRequest = termFinmartRequest;
        quoteBundle = new Bundle();
        quoteBundle.putParcelable(QUOTE_DATA, termFinmartRequest);
        bottomNavigationView.setSelectedItemId(R.id.navigation_quote);

        highlighQuote();
    }

    public void redirectToInput(TermFinmartRequest termFinmartRequest) {
        this.termFinmartRequest = termFinmartRequest;
        quoteBundle = new Bundle();
        quoteBundle.putParcelable(QUOTE_DATA, null);
        quoteBundle.putParcelable(INPUT_DATA, termFinmartRequest);
        bottomNavigationView.setSelectedItemId(R.id.navigation_input);
    }

    public void updateRequest(TermFinmartRequest termFinmartRequest) {
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