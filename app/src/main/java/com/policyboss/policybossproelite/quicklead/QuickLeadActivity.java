package com.policyboss.policybossproelite.quicklead;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.home.HomeActivity;
import com.policyboss.policybossproelite.utility.Constants;
import com.policyboss.policybossproelite.utility.DateTimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.quicklead.QuickLeadController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.register.RegisterController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.QuickLeadRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PincodeResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.QuickLeadResponse;

public class QuickLeadActivity extends BaseActivity implements View.OnClickListener, IResponseSubcriber {

    EditText etFirstName, etLastName, etEmail, etMobile, etFollowupDate, etLoanAmount, etRemark,
            etPincode, etCity, etState, etdob, etPAN, etCompanyName, etMonthlyIncomeITR,
            etyealyIncomeITR;
    Spinner spProduct, spCompanyType, spprofile;
    TableRow tbl_monthly, tbl_yearly;
    Button btnSubmit;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");

    SimpleDateFormat simpleDateFormat_dob = new SimpleDateFormat("yyyy-MM-dd");
    ArrayAdapter<String> productTypeAdapter;
    ArrayAdapter<String> CompanyTypeAdapter;
    ArrayAdapter<String> profileAdapter;

    WebView webView;
    String url = "";
    Boolean isDataUploaded = true;
    QuickLeadRequestEntity requestEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_lead);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestEntity = new QuickLeadRequestEntity();
        init();
        spinnerProductBinding();


    }

    private void init() {
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);

        etdob = (EditText) findViewById(R.id.etdob);
        etPAN = (EditText) findViewById(R.id.etPAN);
        etCompanyName = (EditText) findViewById(R.id.etCompanyName);

        etMonthlyIncomeITR = (EditText) findViewById(R.id.etMonthlyIncomeITR);


        etyealyIncomeITR = (EditText) findViewById(R.id.etyealyIncomeITR);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etFollowupDate = (EditText) findViewById(R.id.etFollowupDate);
        etLoanAmount = (EditText) findViewById(R.id.etLoanAmount);
        etRemark = (EditText) findViewById(R.id.etRemark);

        tbl_monthly = (TableRow) findViewById(R.id.tbl_monthly);
        tbl_yearly = (TableRow) findViewById(R.id.tbl_yearly);
        tbl_monthly.setVisibility(View.GONE);
        tbl_yearly.setVisibility(View.GONE);

        spProduct = (Spinner) findViewById(R.id.spProduct);
        spCompanyType = (Spinner) findViewById(R.id.spCompanyType);
        spprofile = (Spinner) findViewById(R.id.spprofile);

        etPincode = (EditText) findViewById(R.id.etPincode);
        etPincode.addTextChangedListener(pincodeTextWatcher);

        etCity = (EditText) findViewById(R.id.etCity);
        etState = (EditText) findViewById(R.id.etState);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        etFollowupDate.setOnClickListener(datePicker);
        etdob.setOnClickListener(datePickerdob);


        webView = findViewById(R.id.webView);
        url = "http://erp.rupeeboss.com/loansrepository/Loans-repository.html";
        settingWebview();


    }

    private void spinnerProductBinding() {
        productTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.quicklead_product)) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(QuickLeadActivity.this);
                    convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
                String[] items = getResources().getStringArray(R.array.quicklead_product);
                tv.setText(items[position]);
                tv.setTextColor(Color.BLACK);
                tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                return convertView;
            }

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spProduct.setAdapter(productTypeAdapter);

        CompanyTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.quicklead_Company_Type));
        CompanyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCompanyType.setAdapter(CompanyTypeAdapter);

        profileAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.quicklead_Profile));
        profileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spprofile.setAdapter(profileAdapter);


        spprofile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    tbl_monthly.setVisibility(View.GONE);
                    tbl_yearly.setVisibility(View.GONE);
                } else if (position == 1) {
                    tbl_monthly.setVisibility(View.VISIBLE);
                    tbl_yearly.setVisibility(View.GONE);
                } else if (position == 2 || position == 3) {
                    tbl_monthly.setVisibility(View.GONE);
                    tbl_yearly.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected View.OnClickListener datePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Constants.hideKeyBoard(view, QuickLeadActivity.this);
            DateTimePicker.currentDateAndForward(QuickLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    if (datePicker.isShown()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String currentDay = simpleDateFormat.format(calendar.getTime());
                        etFollowupDate.setText(currentDay);
                    }
                }
            });
        }
    };

    protected View.OnClickListener datePickerdob = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Constants.hideKeyBoard(view, QuickLeadActivity.this);
            DateTimePicker.showHealthAgeDatePicker(QuickLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    if (datePicker.isShown()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String currentDay = simpleDateFormat_dob.format(calendar.getTime());
                        etdob.setText(currentDay);
                    }
                }
            });
        }
    };

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSubmit) {

            if (!isEmpty(etFirstName)) {
                etFirstName.setError("Enter First Name");
                etFirstName.setFocusable(true);
                return;
            } else {
                etFirstName.setError(null);
            }
            if (!isEmpty(etLastName)) {
                etLastName.setError("Enter Last Name");
                etLastName.setFocusable(true);
                return;
            } else {
                etLastName.setError(null);
            }

            if (!isValidePhoneNumber(etMobile)) {
                etMobile.setError("Enter Mobile Number");
                etMobile.setFocusable(true);
                return;
            } else {
                etMobile.setError(null);
            }

            if (!isValideEmailID(etEmail)) {
                etEmail.setError("Invalid Email ID");
                etEmail.setFocusable(true);
                return;
            }
            if (!isEmpty(etdob)) {
                etdob.setError("Invalid Dob  date");
                etdob.setFocusable(true);
                return;
            } else {
                etdob.setError(null);
            }
            if (!isEmpty(etFollowupDate)) {
                etFollowupDate.setError("Invalid Follow up date");
                etFollowupDate.setFocusable(true);
                return;
            } else {
                etFollowupDate.setError(null);
            }
            if (isEmpty(etPAN)) {
                if (!isValidPan(etPAN)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        etPAN.setError("Invalid PAN No.");
                        etPAN.setFocusable(true);
                        //   etPAN.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        return;
                    } else {
                        etPAN.setError("Invalid PAN No.");
                        etPAN.setFocusable(true);
                        return;
                    }
                }
            } else {
                etPAN.setError(null);
            }

            if (spProduct.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Select product", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isEmpty(etLoanAmount)) {
                etLoanAmount.setError("Enter Loan Amount");
                etLoanAmount.setFocusable(true);
                return;
            } else {
                etLoanAmount.setError(null);
            }
            if (!isEmpty(etCompanyName)) {
                etCompanyName.setError("Enter Company Name");
                etCompanyName.setFocusable(true);
                return;
            } else {
                etCompanyName.setError(null);
            }
            if (spCompanyType.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Select Company Type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spprofile.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Select Profile", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spprofile.getSelectedItem().toString().equals("Salaried")) {
//                if (!isEmpty(etMonthlyIncome)) {
//                    etMonthlyIncome.setError("Enter Monthly Obligation");
//                    etMonthlyIncome.setFocusable(true);
//                    return;
//                } else {
//                    etMonthlyIncome.setError(null);
//                }

                if (!isEmpty(etMonthlyIncomeITR)) {
                    etMonthlyIncomeITR.setError("Enter Monthly Income ITR");
                    etMonthlyIncomeITR.setFocusable(true);
                    return;
                } else {
                    etMonthlyIncomeITR.setError(null);
                }

            } else {
//                if (!isEmpty(etMonthlyIncomeYealy)) {
//                    etMonthlyIncomeYealy.setError("Enter Yealy Obligation");
//                    etMonthlyIncomeYealy.setFocusable(true);
//                    return;
//                } else {
//                    etMonthlyIncomeYealy.setError(null);
//                }

                if (!isEmpty(etyealyIncomeITR)) {
                    etyealyIncomeITR.setError("Enter Yealy Income ITR");
                    etyealyIncomeITR.setFocusable(true);
                    return;
                } else {
                    etyealyIncomeITR.setError(null);
                }
            }
            if (!isEmpty(etPincode)) {
                etPincode.setError("Enter Pincode");
                etPincode.setFocusable(true);
                return;
            } else {
                etPincode.setError(null);
            }
            if (etPincode.getText().length() == 6) {
            } else {
                etPincode.setError("Enter Six Digit Pincode");
                etPincode.setFocusable(true);
                return;
            }
            if (!isEmpty(etRemark)) {
                etRemark.setError("Enter Remark");
                etRemark.setFocusable(true);
                return;
            } else {
                etRemark.setError(null);
            }


            requestEntity.setName("" + etFirstName.getText().toString() + " " + etLastName.getText().toString());
            requestEntity.setBrokerId(new DBPersistanceController(this).getUserData().getLoanId());
            requestEntity.setEMail(etEmail.getText().toString());
            requestEntity.setFBA_Id(String.valueOf(new DBPersistanceController(this).getUserData().getFBAId()));
            requestEntity.setFollowupDate(etFollowupDate.getText().toString());
            requestEntity.setLoan_amt(etLoanAmount.getText().toString());
            requestEntity.setMobile(etMobile.getText().toString());
            if (spprofile.getSelectedItem().equals("Salaried")) {
                requestEntity.setMonthly_income(etMonthlyIncomeITR.getText().toString());
            } else {
                requestEntity.setMonthly_income(etyealyIncomeITR.getText().toString());
            }

            requestEntity.setProductId(String.valueOf(spProduct.getSelectedItemPosition()));
            requestEntity.setCompanyName(etCompanyName.getText().toString());
            requestEntity.setCompanyType(String.valueOf(spCompanyType.getSelectedItem()));
            requestEntity.setProfile(String.valueOf(spprofile.getSelectedItem()));
            requestEntity.setRemark(etRemark.getText().toString());
            requestEntity.setPAN("" + etPAN.getText().toString());
            requestEntity.setPincode("" + etPincode.getText().toString());
            requestEntity.setCity("" + etCity.getText().toString());
            requestEntity.setState("" + etState.getText().toString());
            showDialog();
            new QuickLeadController(this).saveQuickLead(requestEntity, this);
        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof PincodeResponse) {
            if (response.getStatusNo() == 0) {
                etState.setText("" + ((PincodeResponse) response).getMasterData().getState_name());
                etCity.setText("" + ((PincodeResponse) response).getMasterData().getCityname());

                //  requestEntity.setCity("" + ((PincodeResponse) response).getMasterData().getCityname());
                //  requestEntity.setState("" + ((PincodeResponse) response).getMasterData().getState_name());
                //  requestEntity.setStateID("" + ((PincodeResponse) response).getMasterData().getStateid());

            } else {

                etState.setText("");
                etCity.setText("");

                //   requestEntity.setCity("");
                //    requestEntity.setState("");
                //  requestEntity.setStateID("0");

            }
        } else if (response instanceof QuickLeadResponse) {
            dialogMessage(true, ((QuickLeadResponse) response).getMasterData().getLead_Id(), response.getMessage());
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        dialogMessage(false, t.getMessage(), "");
    }


    private void dialogMessage(final boolean isSuccess, String AppNo, String displayMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        StringBuilder Message = new StringBuilder();
        if (isSuccess) {
            builder.setTitle("Lead Saved..!");
            String strMessage = "Lead ID:" + AppNo + "\n\n";
            String success = displayMessage;
            Message.append(strMessage + success);

        } else {
            builder.setTitle("Failed");
            String failure = AppNo;
            Message.append(failure);
        }
        builder.setMessage(Message.toString())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (isSuccess) {
                            finish();
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        TextView msgTxt = (TextView) dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(14.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

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

    @Override
    public void onBackPressed() {

        supportFinishAfterTransition();
        super.onBackPressed();
    }

    private void settingWebview() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(false);

        settings.setLoadsImagesAutomatically(true);
        settings.setLightTouchEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);


        /*MyWebViewClient webViewClient = new MyWebViewClient(this);
        webView.setWebViewClient(webViewClient);*/
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO show you progress image
                showDialog();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO hide your progress image
                cancelDialog();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.endsWith(".pdf")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        //user does not have a pdf viewer installed
                        String googleDocs = "https://docs.google.com/viewer?url=";
                        webView.loadUrl(googleDocs + url);
                    }
                }
                return false;
            }
        });
        webView.getSettings().setBuiltInZoomControls(true);
        Log.d("URL", url);
        //webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        webView.loadUrl(url);
    }


    //region textwatcher
    TextWatcher pincodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (start == 5) {
                etCity.setText("");
                etState.setText("");
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ((s.length() == 6) && (isDataUploaded)) {
                showDialog("Fetching City...");
                Toast.makeText(QuickLeadActivity.this, "Fetching City...Data", Toast.LENGTH_LONG).show();
                new RegisterController(QuickLeadActivity.this).getCityState(etPincode.getText().toString(), QuickLeadActivity.this);

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
