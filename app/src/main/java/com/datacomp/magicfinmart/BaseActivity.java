package com.datacomp.magicfinmart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.datacomp.magicfinmart.salesmaterial.SalesShareActivity;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import magicfinmart.datacomp.com.finmartserviceapi.Utility;

/**
 * Created by Rohit on 12/12/15.
 */
public class BaseActivity extends AppCompatActivity {

    public Realm realm;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Realm
        Realm.init(this);
        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void cancelDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void showDialog() {
//        dialog = ProgressDialog.show(BaseActivity.this, "", "Loading...", true);
        showDialog("Loading...");
    }

    protected void showDialog(String msg) {
        dialog = ProgressDialog.show(BaseActivity.this, "", msg, true);
    }

    public void dialNumber(String mobNumber) {
        try {
            mobNumber = mobNumber.replaceAll("\\s", "");
            mobNumber = mobNumber.replaceAll("\\+", "");
            mobNumber = mobNumber.replaceAll("-", "");
            mobNumber = mobNumber.replaceAll(",", "");
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + mobNumber));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
        }
    }

    public void composeEmail(String addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{addresses});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        startActivity(Intent.createChooser(intent, "Email via..."));
    }

    public static boolean isValidePhoneNumber(EditText editText) {
        String phoneNumberPattern = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
        String phoneNumberEntered = editText.getText().toString().trim();
        return !(phoneNumberEntered.isEmpty() || !phoneNumberEntered.matches(phoneNumberPattern));
    }

    public static boolean isValideEmailID(EditText editText) {
        String emailEntered = editText.getText().toString().trim();
        return !(emailEntered.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEntered).matches());
    }

    public static boolean isEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        return !(text.isEmpty());
    }

    public static boolean isValidPan(EditText editText) {
        String panNo = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        String panNoAlter = editText.getText().toString().toUpperCase();
        return !(panNoAlter.isEmpty() || !panNoAlter.matches(panNo));
    }

    public static boolean isValidAadhar(EditText editText) {
        String aadharPattern = "[0-9]{12}";
        String aadharNo = editText.getText().toString().toUpperCase();
        return !(aadharNo.isEmpty() || !aadharNo.matches(aadharPattern));
    }


    public File saveImageToStorage(Bitmap bitmap, String name) {
        FileOutputStream outStream = null;

        File dir = Utility.createDirIfNotExists();
        String fileName = name + ".jpg";
        fileName = fileName.replaceAll("\\s+", "");
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outFile;
    }

    public File saveImageToStorage1(Bitmap bitmap, String name) {
        FileOutputStream outStream = null;

        File dir = Utility.createDirIfNotExists();
        String fileName = name + ".jpg";
        fileName = fileName.replaceAll("\\s+", "");
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outFile;
    }


    public Bitmap createBitmap() {
        Bitmap resultBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawText("Name : Rajeev Ranjan", 10, 10, paint);
        return resultBitmap;
    }

    public Bitmap combineImages(Bitmap first, Bitmap second,String pospName,String pospDesg,String pospMob,String pospEmail) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;
        width = first.getWidth();
        height = first.getHeight() + second.getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(first, 0f, 0f, null);
        comboImage.drawBitmap(second, 0f, first.getHeight(), null);

        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/

        return cs;
    }

    public void datashareList(Context context, Bitmap bitmap, String prdSubject, String prdDetail) {

        //  String Deeplink = "https://nykaa.ly/P_" + Sharedata_product_id;

        //  String prdSubject = "Look what I found on Nykaa!";
        //  String prdDetail = "Check out " + Sharedata_product_name + " on Nykaa" + "\n" + Deeplink;
        try {


            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Finmart_product.png");

            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            Uri screenshotUri = Uri.fromFile(file);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);

            shareIntent.setType("text/plain");

            PackageManager pm = context.getPackageManager();


            List<ResolveInfo> resInfo = pm.queryIntentActivities(shareIntent, 0);
            List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();

            for (int i = 0; i < resInfo.size(); i++) {
                // Extract the label, append it, and repackage it in a LabeledIntent
                ResolveInfo ri = resInfo.get(i);
                String packageName = ri.activityInfo.packageName;
                String processName = ri.activityInfo.processName;
                String AppName = ri.activityInfo.name;

                if ((packageName.contains("android.email") || packageName.contains("mms") || packageName.contains("twitter") || (packageName.contains("whatsapp")) || packageName.contains("messaging") || packageName.contains("android.gm") || packageName.contains("com.google.android.apps.plus")) || (packageName.contains("apps.docs")) && processName.contains("android.apps.docs:Clipboard") || (packageName.contains("android.talk")) && AppName.contains("hangouts")) {

                    shareIntent.setComponent(new ComponentName(packageName, ri.activityInfo.name));

                    if (packageName.contains("android.email")) {
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, prdSubject);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                        shareIntent.setPackage(packageName);

                    } else if (packageName.contains("twitter")) {

                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, prdSubject);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        shareIntent.setPackage(packageName);

                    }
//                    else if (packageName.contains("facebook.katana")) {
//                        shareIntent.setType("text/plain");
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, product.getImageUrl());
//                        shareIntent.setPackage("com.facebook.katana");
//                        //shareIntent.putExtra(Intent.EXTRA_STREAM, Deeplink);
//                    }
//                    else if (packageName.contains("facebook.orca")) {
//                        shareIntent.setType("image/*");
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
//                        shareIntent.setPackage("com.facebook.orca");
//
//                    }
                    else if (packageName.contains("mms")) {
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                        shareIntent.setPackage(packageName);

                    } else if (packageName.contains("whatsapp")) {
                        shareIntent.setType("image/*");

                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        shareIntent.setPackage(packageName);


                    } else if (packageName.contains("messaging")) {
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                        shareIntent.setPackage(packageName);
                    } else if (packageName.contains("com.google.android.apps.plus")) {
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        shareIntent.setPackage(packageName);

                    } else if (packageName.contains("android.talk")) {
                        if (AppName.contains("hangouts")) {
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                            shareIntent.setPackage(packageName);
                        }

                    } else if (packageName.contains("apps.docs")) {
                        if (processName.contains("android.apps.docs:Clipboard")) {
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                            shareIntent.setPackage(packageName);
                        }

                    } else if (packageName.contains("android.gm")) {
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, prdSubject);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        shareIntent.setPackage(packageName);

                    } else {
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, prdDetail);

                    }

                    intentList.add(new LabeledIntent(shareIntent, packageName, ri.loadLabel(pm), ri.icon));

                }
            }


            if (intentList.size() > 1) {
                intentList.remove(intentList.size() - 1);
            }

            Intent openInChooser = Intent.createChooser(shareIntent, "Share Via");
            // convert intentList to array
            LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);

            startActivity(openInChooser);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class shareImageNormal extends AsyncTask<Void, Void, Bitmap> {

        String subject, body;
        URL imgUrl;

        public shareImageNormal(String imgUrl, String subject, String body) {
            this.subject = subject;
            this.body = body;

            try {

                this.imgUrl = new URL(imgUrl);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap networkBitmap = null;

            //  URL networkUrl = urls[0]; //Load the first element
            URL networkUrl = imgUrl;
            try {
                networkBitmap = BitmapFactory.decodeStream(
                        networkUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Could not load Bitmap from: " + imgUrl);
            }

            return networkBitmap;
        }

        protected void onPostExecute(Bitmap result) {

            try {
                cancelDialog();
                if (result != null) {

                    datashareList(BaseActivity.this, result, subject, body);

                } else {
                    cancelDialog();

                }


            } catch (Exception e) {
                cancelDialog();
                e.printStackTrace();
                Log.e("TAG", "Could not load Bitmap from: " + e.getMessage());

            }
        }
    }


    public class shareImage extends AsyncTask<Void, Void, Bitmap> {

        String pospNAme, pospDesg, PospMobNo, pospEmail, subject, body;
        URL imgUrl, pospPhotoUrl;

        public shareImage(String imgUrl, String pospPhotoUrl, String pospNAme, String pospDesg, String pospMobNo, String pospEmail, String subject, String body) {
            this.pospNAme = pospNAme;
            this.pospDesg = pospDesg;
            PospMobNo = pospMobNo;
            this.pospEmail = pospEmail;
            this.subject = subject;
            this.body = body;

            try {
                this.pospPhotoUrl = new URL(pospPhotoUrl);
                this.imgUrl = new URL(imgUrl);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap networkBitmap = null;

            //  URL networkUrl = urls[0]; //Load the first element
            URL networkUrl = imgUrl;
            try {
                networkBitmap = BitmapFactory.decodeStream(
                        networkUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Could not load Bitmap from: " + imgUrl);
            }

            return networkBitmap;
        }

        protected void onPostExecute(Bitmap result) {

            try {

                if (result != null) {
                    new createBitmapFromURL(result,pospPhotoUrl,pospNAme,pospDesg,PospMobNo,pospEmail,subject,body).execute();
                    //datashareList(BaseActivity.this, result, Subject, Body);

                } else {
                    cancelDialog();
                    // Snackbar.make()

                }


            } catch (Exception e) {
                cancelDialog();
                e.printStackTrace();
                Log.e("TAG", "Could not load Bitmap from: " + e.getMessage());

            }
        }
    }

    public class createBitmapFromURL extends AsyncTask<Void, Void, Bitmap> {
        URL  pospPhotoUrl;
        String pospNAme, pospDesg, PospMobNo, pospEmail, subject, body;
        Bitmap salesMaterial;
        public createBitmapFromURL(Bitmap salesMaterial,URL pospPhotoUrl, String pospNAme, String pospDesg,
                                   String pospMobNo, String pospEmail, String subject, String body) {

            this.salesMaterial=salesMaterial;
            this.pospNAme = pospNAme;
            this.pospDesg = pospDesg;
            PospMobNo = pospMobNo;
            this.pospEmail = pospEmail;
            this.subject = subject;
            this.body = body;
            this.pospPhotoUrl=pospPhotoUrl;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap networkBitmap = null;

            //  URL networkUrl = urls[0]; //Load the first element
            try {
                networkBitmap = BitmapFactory.decodeStream(
                        pospPhotoUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Could not load Bitmap from: " + pospPhotoUrl);
            }

            return networkBitmap;
        }

        protected void onPostExecute(Bitmap result) {

            try {

                if (result != null) {
                    //datashareList(BaseActivity.this, result);
                    Bitmap combined = combineImages(salesMaterial,result,pospNAme,pospDesg,PospMobNo,pospEmail);
                    datashareList(BaseActivity.this,combined,subject,body);
                    cancelDialog();
                } else {
                    cancelDialog();
                    // Snackbar.make()
                }


            } catch (Exception e) {
                cancelDialog();
                e.printStackTrace();
                Log.e("TAG", "Could not load Bitmap from: " + e.getMessage());

            }
        }
    }

}

