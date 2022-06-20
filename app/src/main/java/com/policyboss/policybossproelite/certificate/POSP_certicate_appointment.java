package com.policyboss.policybossproelite.certificate;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.DynamicController;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.CertificateEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.CertificateResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.register.RegisterController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PospAppointEmailResponse;

public class POSP_certicate_appointment extends BaseActivity implements View.OnClickListener, IResponseSubcriber {
    String type;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginEntity;
    Button btnsendemail, btnview;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posp_certicate_appointment);
        type = getIntent().getStringExtra("TYPE");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (type.equals("1")) {
            getSupportActionBar().setTitle("POSP Appointment Letter");
        } else {
            getSupportActionBar().setTitle("POSP Application Form");
        }
        btnsendemail = (Button) findViewById(R.id.btnsendemail);
        btnview = (Button) findViewById(R.id.btnview);

        dbPersistanceController = new DBPersistanceController(this);
        loginEntity = dbPersistanceController.getUserData();


        btnsendemail.setOnClickListener(this);
        btnview.setOnClickListener(this);

    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();

        if (response instanceof CertificateResponse) {
            if (response.getStatusNo() == 0) {
                URL = "" + ((CertificateResponse) response).getMasterData();
                Utility.loadWebViewUrlInBrowser(this, URL);

            }
        } else if (response instanceof PospAppointEmailResponse) {
            if (response.getStatusNo() == 0) {

                Toast.makeText(this, ((PospAppointEmailResponse) response).getMessage(), Toast.LENGTH_SHORT).show();

            }
        }


    }

    @Override
    public void OnFailure(Throwable t) {

        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();

        cancelDialog();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnsendemail:

                showDialog();

                new RegisterController(this).getEmailTemplate(URL, type, POSP_certicate_appointment.this);

                break;
            case R.id.btnview:

                CertificateEntity requestEntity = new CertificateEntity();
                if (loginEntity.getPOSPNo() != null && !loginEntity.getPOSPNo().equalsIgnoreCase("")) {
                    requestEntity.setSS_ID(Integer.valueOf(loginEntity.getPOSPNo()));
                    //  requestEntity.setSS_ID(10325);
                    requestEntity.setType(Integer.valueOf(type));
                }
                showDialog("Please Wait...");
                new DynamicController(this).GetPospAppointmentLetter(requestEntity, POSP_certicate_appointment.this);

                break;
        }

    }
}
