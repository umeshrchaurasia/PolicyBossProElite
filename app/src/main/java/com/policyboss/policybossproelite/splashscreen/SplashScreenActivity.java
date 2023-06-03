package com.policyboss.policybossproelite.splashscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.home.HomeActivity;
import com.policyboss.policybossproelite.homeMainKotlin.HomeMainActivity;
import com.policyboss.policybossproelite.introslider.WelcomeActivity;
import com.policyboss.policybossproelite.login.LoginActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.policyboss.policybossproelite.utility.NetworkUtils;

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

    String deeplinkurl="";

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

        if (NetworkUtils.isNetworkAvailable(SplashScreenActivity.this)) {

        // for user constant
        if (loginResponseEntity != null) {
            //reset user behaviour flag to send data on every app launch
            prefManager.saveUserbehaviourState(false);
            new MasterController(this).geUserConstant(0, this);
           // new MasterController(this).getConstants(this);
            new MasterController(this).getMenuMaster(this);


        }
        }
        // for user constant

       /* if (userConstantEntity != null) {
            new MasterController(this).geUserConstant(0, this);
        }*/



        if (prefManager.isFirstTimeLaunch()) {

            startActivity(new Intent(this, WelcomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK));

        } else {

            //user behaviour data collection in Async


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        if (loginResponseEntity != null) {
                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }else{
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }

                }
            }, SPLASH_DISPLAY_LENGTH);
        }


        getDynamicLinkfromFireBase();

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

    private void getDynamicLinkfromFireBase() {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                          Log.i("Sync","We have link");
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //check by firebase
                        if(deepLink !=null){

                            deeplinkurl = deepLink.toString(); //+"?ss_id=119225&fba_id=89433&v=20200609&sub_fba_id=0&ip_address=&mac_address=&app_version=policyboss-0.1.1&device_id=e73cb6c8dd4be83c&product_id=1&login_ssid=";
                            Log.i("dynamic url",deeplinkurl);

                           // List<String> parameters = pendingDynamicLinkData.getLink().getPathSegments();
                          //  String param = parameters.get(parameters.size() - 1);

                            prefManager.setDeeplink(deeplinkurl.toString());

//                          String prd=  pendingDynamicLinkData.getLink().getQueryParameter("product_id");
//
//                          if(pendingDynamicLinkData.getLink().getQueryParameter("product_id")!=null)
//                          {
//                              String prd1=  pendingDynamicLinkData.getLink().getQueryParameter("product_id");
//                          }
//                          else
//                          {
//                              String prd2=  pendingDynamicLinkData.getLink().getQueryParameter("product_id");
//                          }


                        }
                        else
                        {
                            //normal intent
                            Uri uri;
                            uri = getIntent().getData();

                                if (uri != null) {

                                    // the path segments and storing it in list.
                                 //   List<String> parameters = uri.getPathSegments();
                                 //   String param = parameters.get(parameters.size() - 1);
                                    //  messageTV.setText(uri.toString());
                                    deeplinkurl =  uri.toString();
                                    Log.d("", uri.toString());
                                    prefManager.setDeeplink(uri.toString());

//                                    startActivity(new Intent(SplashScreenActivity.this, CommonWebViewActivity.class)
//                                            .putExtra("URL", uri.toString())
//                                            .putExtra("NAME", "")
//                                            .putExtra("TITLE", ""));
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            startActivity(new Intent(SplashScreenActivity.this, CommonWebViewActivity.class).putExtra("URL", uri.toString()).putExtra("NAME", "").putExtra("TITLE", "")
//                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//
//                                        }
//                                    }, 1000);


                                }


                        }


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        Toast.makeText(this,"hugfh gh ghfh h ",Toast.LENGTH_SHORT).show();
//    }
}
