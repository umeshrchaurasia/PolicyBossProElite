package com.policyboss.policybossproelite.helpfeedback.raiseticket;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;

public class ViewImageRaiseTicketActivity extends BaseActivity {

    private  String DOC_PATH = "";
    ImageView ivUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_raise_ticket);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_dark)));
        ivUser = findViewById(R.id.ivUser);

       if( getIntent().getStringExtra("RAISE_TICKET_URL") != null)
        {
            DOC_PATH = getIntent().getStringExtra("RAISE_TICKET_URL");
            bindImage();
        }

    }

    private void  bindImage(){
       Glide.with(ViewImageRaiseTicketActivity .this)
            .load(DOC_PATH)
            .into(ivUser);
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
        // finish();
        supportFinishAfterTransition();
        super.onBackPressed();
    }

}
