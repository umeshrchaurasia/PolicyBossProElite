package com.policyboss.policybossproelite.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.homeMainKotlin.HomeMainActivity;
import com.policyboss.policybossproelite.introslider.WelcomeActivity;
import com.policyboss.policybossproelite.login.LoginActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.masters.MasterController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.healthcheckup.response.HealthPackDetailsResponse;
import magicfinmart.datacomp.com.finmartserviceapi.healthcheckup.response.HealthPackResponse;

public class SplashScreenActivity extends BaseActivity implements IResponseSubcriber,
        magicfinmart.datacomp.com.finmartserviceapi.healthcheckup.IResponseSubcriber {

    private static final String TAG = "TOKEN";
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    PrefManager prefManager;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginResponseEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefManager = new PrefManager(this);
        dbPersistanceController = new DBPersistanceController(this);
        loginResponseEntity = dbPersistanceController.getUserData();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (task.isSuccessful()) {
                            prefManager.setToken(task.getResult().getToken());
                            Log.d(TAG, "Refreshed token: " + task.getResult().getToken());
                        }
                    }


                });


        //new MasterController(this).getInsurerList();

//        if (new LoanCityFacade(this) != null) {
//            if (new LoanCityFacade(this).getLoanCity() == null) {
//                new ErpLoanController(this).getcityloan(null);
//            }
//        }


        // for user constant
        if (loginResponseEntity != null) {
            //reset user behaviour flag to send data on every app launch
            prefManager.saveUserbehaviourState(false);
            new MasterController(this).geUserConstant(0, this);
           // new MasterController(this).getConstants(this);
            new MasterController(this).getMenuMaster(this);


        }

        if (prefManager.isFirstTimeLaunch()) {

            startActivity(new Intent(this, WelcomeActivity.class));
        } else {

            //user behaviour data collection in Async


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        if (loginResponseEntity != null) {

                            if(loginResponseEntity.getPOSPNo() != null){
                                startActivity(new Intent(SplashScreenActivity.this, HomeMainActivity.class));
                            }else{
                                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            }

                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        }


                }
            }, SPLASH_DISPLAY_LENGTH);
        }

    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

    }

    @Override
    public void OnSuccess(magicfinmart.datacomp.com.finmartserviceapi.healthcheckup.APIResponse response, String message) {
        if (response instanceof HealthPackResponse) {
            Log.d("Test", "success");
        }
        if (response instanceof HealthPackDetailsResponse) {
            Log.d("Test", "success");
        }

    }


    @Override
    public void OnFailure(Throwable t) {

        //Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }


}
