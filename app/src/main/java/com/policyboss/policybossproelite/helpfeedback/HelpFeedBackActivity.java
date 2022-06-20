package com.policyboss.policybossproelite.helpfeedback;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.helpfeedback.aboutus.AboutUsActivity;
import com.policyboss.policybossproelite.helpfeedback.contactus.ContactUsActivity;
import com.policyboss.policybossproelite.helpfeedback.raiseticket.RaiseTicketActivity;

import com.policyboss.policybossproelite.webviews.CommonWebViewActivity;

public class HelpFeedBackActivity extends BaseActivity implements View.OnClickListener {

    CardView cvContactUs, cvRaiseTicket, cvAboutUs, cvDisclosure ,cvChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feed_back);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidgets();
        setListener();
    }

    private void setListener() {
        cvContactUs.setOnClickListener(this);
        cvRaiseTicket.setOnClickListener(this);
        cvAboutUs.setOnClickListener(this);
        cvDisclosure.setOnClickListener(this);
        cvChat.setOnClickListener(this);
    }

    private void initWidgets() {
        cvContactUs = (CardView) findViewById(R.id.cvContactUs);
        cvRaiseTicket = (CardView) findViewById(R.id.cvRaiseTicket);
        cvAboutUs = (CardView) findViewById(R.id.cvAboutUs);
        cvDisclosure = (CardView) findViewById(R.id.cvDisclosure);
        cvChat  = (CardView) findViewById(R.id.cvChat);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cvChat:
              //    startActivity(new Intent(this, InsertContactActivity.class));
                break;
            case R.id.cvContactUs:
                startActivity(new Intent(this, ContactUsActivity.class));
                break;
            case R.id.cvRaiseTicket:
                startActivity(new Intent(this, RaiseTicketActivity.class));
                break;
            case R.id.cvAboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.cvDisclosure:
                startActivity(new Intent(this, CommonWebViewActivity.class)
                        .putExtra("URL", "file:///android_asset/Disclosure.html")
                        .putExtra("NAME", "DISCLOSURE")
                        .putExtra("TITLE", "DISCLOSURE"));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        supportFinishAfterTransition();
        super.onBackPressed();
    }

}
