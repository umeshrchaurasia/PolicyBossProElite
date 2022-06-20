package com.policyboss.policybossproelite.helpfeedback.raiseticket;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.helpfeedback.raiseticket.adapter.RaiseTicketAdapter;
import com.policyboss.policybossproelite.helpfeedback.raiseticket.adapter.RaiseTicketViewAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.zoho.ZohoController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.QuoteListEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.RaiseTickeViewEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TicketEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.RaiseTicketViewResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.TicketListResponse;

public class RaiseTicketActivity extends BaseActivity implements IResponseSubcriber, View.OnClickListener {

    FloatingActionButton btnAddTicket;
    RecyclerView rvTicketList;
    RaiseTicketAdapter raiseTicketAdapter;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    List<QuoteListEntity> mQuoteList;
    QuoteListEntity removeQuoteEntity;
    ImageView ivSearch, ivUser;
    TextView tvAdd, tvSearch;
    EditText etSearch;
    List<TicketEntity> ticketEntities;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginResponseEntity;
    AlertDialog alertDialog, finmartContacttDialog;
    BottomSheetDialog mBottomSheetDialog;
    RaiseTicketViewAdapter raiseTicketViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_ticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbPersistanceController = new DBPersistanceController(this);
        loginResponseEntity = dbPersistanceController.getUserData();

        initView();
        setListener();
        setTextWatcher();
        fatchingData();
    }

    private void initView() {
        ivSearch = (ImageView) findViewById(R.id.ivSearch);

        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setVisibility(View.INVISIBLE);

        btnAddTicket = (FloatingActionButton) findViewById(R.id.btnAddTicket);
        rvTicketList = (RecyclerView) findViewById(R.id.rvTicketList);
        rvTicketList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvTicketList.setLayoutManager(layoutManager);
        btnAddTicket.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void fatchingData() {
        showDialog("Fetching Tickets..");
        new ZohoController(this).getListOfTickets("" + loginResponseEntity.getFBAId(), this);

    }

    private void setListener() {
        ivSearch.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof TicketListResponse) {

            if (response.getStatusNo() == 0) {
                ticketEntities = ((TicketListResponse) response).getMasterData();
                raiseTicketAdapter = new RaiseTicketAdapter(this, ticketEntities);
                rvTicketList.setAdapter(raiseTicketAdapter);
            }
        } else if (response instanceof RaiseTicketViewResponse) {
            if (response.getStatusNo() == 0) {

                getBottomSheetDialog(((RaiseTicketViewResponse) response).getMasterData());
            }

        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAdd:
            case R.id.btnAddTicket:
                startActivityForResult(new Intent(this, AddTicketActivity.class),2);

                break;
            case R.id.tvSearch:
            case R.id.ivSearch:
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        etSearch.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);*/
                if (etSearch.getVisibility() == View.INVISIBLE) {
                    etSearch.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                }
                break;

        }
    }

    private void setTextWatcher() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                raiseTicketAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void redirectToUpload(TicketEntity entity) {
        Intent intent = new Intent(RaiseTicketActivity.this, UploadRaiseActivity.class);
        intent.putExtra("REQ_ID", entity.getTicketRequestId());
        intent.putExtra("STATUS_ID", entity.getStatusId());
        startActivityForResult(intent, 2);
    }

    public void redirectToView(TicketEntity entity) {
        showDialog();
        new ZohoController(this).viewCommentOfTickets(String.valueOf(entity.getTicketRequestId()), this);

    }

    public void redirectToRaiseTicket(View view ,String URL)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent shareIntent = new Intent(RaiseTicketActivity.this, ViewImageRaiseTicketActivity.class)
                    .putExtra("RAISE_TICKET_URL",URL);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(view, "profileTransition");
            ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(RaiseTicketActivity.this, pairs);

            startActivity(shareIntent,options.toBundle());
        }else{
            startActivity(new Intent(RaiseTicketActivity.this, ViewImageRaiseTicketActivity.class)
                    .putExtra("RAISE_TICKET_URL",URL));
        }
    }


    public void PopUp_addcomment() {

//

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);


        Button btn_submit;
        EditText et_Comment;
        ImageView ivCross, ivProfile;

        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.layout_view_raise_comment, null);

        builder.setView(dialogView);
        finmartContacttDialog = builder.create();
        btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        ivProfile = (ImageView) dialogView.findViewById(R.id.ivProfile);
        ivUser = (ImageView) findViewById(R.id.ivUser);
        ivCross = (ImageView) dialogView.findViewById(R.id.ivCross);


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  galleryCamPopUp();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finmartContacttDialog.dismiss();
                // startActivity(new Intent(getActivity(), MyAttendanceActivity.class));

            }
        });


        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finmartContacttDialog.dismiss();

            }
        });
        finmartContacttDialog.setCancelable(false);
        finmartContacttDialog.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (data != null) {

                fatchingData();
            }
        }


    }

    public void getBottomSheetDialog(List<RaiseTickeViewEntity> viewEntityList) {


        if (viewEntityList != null && viewEntityList.size() > 0) {


            mBottomSheetDialog = new BottomSheetDialog(this, R.style.bottomSheetDialog);

            View sheetView = this.getLayoutInflater().inflate(R.layout.layout_view_raise_comment, null);

            mBottomSheetDialog.setContentView(sheetView);
            TextView txtHdr = mBottomSheetDialog.findViewById(R.id.txtHdr);
            RecyclerView rvViewTicket = (RecyclerView) mBottomSheetDialog.findViewById(R.id.rvViewTicket);
            ImageView ivCross = (ImageView) mBottomSheetDialog.findViewById(R.id.ivCross);

            rvViewTicket.setLayoutManager(new LinearLayoutManager(this));
            rvViewTicket.setHasFixedSize(true);
            raiseTicketViewAdapter = new RaiseTicketViewAdapter(this, viewEntityList);
            rvViewTicket.setAdapter(raiseTicketViewAdapter);

            rvViewTicket.setVisibility(View.VISIBLE);


            ivCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mBottomSheetDialog.isShowing()) {

                        mBottomSheetDialog.dismiss();
                    }
                }
            });


            mBottomSheetDialog.setCancelable(false);
            mBottomSheetDialog.setCanceledOnTouchOutside(true);
            mBottomSheetDialog.show();
        } else {

            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }


    }



}
