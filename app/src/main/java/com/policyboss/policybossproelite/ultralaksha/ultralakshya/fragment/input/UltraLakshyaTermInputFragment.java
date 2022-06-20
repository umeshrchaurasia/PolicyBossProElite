package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.input;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseFragment;
import com.policyboss.policybossproelite.MyApplication;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.term.hdfc.HdfcIProtectAdapter;
import com.policyboss.policybossproelite.ultralaksha.ultralakshya.UltraLakshyaTermBottmActivity;
import com.policyboss.policybossproelite.utility.Constants;
import com.policyboss.policybossproelite.utility.DateTimePicker;
import com.policyboss.policybossproelite.webviews.CommonWebViewActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.term.TermInsuranceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.HDFCEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.IllustrationRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LICEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TermCompareResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.LICIllustrationRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.UltralakshaRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshaIllustrationResponseNew;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UltraLakshaRecalculateResponse;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static java.util.Calendar.YEAR;

public class UltraLakshyaTermInputFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener, BaseFragment.PopUpListener, IResponseSubcriber {


    private PopupWindow mPopupWindow, mPopupWindowSelection;
    View customView, customViewSelection;
    RecyclerView rvIprotectSmart;
    HdfcIProtectAdapter adapter;

    // quote
    //  TextView tvSum, tvGender, tvSmoker, tvAge, tvPolicyTerm, tvCrn, filter;
    ImageView ivEdit;
    TermCompareResponseEntity termCompareResponseEntity;
    CardView cvQuoteDetails;


    TextView txtSumAssured, txt10lac, txt25lac, txt50lac, txt1cr, txtOther, txtDispalaylac, txtAnnualPremiumDisplay,
            txtPlanNAme, txtCover, txtFinalPremium, txtPolicyTerm, txtAge, txtCustomise, txtRiders;
    ImageView imgInsurerLogo, ivBuy, ivPdf;
    LinearLayout llAddon;
    RecyclerView rvAddOn;

    Button btnGetQuote, btnGetrecalculate;
    EditText etFirstName, etLasttName, etMobile, et_DOB;
    TextView tvMale, tvFemale, tvYes, tvNo;
    boolean isMale, isSmoker;
    LinearLayout llGender, llSmoker, lyAnnualPremium, lySeekbar;
    SeekBar sbSumAssured;

    List<String> policyYear;
    DBPersistanceController dbPersistanceController;
    Spinner spPolicyTerm;

    UltralakshaRequestEntity Requestentity;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    LinearLayout llCompareAll;
    String SUM_ASSURED_AMNT = "1000000";


    //region icici form
    Spinner spICICIPremiumFrequency;
    ArrayAdapter<String> ICICIPremiumTermAdapter, ICICIPremiumFrequencyAdapter;
    List<String> premiumTermList, frequenctList;

    EditText etSumICICIAssured;
    TextView etCal_lic, etCal_ultra;

    Button minusICICISum, plusICICISum;


    TextInputLayout tilPremiumPayment;
    //endregion

    boolean isEdit = false, canChangePremiumTerm = true, canChangePolicyTerm = true;
    int termRequestId = 0, insurerID, age = 0;
    String crn = "";
    ArrayAdapter<String> spinnerArraylakshya_policyterm;
    ScrollView mainScroll;
    String[] listOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ultra_lakshya_term_input, container, false);
//        ((UltraLakshyaTermBottmActivity) getActivity()).getSupportActionBar().show();
//        ((UltraLakshyaTermBottmActivity) getActivity()).manageHeader(true);
        init(view);
        isSmoker = false;
        isMale = true;
        setSeekbarSumAssured();
        setListener();
        setPopUpInfo();
        // set initial values
        dbPersistanceController = new DBPersistanceController(getActivity());
        policyYear = dbPersistanceController.getPremYearList();

        Requestentity = new UltralakshaRequestEntity();
        init_adapters();

        adapter_listener();
        setDefaultsultra();

        if (getArguments().getParcelable(UltraLakshyaTermBottmActivity.INPUT_DATA) != null) {
            Requestentity = getArguments().getParcelable(UltraLakshyaTermBottmActivity.INPUT_DATA);
            bindInput(Requestentity);
        }


        return view;
    }

    private void fetchWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                btnGetQuote.performClick();
            }
        }, 2000);
    }

    private void setDefaultsultra() {
        etSumICICIAssured.setText("1000000");
        txtDispalaylac.setVisibility(View.GONE);
        etSumICICIAssured.setVisibility(View.GONE);

        manipulatePremiumTerm(30);//Default Age 30
        Calendar calendar = Calendar.getInstance();


        calendar.add(Calendar.YEAR, -30);
        String currentDay = simpleDateFormat.format(calendar.getTime());
        et_DOB.setTag(R.id.et_DOB, dateToCalendar(stringToDate(simpleDateFormat, currentDay)));
        et_DOB.setText(currentDay);

        String[] listOption = getActivity().getResources().getStringArray(R.array.lakshya_policyterm);


        final List<String> optionsList = new ArrayList<>(Arrays.asList(listOption));
        ArrayAdapter<String> spAdapterOptions = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_spinner_item
                , optionsList);
        //        spAdapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPolicyTerm.setAdapter(spAdapterOptions);


        spPolicyTerm.setSelection(7);
    }


    private void bindInput(UltralakshaRequestEntity termFinmartRequest) {
        try {

            if (termFinmartRequest != null) {

                isEdit = true;

                age = caluclateAge("" + termFinmartRequest.getInsuredDOB());
                //   bindPremiumPayment(getPremiumPaymentId(termRequestEntity.getPPT(), age));

                String[] listOption = getActivity().getResources().getStringArray(R.array.icici_options);
                final List<String> optionsList = new ArrayList<>(Arrays.asList(listOption));
                ArrayAdapter<String> spAdapterOptions = new ArrayAdapter<String>(getActivity()
                        , android.R.layout.simple_spinner_item
                        , optionsList);


                if (termFinmartRequest.getFrequency().equals("Yearly")) {
                    spICICIPremiumFrequency.setSelection(0);
                } else if (termFinmartRequest.getFrequency().equals("Half Yearly")) {
                    spICICIPremiumFrequency.setSelection(1);
                } else if (termFinmartRequest.getFrequency().equals("Monthly")) {
                    spICICIPremiumFrequency.setSelection(3);
                } else if (termFinmartRequest.getFrequency().equals("Quarterly")) {
                    spICICIPremiumFrequency.setSelection(2);
                }

                String[] splitStr = termFinmartRequest.getContactName().split("\\s+");
                String firstName = "", lastName = "";
                for (int i = 0; i < splitStr.length; i++) {
                    if (i == ((splitStr.length) - 1)) {
                        lastName = lastName + splitStr[i];
                    } else {
                        firstName = firstName + " " + splitStr[i];
                    }
                }
                etFirstName.setText("" + firstName);
                etLasttName.setText("" + lastName);

                etMobile.setText("" + termFinmartRequest.getContactMobile());

                et_DOB.setTag(R.id.et_DOB, dateToCalendar(stringToDate(simpleDateFormat, termFinmartRequest.getInsuredDOB())));
                et_DOB.setText("" + termFinmartRequest.getInsuredDOB());


                if (termFinmartRequest.getIs_TabaccoUser().equals("true")) {
                    isSmoker = true;
                    tvYes.setBackgroundResource(R.drawable.customeborder_blue);
                    tvYes.setTypeface(null, Typeface.BOLD);
                    tvNo.setTypeface(null, Typeface.NORMAL);
                    tvNo.setBackgroundResource(R.drawable.customeborder);
                } else {
                    isSmoker = false;
                    tvNo.setBackgroundResource(R.drawable.customeborder_blue);
                    tvNo.setTypeface(null, Typeface.BOLD);
                    tvYes.setTypeface(null, Typeface.NORMAL);
                    tvYes.setBackgroundResource(R.drawable.customeborder);
                }

                if (termFinmartRequest.getInsuredGender().equals("M")) {
                    isMale = true;
                    tvMale.setBackgroundResource(R.drawable.customeborder_blue);
                    tvMale.setTypeface(null, Typeface.BOLD);
                    tvFemale.setTypeface(null, Typeface.NORMAL);
                    tvFemale.setBackgroundResource(R.drawable.customeborder);
                } else {
                    isMale = false;
                    tvFemale.setBackgroundResource(R.drawable.customeborder_blue);
                    tvFemale.setTypeface(null, Typeface.BOLD);
                    tvMale.setTypeface(null, Typeface.NORMAL);
                    tvMale.setBackgroundResource(R.drawable.customeborder);
                }

                if (termFinmartRequest.getSumAssured() == 1000000) {
                    SUM_ASSURED_AMNT = "1000000";

                    setBackgroundSeekBar(txt10lac, txt25lac, txt50lac, txt1cr, txtOther);
                    lySeekbar.setVisibility(View.GONE);
                } else if (termFinmartRequest.getSumAssured() == 2500000) {
                    SUM_ASSURED_AMNT = "2500000";

                    setBackgroundSeekBar(txt25lac, txt10lac, txt50lac, txt1cr, txtOther);
                    lySeekbar.setVisibility(View.GONE);

                } else if (termFinmartRequest.getSumAssured() == 5000000) {
                    SUM_ASSURED_AMNT = "5000000";

                    setBackgroundSeekBar(txt50lac, txt10lac, txt25lac, txt1cr, txtOther);
                    lySeekbar.setVisibility(View.GONE);

                } else if (termFinmartRequest.getSumAssured() == 10000000) {
                    SUM_ASSURED_AMNT = "10000000";
                    setBackgroundSeekBar(txt1cr, txt10lac, txt25lac, txt50lac, txtOther);
                    lySeekbar.setVisibility(View.GONE);

                } else {
                    setBackgroundSeekBar(txtOther, txt10lac, txt25lac, txt50lac, txt1cr);
                    lySeekbar.setVisibility(View.VISIBLE);
                    etSumICICIAssured.setVisibility(View.VISIBLE);
                    txtDispalaylac.setVisibility(View.VISIBLE);
                }

                etSumICICIAssured.setText("" + termFinmartRequest.getSumAssured());


                //               String[] listOptionedit = getActivity().getResources().getStringArray(R.array.lakshya_policyterm);


//                final List<String> optionsListedit = new ArrayList<>(Arrays.asList(listOptionedit));
//                ArrayAdapter<String> spAdapterOptionsedit = new ArrayAdapter<String>(getActivity()
//                        , android.R.layout.simple_spinner_item
//                        , optionsListedit);
//                //        spAdapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spPolicyTerm.setAdapter(spAdapterOptionsedit);

                manipulatePremiumTerm(age);
                int pos = termFinmartRequest.getPolicyTerm() - 13;
                spPolicyTerm.setSelection(pos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setListener() {


        tvMale.setOnClickListener(this);
        tvFemale.setOnClickListener(this);
        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
//        filter.setOnClickListener(this);

        btnGetQuote.setOnClickListener(this);
        btnGetrecalculate.setOnClickListener(this);
        et_DOB.setOnClickListener(datePickerDialog);

//        minusICICISum.setOnClickListener(this);
        //      plusICICISum.setOnClickListener(this);

        //       etSumICICIAssured.setOnFocusChangeListener(this);
        //   sbSumAssured.setOnSeekBarChangeListener(this);


        txt10lac.setOnClickListener(this);
        txt25lac.setOnClickListener(this);
        txt50lac.setOnClickListener(this);
        txt1cr.setOnClickListener(this);
        txtOther.setOnClickListener(this);

        txt10lac.performClick();

    }

    private void init_adapters() {


        String[] listPremiumTerm = getActivity().getResources().getStringArray(R.array.icici_payment_term);
        premiumTermList = new ArrayList<>(Arrays.asList(listPremiumTerm));


        String[] listPremiumFreq = getActivity().getResources().getStringArray(R.array.lakshya_premium_frequency);
        frequenctList = new ArrayList<>(Arrays.asList(listPremiumFreq));

        ICICIPremiumFrequencyAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, frequenctList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setPadding(0, view.getPaddingTop(), 0, view.getPaddingBottom());
                return view;
            }
        };
        spICICIPremiumFrequency.setAdapter(ICICIPremiumFrequencyAdapter);


    }

    private void adapter_listener() {


        //region Premium Frequency
        spICICIPremiumFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                manipulateFrequency();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //endregion


    }

    private void manipulateFrequency() {
        switch (spICICIPremiumFrequency.getSelectedItemPosition()) {
            case 0:
                Requestentity.setFrequency("Yearly");
                break;
            case 1:
                Requestentity.setFrequency("Half Yearly");
                break;
            case 2:
                Requestentity.setFrequency("Quarterly");
                break;
            case 3:
                Requestentity.setFrequency("Monthly");
                break;
        }
    }


    private void init(View view) {


        //quote page
        //   cvInputDetails = (CardView) view.findViewById(R.id.cvInputDetails);
        txtAnnualPremiumDisplay = (TextView) view.findViewById(R.id.txtAnnualPremiumDisplay);
        txtSumAssured = (TextView) view.findViewById(R.id.txtSumAssured);
        txtDispalaylac = (TextView) view.findViewById(R.id.txtDispalaylac);
        txt10lac = (TextView) view.findViewById(R.id.txt10lac);
        txt25lac = (TextView) view.findViewById(R.id.txt25lac);
        txt50lac = (TextView) view.findViewById(R.id.txt50lac);
        txt1cr = (TextView) view.findViewById(R.id.txt1cr);
        txtOther = (TextView) view.findViewById(R.id.txtOther);


        //sbSumAssured = (SeekBar) view.findViewById(R.id.sbSumAssured);
        cvQuoteDetails = (CardView) view.findViewById(R.id.cvQuoteDetails);
        mainScroll = (ScrollView) view.findViewById(R.id.mainScroll);


        btnGetQuote = (Button) view.findViewById(R.id.btnGetQuote);
        btnGetrecalculate = (Button) view.findViewById(R.id.btnGetrecalculate);
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLasttName = (EditText) view.findViewById(R.id.etLasttName);

        etMobile = (EditText) view.findViewById(R.id.etMobile);
        et_DOB = (EditText) view.findViewById(R.id.et_DOB);
        tvMale = (TextView) view.findViewById(R.id.tvMale);
        tvFemale = (TextView) view.findViewById(R.id.tvFemale);
        tvYes = (TextView) view.findViewById(R.id.tvYes);
        tvNo = (TextView) view.findViewById(R.id.tvNo);
        llGender = (LinearLayout) view.findViewById(R.id.llGender);
        llSmoker = (LinearLayout) view.findViewById(R.id.llSmoker);
        lyAnnualPremium = (LinearLayout) view.findViewById(R.id.lyAnnualPremium);
        lySeekbar = (LinearLayout) view.findViewById(R.id.lySeekbar);

        etCal_lic = (TextView) view.findViewById(R.id.etCal_lic);
        etCal_ultra = (TextView) view.findViewById(R.id.etCal_ultra);

        etSumICICIAssured = (EditText) view.findViewById(R.id.etSumICICIAssured);

        spPolicyTerm = (Spinner) view.findViewById(R.id.spPolicyTerm);
        spICICIPremiumFrequency = (Spinner) view.findViewById(R.id.spICICIPremiumFrequency);


        mainScroll = (ScrollView) view.findViewById(R.id.mainScroll);

        //   minusICICISum = (Button) view.findViewById(R.id.minusICICISum);
        //  plusICICISum = (Button) view.findViewById(R.id.plusICICISum);


        //end region
    }

    private void setSeekbarSumAssured() {
//        sbSumAssured.setMax(190);
//        sbSumAssured.setProgress(1);
//        txtSumAssured.setText("10 Lac");

    }

    @Override
    public void onClick(View view) {
        Constants.hideKeyBoard(view, getActivity());
        btnGetQuote.requestFocus();
        switch (view.getId()) {

            case R.id.tvMale:
                isMale = true;
                tvFemale.setBackgroundResource(R.drawable.customeborder);
                tvMale.setBackgroundResource(R.drawable.customeborder_blue);
                tvMale.setTypeface(null, Typeface.BOLD);
                tvFemale.setTypeface(null, Typeface.NORMAL);
                break;
            case R.id.tvFemale:
                isMale = false;
                tvMale.setBackgroundResource(R.drawable.customeborder);
                tvFemale.setBackgroundResource(R.drawable.customeborder_blue);
                tvFemale.setTypeface(null, Typeface.BOLD);
                tvMale.setTypeface(null, Typeface.NORMAL);
                break;
            case R.id.tvYes:
                isSmoker = true;
                tvNo.setBackgroundResource(R.drawable.customeborder);
                tvYes.setBackgroundResource(R.drawable.customeborder_blue);
                tvYes.setTypeface(null, Typeface.BOLD);
                tvNo.setTypeface(null, Typeface.NORMAL);
                break;
            case R.id.tvNo:
                isSmoker = false;
                tvYes.setBackgroundResource(R.drawable.customeborder);
                tvNo.setBackgroundResource(R.drawable.customeborder_blue);
                tvNo.setTypeface(null, Typeface.BOLD);
                tvYes.setTypeface(null, Typeface.NORMAL);
                break;

            case R.id.ivBuy:

                break;
            case R.id.ivPdf:
                MyApplication.getInstance().trackEvent(Constants.LIFE_INS, "ICICI PDF  TERM INSURANCE", "ICICI PDF TERM INSURANCE");
                if (termCompareResponseEntity != null && termCompareResponseEntity.getPdfUrl().equals("")) {
                    Toast.makeText(getActivity(), "Pdf Not Available", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getActivity(), CommonWebViewActivity.class)
                            .putExtra("URL", termCompareResponseEntity.getPdfUrl())
                            .putExtra("NAME", "ICICI PRUDENTIAL DOWNLOAD")
                            .putExtra("TITLE", "ICICI PRUDENTIAL"));

                }

            case R.id.btnGetQuote:

//                if (isValidInput()) {
//                    setTermRequest();
//                    //((IciciTermActivity) getActivity()).redirectToQuote(termFinmartRequest);
//                    showDialog("Please Wait..");
//                    new TermInsuranceController(getActivity()).recalculateUltraLaksha(Requestentity, this);
//                }

                ((UltraLakshyaTermBottmActivity) getActivity()).redirectToQuote();

                break;
            case R.id.btnGetrecalculate:

                if (isValidInput()) {

                    SumICICIAssured();
                    setTermRequest();

                    showDialog("Please Wait..");
                    new TermInsuranceController(getActivity()).recalculateUltraLaksha(Requestentity, this);
                }
                break;

            case R.id.ivEdit:

                break;
            case R.id.ivInfo:

                OpenPoupWnidow(termCompareResponseEntity.getKeyFeatures().split("\\|"));
                break;
            case R.id.txtICICILumpSum:
            case R.id.txtICICIRegularIncome:

            case R.id.txtICICILumpSumRegular:

                break;
            case R.id.plusICICISum:
                changeSumAssured(true);
                break;
            case R.id.minusICICISum:
                changeSumAssured(false);
                break;
            case R.id.plusICICIPTerm:
                //changePolicyTerm(true);
                break;
            case R.id.minusICICIPTerm:
                //changePolicyTerm(false);
                break;

            case R.id.txt10lac:
                SUM_ASSURED_AMNT = "1000000";
                etSumICICIAssured.setText(SUM_ASSURED_AMNT);
                setBackgroundSeekBar(txt10lac, txt25lac, txt50lac, txt1cr, txtOther);
                lySeekbar.setVisibility(View.GONE);

                break;

            case R.id.txt25lac:
                SUM_ASSURED_AMNT = "2500000";
                etSumICICIAssured.setText(SUM_ASSURED_AMNT);
                setBackgroundSeekBar(txt25lac, txt10lac, txt50lac, txt1cr, txtOther);
                lySeekbar.setVisibility(View.GONE);

                break;

            case R.id.txt50lac:
                SUM_ASSURED_AMNT = "5000000";
                etSumICICIAssured.setText(SUM_ASSURED_AMNT);
                setBackgroundSeekBar(txt50lac, txt10lac, txt25lac, txt1cr, txtOther);
                lySeekbar.setVisibility(View.GONE);

                break;

            case R.id.txt1cr:
                SUM_ASSURED_AMNT = "10000000";
                etSumICICIAssured.setText(SUM_ASSURED_AMNT);
                setBackgroundSeekBar(txt1cr, txt10lac, txt25lac, txt50lac, txtOther);
                lySeekbar.setVisibility(View.GONE);

                break;

            case R.id.txtOther:
                // SUM_ASSURED_AMNT= "1000000";
                setBackgroundSeekBar(txtOther, txt10lac, txt25lac, txt50lac, txt1cr);
                lySeekbar.setVisibility(View.VISIBLE);
                etSumICICIAssured.setVisibility(View.VISIBLE);
                txtDispalaylac.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void setBackgroundSeekBar(TextView txtApply, TextView txt1, TextView txt2, TextView txt3, TextView txt4) {

        txtApply.setBackgroundResource(R.drawable.customeborder_blue);
        txtApply.setTypeface(null, Typeface.BOLD);

        txt1.setTypeface(null, Typeface.NORMAL);
        txt2.setTypeface(null, Typeface.NORMAL);
        txt3.setTypeface(null, Typeface.NORMAL);
        txt4.setTypeface(null, Typeface.NORMAL);
        txt1.setBackgroundResource(R.drawable.customeborder);
        txt2.setBackgroundResource(R.drawable.customeborder);
        txt3.setBackgroundResource(R.drawable.customeborder);
        txt4.setBackgroundResource(R.drawable.customeborder);
    }

    private void changeSumAssured(boolean b) {

        long sumAssured = 0;
        if (!etSumICICIAssured.getText().toString().equals(""))
            sumAssured = Long.parseLong(etSumICICIAssured.getText().toString().replaceAll("\\,", ""));
        if (b) {
            if (sumAssured >= 100000 && sumAssured < 1000000)
                sumAssured = sumAssured + 100000;
            else
                sumAssured = sumAssured + 100000;


        } else {
            if (sumAssured > 1000000)
                sumAssured = sumAssured - 100000;
            else if (sumAssured > 500000 && sumAssured <= 1000000)
                sumAssured = sumAssured - 100000;


        }
        // (sumAssured);
        etSumICICIAssured.setText("" + (sumAssured));
    }


    private void setTermRequest() {

        // Requestentity.setPolicyTerm(1);

        if (dbPersistanceController.getUserConstantsData().getParentid() != null
                && !dbPersistanceController.getUserConstantsData().getParentid().equals("")
                && !dbPersistanceController.getUserConstantsData().getParentid().equals("0")) {
            Requestentity.setSubFbaId(String.valueOf(dbPersistanceController.getUserConstantsData().getFBAId()));
            Requestentity.setFBAID(Integer.parseInt(dbPersistanceController.getUserConstantsData().getParentid()));
        } else {
            Requestentity.setSubFbaId("0");
            Requestentity.setFBAID(Integer.parseInt(dbPersistanceController.getUserConstantsData().getFBAId()));
        }


        if (dbPersistanceController.getUserConstantsData().getPospsendid() != null
                && !dbPersistanceController.getUserConstantsData().getPospsendid().equals("")) {
            Requestentity.setPospId(dbPersistanceController.getUserConstantsData().getPospsendid());
        } else {
            Requestentity.setPospId("5");
        }


        Requestentity.setContactName(etFirstName.getText().toString().trim() + " " + etLasttName.getText().toString().trim());
        Requestentity.setContactEmail("finmarttest@gmail.com");
        Requestentity.setContactMobile(etMobile.getText().toString());

        if (isMale)
            Requestentity.setInsuredGender("M");
        else
            Requestentity.setInsuredGender("F");

        if (isSmoker)
            Requestentity.setIs_TabaccoUser("True");
        else
            Requestentity.setIs_TabaccoUser("False");


        //   Requestentity.setSumAssured(Integer.parseInt(etSumICICIAssured.getText().toString().replaceAll("\\,", "")));

        Requestentity.setSumAssured(Long.valueOf(etSumICICIAssured.getText().toString()));   //
        Requestentity.setInsuredDOB(et_DOB.getText().toString());

        switch (spICICIPremiumFrequency.getSelectedItemPosition()) {
            case 0:
                Requestentity.setFrequency("Yearly");
                break;
            case 1:
                Requestentity.setFrequency("Half Yearly");
                break;
            case 2:
                Requestentity.setFrequency("Quarterly");
                break;
            case 3:
                Requestentity.setFrequency("Monthly");
                break;
        }
        Requestentity.setPolicyTerm(Integer.parseInt(spPolicyTerm.getSelectedItem().toString()));
        int fba_id = new DBPersistanceController(getActivity()).getUserData().getFBAId();
        Requestentity.setFBAID(fba_id);

        ((UltraLakshyaTermBottmActivity) getActivity()).updateRequest(Requestentity);

    }


    @Override
    public void onPositiveButtonClick(Dialog dialog, View view) {
        dialog.cancel();
    }

    @Override
    public void onCancelButtonClick(Dialog dialog, View view) {
        dialog.cancel();
    }

    //region date picker


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

        if (etLasttName.getText().toString().isEmpty()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etLasttName.requestFocus();
                etLasttName.setError("Enter Last Name");
                etLasttName.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etLasttName.requestFocus();
                etLasttName.setError("Enter Last Name");
                return false;
            }
        }

        if (etMobile.getText().toString().trim().length() > 0) {

            if (etMobile.getText().toString().trim().length() < 10) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    etMobile.requestFocus();
                    etMobile.setError("Enter Valid Mobile");
                    etMobile.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    return false;
                } else {
                    etMobile.requestFocus();
                    etMobile.setError("Enter Valid Mobile");
                    return false;
                }
            } else if (!isValidePhoneNumber(etMobile)) {
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
        }


        if (et_DOB.getText().toString().isEmpty()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                et_DOB.requestFocus();
                et_DOB.setError("Enter Dob");
                et_DOB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                et_DOB.requestFocus();
                et_DOB.setError("Enter Dob");
                return false;
            }
        }


        if (etSumICICIAssured.getText().toString().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etSumICICIAssured.requestFocus();
                etSumICICIAssured.setError("Enter Sum Assured");
                etSumICICIAssured.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            } else {
                etSumICICIAssured.requestFocus();
                etSumICICIAssured.setError("Enter Sum Assured");
                return false;
            }

        }
        if (Integer.parseInt(etSumICICIAssured.getText().toString()) < 1000000) {
            etSumICICIAssured.requestFocus();
            etSumICICIAssured.setError("Enter Sum Assured greater than 10 Lac");
            return false;
        }


        return true;
    }
    //endregion

    public int caluclateAge(Calendar dob) {
        Calendar current = Calendar.getInstance();
        int diff = current.get(YEAR) - dob.get(YEAR);
//        if (dob.get(MONTH) > current.get(MONTH) ||
//                (dob.get(MONTH) == current.get(MONTH) && dob.get(DATE) > current.get(DATE))) {
//            diff--;
//        }//required year wise age
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
//        if (dob.get(MONTH) > current.get(MONTH) ||
//                (dob.get(MONTH) == current.get(MONTH) && dob.get(DATE) > current.get(DATE))) {
//            diff--;
//        }//required year wise age
        return diff;
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

        try {


            if (response instanceof UltraLakshaRecalculateResponse) {
                //processResponse((UltraLakshaRecalculateResponse) response);
                if (((UltraLakshaRecalculateResponse) response).getMasterData().getLIC().size() != 0) {

                    LICEntity entityLIC = new LICEntity();
                    entityLIC = ((UltraLakshaRecalculateResponse) response).getMasterData().getLIC().get(0);

                    HDFCEntity hDFCEntity = new HDFCEntity();
                    hDFCEntity = ((UltraLakshaRecalculateResponse) response).getMasterData().getHDFC().get(0);

                    Integer ultraLakshya = entityLIC.getNetPremium() + hDFCEntity.getNetPremium();
                    IllustrationRequestEntity reqentity = new IllustrationRequestEntity();
                    reqentity = ((UltraLakshaRecalculateResponse) response).getMasterData().getIllustrationrequest().get(0);

                    etCal_lic.setText("" + reqentity.getTotalPrem());
                    etCal_ultra.setText("" + reqentity.getPremPaidUL());

                    mainScroll.scrollTo(0, mainScroll.getBottom());

                    txtAnnualPremiumDisplay.setText(spICICIPremiumFrequency.getSelectedItem() + " PREMIUM ON " + etSumICICIAssured.getText().toString() + " SA");
                    lyAnnualPremium.setVisibility(View.VISIBLE);
                    lySeekbar.setVisibility(View.GONE);
                    btnGetQuote.setVisibility(View.VISIBLE);


                    LICIllustrationRequestEntity entity = new LICIllustrationRequestEntity();

                    entity.setPlanTerm("" + reqentity.getPlanTerm());
                    entity.setSumAssured(reqentity.getSumAssured());

                    // entity.setPaymentMode("Y");
                    //  entity.setDOB("15/Dec/1988");

                    entity.setPaymentMode(reqentity.getPaymentMode());
                    entity.setDOB(reqentity.getDOB());

                    entity.setHdfcPrem(reqentity.getHdfcPrem());
                    entity.setHdfcBasicPrem(reqentity.getHdfcBasicPrem());
                    entity.setTotalPrem(reqentity.getTotalPrem());
                    entity.setBasicPrem(reqentity.getBasicPrem());
                    entity.setPremPaidUL(reqentity.getPremPaidUL());

                    entity.setHdfcGst1(reqentity.getHdfcGst1());
                    entity.setHdfcGst2(reqentity.getHdfcGst2());

                    entity.setLicGst1(reqentity.getLicGst1());
                    entity.setLicGst2(reqentity.getLicGst2());
                    entity.setPremTermText(reqentity.getPremTermText());

                    new TermInsuranceController(getActivity()).getIllustration(entity, this);


                }
            } else if (response instanceof UltraLakshaIllustrationResponseNew) {

                cancelDialog();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
        //changeInputQuote(true);
    }

    private void setPopUpInfo() {

        // region set Default popUp
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        customView = inflater.inflate(R.layout.layout_benefit_iprotect, null);

        // Initialize a new instance of popup window

        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                700
        );


        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }


        //endregion

        // region set Selection popUp
        LayoutInflater inflaterSelection = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        customViewSelection = inflaterSelection.inflate(R.layout.layout_age_popup_selected, null);

        // Initialize a new instance of popup window
        mPopupWindowSelection = new PopupWindow(
                customViewSelection,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        //endregion

    }

    private void OpenPoupWnidow(String[] strings) {


        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.imgClose);
        TextView tvTitle = (TextView) customView.findViewById(R.id.tvTitle);
        tvTitle.setText("BENEFIT OF " + termCompareResponseEntity.getProductPlanName());
        rvIprotectSmart = (RecyclerView) customView.findViewById(R.id.rvIprotectSmart);
        rvIprotectSmart.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvIprotectSmart.setLayoutManager(layoutManager);

        adapter = new HdfcIProtectAdapter(getActivity(), strings);
        rvIprotectSmart.setAdapter(adapter);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindowSelection.dismiss();


                }
            }
        });


        //   mPopupWindowSelection.showAsDropDown(ivInfo, 0, 0);
        mPopupWindowSelection.setTouchable(true);
        mPopupWindowSelection.setFocusable(true);


        // mPopupWindow.setAnimationStyle(R.style.Animation);
        //    mPopupWindow.showAsDropDown(ivInfo, -40, 40);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);

    }

    //region dob
    protected View.OnClickListener datePickerDialog = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Constants.hideKeyBoard(view, getActivity());


            DateTimePicker.showDataPickerDialog_lakshya(view.getContext(), (Calendar) view.getTag(R.id.et_DOB), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                    if (view1.isShown()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);


                        et_DOB.setTag(R.id.et_DOB, calendar);
                        age = caluclateAge(calendar);

                        String currentDay = simpleDateFormat.format(calendar.getTime());
                        et_DOB.setText(currentDay);
                        //TODO:set tag to DOB -- nilesh
                        et_DOB.setTag(R.id.et_DOB, calendar);
                        manipulatePremiumTerm(age);

                    }
                }
            });


        }
    };

    //endregion
    private void manipulatePremiumTerm(int age) {


        int selection = 0;
        //region remove pay till 60 if age > 55
        if (age >= 50) {
            listOption = new String[]{"13", "14", "15"};
            selection = 2;
        } else if (age > 40 && age < 50) {
            int arraysize = (65 - age) - 12;

            listOption = new String[arraysize];
            for (int i = 0; i < arraysize; i++) {
                String s = String.valueOf(i + 13);
                listOption[i] = s;

            }
            if (age > 45 && age < 50) {
                selection = arraysize - 1;
            }
            if (age > 40 && age <= 45) {
                selection = 7;
            }


        } else if (age <= 40) {
            listOption = getActivity().getResources().getStringArray(R.array.lakshya_policyterm);
            selection = 7;
        }

        final List<String> optionsList = new ArrayList<>(Arrays.asList(listOption));
        ArrayAdapter<String> spAdapterOptions = new ArrayAdapter<String>(getActivity()
                , android.R.layout.simple_spinner_item
                , optionsList);
//        spAdapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPolicyTerm.setAdapter(spAdapterOptions);
        spPolicyTerm.setSelection(selection);
//
//        ArrayAdapter<String> spAdapterPremiumFreq = new ArrayAdapter<String>(getActivity()
//                , android.R.layout.simple_spinner_item
//                , premiumFreqList);
//
//        spAdapterPremiumFreq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spICICIPremiumFrequency.setAdapter(spAdapterPremiumFreq);


        // bindPremiumPayment(0);


    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {

            case R.id.etSumICICIAssured:
                if (!b) {

                }
                break;

        }
    }

    public void SumICICIAssured() {
        String etsumvalue = etSumICICIAssured.getText().toString();

        long finalvalue = 0;
        String calValue = "0";


        //      finalvalue = (Long.valueOf(etsumvalue)/100000)*100000;
        calValue = String.valueOf((Double.valueOf(etsumvalue)) / 100000);

        if (String.valueOf(calValue).contains(".")) {
            String[] arrOfStr = calValue.split("\\.");
            if (!arrOfStr[1].toString().equalsIgnoreCase("0")) {
                finalvalue = (Long.valueOf(arrOfStr[0].toString()) + 1) * 100000;
                etSumICICIAssured.setText("" + finalvalue);
            }
        }

//        String Othervalue="";
//        Integer finalOthvalue=0;
//        if(etsumvalue.length() == 7) {
//            filtervalue = etsumvalue.substring(0, 2);
//            filtervalue = etsumvalue.substring(2, 2);
//            finalvalue=(Integer.parseInt(filtervalue) + 1)*100000;
//        }
//        if(etsumvalue.length() == 8) {
//            filtervalue = etsumvalue.substring(0, 3);
//            finalvalue=(Integer.parseInt(filtervalue) + 1)*100000;
//        }
//        if(etsumvalue.length() == 9) {
//            filtervalue= etsumvalue.substring(0, 4);
//            finalvalue=(Integer.parseInt(filtervalue) + 1)*100000;
//        }

    }

    private void calculatePremiumTerm() {
        /*
        int maxPremiumTerm = 100;
        int minPremiumTerm = 1;
        long policyTerm = 0;
        long premiumTerm = 0;
        if (!etICICIPremiumTerm.getText().toString().equals(""))
            premiumTerm = Long.parseLong(etICICIPremiumTerm.getText().toString().replaceAll("\\,", ""));

        if (!etICICIPolicyTerm.getText().toString().equals(""))
            policyTerm = Long.parseLong(etICICIPolicyTerm.getText().toString().replaceAll("\\,", ""));


        if (policyTerm >= maxPremiumTerm) {
            etICICIPremiumTerm.setText("" + (maxPremiumTerm));
        } else {
            if (premiumTerm >= policyTerm)
                etICICIPremiumTerm.setText("" + (policyTerm));
        }

        if (premiumTerm <= minPremiumTerm) {
            etICICIPremiumTerm.setText("" + minPremiumTerm);
        }*/
    }

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//        switch (seekBar.getId()) {
//            case R.id.sbSumAssured:
//                int MIN = 0;
//                if (progress >= MIN) {
//                    if (fromUser) {
//                        // progress = ((int) Math.round(progress / seekBarTenureProgress)) * seekBarTenureProgress;
//                        txtSumAssured.setText(String.valueOf(progress + 1) + "Lac");
//                        SUM_ASSURED_AMNT = String.valueOf((progress + 1) * 1000000);
//                    }
//                } else {
//                    sbSumAssured.setProgress(MIN);
//                    SUM_ASSURED_AMNT = "1000000";
//                    txtSumAssured.setText(String.valueOf(MIN) + "Lac");
//                }
//                break;
//
//
//        }
////    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//    }
}
