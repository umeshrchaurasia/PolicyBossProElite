package com.policyboss.policybossproelite.festivelink;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.utility.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.salesmaterial.SalesMaterialController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.FestivalCompaignEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.FestivalCampaignResponse;


public class festivelinkActivity extends BaseActivity implements IResponseSubcriber {

    RecyclerView recyclerShareMessage;
    festivelinkAdapter mAdapter;
    List<FestivalCompaignEntity> listShareMessag;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginResponseEntity;
    FestivalCampaignResponse getfestivelinkResponse;
    int numberOfColumns = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festivelink);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init_widgets();


        dbPersistanceController = new DBPersistanceController(festivelinkActivity.this);
        loginResponseEntity = dbPersistanceController.getUserData();
        showDialog();
        new SalesMaterialController(festivelinkActivity.this)
                .getfestivelink(String.valueOf(loginResponseEntity.getFBAId()), String.valueOf(loginResponseEntity.getLoanId()), "Finmart", festivelinkActivity.this);


    }

    private void init_widgets() {

        recyclerShareMessage = (RecyclerView) findViewById(R.id.recyclerShareMSG);
        recyclerShareMessage.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,numberOfColumns);
        recyclerShareMessage.setLayoutManager(mLayoutManager);


    }


    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof FestivalCampaignResponse) {

            getfestivelinkResponse = ((FestivalCampaignResponse) response);
            if (getfestivelinkResponse != null) {
                if (getfestivelinkResponse.getMasterData().size() > 0) {
                    mAdapter = new festivelinkAdapter(festivelinkActivity.this, getfestivelinkResponse.getMasterData());
                    recyclerShareMessage.setAdapter(mAdapter);


                } else {
                    Toast.makeText(this, "Data Not Found", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getfestivelinkResponse.getMessage(), Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
       // Toast.makeText(this, getfestivelinkResponse.getMessage(), Toast.LENGTH_LONG).show();

    }

    public void redirectToApplyMain(FestivalCompaignEntity festivalCompaignEntity) {

        Intent intent = new Intent(this, festivalShareActivity.class);
        intent.putExtra(Constants.FESTIVAL_DATA, festivalCompaignEntity);

        startActivity(intent);
    }

    public void shareData(String strURL, String strSub , String strDetail)
    {

        new createBitmapFromURL(strURL,strSub,strDetail).execute();
    }


    public class createBitmapFromURL extends AsyncTask<Void, Void, Bitmap> {
        URL NotifyPhotoUrl;
        String imgURL,strSub,strDetail;
        public createBitmapFromURL(String imgURL,String strSub , String strDetail) {
            this.imgURL = imgURL;
            this.strSub = strSub;
            this.strDetail = strDetail;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap networkBitmap = null;
            try {
                NotifyPhotoUrl = new URL(imgURL);
                networkBitmap = BitmapFactory.decodeStream(
                        NotifyPhotoUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Could not load Bitmap from: " + NotifyPhotoUrl);
            }
            return networkBitmap;
        }
        protected void onPostExecute(Bitmap result) {
            cancelDialog();
          //  bitmap_image = result;
            datashareList(festivelinkActivity.this, result, strSub ,strDetail);
        }
    }


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
        finish();
        super.onBackPressed();
    }

}
