package com.policyboss.policybossproelite.ncd;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.utility.Constants;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.DynamicController;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.model.NDCMasterEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.UploadNCDRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.UploadNCDResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.DocumentResponse;
import okhttp3.MultipartBody;

public class UploadNCDDocActivity extends BaseActivity implements View.OnClickListener, IResponseSubcriber {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1800;
    PrefManager prefManager;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginEntity;
    NDCMasterEntity mNCDEntity;
    EditText etRefNum, etCustomerName;
    int type;
    File file;
    ImageView ivPhotoCam1, ivPhotoCam2, ivPhotoCam3, ivPhotoCam4, ivPhoto1,
            ivPhoto2, ivPhoto3, ivPhoto4;

    Button btnSave;

    HashMap<String, String> body;
    MultipartBody.Part part;
    File Docfile;
    Uri imageUri;
    InputStream inputStream;
    ExifInterface ei;

    private int APPLICATION = 1, OTH1 = 2, OTH2 = 3, OTH3 = 4, OTH4 = 5;

    private String file1 = "APPLICATION_FORM", file2 = "OTHER1", file3 = "OTHER2", file4 = "OTHER3";

    LoginResponseEntity loginResponseEntity;
    String[] permissionsRequired = new String[]{Manifest.permission.CALL_PHONE};

    boolean isUploaded = false;
    String[] perms = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ncddoc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbPersistanceController = new DBPersistanceController(this);
        loginResponseEntity = dbPersistanceController.getUserData();
        loginEntity = dbPersistanceController.getUserData();
        prefManager = new PrefManager(this);

        initWidgets();
        setListener();

        if (getIntent().getParcelableExtra("UPLOAD_NCD") != null) {
            mNCDEntity = getIntent().getParcelableExtra("UPLOAD_NCD");

        }
    }

    private void initWidgets() {
        ivPhotoCam1 = (ImageView) findViewById(R.id.ivPhotoCam1);
        ivPhotoCam2 = (ImageView) findViewById(R.id.ivPhotoCam2);
        ivPhotoCam3 = (ImageView) findViewById(R.id.ivPhotoCam3);
        ivPhotoCam4 = (ImageView) findViewById(R.id.ivPhotoCam4);

        ivPhoto1 = (ImageView) findViewById(R.id.ivPhoto1);
        ivPhoto2 = (ImageView) findViewById(R.id.ivPhoto2);
        ivPhoto3 = (ImageView) findViewById(R.id.ivPhoto3);
        ivPhoto4 = (ImageView) findViewById(R.id.ivPhoto4);

        etRefNum = (EditText) findViewById(R.id.etRefNum);
        etCustomerName = (EditText) findViewById(R.id.etCustomerName);

        btnSave = (Button) findViewById(R.id.btnSave);

    }

    private void setListener() {
        ivPhotoCam1.setOnClickListener(this);
        ivPhotoCam2.setOnClickListener(this);
        ivPhotoCam3.setOnClickListener(this);
        ivPhotoCam4.setOnClickListener(this);

        btnSave.setOnClickListener(this);
    }

    private void setDocumentUpload(String URL) {
        isUploaded = true;
        if (type == 1) {
            ivPhoto1.setImageResource(R.drawable.doc_uploaded);
        } else if (type == 2) {
            ivPhoto2.setImageResource(R.drawable.doc_uploaded);

        } else if (type == 3) {
            ivPhoto3.setImageResource(R.drawable.doc_uploaded);
        } else if (type == 4) {
            ivPhoto4.setImageResource(R.drawable.doc_uploaded);
        }

    }


    @Override
    public void onClick(View view) {
        Constants.hideKeyBoard(view, this);

        switch (view.getId()) {
            case R.id.ivPhotoCam1:
                type = 1;
                galleryCamPopUp();
                break;

            case R.id.ivPhotoCam2:
                type = 2;
                galleryCamPopUp();
                break;

            case R.id.ivPhotoCam3:
                type = 3;
                galleryCamPopUp();
                break;

            case R.id.ivPhotoCam4:
                type = 4;
                galleryCamPopUp();
                break;

            case R.id.btnSave:

                if (!isEmpty(etCustomerName)) {
                    etCustomerName.requestFocus();
                    etCustomerName.setError("Enter Customer Name");
                    return;
                } else if (!isEmpty(etRefNum)) {
                    etRefNum.requestFocus();
                    etRefNum.setError("Enter Referene Number");
                    return;
                } else if (!isUploaded) {
                    Toast.makeText(this, "Please Upload atleast One", Toast.LENGTH_SHORT).show();
                    return;
                }

                UploadNCDRequestEntity requestEntity = new UploadNCDRequestEntity();
                requestEntity.setCustomername(etCustomerName.getText().toString());
                requestEntity.setReferenceno(etRefNum.getText().toString());
                requestEntity.setCampaignid("" + mNCDEntity.getId());
                requestEntity.setGuid(mNCDEntity.getGuid());

                requestEntity.setFbaid("" + loginEntity.getFBAId());

                showDialog();
                new DynamicController(this).uploadNCDDetails(requestEntity, this);

                break;


        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();
        if (response instanceof DocumentResponse) {
            if (response.getStatusNo() == 0) {

                // Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
                setDocumentUpload(((DocumentResponse) response).getMasterData().get(0).getPrv_file());


            }
        } else if (response instanceof UploadNCDResponse) {
            if (response.getStatusNo() == 0) {

                Toast.makeText(this, ((UploadNCDResponse) response).getMessage(), Toast.LENGTH_LONG).show();

                this.finish();


            }
        }
    }

    @Override
    public void OnFailure(Throwable t) {

        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }


    //region Gallery Camera

    private void galleryCamPopUp() {

        if (!mNCDEntity.getGuid().equalsIgnoreCase("")) {
            if (!checkPermission()) {

                if (checkRationalePermission()) {
                    //Show Information about why you need the permission
                    requestPermission();

                } else {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission

                    //  permissionAlert(navigationView,"Need Call Permission","This app needs Call permission.");
                    openPopUp(btnSave, "Need  Permission", "This app needs all permissions.", "GRANT", true);


                }
            } else {

                showCamerGalleryPopUp();
            }
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


    private void showCamerGalleryPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);

        LinearLayout lyCamera, lyGallery;
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.layout_cam_gallery, null);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        // set the custom dialog components - text, image and button
        lyCamera = (LinearLayout) dialogView.findViewById(R.id.lyCamera);
        lyGallery = (LinearLayout) dialogView.findViewById(R.id.lyGallery);

        lyCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
                alertDialog.dismiss();

            }
        });

        lyGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                alertDialog.dismiss();

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.show();
        //  alertDialog.getWindow().setLayout(900, 600);

        // for user define height and width..
    }

    private void launchCamera() {


        String FileName = "";

        switch (type) {
            case 1:
                FileName = file1;
                break;
            case 2:
                FileName = file2;
                break;
            case 3:
                FileName = file3;
                break;
            case 4:
                FileName = file4;
                break;

        }
        Docfile = createImageFile(FileName);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            imageUri = Uri.fromFile(Docfile);
        } else {
            imageUri = FileProvider.getUriForFile(UploadNCDDocActivity.this,
                    getString(R.string.file_provider_authority), Docfile);
        }


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {

        String  mimeType = "image/*";

        Uri collection ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection =  MediaStore.Video.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
            );
        } else {
            collection =  MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }


        try {
            Intent intent = new  Intent(Intent.ACTION_PICK, collection);

            intent.setType(mimeType);
            intent.resolveActivity(getPackageManager());
            startActivityForResult(intent, SELECT_PICTURE);

        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //endregion


    // region Permission
    private boolean checkPermission() {

        int camera = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[0]);

        int WRITE_EXTERNAL = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[1]);
        int READ_EXTERNAL = ActivityCompat.checkSelfPermission(getApplicationContext(), perms[2]);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return camera == PackageManager.PERMISSION_GRANTED

                    && READ_EXTERNAL == PackageManager.PERMISSION_GRANTED;
        }else{
            return camera == PackageManager.PERMISSION_GRANTED
                    &&  WRITE_EXTERNAL == PackageManager.PERMISSION_GRANTED
                    && READ_EXTERNAL == PackageManager.PERMISSION_GRANTED;

        }
    }

    private boolean checkRationalePermission() {

        boolean camera = ActivityCompat.shouldShowRequestPermissionRationale(UploadNCDDocActivity.this, perms[0]);

        boolean write_external = ActivityCompat.shouldShowRequestPermissionRationale(UploadNCDDocActivity.this, perms[1]);
        boolean read_external = ActivityCompat.shouldShowRequestPermissionRationale(UploadNCDDocActivity.this, perms[2]);


        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return  camera ||  read_external;
        }else{
            return  camera ||write_external   || read_external;

        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, perms, Constants.PERMISSION_CAMERA_STORACGE_CONSTANT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case Constants.PERMISSION_CAMERA_STORACGE_CONSTANT:
                if (grantResults.length > 0) {

                    //boolean writeExternal = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternal = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternal = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

                    if (camera && (writeExternal || minSdk29 ) && readExternal) {

                        showCamerGalleryPopUp();

                    }


                }
                break;

        }
    }


    //endregion


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = null;
            try {
              //  mphoto = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mphoto = getBitmapFromContentResolver(imageUri);
                mphoto = getResizedBitmap(mphoto, 800);
                mphoto = rotateImageIfRequired(this, mphoto, Docfile);

            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (type) {
                case 1:
                    showDialog();
                    file = saveImageToStorage(mphoto, file1);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), APPLICATION);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;
                case 2:
                    showDialog();
                    file = saveImageToStorage(mphoto, file2);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), OTH1);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;
                case 3:

                    showDialog();
                    file = saveImageToStorage(mphoto, file3);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), OTH2);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;

                case 4:
                    showDialog();
                    file = saveImageToStorage(mphoto, file4);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), OTH3);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;

            }


        }
        else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Bitmap mphoto = null;
            // mphoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            mphoto = getBitmapFromContentResolver(selectedImageUri);
            mphoto = getResizedBitmap(mphoto, 800);
            //  mphoto = rotateImageIfRequired(this, mphoto, Docfile);

            switch (type) {
                case 1:
                    showDialog();
                    file = saveImageToStorage(mphoto, file1);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), APPLICATION);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;
                case 2:
                    showDialog();
                    file = saveImageToStorage(mphoto, file2);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), OTH1);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;
                case 3:

                    showDialog();
                    file = saveImageToStorage(mphoto, file3);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), OTH2);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;

                case 4:
                    showDialog();
                    file = saveImageToStorage(mphoto, file4);

                    part = Utility.getMultipartImage(file);
                    body = Utility.getNCDBody(this, mNCDEntity.getGuid(), OTH3);

                    new DynamicController(this).uploadNCDDocuments(part, body, this);
                    break;
            }


        }
    }


    //region Rotaion and Resize
    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }

    private Bitmap rotateImageIfRequired(Context context, Bitmap bitmap, File file) {
        Bitmap rotatedBitmap = null;
        try {

            // region Handling Default Rotation Of Image
            Uri uri = Uri.fromFile(file);
            inputStream = context.getContentResolver().openInputStream(uri);

            if (Build.VERSION.SDK_INT > 23) {
                ei = new ExifInterface(inputStream);
            } else {

                // ei = new ExifInterface("/storage/emulated/0/FINMART/FBAPhotograph.jpg");
                ei = new ExifInterface(file.getAbsolutePath());
            }


            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);


            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;

            }
            //endregion

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rotatedBitmap;
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //endregion


}
