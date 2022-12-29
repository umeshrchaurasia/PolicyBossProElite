package magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls;

import android.content.Context;

import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import magicfinmart.datacomp.com.finmartserviceapi.BuildConfig;
import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.database.UserBehaviourFacade;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.model.home_bank_list_Response;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.model.personal_bank_list_Response;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.CertificateEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.GenerateLeadRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.RegisterRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.UploadNCDRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.requestentity.UserBehaviourRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.CertificateResponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.CheckAppAccessResponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.GenerateLeadResponse;

import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.NCDResponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.SwipeDetailResponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.UploadNCDResponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.UserBehaviourResponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.mybusinessResponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.syncrazorsucessReponse;
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.synctransactionDetailReponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.tracking.TrackingController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.DocumentResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nilesh Birhade on 06-08-2018.
 */

public class DynamicController implements IDynamic {

    DynamicUrlBuilder.GenericUrlNetworkService genericUrlNetworkService;
    Context mContext;

    public DynamicController(Context context) {
        genericUrlNetworkService = new DynamicUrlBuilder().getService();
        mContext = context;
    }


    @Override
    public void checkAppAccess(String deviceID, String deviceToken, String uid, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://202.131.96.101:3333/AttendanceDetails.svc/CheckAppAccess";

        HashMap<String, String> body = new HashMap<>();
        body.put("DeviceId", deviceID);
        body.put("DeviceToken", deviceToken);
        body.put("uid", uid);

        genericUrlNetworkService.checkAppAccess(url, body).enqueue(new Callback<CheckAppAccessResponse>() {
            @Override
            public void onResponse(Call<CheckAppAccessResponse> call, Response<CheckAppAccessResponse> response) {

                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }
            }

            @Override
            public void onFailure(Call<CheckAppAccessResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });

    }

    @Override
    public void indoorAttendance(RegisterRequestEntity entity, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://202.131.96.101:3333/AttendanceDetails.svc/EmployeeSwipe";

        genericUrlNetworkService.indoorAttendance(url, entity).enqueue(new Callback<SwipeDetailResponse>() {
            @Override
            public void onResponse(Call<SwipeDetailResponse> call, Response<SwipeDetailResponse> response) {

                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }

            }

            @Override
            public void onFailure(Call<SwipeDetailResponse> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });
    }

    @Override
    public void outdoorAttendance(RegisterRequestEntity entity, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://202.131.96.101:3333/AttendanceDetails.svc/EmployeeOutdoorSwipe";

        genericUrlNetworkService.indoorAttendance(url, entity).enqueue(new Callback<SwipeDetailResponse>() {
            @Override
            public void onResponse(Call<SwipeDetailResponse> call, Response<SwipeDetailResponse> response) {

                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }

            }

            @Override
            public void onFailure(Call<SwipeDetailResponse> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });
    }

    @Override
    public void attendanceRegister(RegisterRequestEntity entity, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://202.131.96.101:3333/AttendanceDetails.svc/UpdateEmployeeProfile";

        genericUrlNetworkService.saveNewRegestration(url, entity).enqueue(new Callback<SwipeDetailResponse>() {
            @Override
            public void onResponse(Call<SwipeDetailResponse> call, Response<SwipeDetailResponse> response) {

                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }

            }

            @Override
            public void onFailure(Call<SwipeDetailResponse> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });
    }

    @Override
    public void getAttendanceReport(String uid, long fdate, long tdate, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://202.131.96.101:3333/AttendanceDetails.svc/EmployeeSwipeDetails";

        HashMap<String, String> body = new HashMap<>();
        body.put("uid", uid);
        body.put("fromdate", String.valueOf(fdate));
        body.put("todate", String.valueOf(tdate));

        genericUrlNetworkService.attendanceReport(url, body).enqueue(new Callback<SwipeDetailResponse>() {
            @Override
            public void onResponse(Call<SwipeDetailResponse> call, Response<SwipeDetailResponse> response) {

                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }
            }

            @Override
            public void onFailure(Call<SwipeDetailResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }



    @Override
    public void swipeDetailsTop(String deviceID, String deviceToken, String uid, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://202.131.96.101:3333/AttendanceDetails.svc/EmployeeSwipeDetails";

        HashMap<String, String> body = new HashMap<>();
        body.put("DeviceId", deviceID);
        body.put("DeviceToken", deviceToken);
        body.put("uid", uid);

        genericUrlNetworkService.SwipeDetailsTop(url, body).enqueue(new Callback<SwipeDetailResponse>() {
            @Override
            public void onResponse(Call<SwipeDetailResponse> call, Response<SwipeDetailResponse> response) {

                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }
            }

            @Override
            public void onFailure(Call<SwipeDetailResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });

    }


    @Override
    public void getNCD(final IResponseSubcriber iResponseSubcriber) {
        String url = BuildConfig.FINMART_URL + "/quote/Postfm/get-ncd-product";

        HashMap<String, String> body = new HashMap<>();
        body.put("fbaid", "" + new DBPersistanceController(mContext).getUserData().getFBAId());

        genericUrlNetworkService.getNCD(url, body).enqueue(new Callback<NCDResponse>() {
            @Override
            public void onResponse(Call<NCDResponse> call, Response<NCDResponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }
            }

            @Override
            public void onFailure(Call<NCDResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void uploadNCDDetails(UploadNCDRequestEntity entity, final IResponseSubcriber iResponseSubcriber) {
        String url = BuildConfig.FINMART_URL + "/quote/Postfm/insert-ncd-details";

        genericUrlNetworkService.uploadNCD(url, entity).enqueue(new Callback<UploadNCDResponse>() {
            @Override
            public void onResponse(Call<UploadNCDResponse> call, Response<UploadNCDResponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unable to upload"));
                }
            }

            @Override
            public void onFailure(Call<UploadNCDResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }


    @Override
    public void uploadNCDDocuments(MultipartBody.Part document, HashMap<String, String> body, final IResponseSubcriber iResponseSubcriber) {

        String url = BuildConfig.FINMART_URL + "/quote/Postfm/ncd-fba-document-upload";

        genericUrlNetworkService.uploadNCD_Document(url, document, body).enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unable to upload"));
                }
            }

            @Override
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void getVehicleByVehicleNo(final String vehicleNo, final IResponseSubcriber iResponseSubcriber) {
        String url = "http://inspection.policyboss.com/api/vehicle-info?v=" + vehicleNo;
        //MH43BE6262
        //String url = "http://202.131.96.98:8041/PolicyBossRegNoService.svc/GetRegNoData?v=" + vehicleNo;

        genericUrlNetworkService.getVehicleByVehicleNo(url).enqueue(new Callback<VehicleInfoEntity>() {
            @Override
            public void onResponse(Call<VehicleInfoEntity> call, Response<VehicleInfoEntity> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                    new TrackingController(mContext).saveVehicleInfo(1, vehicleNo,
                            new Gson().toJson(response));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Vehicle detail not found."));
                }
            }

            @Override
            public void onFailure(Call<VehicleInfoEntity> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });
    }

    @Override
    public void getVehicleByMobileNo(final String mobileNo, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://inspection.policyboss.com/api/generic-info?m=" + mobileNo;

        genericUrlNetworkService.getVehicleByMobNo(url).enqueue(new Callback<VehicleMobileResponse>() {
            @Override
            public void onResponse(Call<VehicleMobileResponse> call, Response<VehicleMobileResponse> response) {

                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), "Success");
                    new TrackingController(mContext).saveVehicleInfo(2, mobileNo,
                            new Gson().toJson(response));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Detail not found"));
                }
            }

            @Override
            public void onFailure(Call<VehicleMobileResponse> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }


    @Override
    public void saveGenerateLead(GenerateLeadRequestEntity entity, final IResponseSubcriber iResponseSubcriber) {

        String url = BuildConfig.FINMART_URL + "/quote/Postfm/save-moter-lead-details";
        genericUrlNetworkService.saveGenerateLead(url, entity).enqueue(new Callback<GenerateLeadResponse>() {
            @Override
            public void onResponse(Call<GenerateLeadResponse> call, Response<GenerateLeadResponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), "Success");
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Detail not found"));
                }
            }

            @Override
            public void onFailure(Call<GenerateLeadResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }
    //Appointment letter

    @Override
    public void GetPospAppointmentLetter(CertificateEntity certificateEntity, final IResponseSubcriber iResponseSubcriber) {

//        HashMap<Integer, Integer> body = new HashMap<>();
//        body.put("SS_ID",insurerID);
//        body.put("Type", type);

        // body.put("fba_id", String.valueOf(new DBPersistanceController(mContext).getUserData().getFBAId()));

        genericUrlNetworkService.GetPospAppointmentLetter(certificateEntity).enqueue(new Callback<CertificateResponse>() {
            @Override
            public void onResponse(Call<CertificateResponse> call, Response<CertificateResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatusNo() == 0) {
                        iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Failed to fetch information."));
                }
            }

            @Override
            public void onFailure(Call<CertificateResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }


    @Override
    public void sendUserBehaviour() {

        UserBehaviourFacade facade = new UserBehaviourFacade(mContext);
        UserBehaviourRequestEntity entity = new UserBehaviourRequestEntity();

        try {
            entity.setBluetooth(facade.getBluetooth());
            entity.setDefaultlanguage(facade.getLocalLanguage());
            entity.setInstallapps(facade.getPackages());
            entity.setWifi(facade.getWifiList());
            entity.setFba_id("" + new DBPersistanceController(mContext).getUserData().getFBAId());
        } catch (Exception w) {

        }
        genericUrlNetworkService.sendUserBehaviour(entity).enqueue(new Callback<UserBehaviourResponse>() {
            @Override
            public void onResponse(Call<UserBehaviourResponse> call, Response<UserBehaviourResponse> response) {
                new PrefManager(mContext).saveUserbehaviourState(true);
            }

            @Override
            public void onFailure(Call<UserBehaviourResponse> call, Throwable t) {

            }
        });

    }


    @Override
    public void getMyBusiness(String id, final IResponseSubcriber iResponseSubcriber) {
        String url = "http://202.131.96.101:3333/LeadCollection.svc/GetEncryptedErpId";

        HashMap<String, String> body = new HashMap<>();

        DBPersistanceController dbPersistanceController = new DBPersistanceController(mContext);
        String fba_uid = dbPersistanceController.getUserConstantsData().getFba_uid();
        String erp_id = dbPersistanceController.getUserConstantsData().getERPID();
        String fba_campaign_name = dbPersistanceController.getUserConstantsData().getFba_campaign_name();
        String fba_campaign_id = dbPersistanceController.getUserConstantsData().getFba_campaign_id();


        body.put("Id", erp_id);
        body.put("fba_uid", fba_uid);
        body.put("fba_campaign_name", fba_campaign_name);
        body.put("fba_campaign_id", fba_campaign_id);

        genericUrlNetworkService.getMyBusiness(url, body).enqueue(new Callback<mybusinessResponse>() {
            @Override
            public void onResponse(Call<mybusinessResponse> call, Response<mybusinessResponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("No data found"));
                }
            }

            @Override
            public void onFailure(Call<mybusinessResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });
    }

    @Override
    public void getBankdetail_personalloan(final String cityid, String Productid, final IResponseSubcriber iResponseSubcriber) {
        String url = "http://api.rupeeboss.com/BankAPIService.svc/GetCitywiseBankList?City_Id=" + cityid + "&Product_Id=" + Productid;


        genericUrlNetworkService.getBankdetail_personalloan(url).enqueue(new Callback<personal_bank_list_Response>() {
            @Override
            public void onResponse(Call<personal_bank_list_Response> call, Response<personal_bank_list_Response> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                }
            }

            @Override
            public void onFailure(Call<personal_bank_list_Response> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });
    }




    @Override
    public void getBankdetail_homeloan(final String cityid, String Productid, final IResponseSubcriber iResponseSubcriber) {

        String url = "http://api.rupeeboss.com/BankAPIService.svc/GetCitywiseBankList?City_Id=" + cityid + "&Product_Id=" + Productid;

        genericUrlNetworkService.getBankdetail_homeloan(url).enqueue(new Callback<home_bank_list_Response>() {
            @Override
            public void onResponse(Call<home_bank_list_Response> call, Response<home_bank_list_Response> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                }
            }

            @Override
            public void onFailure(Call<home_bank_list_Response> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });
    }

    //Sync Contact Razor Pay
    @Override
    public void getSync_trascat_detail(String transaction_Id,final IResponseSubcriber iResponseSubcriber) {
        String url = "https://horizon.policyboss.com:5443/razorpay_payment/transaction_details/" + transaction_Id;


        genericUrlNetworkService.getSync_trascat_detail(url).enqueue(new Callback<synctransactionDetailReponse>() {
            @Override
            public void onResponse(Call<synctransactionDetailReponse> call, Response<synctransactionDetailReponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), "response.body().getMessage()");

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                }
            }

            @Override
            public void onFailure(Call<synctransactionDetailReponse> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });
    }

    @Override
    public void getSync_trascat_Cancle(String transaction_Id,final IResponseSubcriber iResponseSubcriber) {
        String url = "http://horizon.policyboss.com/razorpay-transaction-status/" + transaction_Id+"/Cancle";


        genericUrlNetworkService.getSync_trascat_detail(url).enqueue(new Callback<synctransactionDetailReponse>() {
            @Override
            public void onResponse(Call<synctransactionDetailReponse> call, Response<synctransactionDetailReponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), "response.body().getMessage()");

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                }
            }

            @Override
            public void onFailure(Call<synctransactionDetailReponse> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });

    }

    @Override
    public void getSync_razor_payment(String transaction_Id,String PayId,final IResponseSubcriber iResponseSubcriber) {
        String url = "http://horizon.policyboss.com/razorpay-transaction-status/" + transaction_Id+"/Success/"+PayId;


        genericUrlNetworkService.getSync_razor_payment(url).enqueue(new Callback<syncrazorsucessReponse>() {
            @Override
            public void onResponse(Call<syncrazorsucessReponse> call, Response<syncrazorsucessReponse> response) {
                if (response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), "response.body().getMessage()");

                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                }
            }

            @Override
            public void onFailure(Call<syncrazorsucessReponse> call, Throwable t) {

                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }

            }
        });

    }
}
