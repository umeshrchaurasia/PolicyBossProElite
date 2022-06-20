package com.policyboss.policybossproelite.festivelink;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.home.HomeActivity;
import com.policyboss.policybossproelite.utility.Constants;
import com.policyboss.policybossproelite.utility.TouchImageView;

import java.io.FileInputStream;
import java.net.URL;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.AccountDtlEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.FestivalCompaignEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.UserConstantEntity;

public class festivalShareActivity extends BaseActivity implements BaseActivity.PopUpListener {

    TouchImageView ivProduct;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginResponseEntity;
    Bitmap salesPhoto;

    Bitmap combinedImage;
    String pospNAme, pospDesg = "LandMark POSP", pospEmail, PospMobNo;

    AccountDtlEntity accountDtlEntity;
    UserConstantEntity userConstantEntity;
    boolean isSecondImageToShow = false;

    FestivalCompaignEntity festivalCompaignEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_share);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerPopUp(this);
        dbPersistanceController = new DBPersistanceController(this);
        userConstantEntity = dbPersistanceController.getUserConstantsData();
        loginResponseEntity = dbPersistanceController.getUserData();
        accountDtlEntity = dbPersistanceController.getAccountData();


        //Note : verify wheather User Data Exist or Not
        if (userConstantEntity.getParentid() != null
                && !userConstantEntity.getParentid().equals("")
                && !userConstantEntity.getParentid().equals("0")) {

            isSecondImageToShow = false;

        } else {
            isSecondImageToShow = true;
        }

        initialize();
        //new createBitmapFromURL(pospPhotoUrl).execute();
        new createBitmapFromURLNew().execute();

    }

    private void initialize() {
        ivProduct = (TouchImageView) findViewById(R.id.ivProduct);

        if (getIntent().hasExtra(Constants.FESTIVAL_DATA)) {
            festivalCompaignEntity = getIntent().getExtras().getParcelable(Constants.FESTIVAL_DATA);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                showShareProduct();
                break;

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
//
//    public void showShareProduct() {
//        if (combinedImage != null) {
//            datashareList(festivalShareActivity.this, combinedImage, festivalCompaignEntity.getTitle(),  festivalCompaignEntity.getDescription() +"\n" + festivalCompaignEntity.getShorturl());
//        }
//    }

    public void showShareProduct() {
        if (combinedImage != null) {
            String desc = "";
            if (!festivalCompaignEntity.getShorturl().isEmpty()) {
                desc = festivalCompaignEntity.getDescription() + "\n" + festivalCompaignEntity.getShorturl();
            } else {
                desc = festivalCompaignEntity.getDescription();
            }

            datashareList(festivalShareActivity.this, combinedImage, festivalCompaignEntity.getTitle(), desc);
        }
    }


    @Override
    public void onPositiveButtonClick(Dialog dialog, View view) {
        switch (view.getId()) {
            case R.id.ivProduct:
                dialog.cancel();
                break;
        }
    }

    @Override
    public void onCancelButtonClick(Dialog dialog, View view) {
        switch (view.getId()) {
            case R.id.ivProduct:
                dialog.cancel();
                break;
        }
    }


    public class createBitmapFromURLNew extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            showDialog();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            if (isSecondImageToShow) {
                try {
                    // create User Detail Image
                    combinedImage = BitmapFactory.decodeStream(new FileInputStream(getImageFromStorage("fbaSalesMaterialDetails")));

                } catch (Exception e) {

                }
            } else {
                combinedImage = null;
            }


            try {

                // create Sales Detail Image
                URL salePhotoUrl = new URL(festivalCompaignEntity.getImagelink());
                salesPhoto = BitmapFactory.decodeStream(
                        salePhotoUrl.openConnection().getInputStream());
                if (salesPhoto != null) {
                    combinedImage = combineImages(salesPhoto, combinedImage);  // combining both image
                    //ivProduct.setImageBitmap(combinedImage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return combinedImage;
        }

        protected void onPostExecute(Bitmap result) {

            cancelDialog();
            if (result == null) {
                // if above operation failed than Default image display
                Glide.with(festivalShareActivity.this)
                        .load(festivalCompaignEntity.getImagelink())
                        .asBitmap()
                        .placeholder(getResources().getDrawable(R.drawable.finmart_placeholder))
                        .into(ivProduct);
            } else {
                ivProduct.setImageBitmap(result);
            }


        }
    }

}
