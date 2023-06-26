package com.policyboss.policybossproelite.login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.BuildConfig;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.helpfeedback.raiseticketDialog.RaiseTicketDialogActivity;
import com.policyboss.policybossproelite.homeMainKotlin.HomeMainActivity;
import com.policyboss.policybossproelite.register.RegisterActivity;
import com.policyboss.policybossproelite.salesmaterial.SalesDetailActivity;
import com.policyboss.policybossproelite.utility.Constants;
import com.policyboss.policybossproelite.utility.NetworkUtils;
import com.policyboss.policybossproelite.utility.ReadDeviceID;
import com.policyboss.policybossproelite.webviews.PrivacyWebViewActivity;

import io.realm.Realm;
import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.login.LoginController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.register.RegisterController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.salesmaterial.SalesMaterialController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.LoginRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.ForgotResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.LoginResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UserHideResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UsersignupResponse;

import static android.os.Build.VERSION.SDK_INT;

public class LoginActivity extends BaseActivity implements View.OnClickListener, BaseActivity.PopUpListener, IResponseSubcriber {
    PrefManager prefManager;
    EditText etEmail, etPassword;
    LoginRequestEntity loginRequestEntity;
    TextView tvSignUp, tvForgotPass, txtterm,txtprivacy;;
    Button btnSignIn;
    LinearLayout lyRaiseTicket;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 1111;
    DBPersistanceController dbPersistanceController;
    private static int PERMISSION_DENIED = 0;


    String enable_elite_signupurl = "";

    String[] perms = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_CONTACTS",
            "android.permission.READ_CALL_LOG"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginRequestEntity = new LoginRequestEntity();
        initWidgets();
        setListener();
        realm = Realm.getDefaultInstance();
        prefManager = new PrefManager(this);
        dbPersistanceController = new DBPersistanceController(this);
        dbPersistanceController.clearUserData();
        registerPopUp(this);

        prefManager.setDeviceID( Utility.getDeviceId(LoginActivity.this.getApplicationContext()));

        prefManager.setAppVersion("policybosselite-" + BuildConfig.VERSION_NAME);

       // Log.d("policybosselite" , prefManager.getDeviceID() +" And " +  prefManager.getAppVersion());


        if (!checkPermission()) {
            requestPermission();
        }

    }

    @Override
    public void onBackPressed() {
        dialogExit();
    }


    //region permission

    private boolean checkPermission() {

        int camera = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[0]);

        int WRITE_EXTERNAL = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[1]);
        int READ_EXTERNAL = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[2]);
        int READ_CONTACTS = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[3]);
        int READ_CALL_LOG = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[4]);
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return camera == PackageManager.PERMISSION_GRANTED

                    && READ_EXTERNAL == PackageManager.PERMISSION_GRANTED;
        }else{
            return camera == PackageManager.PERMISSION_GRANTED
                    &&  WRITE_EXTERNAL == PackageManager.PERMISSION_GRANTED
                    && READ_EXTERNAL == PackageManager.PERMISSION_GRANTED
                    && READ_CONTACTS == PackageManager.PERMISSION_GRANTED
                    && READ_CALL_LOG == PackageManager.PERMISSION_GRANTED;
        }


    }

    private void checkRationale(){
        if (checkRationalePermission()) {
            //Show Information about why you need the permission

            requestPermission();

        }
        else {

            openPopUp(btnSignIn, "Need  Permission", "This app needs all permissions.", "GRANT", false);



        }
    }

    private boolean checkRationalePermission() {

        boolean camera = ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, perms[0]);

        boolean write_external = ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, perms[1]);
        boolean read_external = ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, perms[2]);
        boolean read_contacts = ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, perms[3]);
        boolean read_call_log = ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, perms[4]);

        // boolean minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;


        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return  camera ||  read_external || read_contacts || read_call_log ;
        }else{
            return  camera ||write_external   || read_external  || read_contacts || read_call_log;

        }
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, perms, Constants.PERMISSION_CAMERA_STORACGE_CONSTANT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_CAMERA_STORACGE_CONSTANT:
                // if (grantResults.length > 0) {


                // if (grantResults.length > 0)

                if (SDK_INT < Build.VERSION_CODES.Q) {

                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternal = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternal = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean read_contacts = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean read_call_log = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    if (camera && writeExternal && readExternal && read_contacts && read_call_log) {

                        // Toast.makeText(this, "All permission granted", Toast.LENGTH_SHORT).show();
                    } else {

                        //Permission Denied, You cannot access location data and camera
                        if (SDK_INT >= Build.VERSION_CODES.M) {


                            showMessageOKCancel("Required permissions to proceed PolicyBossProElite..!",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // finish();
                                            if (2 > PERMISSION_DENIED) {
                                                PERMISSION_DENIED++;
                                                checkRationale();
                                            } else {
                                                dialogInterface.dismiss();

                                            }

                                        }
                                    });

                        }
                    }

                }

                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialog_Theme)
                .setCancelable(false)
                .setTitle("Retry")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", null)
                .create()

                .show();
    }

    //endregion

    private void setListener() {
        tvSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        tvForgotPass.setOnClickListener(this);
        lyRaiseTicket.setOnClickListener(this);

        txtterm.setOnClickListener(this);
        txtprivacy.setOnClickListener(this);
    }

    private void initWidgets() {
        txtterm = findViewById(R.id.txtterm);
        txtprivacy =  findViewById(R.id.txtprivacy);

        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        lyRaiseTicket = (LinearLayout) findViewById(R.id.lyRaiseTicket);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvForgotPass:
                dialogForgotPassword();
                break;
            case R.id.tvSignUp:

                if (!NetworkUtils.isNetworkAvailable(this)) {

                    Snackbar.make( view, getString(R.string.noInternet), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                showDialog();
                new LoginController(this).Getusersignup(LoginActivity.this);


                break;
            case R.id.lyRaiseTicket:
               // String url = "http://qa.policyboss.com/Finmart/Ticketing/ticket_login.html?landing_page=login_page";
                String url = "https://origin-cdnh.policyboss.com/fmweb/Ticketing/ticket_login.html?landing_page=login_page";
                Log.d("URL", "Raise Ticket URL: " + url);


                startActivity(new Intent(this, RaiseTicketDialogActivity.class)
                             .putExtra("URL", url));
                break;
            case R.id.txtprivacy:

                startActivity(new Intent(this, PrivacyWebViewActivity.class)
                        .putExtra(
                                "URL",
                                "https://www.policyboss.com/privacy-policy-policyboss-pro-elite"
                        )
                        .putExtra("NAME", "" + "privacy-policy")
                        .putExtra("TITLE", "" + "privacy-policy"));
                break;
            case R.id.txtterm:
                startActivity(new Intent(this, PrivacyWebViewActivity.class)
                        .putExtra("URL", "https://www.policyboss.com/terms-condition")
                        .putExtra("NAME", "" + "Terms & Conditions")
                        .putExtra("TITLE", "" + "Terms & Conditions"));
                break;

            case R.id.btnSignIn:
                if (!isEmpty(etEmail)) {
                    etEmail.requestFocus();
                    Toast.makeText(this, "Enter Valid Email/User Id", Toast.LENGTH_SHORT).show();
                    //etEmail.setError("Enter Valid Email");
                    return;
                }
                if (!isEmpty(etPassword)) {
                    etPassword.requestFocus();
                    Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                    //etPassword.setError("Enter Password");
                    return;
                }
                if (!NetworkUtils.isNetworkAvailable(this)) {

                    Snackbar.make( view, getString(R.string.noInternet), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //   Toast.makeText(this,prefManager.getToken(),Toast.LENGTH_LONG).show();
                //switch user clear
                SharedPreferences preferences = getSharedPreferences(Constants.SWITCh_ParentDeatils_FINMART, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                loginRequestEntity.setUserName(etEmail.getText().toString());
                loginRequestEntity.setPassword(etPassword.getText().toString());
                loginRequestEntity.setDeviceId("" + new ReadDeviceID(this).getAndroidID());
                loginRequestEntity.setTokenId(prefManager.getToken());

                loginRequestEntity.setIsChildLogin("");

                prefManager.setFirstTimeLaunch(false);

                Log.d("TOKEN" , prefManager.getToken());
                showDialog();
                new LoginController(this).login(loginRequestEntity, this);
                break;
        }
    }

    private void dialogForgotPassword() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomDialog);
        builder.setCancelable(true);
        // builder.setTitle("FORGOT PASSWORD");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_forgot_password, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText etEmail = (EditText) view.findViewById(R.id.etEmail);
        Button btnReset = (Button) view.findViewById(R.id.btnReset);
        ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
       //Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isValideEmailID(etEmail)) {
                    etEmail.setError("Invalid Email ID");
                    etEmail.setFocusable(true);
                    //return;
                } else {
                    dialog.dismiss();
                    forgotPasswrod(etEmail);
                }
            }
        });

    }

    private void forgotPasswrod(EditText etEmail) {
        showDialog("Retrieving password...");
        new LoginController(LoginActivity.this)
                .forgotPassword(etEmail.getText().toString(), LoginActivity.this);
    }

    public void showPospAlert(String strBody) {
        try {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this,R.style.AlertDialog_Theme);
            builder.setTitle("PolicyBossPro Elite");

            builder.setMessage(strBody);
            String positiveText = "Ok";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
            final androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (Exception ex) {
            Toast.makeText(this, "Please try again..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        Constants.hideKeyBoard(tvSignUp, LoginActivity.this);
        if (response instanceof LoginResponse) {
            if (response.getStatusNo() == 0) {

                prefManager.setUserPassword("" + etPassword.getText().toString());

                if (((LoginResponse) response).getMasterData().getPOSPNo() != null) {
                    if (  !((LoginResponse) response).getMasterData().getPOSPNo().trim().isEmpty()) {
                        String PospID = ((LoginResponse) response).getMasterData().getPOSPNo();
                        new RegisterController(this).hideFOSUser(PospID, LoginActivity.this);

                    }else{
                        showPospAlert("Your Posp Number is not generated.!!\nNot Eligible For Login.Please Contact Admin");
                    }
                }else{

                    showPospAlert("Your Posp Number is not generated.!!\nNot Eligible For Login.Please Contact Admin");
                }
                // prefManager.setIsUserLogin(true);

            } else {
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else if(response instanceof UsersignupResponse){
            if (response.getStatusNo() == 0) {
                if( ((UsersignupResponse) response).getMasterData().get(0).getEnableEliteSignupurl() != null) {
                    enable_elite_signupurl = ((UsersignupResponse) response).getMasterData().get(0).getEnableEliteSignupurl();

                }

            }
            if(enable_elite_signupurl  != null) {
                if (enable_elite_signupurl.isEmpty()) {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                else
                {
                    Utility.loadWebViewUrlInBrowser(LoginActivity.this, enable_elite_signupurl);
                }
            }else
            {
                startActivity(new Intent(this, RegisterActivity.class));
            }
        }

        else if (response instanceof ForgotResponse) {
            if (response.getStatusNo() == 0) {
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
           // new TrackingController(this).sendData(new TrackingRequestEntity(new TrackingData("Login Success : " + response.getMessage()), "Login"), null);
        } else if (response instanceof UserHideResponse) {

            String strFOS_STATUS = "";
            if (((UserHideResponse) response).getMasterData().getHidesubuser() != null) {

                strFOS_STATUS = ((UserHideResponse) response).getMasterData().getHidesubuser();
                prefManager.setFosUSER(strFOS_STATUS);
            }

            if (!prefManager.getSharePushType().equals("")) {

                Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
                intent.putExtra(Utility.PUSH_LOGIN_PAGE, "555");
                startActivity(intent);

            } else {
                Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
                startActivity(intent);

            }

            finish();


        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    //    new TrackingController(this).sendData(new TrackingRequestEntity(new TrackingData("Login Failure : " + t.getMessage()), "Login"), null);
    }


    @Override
    public void onPositiveButtonClick(Dialog dialog, View view) {
        dialog.cancel();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, Constants.REQUEST_PERMISSION_SETTING);

    }

    @Override
    public void onCancelButtonClick(Dialog dialog, View view) {
        dialog.cancel();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, Constants.REQUEST_PERMISSION_SETTING);
    }

}
