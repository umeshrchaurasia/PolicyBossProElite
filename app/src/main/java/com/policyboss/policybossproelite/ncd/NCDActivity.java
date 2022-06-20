package com.policyboss.policybossproelite.ncd;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;

import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.DynamicController;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.model.DocumentNCDEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.model.NDCMasterEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.NCDResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;

public class NCDActivity extends BaseActivity implements IResponseSubcriber, View.OnClickListener {

    TextView txtProduct;
    ImageView imgBanner;
    LinearLayout llListProduct;
    Button btnUploadDocument, btnOpenDocuments;
    NDCMasterEntity ndcMasterEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Service hit to fetch data.
        fetchNCD();
    }

    private void fetchNCD() {
        showDialog();
        new DynamicController(this).getNCD(this);
    }

    private void init() {
        txtProduct = findViewById(R.id.txtProduct);
        imgBanner = findViewById(R.id.imgBanner);
        llListProduct = findViewById(R.id.llListProduct);
        btnUploadDocument = findViewById(R.id.btnUploadDocument);
        btnOpenDocuments = findViewById(R.id.btnOpenDocuments);
        btnUploadDocument.setOnClickListener(this);
        btnOpenDocuments.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUploadDocument) {
            Intent intent = new Intent(this, UploadNCDDocActivity.class);
            intent.putExtra("UPLOAD_NCD", ndcMasterEntity);
            startActivity(intent);
        } else if (v.getId() == R.id.btnOpenDocuments) {
            Utility.loadWebViewUrlInBrowser(NCDActivity.this,
                    ndcMasterEntity.getViewaddedncd());
        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();
        if (response instanceof NCDResponse) {
            if (((NCDResponse) response).getMasterData() != null) {
                ndcMasterEntity = ((NCDResponse) response).getMasterData();
                bindData(ndcMasterEntity);
            } else {
                Toast.makeText(this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void bindData(NDCMasterEntity masterData) {
        txtProduct.setText(masterData.getCamapignname());

        Glide.with(this)
                .load(masterData.getBannerimage())
                .into(imgBanner);

        if (masterData.getDocument() != null && masterData.getDocument().size() > 0)
            addDynamicProducts(masterData.getDocument());
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void addDynamicProducts(List<DocumentNCDEntity> listDocuments) {

        llListProduct.removeAllViews();
        for (int i = 0; i < listDocuments.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.sp_item_textview, null);
            TextView txtDocumentName = ((TextView) view.findViewById(R.id.txtspinneritem));

            txtDocumentName.setPadding(16, 8, 8, 8);
            txtDocumentName.setTag(R.id.txtspinneritem, listDocuments.get(i));
            txtDocumentName.setText(listDocuments.get(i).getDocumentname());
            txtDocumentName.setPaintFlags(txtDocumentName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            txtDocumentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (v.getTag(R.id.txtspinneritem) != null)
                        Utility.loadWebViewUrlInBrowser(NCDActivity.this, ((DocumentNCDEntity) v.getTag(R.id.txtspinneritem)).getDocumentpath());
                }
            });
            llListProduct.addView(view, i);
        }


    }


}
