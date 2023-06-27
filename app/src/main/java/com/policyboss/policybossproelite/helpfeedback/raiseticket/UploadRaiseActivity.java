package com.policyboss.policybossproelite.helpfeedback.raiseticket;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.policyboss.policybossproelite.BaseActivity;
import com.policyboss.policybossproelite.R;
import com.policyboss.policybossproelite.cropActivity.UcropperActivity;
import com.policyboss.policybossproelite.myaccount.MyAccountActivity;
import com.policyboss.policybossproelite.utility.CircleTransform;
import com.policyboss.policybossproelite.utility.Constants;


import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.Utility;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.APIResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.zoho.ZohoController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.DocumentResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.RaiseTicketCommentResponse;
import okhttp3.MultipartBody;

public class UploadRaiseActivity extends BaseActivity implements BaseActivity.PopUpListener, IResponseSubcriber, View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1800;
    Button btnSubmit;
    EditText etComment;
    ImageView ivUser, ivCross, ivProfile;
    String[] perms = {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"

    };
    File Docfile;
    File file;
    Uri imageUri;
    private Uri cropImageUri;
    InputStream inputStream;
    ExifInterface ei;
    Bitmap bitmapPhoto = null;
    private String PHOTO_File = "FBAPhotograph";
    MultipartBody.Part part;
    HashMap<String, String> body;
    private int PROFILE = 1;
    DBPersistanceController dbPersistanceController;
    LoginResponseEntity loginResponseEntity;
    int RequestID = 0;
    int STATUS_ID = 0;
    String DOC_PATH = "";

    ActivityResultLauncher<String> galleryLauncher;
    ActivityResultLauncher<Uri> cameraLauncher;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_upload_raise);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFinishOnTouchOutside(true);


        dbPersistanceController = new DBPersistanceController(this);
        loginResponseEntity = dbPersistanceController.getUserData();
        registerPopUp(this);
        prefManager = new PrefManager(this);
        initView();
        setOnClickListener();

        if (getIntent().hasExtra("REQ_ID")) {
            RequestID = getIntent().getIntExtra("REQ_ID", 0);
            STATUS_ID  = getIntent().getIntExtra("STATUS_ID", 0);
        }



        // region  Camera and Gallery Launcher
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result ->  {

            Intent intent = new Intent(UploadRaiseActivity.this.getApplicationContext(), UcropperActivity.class);

            intent.putExtra("SendImageData",result.toString());

            startActivityForResult(intent, SELECT_PICTURE);
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                // binding.imgProfile.setImageURI(imageUri);

                Intent intent = new Intent(UploadRaiseActivity.this.getApplicationContext(),UcropperActivity.class);

                intent.putExtra("SendImageData",imageUri.toString());


                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                // Handle failure or cancellation
            }
        });


        //endregion


    }

    private void initView() {
        btnSubmit = findViewById(R.id.btn_submit);
        ivProfile = findViewById(R.id.ivProfile);
        ivUser = findViewById(R.id.ivUser);
        ivCross = findViewById(R.id.ivCross);
        etComment = findViewById(R.id.et_Comment);


    }

    private void setOnClickListener() {
        btnSubmit.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        ivCross.setOnClickListener(this);
    }

    private void galleryCamPopUp() {

        if (!checkPermission()) {

            if (checkRationalePermission()) {
                //Show Information about why you need the permission
                requestPermission();

            } else {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission

                //  permissionAlert(navigationView,"Need Call Permission","This app needs Call permission.");
                openPopUp(ivUser, "Need  Permission", "This app needs all permissions.", "GRANT", true);


            }
        } else {

            showCamerGalleryPopUp();
        }
    }

    // region permission
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

        boolean camera = ActivityCompat.shouldShowRequestPermissionRationale(UploadRaiseActivity.this, perms[0]);

        boolean write_external = ActivityCompat.shouldShowRequestPermissionRationale(UploadRaiseActivity.this, perms[1]);
        boolean read_external = ActivityCompat.shouldShowRequestPermissionRationale(UploadRaiseActivity.this, perms[2]);
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

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_CAMERA_STORACGE_CONSTANT:
                if (grantResults.length > 0) {

                    //boolean writeExternal = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternal = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternal = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

                    if (camera && (writeExternal || minSdk29) && readExternal) {

                        showCamerGalleryPopUp();

                    }


                }
                break;


        }
    }


    private void showCamerGalleryPopUp() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomDialog);

        LinearLayout lyCamera, lyGallery;
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.layout_cam_gallery, null);

        builder.setView(dialogView);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
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


        String FileName = "PHOTO_File";

        Docfile = createImageFile(FileName);


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            imageUri = Uri.fromFile(Docfile);
        } else {
            imageUri = FileProvider.getUriForFile(UploadRaiseActivity.this,
                    getString(R.string.file_provider_authority), Docfile);
        }


//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
//                imageUri);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        cameraLauncher.launch(imageUri);
    }


    private void openGallery() {

        galleryLauncher.launch("image/*");
//        String  mimeType = "image/*";
//
//        Uri collection ;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            collection =  MediaStore.Video.Media.getContentUri(
//                    MediaStore.VOLUME_EXTERNAL
//            );
//        } else {
//            collection =  MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        }
//
//
//        try {
//            Intent intent = new  Intent(Intent.ACTION_PICK, collection);
//
//            intent.setType(mimeType);
//            intent.resolveActivity(getPackageManager());
//            startActivityForResult(intent, SELECT_PICTURE);
//
//        } catch (ActivityNotFoundException ex) {
//            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//        }
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

    @Override
    public void onPositiveButtonClick(Dialog dialog, View view) {
        dialog.cancel();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, Constants.REQUEST_PERMISSION_SETTING);
    }

    @Override
    public void onCancelButtonClick(Dialog dialog, View view) {
        dialog.cancel();
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

    private void startCropImageActivity(Uri imageUri) {
//        CropImage.activity(imageUri)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setMultiTouchEnabled(true)
//                .start(this);
    }

    private void handleCropImage( Uri crop_uri){

        try {

            Bitmap mphoto = null;
            try {
                // mphoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cropImageUri);
                mphoto = getBitmapFromContentResolver(crop_uri);
                //  mphoto = getResizedBitmap(mphoto, 800);


            } catch (Exception e) {
                e.printStackTrace();
            }
            showDialog();
            file = saveImageToStorage(mphoto, PHOTO_File);
            setProfilePhoto(mphoto);
            part = Utility.getMultipartImage(file);
            body = Utility.getBody(this, loginResponseEntity.getFBAId(), PROFILE, PHOTO_File, loginResponseEntity.getPOSPNo(),prefManager.getAppVersion(),prefManager.getDeviceID() );

            new ZohoController(this).uploadDocuments(part, body, this);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


        @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == CAMERA_REQUEST && resultCode ==101 ){

                String result = data.getStringExtra("CROP");
                Uri crop_uri = data.getData();

                if(result!= null){
                    crop_uri = Uri.parse(result);

                }


                handleCropImage(crop_uri);



            }
            else if(requestCode== SELECT_PICTURE && resultCode ==101 ){

                String result = data.getStringExtra("CROP");
                Uri crop_uri = data.getData();

                if(result!= null){
                    crop_uri = Uri.parse(result);
                }

                handleCropImage(crop_uri);


            }


            // Below For Cropping The Camera Image
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            //extractTextFromImage();
//            startCropImageActivity(imageUri);
//        }
//        // Below For Cropping The Gallery Image
//        else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
//            Uri selectedImageUri = data.getData();
//            startCropImageActivity(selectedImageUri);
//        }
//
//        //region Below  handle result of CropImageActivity
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if (resultCode == RESULT_OK) {
//                try {
//                    cropImageUri = result.getUri();
//                    Bitmap mphoto = null;
//                    try {
//                       // mphoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cropImageUri);
//                        mphoto = getBitmapFromContentResolver(cropImageUri);
//                      //  mphoto = getResizedBitmap(mphoto, 800);
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    showDialog();
//                    file = saveImageToStorage(mphoto, PHOTO_File);
//                    setProfilePhoto(mphoto);
//                    part = Utility.getMultipartImage(file);
//                    body = Utility.getBody(this, loginResponseEntity.getFBAId(), PROFILE, PHOTO_File);
//
//                    new ZohoController(this).uploadDocuments(part, body, this);
//
//                } catch (Exception e) {
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
//            }
//        }

        //endregion


    }

    private void setProfilePhoto(Bitmap mphoto) {

        bitmapPhoto = mphoto;        //original

    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }

    private void setDocumentUpload(String URL) {

        DOC_PATH = URL;
        Glide.with(UploadRaiseActivity.this)
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.circle_placeholder)
                .skipMemoryCache(true)
                .override(120, 120)
                .transform(new CircleTransform(UploadRaiseActivity.this)) // applying the image transformer
                .into(ivUser);

    }


    @Override
    public void OnSuccess(APIResponse response, String message) {
        cancelDialog();
        if (response instanceof DocumentResponse) {
            if (response.getStatusNo() == 0) {

                setDocumentUpload(((DocumentResponse) response).getMasterData().get(0).getPrv_file());


            }
        }else if(response instanceof RaiseTicketCommentResponse)
        {
            Toast.makeText(this,  ((RaiseTicketCommentResponse)response).getMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            setResult(2, intent);
            finish();      //finishi
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        DOC_PATH = "";
        cancelDialog();
        Toast.makeText(this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:

                if (!isEmpty(etComment)) {
                    etComment.requestFocus();
                    etComment.setError("Enter Comment");
                    return ;
                }
                showDialog();
                new ZohoController(this).saveCommentOfTickets(String.valueOf(RequestID), etComment.getText().toString(), DOC_PATH,String.valueOf(STATUS_ID), this);


                break;

            case R.id.ivProfile:

                galleryCamPopUp();
                break;

            case R.id.ivCross:
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        galleryLauncher.unregister();
        cameraLauncher.unregister();

    }
}
