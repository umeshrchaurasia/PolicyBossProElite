package com.policyboss.policybossproelite.paymentEliteplan;


import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;

import com.policyboss.policybossproelite.home.HomeActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.register.RegisterController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserConstantEntity;

import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.PaymentEliteEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PaymentDetail_EliteResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.RazorPayResponse;

public class RazorPaymentEliteActivity extends BaseActivity implements PaymentResultListener, View.OnClickListener, IResponseSubcriber {

    private static final String TAG = "RAZOR_PAYMENT";
    Button btnBuy, btnCancel, btnContinue, btnHomeContinue;
    CardView cvBuy, cvSuccess, cvFailure;
    TextView txtCustomerName, txtProdName, txtDisplayAmount, txtSuccessMessage, txtSuccessTitle, txtFailureMessage,
            txtpaymentstatus,txtfailcustid;

    LoginResponseEntity loginResponseEntity;
    DBPersistanceController db;
    UserConstantEntity userConstantEntity;
    PaymentEliteEntity paymentEliteEntity;
    String custid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_payment_elite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        custid = getIntent().getStringExtra("cust_id");


        db = new DBPersistanceController(this);
        loginResponseEntity = db.getUserData();
        userConstantEntity = db.getUserConstantsData();



        initialize();
        setListner();
        cvBuy.setVisibility(View.VISIBLE);
        cvSuccess.setVisibility(View.GONE);
        cvFailure.setVisibility(View.GONE);

        Checkout.preload(getApplicationContext());

        Log.d(TAG, "Custtid :   " + "" + custid);
        showDialog();
        new RegisterController(this).getDataForPayment_elite(String.valueOf(custid), RazorPaymentEliteActivity.this);


    }

    private void initialize() {


        cvBuy = (CardView) findViewById(R.id.cvBuy);
        cvSuccess = (CardView) findViewById(R.id.cvSuccess);
        cvFailure = (CardView) findViewById(R.id.cvFailure);

        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnHomeContinue = (Button) findViewById(R.id.btnHomeContinue);
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtProdName = (TextView) findViewById(R.id.txtProdName);
        txtDisplayAmount = (TextView) findViewById(R.id.txtDisplayAmount);
        txtSuccessMessage = (TextView) findViewById(R.id.txtSuccessMessage);
        txtSuccessTitle = (TextView) findViewById(R.id.txtSuccessTitle);
        txtFailureMessage = (TextView) findViewById(R.id.txtFailureMessage);

        txtpaymentstatus = (TextView) findViewById(R.id.txtpaymentstatus);
        txtfailcustid = (TextView) findViewById(R.id.txtfailcustid);

    }

    private void setListner() {
        btnBuy.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnHomeContinue.setOnClickListener(this);
    }


    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", paymentEliteEntity.getFullname() + " - " + paymentEliteEntity.getCustID());
            options.put("description", paymentEliteEntity.getPlanname());
            //You can omit the image option to fetch the image from dashboard
            options.put("image", paymentEliteEntity.getImage());
            options.put("currency", "INR");
            options.put("amount", paymentEliteEntity.getAmount());//paymentEliteEntity.getAmount());

            JSONObject preFill = new JSONObject();
            preFill.put("email", paymentEliteEntity.getEmail());
            preFill.put("contact", paymentEliteEntity.getMobile());


            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    private void setPaymentDetail(PaymentEliteEntity paymentEliteEntity) {

        String custname = paymentEliteEntity.getFullname() + " - " + paymentEliteEntity.getCustID();
        txtCustomerName.setText(custname);
        txtProdName.setText(paymentEliteEntity.getPlanname());
        txtDisplayAmount.setText("\u20B9" + " " + paymentEliteEntity.getDisplayamount());
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnBuy) {

            if (paymentEliteEntity != null) {
                startPayment();
            }


        } else if (view.getId() == R.id.btnCancel) {

            RazorPaymentEliteActivity.this.finish();
            startActivity(new Intent(RazorPaymentEliteActivity.this, HomeActivity.class));
        } else if (view.getId() == R.id.btnContinue) {
            RazorPaymentEliteActivity.this.finish();
           // this.finish();
            startActivity(new Intent(RazorPaymentEliteActivity.this, HomeActivity.class));
        } else if (view.getId() == R.id.btnHomeContinue) {
            RazorPaymentEliteActivity.this.finish();

            startActivity(new Intent(RazorPaymentEliteActivity.this, HomeActivity.class));
        }


    }


    // region Razor Pay CallBack
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // Toast.makeText(this, "Payment Successfully Done : " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Pyment Success with RazorPaymentID  " + razorpayPaymentID);

        showDialog();

        new RegisterController(this).addToRazorPay_elite(String.valueOf(loginResponseEntity.getFBAId()), String.valueOf(paymentEliteEntity.getCustID()), razorpayPaymentID, RazorPaymentEliteActivity.this);


    }

    @Override
    public void onPaymentError(int code, String response) {

        try {

            cvFailure.setVisibility(View.VISIBLE);
            cvBuy.setVisibility(View.GONE);
            cvSuccess.setVisibility(View.GONE);

            txtpaymentstatus.setText("FAILED");
            txtfailcustid.setText(paymentEliteEntity.getCustID());

            Log.d(TAG, "Payment failed: " + code + " " + response);
        } catch (Exception e) {
            Log.d(TAG, "Exception in onPaymentError " + e.toString());
        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();
        if (response instanceof PaymentDetail_EliteResponse) {

            if (response.getStatusNo() == 0) {
                paymentEliteEntity = ((PaymentDetail_EliteResponse) response).getMasterData();

                if (String.valueOf(paymentEliteEntity.getCustID()).equals("0")) {
                   useretailPopUp(paymentEliteEntity);
                } else {
                    setPaymentDetail(paymentEliteEntity);
                }

            }

        } else if (response instanceof RazorPayResponse) {////////////
            if (response.getStatusNo() == 0) {

                // prefManager.setIsUserLogin(true);
                // Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                cvBuy.setVisibility(View.GONE);
                cvFailure.setVisibility(View.GONE);

                cvSuccess.setVisibility(View.VISIBLE);

                txtSuccessMessage.setText(((RazorPayResponse) response).getMasterData().get(0).getRespmsg());

            } else {

                Log.d(TAG, "Failure : get fba data for pay method to sever   " + "" + response.getMessage());
            }
        }
    }

    @Override
    public void OnFailure(Throwable t) {

        cancelDialog();

        Log.d(TAG, "Failure :   " + "" + t.getMessage());
    }

    //endregion

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
        if (cvFailure.getVisibility() == View.VISIBLE) {
            startActivity(new Intent(RazorPaymentEliteActivity.this, HomeActivity.class));
        }
        this.finish();
        super.onBackPressed();
    }


    public void useretailPopUp(PaymentEliteEntity paymentDetailEntity) {


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(RazorPaymentEliteActivity.this);
        TextView txtTitle, txtMessage;
        Button btnShare;
        ImageView ivCross;
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.layout_share_popup, null);

        builder.setView(dialogView);
        androidx.appcompat.app.AlertDialog shareProdDialog = builder.create();
        // set the custom dialog components - text, image and button
        txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
        txtMessage = (TextView) dialogView.findViewById(R.id.txtMessage);
        btnShare = (Button) dialogView.findViewById(R.id.btnShare);
        ivCross = (ImageView) dialogView.findViewById(R.id.ivCross);

        txtTitle.setText("Finmart Message");
        btnShare.setText("OK");
      //  txtMessage.setText("" + paymentDetailEntity.getDispmsg());
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shareProdDialog.dismiss();
                RazorPaymentEliteActivity.this.finish();

            }
        });

        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareProdDialog.dismiss();
                RazorPaymentEliteActivity.this.finish();
            }
        });

        shareProdDialog.setCancelable(false);
        shareProdDialog.show();
        //  alertDialog.getWindow().setLayout(900, 600);

        // for user define height and width..
    }
}