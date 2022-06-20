package com.policyboss.policybossproelite.notification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;

public class NotificationSmsActivity extends AppCompatActivity {

    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginEntity;

    TextView txtTitle,txtMessage;
    String TITLE, MESSAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbPersistanceController = new DBPersistanceController(this);
        loginEntity = dbPersistanceController.getUserData();

        initialize();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TITLE = extras.getString("NOTIFY_TITLE", "");
            MESSAGE = extras.getString("NOTIFY_BODY", "");

            txtMessage.setText(""+ MESSAGE);
            txtTitle.setText(""+ TITLE);

        }
    }

    private void initialize() {

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtMessage = (TextView)findViewById(R.id.txtMessage);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

}
