package com.datacomp.magicfinmart.term.hdfc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datacomp.magicfinmart.BaseFragment;
import com.datacomp.magicfinmart.R;
import com.datacomp.magicfinmart.utility.Constants;
import com.datacomp.magicfinmart.utility.DateTimePicker;
import com.datacomp.magicfinmart.webviews.CommonWebViewActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.term.TermInsuranceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.tracking.TrackingController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TermCompareResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TrackingData;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TermFinmartRequest;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TermRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TrackingRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.TermCompareQuoteResponse;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by Rajeev Ranjan on 17/05/2018.
 */

public class HdfcInputFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener, IResponseSubcriber {

    //region header views
    LinearLayout llGender, llSmoker;
    EditText etFirstName, etLastName, etMobile, etDOB;
    TextView tvMale, tvFemale, tvYes, tvNo;
    boolean isMale, isSmoker;
    //endregion

    //region headers
    TextView tvSum, tvGender, tvSmoker, tvAge, tvPolicyTerm, tvCrn;
    ImageView ivEdit;
    TermCompareQuoteResponse termCompareQuoteResponse;
    CardView cvInputDetails, cvQuoteDetails;
    LinearLayout layoutCompare;

    TextView txtPlanNAme, txtCover, txtFinalPremium, txtPolicyTerm, txtAge;
    ImageView imgInsurerLogo, ivBuy;
    LinearLayout llAddon;
    RecyclerView rvAddOn;

    Button btnGetQuote;
    TextInputLayout tilPincode;
    EditText etPincode, etSumAssured;
    TermRequestEntity termRequestEntity;
    TermFinmartRequest termFinmartRequest;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    //endregion

    //region hdfc specific
    LinearLayout llHDFCSAInc, llIncDeath, llIncPeriod, llINCREASING, llAdb;
    Button minusICICISum, plusICICISum, minusICICIPTerm, plusICICIPTerm,
            minusICICIPreTerm, plusICICIPreTerm, minusHDFCSAInc, plusHDFCSAInc,
            minusIncDeath, plusIncDeath, minusIncPeriod, plusIncPeriod,
            minusINCREASING, plusINCREASING, minusAdb, plusAdb;
    EditText etICICISumAssured, etICICIPolicyTerm, etICICIPremiumTerm, etHDFCSAInc,
            etIncDeath, etIncPeriod, etINCREASING, etAdb;

    int minSumAssured, maxSumAssured, minPolicyTerm, maxPolicyTerm, minPremiumTerm, maxPremiumTerm,
            minHDFCSAInc, maxHDFCSAInc, minIncDeath, maxIncDeath, minIncPeriod, maxIncPeriod,
            minINCREASING, maxINCREASING, minAdb, maxAdb;
    long hfLumsumPayOutOnDeath, hfLumsumAmt;
    //endregion

    DBPersistanceController dbPersistanceController;
    Spinner spHDFCOptions, spHdfcPremFrq;
    int termRequestId = 0;
    int age = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hdfc_input, container, false);
        init_view(view);
        setListener();
        // set initial values
        dbPersistanceController = new DBPersistanceController(getActivity());
        termRequestEntity = new TermRequestEntity();
        termFinmartRequest = new TermFinmartRequest();
        setDefaultValues();
        //init_adapters();

        //adapter_listener();
        if (getArguments() != null) {
            if (getArguments().getParcelable(HdfcTermActivity.INPUT_DATA) != null) {
                termFinmartRequest = getArguments().getParcelable(HdfcTermActivity.INPUT_DATA);
                termRequestId = termFinmartRequest.getTermRequestId();
            }
            //bindICICI();
            if (termFinmartRequest != null && termFinmartRequest.getTermRequestEntity() != null)
                bindInput(termFinmartRequest);
        }
        changeInputQuote(true);
        return view;
    }

    private void bindInput(TermFinmartRequest termFinmartRequest) {
        try {
            TermRequestEntity termRequestEntity = termFinmartRequest.getTermRequestEntity();
            if (termRequestEntity != null) {

                if (termRequestEntity.getPlanTaken().equals("LIFE")) {
                    spHDFCOptions.setSelection(0);
                } else if (termRequestEntity.getPlanTaken().equals("3D LIFE")) {
                    spHDFCOptions.setSelection(1);
                } else if (termRequestEntity.getPlanTaken().equals("LIFE LONG PROTECTION")) {
                    spHDFCOptions.setSelection(2);
                } else if (termRequestEntity.getPlanTaken().equals("3D LIFE LONG PROTECTION")) {
                    spHDFCOptions.setSelection(3);
                } else if (termRequestEntity.getPlanTaken().equals("EXTRA LIFE")) {
                    spHDFCOptions.setSelection(3);
                } else if (termRequestEntity.getPlanTaken().equals("EXTRA LIFE INCOME")) {
                    spHDFCOptions.setSelection(3);
                } else if (termRequestEntity.getPlanTaken().equals("INCOME OPTION")) {
                    spHDFCOptions.setSelection(3);
                } else if (termRequestEntity.getPlanTaken().equals("INCOME REPLACEMENT")) {
                    spHDFCOptions.setSelection(3);
                } else if (termRequestEntity.getPlanTaken().equals("RETURN OF PREMIUM")) {
                    spHDFCOptions.setSelection(3);
                }

                if (termRequestEntity.getFrequency().equals("YEARLY")) {
                    spHdfcPremFrq.setSelection(0);
                } else if (termRequestEntity.getFrequency().equals("HALF YEARLY")) {
                    spHdfcPremFrq.setSelection(1);
                } else if (termRequestEntity.getFrequency().equals("QUARTERLY")) {
                    spHdfcPremFrq.setSelection(2);
                } else if (termRequestEntity.getFrequency().equals("MONTHLY")) {
                    spHdfcPremFrq.setSelection(3);
                } else if (termRequestEntity.getFrequency().equals("SINGLE")) {
                    spHdfcPremFrq.setSelection(4);
                }


                String[] splitStr = termRequestEntity.getContactName().split("\\s+");
                etFirstName.setText("" + splitStr[0]);
                etLastName.setText("" + splitStr[1]);
                etMobile.setText("" + termRequestEntity.getContactMobile());
                etDOB.setText("" + termRequestEntity.getInsuredDOB());
                etPincode.setText("" + termRequestEntity.getPincode());

                if (termRequestEntity.getIs_TabaccoUser().equals("true")) {
                    tvYes.setBackgroundResource(R.drawable.customeborder_blue);
                    tvNo.setBackgroundResource(R.drawable.customeborder);
                } else {
                    tvNo.setBackgroundResource(R.drawable.customeborder_blue);
                    tvYes.setBackgroundResource(R.drawable.customeborder);
                }

                if (termRequestEntity.getInsuredGender().equals("M")) {
                    tvMale.setBackgroundResource(R.drawable.customeborder_blue);
                    tvFemale.setBackgroundResource(R.drawable.customeborder);
                } else {
                    tvFemale.setBackgroundResource(R.drawable.customeborder_blue);
                    tvMale.setBackgroundResource(R.drawable.customeborder);
                }

                if (termRequestEntity.getMonthlyIncome() != null && !termRequestEntity.getMonthlyIncome().equals(""))
                    etIncDeath.setText("" + termRequestEntity.getMonthlyIncome());

                if (termRequestEntity.getIncomeTerm() != null && !termRequestEntity.getIncomeTerm().equals(""))
                    etIncPeriod.setText("" + termRequestEntity.getIncomeTerm());

                if (termRequestEntity.getIncreaseIncomePercentage() != null && !termRequestEntity.getIncreaseIncomePercentage().equals(""))
                    etINCREASING.setText("" + termRequestEntity.getIncreaseIncomePercentage());

                if (termRequestEntity.getADBPercentage() != null && !termRequestEntity.getADBPercentage().equals(""))
                    etAdb.setText("" + termRequestEntity.getLumpsumPercentage());

                etICICISumAssured.setText("" + termRequestEntity.getSumAssured());
                etICICIPolicyTerm.setText("" + termRequestEntity.getPolicyTerm());
                etICICIPremiumTerm.setText("" + termRequestEntity.getPPT());
                etHDFCSAInc.setText("" + termRequestEntity.getIncreaseSAPercentage());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bindHeaders() {
        if (termRequestEntity != null) {

            tvSum.setText("");
            tvSum.append("SUM  ");
            SpannableString SUM = new SpannableString(termRequestEntity.getSumAssured());
            SUM.setSpan(new StyleSpan(Typeface.BOLD), 0, termRequestEntity.getSumAssured().length(), 0);
            tvSum.append(SUM);


            try {
                tvAge.setText("");
                tvAge.append("AGE  ");
                Date ag = simpleDateFormat.parse(termRequestEntity.getInsuredDOB());
                Calendar ageCalender = Calendar.getInstance();
                ageCalender.setTime(ag);
                String age = "" + caluclateAge(ageCalender);
                SpannableString AGE = new SpannableString(age);
                AGE.setSpan(new StyleSpan(Typeface.BOLD), 0, age.length(), 0);
                tvAge.append(AGE);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            tvPolicyTerm.setText("");
            tvPolicyTerm.append("TERM  ");
            SpannableString TERM = new SpannableString(termRequestEntity.getPolicyTerm());
            TERM.setSpan(new StyleSpan(Typeface.BOLD), 0, termRequestEntity.getPolicyTerm().length(), 0);
            tvPolicyTerm.append(TERM);
            tvPolicyTerm.append(" YRS");

            if (termRequestEntity.getInsuredGender().equals("M"))
                tvGender.setText("MALE");
            else
                tvGender.setText("FEMALE");
            if (termRequestEntity.getIs_TabaccoUser().equals("true"))
                tvSmoker.setText("SMOKER");
            else
                tvSmoker.setText("NON-SMOKER");


            tvCrn.setText("");
            tvCrn.append("CRN  ");
            String crn = "" + termCompareQuoteResponse.getMasterData().getResponse().get(0).getCustomerReferenceID();
            SpannableString CRN = new SpannableString(crn);
            CRN.setSpan(new StyleSpan(Typeface.BOLD), 0, crn.length(), 0);
            tvCrn.append(CRN);
            termRequestEntity.setCrn(crn);
            termFinmartRequest.setTermRequestEntity(termRequestEntity);

            // tvAge.setText("" + termRequestEntity.getInsuredDOB());
            //tvPolicyTerm.setText("" + termRequestEntity.getPolicyTerm() + " YEARS");
            //tvCrn.setText("---");
        }
    }

    private void bindQuotes() {
        final TermCompareResponseEntity responseEntity = termCompareQuoteResponse.getMasterData().getResponse().get(0);
        txtPlanNAme.setText("" + responseEntity.getProductPlanName());
        txtCover.setText("\u20B9 " + responseEntity.getSumAssured());
        txtPolicyTerm.setText(responseEntity.getPolicyTermYear() + " Yrs.");
        txtFinalPremium.setText("\u20B9 " + responseEntity.getNetPremium() + "/Year");
        int uptoAge = Integer.parseInt(termRequestEntity.getPPT()) + caluclateAge(etDOB.getText().toString());
        txtAge.setText("" + uptoAge + " Yrs.");
        //  txtFinalPremium.setText("\u20B9 " + Math.round(Double.parseDouble(responseEntity.getFinal_premium_with_addon())));

       /* Glide.with(getActivity())
                .load("http://www.policyboss.com/Images/insurer_logo/" + responseEntity.getInsurerLogoName())
                .into(imgInsurerLogo);*/

        txtFinalPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((TermInputFragment) getActivity()).redirectToBuy(responseEntity);
            }
        });

        /*if (responseEntity.getKeyFeatures() != null) {
            llAddon.setVisibility(View.VISIBLE);
            rvAddOn.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext.getActivity(), 2);
            rvAddOn.setLayoutManager(mLayoutManager);
            GridTermAdapter adapter = new GridTermAdapter(mContext.getActivity(), responseEntity.getKeyFeatures().split("\\|"));
            rvAddOn.setAdapter(adapter);

        } else {
            llAddon.setVisibility(View.GONE);
        }*/
    }

    public boolean isValidInput() {
        if (etFirstName.getText().toString().isEmpty()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etFirstName.requestFocus();
                etFirstName.setError("Enter First Name");
                etFirstName.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etFirstName.requestFocus();
                etFirstName.setError("Enter First Name");
                return false;
            }
        }

        if (etLastName.getText().toString().isEmpty()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etLastName.requestFocus();
                etLastName.setError("Enter Last Name");
                etLastName.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etLastName.requestFocus();
                etLastName.setError("Enter Last Name");
                return false;
            }
        }
        if (etDOB.getText().toString().isEmpty()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etDOB.requestFocus();
                etDOB.setError("Enter Dob");
                etDOB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etDOB.requestFocus();
                etDOB.setError("Enter Dob");
                return false;
            }
        }
        if (!isValidePhoneNumber(etMobile)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etMobile.requestFocus();
                etMobile.setError("Enter Mobile");
                etMobile.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etMobile.requestFocus();
                etMobile.setError("Enter Mobile");
                return false;
            }
        }
        if (etPincode.getText().toString().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etPincode.requestFocus();
                etPincode.setError("Enter Pincode");
                etPincode.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etPincode.requestFocus();
                etPincode.setError("Enter Pincode");
                return false;
            }

        }
        if (!etPincode.getText().toString().isEmpty()) {
            if (etPincode.getText().toString().length() != 6) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etPincode.requestFocus();
                    etPincode.setError("Enter Valid Pincode");
                    etPincode.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    return false;
                } else {
                    etPincode.requestFocus();
                    etPincode.setError("Enter Valid Pincode");
                    return false;
                }
            }

        }

        if (etICICISumAssured.getText().toString().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etICICISumAssured.requestFocus();
                etICICISumAssured.setError("Enter Sum Assured");
                etICICISumAssured.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etICICISumAssured.requestFocus();
                etICICISumAssured.setError("Enter Sum Assured");
                return false;
            }

        }

        if (etICICIPolicyTerm.getText().toString().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etICICIPolicyTerm.requestFocus();
                etICICIPolicyTerm.setError("Enter Policy Term ");
                etICICIPolicyTerm.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etICICIPolicyTerm.requestFocus();
                etICICIPolicyTerm.setError("Enter Policy Term ");
                return false;
            }

        }

        if (etICICIPremiumTerm.getText().toString().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etICICIPremiumTerm.requestFocus();
                etICICIPremiumTerm.setError("Enter Premium Term ");
                etICICIPremiumTerm.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etICICIPremiumTerm.requestFocus();
                etICICIPremiumTerm.setError("Enter Premium Term ");
                return false;
            }

        }
        if (llHDFCSAInc.getVisibility() == View.VISIBLE) {
            if (etHDFCSAInc.getText().toString().isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etHDFCSAInc.requestFocus();
                    etHDFCSAInc.setError("Enter SA Increasing@");
                    etHDFCSAInc.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    return false;
                } else {
                    etHDFCSAInc.requestFocus();
                    etHDFCSAInc.setError("Enter SA Increasing@");
                    return false;
                }
            }
        }
        if (llIncDeath.getVisibility() == View.VISIBLE) {
            if (etIncDeath.getText().toString().isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etIncDeath.requestFocus();
                    etIncDeath.setError("Enter Income On Death");
                    etIncDeath.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    return false;
                } else {
                    etIncDeath.requestFocus();
                    etIncDeath.setError("Enter Income On Death");
                    return false;
                }
            }
        }
        if (llIncPeriod.getVisibility() == View.VISIBLE) {
            if (etIncPeriod.getText().toString().isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etIncPeriod.requestFocus();
                    etIncPeriod.setError("Enter Income Period");
                    etIncPeriod.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    return false;
                } else {
                    etIncPeriod.requestFocus();
                    etIncPeriod.setError("Enter Income Period");
                    return false;
                }
            }
        }
        if (llINCREASING.getVisibility() == View.VISIBLE) {
            if (etINCREASING.getText().toString().isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etINCREASING.requestFocus();
                    etINCREASING.setError("Enter Increasing@");
                    etINCREASING.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    return false;
                } else {
                    etINCREASING.requestFocus();
                    etINCREASING.setError("Enter Increasing@");
                    return false;
                }
            }
        }
        if (llAdb.getVisibility() == View.VISIBLE) {
            if (etAdb.getText().toString().isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etAdb.requestFocus();
                    etAdb.setError("Enter ADB %");
                    etAdb.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    return false;
                } else {
                    etAdb.requestFocus();
                    etAdb.setError("Enter ADB %");
                    return false;
                }
            }
        }


        return true;
    }

    private void setTermRequest() {
        //termRequestEntity.setPolicyTerm("" + dbPersistanceController.getPremYearID(spPolicyTerm.getSelectedItem().toString()));

        if (isMale)
            termRequestEntity.setInsuredGender("M");
        else
            termRequestEntity.setInsuredGender("F");

        if (isSmoker)
            termRequestEntity.setIs_TabaccoUser("true");
        else
            termRequestEntity.setIs_TabaccoUser("false");


        termRequestEntity.setSumAssured(etSumAssured.getText().toString().replaceAll("\\,", ""));
        termRequestEntity.setInsuredDOB(etDOB.getText().toString());
        termRequestEntity.setPaymentModeValue("1");
        termRequestEntity.setPolicyCommencementDate(etDOB.getText().toString());
        termRequestEntity.setCityName("Mumbai");
        termRequestEntity.setState("Maharashtra");
        //termRequestEntity.setPlanTaken("Life");
        // termRequestEntity.setFrequency("Annual");
        //termRequestEntity.setDeathBenefitOption("Lump-Sum");
        //termRequestEntity.setPPT("" + dbPersistanceController.getPremYearID(spPremTerm.getSelectedItem().toString()));
        //termRequestEntity.setIncomeTerm("" + dbPersistanceController.getPremYearID(spPremTerm.getSelectedItem().toString()));

        //termRequestEntity.setInsurerId(0);
        termRequestEntity.setSessionID("");
        termRequestEntity.setExisting_ProductInsuranceMapping_Id("");
        termRequestEntity.setContactName(etFirstName.getText().toString() + " " + etLastName.getText().toString());
        termRequestEntity.setContactEmail("finmarttest@gmail.com");
        termRequestEntity.setContactMobile(etMobile.getText().toString());
        termRequestEntity.setSupportsAgentID("1682");
        termRequestEntity.setPincode(etPincode.getText().toString());


        //icici specific
        termRequestEntity.setMaritalStatus("");
        //termRequestEntity.setPremiumPaymentOption(""); //set in optionSelected
        termRequestEntity.setServiceTaxNotApplicable("");// not known


        if (llHDFCSAInc.getVisibility() == View.VISIBLE)
            termRequestEntity.setIncreaseSAPercentage("" + etHDFCSAInc.getText().toString().replaceAll("\\,", ""));
        else
            termRequestEntity.setIncreaseSAPercentage("0");

        if (llIncDeath.getVisibility() == View.VISIBLE)
            termRequestEntity.setMonthlyIncome("" + etIncDeath.getText().toString().replaceAll("\\,", ""));
        else
            termRequestEntity.setMonthlyIncome("0");

        if (llIncPeriod.getVisibility() == View.VISIBLE)
            termRequestEntity.setIncomeTerm("" + etIncPeriod.getText().toString().replaceAll("\\,", ""));
        else
            termRequestEntity.setIncomeTerm("0");

        if (llINCREASING.getVisibility() == View.VISIBLE)
            termRequestEntity.setIncreaseIncomePercentage(etINCREASING.getText().toString());
        else
            termRequestEntity.setIncreaseIncomePercentage("0");

        if (llAdb.getVisibility() == View.VISIBLE)
            termRequestEntity.setADBPercentage(etAdb.getText().toString());
        else
            termRequestEntity.setADBPercentage("0");

        if (hfLumsumAmt != 0) {
            termRequestEntity.setLumpsumAmount("" + hfLumsumAmt);
        }
        if (hfLumsumPayOutOnDeath != 0) {
            termRequestEntity.setMonthlyIncome("" + hfLumsumAmt);
        }


        termRequestEntity.setPolicyTerm("" + etICICIPolicyTerm.getText().toString());
        termRequestEntity.setInsurerId(28);
        //termRequestEntity.setPlanTaken("Life");// set in manipulateInputs()
        //termRequestEntity.setFrequency("Annual"); //set in optionSelected
        //termRequestEntity.setDeathBenefitOption("Lump-Sum"); //set in incomeSelection()
        termRequestEntity.setPPT("" + etICICIPremiumTerm.getText().toString());

       /* if (termCompareQuoteResponse != null && termCompareQuoteResponse.getMasterData() != null && termCompareQuoteResponse.getMasterData().getLifeTermRequestID() != 0)
            termFinmartRequest.setTermRequestId(termCompareQuoteResponse.getMasterData().getLifeTermRequestID());
        else
            termFinmartRequest.setTermRequestId(0);*/
        termFinmartRequest.setFba_id(new DBPersistanceController(getActivity()).getUserData().getFBAId());
        termFinmartRequest.setTermRequestEntity(termRequestEntity);
    }

    private void setDefaultValues() {
        etICICISumAssured.setText("10,000,000");
        etICICIPolicyTerm.setText("20");
        etICICIPremiumTerm.setText("20");
        etHDFCSAInc.setText("10");
        etIncDeath.setText("25,000");
        etIncPeriod.setText("20");
        etINCREASING.setText("5");
        etAdb.setText("100");
    }


    private void init_view(View view) {

        cvInputDetails = (CardView) view.findViewById(R.id.cvInputDetails);
        cvQuoteDetails = (CardView) view.findViewById(R.id.cvQuoteDetails);
        layoutCompare = (LinearLayout) view.findViewById(R.id.layoutCompare);
        tilPincode = (TextInputLayout) view.findViewById(R.id.tilPincode);
        tvSum = (TextView) view.findViewById(R.id.tvSum);
        tvGender = (TextView) view.findViewById(R.id.tvGender);
        tvSmoker = (TextView) view.findViewById(R.id.tvSmoker);
        tvAge = (TextView) view.findViewById(R.id.tvAge);
        tvPolicyTerm = (TextView) view.findViewById(R.id.tvPolicyTerm);
        tvCrn = (TextView) view.findViewById(R.id.tvCrn);
        ivEdit = (ImageView) view.findViewById(R.id.ivEdit);

        llAddon = (LinearLayout) view.findViewById(R.id.llAddon);
        rvAddOn = (RecyclerView) view.findViewById(R.id.rvAddOn);
        txtAge = (TextView) view.findViewById(R.id.txtAge);
        txtPlanNAme = (TextView) view.findViewById(R.id.txtPlanNAme);
        txtCover = (TextView) view.findViewById(R.id.txtCover);
        txtFinalPremium = (TextView) view.findViewById(R.id.txtFinalPremium);
        imgInsurerLogo = (ImageView) view.findViewById(R.id.imgInsurerLogo);
        ivBuy = (ImageView) view.findViewById(R.id.ivBuy);
        txtPolicyTerm = (TextView) view.findViewById(R.id.txtPolicyTerm);

        btnGetQuote = (Button) view.findViewById(R.id.btnGetQuote);
        etPincode = (EditText) view.findViewById(R.id.etPincode);
        etSumAssured = (EditText) view.findViewById(R.id.etICICISumAssured);

        // common input
        tvMale = (TextView) view.findViewById(R.id.tvMale);
        tvFemale = (TextView) view.findViewById(R.id.tvFemale);
        tvYes = (TextView) view.findViewById(R.id.tvYes);
        tvNo = (TextView) view.findViewById(R.id.tvNo);
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etMobile = (EditText) view.findViewById(R.id.etMobile);
        etDOB = (EditText) view.findViewById(R.id.etDateofBirth);
        llGender = (LinearLayout) view.findViewById(R.id.llGender);
        llSmoker = (LinearLayout) view.findViewById(R.id.llSmoker);

        spHDFCOptions = (Spinner) view.findViewById(R.id.spHDFCOptions);
        spHdfcPremFrq = (Spinner) view.findViewById(R.id.spHdfcPremFrq);

        //region hdfc specifc

        minusICICISum = (Button) view.findViewById(R.id.minusICICISum);
        plusICICISum = (Button) view.findViewById(R.id.plusICICISum);
        etICICISumAssured = (EditText) view.findViewById(R.id.etICICISumAssured);

        minusICICIPTerm = (Button) view.findViewById(R.id.minusICICIPTerm);
        plusICICIPTerm = (Button) view.findViewById(R.id.plusICICIPTerm);
        etICICIPolicyTerm = (EditText) view.findViewById(R.id.etICICIPolicyTerm);

        minusICICIPreTerm = (Button) view.findViewById(R.id.minusICICIPreTerm);
        plusICICIPreTerm = (Button) view.findViewById(R.id.plusICICIPreTerm);
        etICICIPremiumTerm = (EditText) view.findViewById(R.id.etICICIPremiumTerm);

        llHDFCSAInc = (LinearLayout) view.findViewById(R.id.llHDFCSAInc);
        minusHDFCSAInc = (Button) view.findViewById(R.id.minusHDFCSAInc);
        plusHDFCSAInc = (Button) view.findViewById(R.id.plusHDFCSAInc);
        etHDFCSAInc = (EditText) view.findViewById(R.id.etHDFCSAInc);

        llIncDeath = (LinearLayout) view.findViewById(R.id.llIncDeath);
        minusIncDeath = (Button) view.findViewById(R.id.minusIncDeath);
        plusIncDeath = (Button) view.findViewById(R.id.plusIncDeath);
        etIncDeath = (EditText) view.findViewById(R.id.etIncDeath);

        llIncPeriod = (LinearLayout) view.findViewById(R.id.llIncPeriod);
        minusIncPeriod = (Button) view.findViewById(R.id.minusIncPeriod);
        plusIncPeriod = (Button) view.findViewById(R.id.plusIncPeriod);
        etIncPeriod = (EditText) view.findViewById(R.id.etIncPeriod);

        llINCREASING = (LinearLayout) view.findViewById(R.id.llINCREASING);
        minusINCREASING = (Button) view.findViewById(R.id.minusINCREASING);
        plusINCREASING = (Button) view.findViewById(R.id.plusINCREASING);
        etINCREASING = (EditText) view.findViewById(R.id.etINCREASING);

        llAdb = (LinearLayout) view.findViewById(R.id.llAdb);
        minusAdb = (Button) view.findViewById(R.id.minusAdb);
        plusAdb = (Button) view.findViewById(R.id.plusAdb);
        etAdb = (EditText) view.findViewById(R.id.etAdb);

        //endregion

    }

    private void setListener() {
        ivEdit.setOnClickListener(this);
        ivBuy.setOnClickListener(this);
        tvMale.setOnClickListener(this);
        tvFemale.setOnClickListener(this);
        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
//        filter.setOnClickListener(this);

        btnGetQuote.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        ivBuy.setOnClickListener(this);
//        filter.setOnClickListener(this);

        btnGetQuote.setOnClickListener(this);
        minusICICISum.setOnClickListener(this);
        plusICICISum.setOnClickListener(this);
        minusICICIPTerm.setOnClickListener(this);
        plusICICIPTerm.setOnClickListener(this);
        minusICICIPreTerm.setOnClickListener(this);
        plusICICIPreTerm.setOnClickListener(this);
        minusHDFCSAInc.setOnClickListener(this);
        plusHDFCSAInc.setOnClickListener(this);
        minusIncDeath.setOnClickListener(this);
        plusIncDeath.setOnClickListener(this);
        minusIncPeriod.setOnClickListener(this);
        plusIncPeriod.setOnClickListener(this);
        minusINCREASING.setOnClickListener(this);
        plusINCREASING.setOnClickListener(this);
        minusAdb.setOnClickListener(this);
        plusAdb.setOnClickListener(this);


        etDOB.setOnClickListener(datePickerDialog);


        etICICISumAssured.setOnFocusChangeListener(this);
        etICICIPolicyTerm.setOnFocusChangeListener(this);
        etICICIPremiumTerm.setOnFocusChangeListener(this);
        etHDFCSAInc.setOnFocusChangeListener(this);
        etIncDeath.setOnFocusChangeListener(this);
        etIncPeriod.setOnFocusChangeListener(this);
        etINCREASING.setOnFocusChangeListener(this);
        etIncDeath.setOnFocusChangeListener(this);
        etAdb.setOnFocusChangeListener(this);


        spHDFCOptions.setOnItemSelectedListener(optionSelected);
        spHdfcPremFrq.setOnItemSelectedListener(optionSelected);
    }

    AdapterView.OnItemSelectedListener optionSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.spHDFCOptions) {
                manipulateInputs(spHDFCOptions.getSelectedItem().toString());

            } else if (parent.getId() == R.id.spHdfcPremFrq) {


                switch (spHdfcPremFrq.getSelectedItemPosition()) {

                    case 0:
                        termRequestEntity.setFrequency("YEARLY");
                        etICICIPremiumTerm.setEnabled(true);
                        etHDFCSAInc.setEnabled(true);
                        etICICIPremiumTerm.setText("" + etICICIPolicyTerm.getText().toString());
                        break;
                    case 1:
                        termRequestEntity.setFrequency("HALF YEARLY");
                        etICICIPremiumTerm.setEnabled(true);
                        etHDFCSAInc.setEnabled(true);
                        etICICIPremiumTerm.setText("" + etICICIPolicyTerm.getText().toString());
                        break;
                    case 2:
                        termRequestEntity.setFrequency("QUARTERLY");
                        etICICIPremiumTerm.setEnabled(true);
                        etHDFCSAInc.setEnabled(true);
                        etICICIPremiumTerm.setText("" + etICICIPolicyTerm.getText().toString());
                        break;
                    case 3:
                        termRequestEntity.setFrequency("MONTHLY");
                        etICICIPremiumTerm.setEnabled(true);
                        etHDFCSAInc.setEnabled(true);
                        etICICIPremiumTerm.setText("" + etICICIPolicyTerm.getText().toString());
                        break;
                    case 4:
                        termRequestEntity.setFrequency("SINGLE");
                        if (!spHDFCOptions.getSelectedItem().toString().equals("LIFE LONG PROTECTION") &&
                                !spHDFCOptions.getSelectedItem().toString().equals("3D LIFE LONG PROTECTION")) {

                            etHDFCSAInc.setText("0");
                            etICICIPremiumTerm.setText("1");
                            etICICIPremiumTerm.setEnabled(false);
                            etHDFCSAInc.setEnabled(false);
                        }
                        break;
                }

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.ivBuy:
                new TermInsuranceController(getActivity()).convertQuoteToApp("" + termFinmartRequest.getTermRequestId(),
                        "39",
                        "" + dbPersistanceController.getUserData().getFBAId(),
                        "" + termCompareQuoteResponse.getMasterData().getResponse().get(0).getNetPremium(), this);
                startActivity(new Intent(getActivity(), CommonWebViewActivity.class)
                        .putExtra("URL", termCompareQuoteResponse.getMasterData().getResponse().get(0).getProposerPageUrl())
                        .putExtra("NAME", "ICICI PRUDENTIAL")
                        .putExtra("TITLE", "ICICI PRUDENTIAL"));
                new TrackingController(getActivity()).sendData(new TrackingRequestEntity(new TrackingData("Life Ins Buy"), Constants.LIFE_INS), null);

                break;
            case R.id.btnGetQuote:

                if (isValidInput()) {
                    setTermRequest();
                    //((IciciTermActivity) getActivity()).redirectToQuote(termFinmartRequest);
                    fetchQuotes();
                }
                break;

            case R.id.ivEdit:
                //((IciciTermActivity) getActivity()).redirectToInput(termFinmartRequest);
                changeInputQuote(true);
                break;

            case R.id.tvMale:
                isMale = true;
                tvFemale.setBackgroundResource(R.drawable.customeborder);
                tvMale.setBackgroundResource(R.drawable.customeborder_blue);
                break;
            case R.id.tvFemale:
                isMale = false;
                tvMale.setBackgroundResource(R.drawable.customeborder);
                tvFemale.setBackgroundResource(R.drawable.customeborder_blue);
                break;
            case R.id.tvYes:
                isSmoker = true;
                tvNo.setBackgroundResource(R.drawable.customeborder);
                tvYes.setBackgroundResource(R.drawable.customeborder_blue);
                break;
            case R.id.tvNo:
                isSmoker = false;
                tvYes.setBackgroundResource(R.drawable.customeborder);
                tvNo.setBackgroundResource(R.drawable.customeborder_blue);
                break;
            case R.id.minusICICISum:
                changeSumAssured(false);
                break;
            case R.id.plusICICISum:
                changeSumAssured(true);
                break;

            case R.id.minusICICIPTerm:
                changePolicyTerm(false);
                break;
            case R.id.plusICICIPTerm:
                changePolicyTerm(true);
                break;
            case R.id.minusICICIPreTerm:
                changePremTerm(false);
                break;
            case R.id.plusICICIPreTerm:
                changePremTerm(true);
                break;

            case R.id.minusHDFCSAInc:
                changeSAIncreasing(false, etHDFCSAInc, 1);
                break;
            case R.id.plusHDFCSAInc:
                changeSAIncreasing(true, etHDFCSAInc, 10);
                break;


            case R.id.minusIncDeath:
                changeIncDeath(false);
                break;
            case R.id.plusIncDeath:
                changeIncDeath(true);
                break;


            case R.id.minusIncPeriod:
                changeSAIncreasing(false, etIncPeriod, 1);
                break;
            case R.id.plusIncPeriod:
                changeSAIncreasing(true, etIncPeriod, 20);
                break;


            case R.id.minusINCREASING:
                changeSAIncreasing(false, etINCREASING, 1);
                break;
            case R.id.plusINCREASING:
                changeSAIncreasing(true, etINCREASING, 100);
                break;


            case R.id.minusAdb:
                changeSAIncreasing(false, etAdb, 1);
                break;
            case R.id.plusAdb:
                changeSAIncreasing(true, etAdb, 100);
                break;

        }
    }

    private void fetchQuotes() {
        showDialog();
        new TermInsuranceController(getActivity()).getTermInsurer(termFinmartRequest, this);
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        if (response instanceof TermCompareQuoteResponse) {
            cancelDialog();
            this.termCompareQuoteResponse = (TermCompareQuoteResponse) response;
            this.cvInputDetails.requestFocus();
            //mAdapter = new TermQuoteAdapter(IciciTermQuoteFragment.this, termCompareQuoteResponse);
            //rvTerm.setAdapter(mAdapter);
            String crn = "" + termCompareQuoteResponse.getMasterData().getResponse().get(0).getCustomerReferenceID();
            termRequestEntity.setCrn(crn);
            termFinmartRequest.setTermRequestEntity(termRequestEntity);
            if (((TermCompareQuoteResponse) response).getMasterData().getLifeTermRequestID() != 0)
                termRequestId = ((TermCompareQuoteResponse) response).getMasterData().getLifeTermRequestID();
            termFinmartRequest.setTermRequestId(termRequestId);
            bindHeaders();
            bindQuotes();
            changeInputQuote(false);
        }

    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    //region datepicker
    protected View.OnClickListener datePickerDialog = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Constants.hideKeyBoard(view, getActivity());

            //region dob

            DateTimePicker.showHealthAgeDatePicker(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                    if (view1.isShown()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String currentDay = simpleDateFormat.format(calendar.getTime());
                        etDOB.setText(currentDay);
                        age = caluclateAge(calendar);
                        String options = spHDFCOptions.getSelectedItem().toString();
                        if (options.equals("LIFE LONG PROTECTION") || options.equals("3D LIFE LONG PROTECTION")) {
                            CalculatePolicyAndPremTerm();
                        }
                        //setPolicyTerm((75 - age));
                    }
                }
            });

            //endregion
        }
    };

//endregion

    public int caluclateAge(Calendar dob) {
        Calendar current = Calendar.getInstance();
        int diff = current.get(YEAR) - dob.get(YEAR);
        if (dob.get(MONTH) > current.get(MONTH) ||
                (dob.get(MONTH) == current.get(MONTH) && dob.get(DATE) > current.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public int caluclateAge(String dateofbirth) {
        Date date = new Date();
        try {

            date = simpleDateFormat.parse(dateofbirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar dob = Calendar.getInstance();
        dob.setTime(date);
        Calendar current = Calendar.getInstance();
        int diff = current.get(YEAR) - dob.get(YEAR);
        if (dob.get(MONTH) > current.get(MONTH) ||
                (dob.get(MONTH) == current.get(MONTH) && dob.get(DATE) > current.get(DATE))) {
            diff--;
        }
        return diff;
    }

    private void changeInputQuote(boolean isInput) {
        if (isInput) {
            //((IciciTermActivity) getActivity()).redirectToInput(termFinmartRequest);
            btnGetQuote.setText("GET QUOTE");
            tilPincode.setVisibility(View.VISIBLE);
            layoutCompare.setVisibility(View.VISIBLE);
            cvInputDetails.setVisibility(View.GONE);
            cvQuoteDetails.setVisibility(View.GONE);
        } else {
            ((HdfcTermActivity) getActivity()).redirectToQuote(termFinmartRequest);
            btnGetQuote.setText("UPDATE QUOTE");
            tilPincode.setVisibility(View.INVISIBLE);
            layoutCompare.setVisibility(View.GONE);
            cvInputDetails.setVisibility(View.VISIBLE);
            cvQuoteDetails.setVisibility(View.VISIBLE);
        }
    }

    private void manipulateInputs(String s) {
        switch (s) {
            case "LIFE":
                termRequestEntity.setPlanTaken("LIFE");
                hideAllLayout();
                clearValues();
                etHDFCSAInc.setText("10");
                SetLumsumPayOutOnDeath();
                if (spHdfcPremFrq.getSelectedItemPosition() != 4) {// not single
                    etICICIPremiumTerm.setText("20");
                    etICICIPolicyTerm.setText("20");
                    etICICIPremiumTerm.setEnabled(true);
                    etHDFCSAInc.setEnabled(true);
                } else {
                    etHDFCSAInc.setText("0");
                    etICICIPremiumTerm.setText("1");
                    etICICIPremiumTerm.setEnabled(false);
                    etHDFCSAInc.setEnabled(false);
                    etICICIPolicyTerm.setText("20");
                }

                break;
            case "3D LIFE":
                termRequestEntity.setPlanTaken("3D LIFE");
                hideAllLayout();
                clearValues();

                etHDFCSAInc.setText("10");
                SetLumsumPayOutOnDeath();
                if (spHdfcPremFrq.getSelectedItemPosition() != 4) {// not single
                    etICICIPremiumTerm.setText("20");
                    etICICIPolicyTerm.setText("20");
                    etICICIPremiumTerm.setEnabled(true);
                    etHDFCSAInc.setEnabled(true);
                } else {
                    etHDFCSAInc.setText("0");
                    etICICIPremiumTerm.setText("1");
                    etICICIPremiumTerm.setEnabled(false);
                    etHDFCSAInc.setEnabled(false);
                    etICICIPolicyTerm.setText("20");
                }

                break;
            case "LIFE LONG PROTECTION":
                termRequestEntity.setPlanTaken("LIFE LONG PROTECTION");
                hideAllLayout();
                clearValues();
                etHDFCSAInc.setText("10");
                SetLumsumPayOutOnDeath();
                CalculatePolicyAndPremTerm();

                break;
            case "3D LIFE LONG PROTECTION":
                termRequestEntity.setPlanTaken("3D LIFE LONG PROTECTION");
                hideAllLayout();
                clearValues();
                etHDFCSAInc.setText("10");
                SetLumsumPayOutOnDeath();
                CalculatePolicyAndPremTerm();

                break;
            case "EXTRA LIFE":
                termRequestEntity.setPlanTaken("EXTRA LIFE");
                hideAllLayout();
                clearValues();
                llAdb.setVisibility(View.VISIBLE);

                etAdb.setText("100");
                etHDFCSAInc.setText("10");

                CalculateLumsumAmt();

                if (spHdfcPremFrq.getSelectedItemPosition() != 4) {// not single
                    etICICIPremiumTerm.setText("20");
                    etICICIPolicyTerm.setText("20");
                    //etICICIPremiumTerm.setEnabled(true);
                    //etHDFCSAInc.setEnabled(true);
                } else {
                    //etHDFCSAInc.setText("0");
                    etICICIPremiumTerm.setText("1");
                    //etICICIPremiumTerm.setEnabled(false);
                    //etHDFCSAInc.setEnabled(false);
                    etICICIPolicyTerm.setText("20");
                }
                break;
            case "EXTRA LIFE INCOME":
                termRequestEntity.setPlanTaken("EXTRA LIFE INCOME");
                hideAllLayout();
                llIncDeath.setVisibility(View.VISIBLE);
                llIncPeriod.setVisibility(View.VISIBLE);
                llINCREASING.setVisibility(View.VISIBLE);
                llAdb.setVisibility(View.VISIBLE);
                clearValues();

                etHDFCSAInc.setText("10");
                etIncDeath.setText("25000");
                etIncPeriod.setText("20");
                etIncPeriod.setEnabled(true);
                etINCREASING.setText("5");
                etINCREASING.setEnabled(true);
                etAdb.setText("100");

                CalculateLumsumAmt();

                if (spHdfcPremFrq.getSelectedItemPosition() != 4) {// not single
                    etICICIPremiumTerm.setText("20");
                    etICICIPolicyTerm.setText("20");
                    //etICICIPremiumTerm.setEnabled(true);
                    //etHDFCSAInc.setEnabled(true);
                } else {
                    //etHDFCSAInc.setText("0");
                    etICICIPremiumTerm.setText("1");
                    //etICICIPremiumTerm.setEnabled(false);
                    //etHDFCSAInc.setEnabled(false);
                    etICICIPolicyTerm.setText("20");
                }

                break;
            case "INCOME OPTION":
                termRequestEntity.setPlanTaken("INCOME OPTION");
                hideAllLayout();
                llIncDeath.setVisibility(View.VISIBLE);
                llIncPeriod.setVisibility(View.VISIBLE);
                llINCREASING.setVisibility(View.VISIBLE);

                clearValues();

                etHDFCSAInc.setText("10");
                etIncDeath.setText("25000");

                etIncPeriod.setText("20");
                etIncPeriod.setEnabled(true);
                etINCREASING.setEnabled(true);
                etINCREASING.setText("5");

                SetLumsumPayOutOnDeath();

                if (spHdfcPremFrq.getSelectedItemPosition() != 4) {// not single
                    etICICIPremiumTerm.setText("20");
                    etICICIPolicyTerm.setText("20");
                    //etICICIPremiumTerm.setEnabled(true);
                    //etHDFCSAInc.setEnabled(true);
                } else {
                    //etHDFCSAInc.setText("0");
                    etICICIPremiumTerm.setText("1");
                    //etICICIPremiumTerm.setEnabled(false);
                    //etHDFCSAInc.setEnabled(false);
                    etICICIPolicyTerm.setText("20");
                }

                break;
            case "INCOME REPLACEMENT":
                termRequestEntity.setPlanTaken("INCOME REPLACEMENT");
                hideAllLayout();
                llIncDeath.setVisibility(View.VISIBLE);
                llINCREASING.setVisibility(View.VISIBLE);
                clearValues();

                etHDFCSAInc.setText("10");
                etIncDeath.setText("25000");
                etINCREASING.setText("10");

                if (!etIncDeath.getText().toString().equals("")) {
                    long txtMntlyIncomOnDeath = Long.parseLong(etIncDeath.getText().toString());
                    long Lumsum = txtMntlyIncomOnDeath * 12;
                    hfLumsumPayOutOnDeath = Lumsum;
                }

                if (spHdfcPremFrq.getSelectedItemPosition() != 4) {// not single
                    etICICIPremiumTerm.setText("20");
                    etICICIPolicyTerm.setText("20");
                    //etICICIPremiumTerm.setEnabled(true);
                    //etHDFCSAInc.setEnabled(true);
                } else {
                    //etHDFCSAInc.setText("0");
                    etICICIPremiumTerm.setText("1");
                    //etICICIPremiumTerm.setEnabled(false);
                    //etHDFCSAInc.setEnabled(false);
                    etICICIPolicyTerm.setText("20");
                }

                break;
            case "RETURN OF PREMIUM":
                termRequestEntity.setPlanTaken("RETURN OF PREMIUM");
                hideAllLayout();
                clearValues();

                etHDFCSAInc.setText("10");

                SetLumsumPayOutOnDeath();

                if (spHdfcPremFrq.getSelectedItemPosition() != 4) {// not single
                    etICICIPremiumTerm.setText("20");
                    etICICIPolicyTerm.setText("20");
                    //etICICIPremiumTerm.setEnabled(true);
                    //etHDFCSAInc.setEnabled(true);
                } else {
                    //etHDFCSAInc.setText("0");
                    etICICIPremiumTerm.setText("1");
                    //etICICIPremiumTerm.setEnabled(false);
                    //etHDFCSAInc.setEnabled(false);
                    etICICIPolicyTerm.setText("20");
                }

                break;
        }
    }

    private void hideAllLayout() {
        //llHDFCSAInc.setVisibility(View.GONE);
        llIncDeath.setVisibility(View.GONE);
        llIncPeriod.setVisibility(View.GONE);
        llINCREASING.setVisibility(View.GONE);
        llAdb.setVisibility(View.GONE);
    }

    public void clearValues() {
        etIncDeath.setText("");
        etINCREASING.setText("");
        etIncPeriod.setText("");
        etAdb.setText("");
        etHDFCSAInc.setText("");
    }

    public void CalculatePolicyAndPremTerm() {
        int age = caluclateAge(etDOB.getText().toString());
        int policyTerm = 99 - age;
        int ppt = 65 - age;
        etICICIPolicyTerm.setText("" + policyTerm);
        etICICIPremiumTerm.setText("" + ppt);
    }

    public void CalculateLumsumAmt() {
        int txtSumAssu = 0;
        double AdbPercentage = 0;

        long txtMntlyIncomOnDeath = 0;

        if (!etIncDeath.getText().toString().equals("")) {
            txtMntlyIncomOnDeath = Long.parseLong(etIncDeath.getText().toString().replaceAll("\\,", ""));
        }
        if (!etICICISumAssured.getText().toString().equals("")) {
            txtSumAssu = Integer.parseInt(etICICISumAssured.getText().toString().replaceAll("\\,", ""));
            hfLumsumPayOutOnDeath = txtSumAssu;
        }


        if (!etAdb.getText().toString().equals("")) {
            AdbPercentage = Double.parseDouble(etAdb.getText().toString());
            if (AdbPercentage > 100) {
                AdbPercentage = 100;
                etAdb.setText("100");
            }
        }

        if (!etICICISumAssured.getText().toString().equals("") && !etAdb.getText().toString().equals("")) {
            double Adb = AdbPercentage / 100;
            double LumsumAmt = txtSumAssu * Adb;
            hfLumsumAmt = Math.round(LumsumAmt);
        }

        /*if (!etIncDeath.getText().toString().equals("") && !etAdb.getText().toString().equals("")) {
            //double Adb = AdbPercentage / 100;
            //double MlyIncome = txtMntlyIncomOnDeath * Adb;

            //$('#hfLumsumAmt').val(Math.round(LumsumAmt));
        }*/
    }

    public void SetLumsumPayOutOnDeath() {
        int SumInsu = 0;
        if (!etICICISumAssured.getText().toString().equals("")) {
            SumInsu = Integer.parseInt(etICICISumAssured.getText().toString().replaceAll("\\,", ""));
            hfLumsumPayOutOnDeath = SumInsu;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.etICICISumAssured:
                if (!b) {
                    CalculateLumsumAmt();
                    ChkSumAssu();
                }
                break;
            case R.id.etICICIPolicyTerm:
                if (!b) {
                    ChkPolicyTerm();
                }
                break;
            case R.id.etICICIPremiumTerm:
                if (!b) {
                    ChkPremTerm();
                }
                break;
            case R.id.etHDFCSAInc:
                if (!b) {
                    CheckMinMax(etHDFCSAInc, 10);
                }
                break;
            case R.id.etIncDeath:
                if (!b) {
                    CheckMntlyIncomOnDeath();
                }
                break;
            case R.id.etIncPeriod:
                if (!b) {
                    CheckMinMax(etIncPeriod, 20);
                }
                break;
            case R.id.etINCREASING:
                if (!b) {
                    CheckMinMax(etINCREASING, 100);
                }
                break;
            case R.id.etAdb:
                if (!b) {
                    CalculateLumsumAmt();
                }
                break;
        }
    }

    private void ChkSumAssu() {
        long txtSumAssu = 0;
        if (!etICICISumAssured.getText().toString().equals(""))
            txtSumAssu = Long.parseLong(etICICISumAssured.getText().toString().replaceAll("\\,", ""));
        if (txtSumAssu != 0) {
            if ((txtSumAssu) < 2500000) {
                txtSumAssu = 2500000;
            }

            if (txtSumAssu > 500000000) {
                etHDFCSAInc.setText("0");
                etHDFCSAInc.setEnabled(false);

            } else {
                //etHDFCSAInc.setText("10");
                etHDFCSAInc.setEnabled(true);
            }

            etICICISumAssured.setText("" + NumberFormat.getNumberInstance(Locale.US).format(txtSumAssu));
        }

    }

    private void changeSumAssured(boolean b) {
        long SumInsu = 0;

        if (!etICICISumAssured.getText().toString().equals(""))
            SumInsu = Long.parseLong(etICICISumAssured.getText().toString().replaceAll("\\,", ""));

        if (b) {


            if (SumInsu != 0) {
                if (SumInsu < 100000) {
                    SumInsu = SumInsu + 10000;
                } else if ((SumInsu) >= 100000 && (SumInsu) < 1000000) {
                    SumInsu = (SumInsu) + 100000;
                } else if ((SumInsu) >= 1000000 && (SumInsu) < 10000000) {
                    SumInsu = (SumInsu) + 500000;
                } else if ((SumInsu) >= 10000000) {
                    SumInsu = (SumInsu) + 500000;
                }

                if (SumInsu > 500000000) {
                    etHDFCSAInc.setText("0");
                    etHDFCSAInc.setEnabled(false);

                } else {
                    //etHDFCSAInc.setText("10");
                    etHDFCSAInc.setEnabled(true);
                }
            } else {
                SumInsu = 50000;
            }

            if (!spHDFCOptions.getSelectedItem().toString().equals("INCOME REPLACEMENT")) {
                hfLumsumPayOutOnDeath = SumInsu;
            }


        } else {

            if (SumInsu != 0) {
                if (SumInsu <= 2500000) {
                    SumInsu = 2500000;
                } else if (SumInsu > 2500000 && SumInsu <= 10000000) {
                    SumInsu = SumInsu - 500000;
                } else if (SumInsu > 10000000) {
                    SumInsu = SumInsu - 500000;
                }

                if (SumInsu > 500000000) {
                    etHDFCSAInc.setText("0");
                    etHDFCSAInc.setEnabled(false);

                } else {
                    //etHDFCSAInc.setText("10");
                    etHDFCSAInc.setEnabled(true);
                }
            } else {
                SumInsu = 2500000;
            }

            if (!spHDFCOptions.getSelectedItem().toString().equals("INCOME REPLACEMENT")) {
                hfLumsumPayOutOnDeath = SumInsu;
            }


        }

        //NumberFormat.getNumberInstance(Locale.US).format(sumAssured);
        etICICISumAssured.setText("" + NumberFormat.getNumberInstance(Locale.US).format(SumInsu));
    }

    private void changePolicyTerm(boolean b) {

        int min = 5;
        int term = 0;
        if (!etICICIPolicyTerm.getText().toString().equals(""))
            term = Integer.parseInt(etICICIPolicyTerm.getText().toString().replaceAll("\\,", ""));

        //var dllPremFreq = $('#dllPremFreq').val();

        if (b) {

            if (term != 0) {
                term = (term) + 1;
                if (spHdfcPremFrq.getSelectedItemPosition() == 4) {
                    term = 1;
                }
            } else {
                term = 10;
            }
        } else {
            if (term != 0) {
                term = (term) - 1;
                if ((term) < min) {
                    term = min;
                }
                if (spHdfcPremFrq.getSelectedItemPosition() == 4) {
                    term = 1;
                }

            } else {
                term = 10;
            }
        }

        if (spHdfcPremFrq.getSelectedItemPosition() != 4) {

            if (!etICICIPolicyTerm.getText().toString().equals("") && !etICICIPremiumTerm.getText().toString().equals("")) {
                int txtPolicyTerm = Integer.parseInt(etICICIPolicyTerm.getText().toString().replaceAll("\\,", ""));
                int txtPremiumTerm = Integer.parseInt(etICICIPremiumTerm.getText().toString().replaceAll("\\,", ""));

                if ((txtPremiumTerm) > (txtPolicyTerm)) {
                    etICICIPremiumTerm.setText("" + txtPolicyTerm);
                }
            }
        }
        etICICIPolicyTerm.setText("" + NumberFormat.getNumberInstance(Locale.US).format(term));
    }

    private void ChkPolicyTerm() {
        int min = 5;
        int term = 0;
        if (!etICICIPolicyTerm.getText().toString().equals(""))
            term = Integer.parseInt(etICICIPolicyTerm.getText().toString().replaceAll("\\,", ""));

        if (term != 0) {

            if ((term) < min) {
                term = min;
            }
        } else {
            term = 10;
        }
        etICICIPolicyTerm.setText("" + NumberFormat.getNumberInstance(Locale.US).format(term));

        if (!etICICIPolicyTerm.getText().toString().equals("") && !etICICIPremiumTerm.getText().toString().equals("")) {
            int txtPolicyTerm = Integer.parseInt(etICICIPolicyTerm.getText().toString().replaceAll("\\,", ""));
            int txtPremiumTerm = Integer.parseInt(etICICIPremiumTerm.getText().toString().replaceAll("\\,", ""));

            if ((txtPremiumTerm) > (txtPolicyTerm)) {
                etICICIPremiumTerm.setText("" + txtPolicyTerm);
            }
        }
    }

    private void changePremTerm(boolean b) {

        int min = 5;
        int term = 0;
        if (!etICICIPremiumTerm.getText().toString().equals(""))
            term = Integer.parseInt(etICICIPremiumTerm.getText().toString().replaceAll("\\,", ""));

        //var dllPremFreq = $('#dllPremFreq').val();

        if (b) {

            if (term != 0) {
                term = (term) + 1;
                if (spHdfcPremFrq.getSelectedItemPosition() == 4) {
                    term = 1;
                }
            } else {
                term = 10;
            }
        } else {
            if (term != 0) {
                term = (term) - 1;
                if ((term) < min) {
                    term = min;
                }
                if (spHdfcPremFrq.getSelectedItemPosition() == 4) {
                    term = 1;
                }

            } else {
                term = 10;
            }
        }
        etICICIPremiumTerm.setText("" + NumberFormat.getNumberInstance(Locale.US).format(term));
        if (spHdfcPremFrq.getSelectedItemPosition() != 4) {

            if (!etICICIPolicyTerm.getText().toString().equals("") && !etICICIPremiumTerm.getText().toString().equals("")) {
                int txtPolicyTerm = Integer.parseInt(etICICIPolicyTerm.getText().toString().replaceAll("\\,", ""));
                int txtPremiumTerm = Integer.parseInt(etICICIPremiumTerm.getText().toString().replaceAll("\\,", ""));

                if ((txtPremiumTerm) > (txtPolicyTerm)) {
                    etICICIPremiumTerm.setText("" + txtPolicyTerm);
                }
            }
        }

    }

    private void ChkPremTerm() {
        int min = 5;
        int term = 0;
        if (!etICICIPremiumTerm.getText().toString().equals(""))
            term = Integer.parseInt(etICICIPremiumTerm.getText().toString().replaceAll("\\,", ""));

        if (term != 0) {

            if ((term) < min) {
                term = min;
            }
        } else {
            term = 10;
        }
        etICICIPremiumTerm.setText("" + NumberFormat.getNumberInstance(Locale.US).format(term));

        if (!etICICIPolicyTerm.getText().toString().equals("") && !etICICIPremiumTerm.getText().toString().equals("")) {
            int txtPolicyTerm = Integer.parseInt(etICICIPolicyTerm.getText().toString().replaceAll("\\,", ""));
            int txtPremiumTerm = Integer.parseInt(etICICIPremiumTerm.getText().toString().replaceAll("\\,", ""));

            if ((txtPremiumTerm) > (txtPolicyTerm)) {
                etICICIPremiumTerm.setText("" + txtPolicyTerm);
            }
        }
    }

    private void changeSAIncreasing(boolean b, EditText editText, int min) {

        long term = 0, SumInsu = 0;
        String dllOption = spHDFCOptions.getSelectedItem().toString();
        if (!etICICISumAssured.getText().toString().equals(""))
            SumInsu = Long.parseLong(etICICISumAssured.getText().toString().replaceAll("\\,", ""));
        if (!editText.getText().toString().equals(""))
            term = Long.parseLong(editText.getText().toString().replaceAll("\\,", ""));

        if (b) {


            if (dllOption == "INCOME REPLACEMENT" && editText.getId() == R.id.etINCREASING) {

            } else {
                if ((SumInsu) > 500000000 && editText.getId() == R.id.etHDFCSAInc) {

                } else if (spHdfcPremFrq.getSelectedItemPosition() == 4 && editText.getId() == R.id.etHDFCSAInc) {

                } else {
                    if (term != 0) {
                        if ((term) >= min) {
                            term = min;
                        } else {
                            term = (term) + 1;
                        }
                    } else {
                        term = 10;
                    }

                    if (editText.getId() == R.id.etAdb) {
                        CalculateLumsumAmt();
                    }

                    editText.setText("" + NumberFormat.getNumberInstance(Locale.US).format(term));
                }
            }


        } else {

            if (dllOption == "INCOME REPLACEMENT" && editText.getId() == R.id.etINCREASING) {

            } else {
                if ((SumInsu) > 500000000 && editText.getId() == R.id.etHDFCSAInc) {

                } else if (spHdfcPremFrq.getSelectedItemPosition() == 4 && editText.getId() == R.id.etHDFCSAInc) {

                } else {
                    if (term != 0) {

                        term = (term) - 1;

                        if ((term) <= 0) {
                            term = min;
                        }
                    } else {
                        term = 10;
                    }

                    if (editText.getId() == R.id.etAdb) {
                        CalculateLumsumAmt();
                    }

                    editText.setText("" + NumberFormat.getNumberInstance(Locale.US).format(term));
                }
            }
        }
    }

    private void CheckMinMax(EditText editText, int max) {
        long value = 0;
        if (!editText.getText().toString().equals(""))
            value = Long.parseLong(editText.getText().toString().replaceAll("\\,", ""));


        if (value != 0) {
            if ((value) > max) {
                value = max;
            }

            if ((value) <= 0) {
                value = 1;
            }
        } else {
            value = 0;
        }

        editText.setText("" + NumberFormat.getNumberInstance(Locale.US).format(value));
    }

    private void changeIncDeath(boolean b) {
        long Income = 0;
        String dllOption = spHDFCOptions.getSelectedItem().toString();
        if (!etIncDeath.getText().toString().equals(""))
            Income = Long.parseLong(etIncDeath.getText().toString().replaceAll("\\,", ""));


        if (b) {
            if (Income != 0) {
                if ((Income) < 1000) {
                    Income = (Income) + 100;
                } else if ((Income) >= 1000 && (Income) < 100000) {
                    Income = (Income) + 1000;
                } else if ((Income) >= 100000 && (Income) < 2500000) {
                    Income = (Income) + 100000;
                } else if ((Income) >= 2500000 && (Income) < 10000000) {
                    Income = (Income) + 500000;
                } else if ((Income) >= 10000000) {
                    Income = (Income) + 500000;
                }
            } else {
                Income = 25000;
            }


        } else {

            if (Income != 0) {
                if ((Income) <= 100) {
                    Income = 0;
                } else if ((Income) <= 1000) {
                    Income = (Income) - 100;
                } else if ((Income) <= 100000) {
                    Income = (Income) - 1000;
                }
                //else if (parseInt(Income) >= 100000) {
                //    Income = parseInt(Income) - 10000;
                //}
                else if ((Income) > 100000 && (Income) <= 1000000) {
                    Income = (Income) - 100000;
                } else if ((Income) > 1000000 && (Income) <= 10000000) {
                    Income = (Income) - 500000;
                } else if ((Income) > 10000000) {
                    Income = (Income) - 500000;
                }
            } else {
                Income = 25000;
            }
        }

        if (dllOption == "INCOME REPLACEMENT") {
            if (Income != 0) {
                long Lumsum = (Income) * 12;
                hfLumsumPayOutOnDeath = Lumsum;
                //$('#hfLumsumPayOutOnDeath').val(Lumsum);
            }
        }
        etIncDeath.setText("" + NumberFormat.getNumberInstance(Locale.US).format(Income));


    }

    private void CheckMntlyIncomOnDeath() {
        long Income = 0;
        String dllOption = spHDFCOptions.getSelectedItem().toString();
        if (!etIncDeath.getText().toString().equals(""))
            Income = Long.parseLong(etIncDeath.getText().toString().replaceAll("\\,", ""));
        if (dllOption == "INCOME REPLACEMENT") {
            if (Income != 0) {
                long Lumsum = (Income) * 12;
                hfLumsumPayOutOnDeath = Lumsum;
            }
        }
    }
}

