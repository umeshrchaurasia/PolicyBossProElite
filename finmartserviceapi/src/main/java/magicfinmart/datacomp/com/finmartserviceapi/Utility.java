package magicfinmart.datacomp.com.finmartserviceapi;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.tracking.TrackingController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.ConstantEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.LoginResponseEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.model.TrackingData;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.TrackingRequestEntity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Nilesh Birhade on 11-01-2018.
 */

public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
   /* public static final String HORIZON_URL = "http://qa-horizon.policyboss.com:3000";
    public static final String QUOTE_BASE_URL = "http://qa.policyboss.com/";
    public static final String SECRET_KEY = "SECRET-ODARQ6JP-9V2Q-7BIM-0NNM-DNRTXRWMRTAL";
    public static final String CLIENT_KEY = "CLIENT-GLF2SRA5-CFIF-4X2T-HC1Z-CXV4ZWQTFQ3T";
    public static final int CLIENT_ID = 4;*/

    //public static final String HORIZON_URL = "https://horizon.policyboss.com:5443";
    //public static final String QUOTE_BASE_URL = "http://www.policyboss.com/";
    //public static final String HORIZON_URL = "http://qa-horizon.policyboss.com:3000";
    //public static final String QUOTE_BASE_URL = "http://qa.policyboss.com/";
    public static final String SECRET_KEY = "SECRET-VG9N6EVV-MIK3-1GFC-ZRBV-PE7XIQ8DV4GY";
    public static final String CLIENT_KEY = "CLIENT-WF4GWODI-HMEB-Q7M6-CLES-DEJCRF7XLRVI";
    public static final int CLIENT_ID = 3;
    public static final int LEAD_REQUEST_CODE = 22;

    public static final String VERSION_CODE = "2.0";
    public static final String RELEASE_DATE = "22/08/2018";
    public static final String BIKEQUOTE_UNIQUEID = "bike_quote_uniqueid";
    public static final String CARQUOTE_UNIQUEID = "car_quote_uniqueid";
    public static final String QUOTE_COUNTER = "quote_counter";
    public static final String SHARED_PREFERENCE_POLICYBOSS = "shared_finmart";
    public static final String HMLOAN_APPLICATION = "hmLoan_Application_LoanApply";
    public static final String PLLOAN_APPLICATION = "plLoan_Application_LoanApply";
    public static final String BTLOAN_APPLICATION = "btLoan_Application_LoanApply";
    public static String PUSH_BROADCAST_ACTION = "Finmart_Push_BroadCast_Action";
    public static String PUSH_NOTIFY = "notifyFlag";
    public static String PUSH_LOGIN_PAGE = "pushloginPage";
    public static String FOS_INFOMATION = "fosinfomation";

    public static String USER_PROFILE_ACTION = "Finmart_User_Profile_Action";

    public static String USER_DASHBOARD = "user_dashboard";
    public static String ULTRA_LAKSHYA_HDR_NAME = "ultra_lakshya_hdr_name";

    public static int OFFLINE_REQUEST_CODE = 10;
    public static String MY_BUSISNESS= "mybuisness";
    public static String MY_BUSISNESS_HDR= "mybuisnessheader";
    public static String MY_BUSISNESS_type= "mybuisnesstype";

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCE_POLICYBOSS, MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPreferenceEditor(Context context) {
        return getSharedPreference(context).edit();
    }

    public static String getCurrentMobileDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static MultipartBody.Part  getMultipartImage(File file) {
        RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imgFile = MultipartBody.Part.createFormData("DocFile", file.getName(), imgBody);
        return imgFile;
    }

    public static MultipartBody.Part getMultipartImage(File file,String serverkey) {
        RequestBody imgBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imgFile = MultipartBody.Part.createFormData(serverkey, file.getName(), imgBody);
        return imgFile;
    }

    public static MultipartBody.Part  getMultipartPdf(File file,String fileName,String serverkey) {
        RequestBody imgBody = RequestBody.create(MediaType.parse("file/*"), file);
        MultipartBody.Part imgFile = MultipartBody.Part.createFormData(serverkey,file.getName(), imgBody);
        //MediaType.parse(getContentResolver().getType(fileUri)),
        return imgFile;
    }

    public static MultipartBody.Part  getMultipartPdfUsingURI(Context context,File file, Uri fileUri, String serverkey) {
        RequestBody imgBody =  RequestBody.create(
                MediaType.parse(context.getContentResolver().getType(fileUri)),
                file
        );
        MultipartBody.Part imgFile = MultipartBody.Part.createFormData(serverkey, "RaiseTicket", imgBody);

        return imgFile;
    }
    public static MultipartBody.Part getMultipartVideo(File file) {
        RequestBody imgBody = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part imgFile = MultipartBody.Part.createFormData("video", file.getName(), imgBody);
        return imgFile;
    }

//    public static HashMap<String, String> getBody(Context context,int FbaID,String DocTyp, String docName, String DocExt) {
//        HashMap<String, String> body = new HashMap<String, String>();
//
//
//        body.put("FBAID", String.valueOf(FbaID) );
//        body.put("DocType",DocTyp);
//        body.put("DocName", docName);
//        body.put("DocExt",DocExt);
//
//        return body;
//    }


    public static HashMap<String, String> getBody(Context context, int FbaID, int DocTyp, String DocName) {
        HashMap<String, String> body = new HashMap<String, String>();


        body.put("FBAID", String.valueOf(FbaID));
        body.put("DocType", String.valueOf(DocTyp));
        body.put("DocName", DocName);


        return body;
    }

    public static HashMap<String, String> getNCDBody(Context context, String GUID, int DocTyp) {
        HashMap<String, String> body = new HashMap<String, String>();


        body.put("guid", String.valueOf(GUID));
        body.put("DocType",String.valueOf(DocTyp));



        return body;
    }
    public static String getNewFileName(String name)  {
        String timeStamp  =new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return name + timeStamp + ".jpg";
    }
    public static String getPdfFileName(String name)  {
        String timeStamp  =new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return name + timeStamp + ".pdf";
    }

    public static String  getImageDirectoryPath() {
        return Environment.DIRECTORY_PICTURES + File.separator + "PolicyBossProElite";
    }

    public static File createDirIfNotExists() {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), "/PolicyBossProElite");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return file;
    }

    public static File createShareDirIfNotExists() {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), "/PolicyBossProElite/QUOTES");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Quotes folder");
                ret = false;
            }
        }
        return file;
    }

    public static int checkShareStatus(Context context) {
        int pospStatus;
        /*DBPersistanceController dbPersistanceController = new DBPersistanceController(context);
        UserConstantEntity userConstantEntity = dbPersistanceController.getUserConstantsData();

        if (userConstantEntity != null) {
            pospStatus = Integer.parseInt(userConstantEntity.getPOSP_STATUS());
            if (pospStatus == 6)
                return 1;
        }*/
        return 1;
    }

    public static int checkPospTrainingStatus(Context context) {
        int pospStatus;
        DBPersistanceController dbPersistanceController = new DBPersistanceController(context);
        ConstantEntity constantEntity = dbPersistanceController.getConstantsData();

        if (constantEntity != null) {
            pospStatus = Integer.parseInt(constantEntity.getPOSPTraining());
            if (pospStatus == 1)
                return 1;
        }
        return 0;
    }

    public static String getLocalIpAddress(Context context) {
        String IPaddress;


        return "";
    }



    public static String getinfoaddress(Context context) throws IOException {

        String address = "";



        //Toast.makeText(context, "" + address, Toast.LENGTH_SHORT).show();
        return address;
    }



    public static String getVersionName(Context context) {
        String versionName = "";
        PackageInfo pinfo;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageInfo pinfo;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getDeviceId(Context context) {
        String deviceId = "";
        if (context != null)
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public static String getTokenId(Context context) {
        PrefManager prefManager = new PrefManager(context);
        if (prefManager != null)
            return prefManager.getToken();
        return "";
    }

    public static String getMotorUrl(Context context, String Service_Log_Unique_Id) {
        //new TrackingController(context).sendData(new TrackingRequestEntity(new TrackingData("Motor buy : buy button for motor"), "MOTOR INSURANCE"), null);
        LoginResponseEntity loginResponseEntity = new DBPersistanceController(context).getUserData();
        String ssid = "5";
        if (loginResponseEntity != null && loginResponseEntity.getPOSPNo() != null && !loginResponseEntity.getPOSPNo().equals(""))
            ssid = loginResponseEntity.getPOSPNo();
        String url = BuildConfig.PROPOSAL_BASE_URL;

        //Changed by 4/4/2019 Umesh C.
       // https://www.policyboss.com/car-insurance/buynow/2/ARN-CSV3M0BR-FV9L-PIN1-1WVQ-HZMQJGEKHRRJ_10681167_621570/NonPOSP/0
        //url = url + "buynowprivatecar/" + Utility.CLIENT_ID + "/" + Service_Log_Unique_Id + "/posp/" + ssid;

        url = url + "car-insurance/buynow/" + Utility.CLIENT_ID + "/" + Service_Log_Unique_Id + "/posp/" + ssid;
        return url;
    }

    public static String getTwoWheelerUrl(Context context, String Service_Log_Unique_Id) {
      //  new TrackingController(context).sendData(new TrackingRequestEntity(new TrackingData("TW buy : buy button for TW"), "TWO WHEELER INSURANCE"), null);
        LoginResponseEntity loginResponseEntity = new DBPersistanceController(context).getUserData();
        String ssid = "5";
        if (loginResponseEntity != null && loginResponseEntity.getPOSPNo() != null && !loginResponseEntity.getPOSPNo().equals(""))
            ssid = loginResponseEntity.getPOSPNo();
        String url = BuildConfig.PROPOSAL_BASE_URL;
        //https://www.policyboss.com/two-wheeler-insurance/buynow/2/ARN-RR6SNRE0-ZQQW-VHKX-BPQ1-Q24ICBRLG5T9_10680516_621536/NonPOSP/0
       // url = url + "buynowTwoWheeler/" + Utility.CLIENT_ID + "/" + Service_Log_Unique_Id + "/posp/" + ssid;

        url = url + "two-wheeler-insurance/buynow/" + Utility.CLIENT_ID + "/" + Service_Log_Unique_Id + "/posp/" + ssid;
        return url;
    }

    public static String getInsurerImage(String insId) {
        int ID = 0;
        if (insId != null && !insId.equals(""))
            ID = Integer.parseInt(insId);
        return "https://horizon.policyboss.com:5443/InsurerImages/car_" + ID + ".png";
    }

    public static void loadWebViewUrlInBrowser(Context context, String url) {
        Log.d("URL", url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        if (Uri.parse(url) != null) {
            browserIntent.setData(Uri.parse(url));
        }
        context.startActivity(browserIntent);
    }


    public static void WritePhoneContact(String displayName, String number, Context cntx /*App or Activity Ctx*/) {
        Context contetx = cntx; //Application's context or Activity's context
        String strDisplayName = displayName; // Name of the Person to add
        String strNumber = number; //number of the person to add with the Contact
        System.out.println("NAME> " + strDisplayName + "    NUMBER ====>  " + strNumber);
        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
        int contactIndex = cntProOper.size();//ContactSize

        //Newly Inserted contact
        // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                .build());
        //Mobile number will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, strNumber) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        try {
            // We will do batch operation to insert all above data
            //Contains the output of the app of a ContentProviderOperation.
            //It is sure to have exactly one of uri or count set
            ContentProviderResult[] contentProresult = null;
            contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list

            Toast.makeText(cntx,"Contact Saved Successfully..",Toast.LENGTH_SHORT).show();

        } catch (RemoteException exp) {
            //logs;
        } catch (OperationApplicationException exp) {
            //logs
        }
    }



}
