package magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.login;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestbuilder.LoginRequestBuilder;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestentity.LoginRequestEntity;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.ChangePasswordResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.CheckLoginResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.ForgotResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.LoginResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.PospAgentResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.ReferFriendResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.SalesMaterialProductResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.UsersignupResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajeev Ranjan on 25/01/2018.
 */

public class LoginController implements ILogin {

    LoginRequestBuilder.LoginNetworkService loginNetworkService;
    Context mContext;
    DBPersistanceController dbPersistanceController;
    PrefManager prefManager;

    public LoginController(Context context) {
        loginNetworkService = new LoginRequestBuilder().getService();
        mContext = context;
        dbPersistanceController = new DBPersistanceController(mContext);
        prefManager = new PrefManager(mContext);
    }

    @Override
    public void checkLoginSwitchUser(LoginRequestEntity loginRequestEntity, final IResponseSubcriber iResponseSubcriber) {

        loginNetworkService.chklogin(loginRequestEntity).enqueue(new Callback<CheckLoginResponse>() {
            @Override
            public void onResponse(Call<CheckLoginResponse> call, Response<CheckLoginResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatusNo() == 0) {

                      //  dbPersistanceController.storeUserData(response.body().getMasterData());
                        iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Failed to fetch information."));
                }
            }

            @Override
            public void onFailure(Call<CheckLoginResponse> call, Throwable t) {
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
    public void login(LoginRequestEntity loginRequestEntity, final IResponseSubcriber iResponseSubcriber) {

        loginNetworkService.login(loginRequestEntity).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatusNo() == 0) {

                        dbPersistanceController.storeUserData(response.body().getMasterData());
                        iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Failed to fetch information."));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
    public void forgotPassword(String emailID, final IResponseSubcriber iResponseSubcriber) {

        HashMap<String, String> body = new HashMap<>();
        body.put("EmailID", emailID);
        body.put("app_version", prefManager.getAppVersion());
        body.put("ssid", "");
        body.put("fbaid", "");
        body.put("device_code", prefManager.getDeviceID());
        loginNetworkService.forgotPassword(body).enqueue(new Callback<ForgotResponse>() {
            @Override
            public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                if (response != null) {
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
            public void onFailure(Call<ForgotResponse> call, Throwable t) {
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
    public void changePassword(String oldPass, String newPass, final IResponseSubcriber iResponseSubcriber) {
        HashMap<String, String> body = new HashMap<>();
        body.put("FBAID", String.valueOf(new DBPersistanceController(mContext).getUserData().getFBAId()));
        body.put("Old_Password", oldPass);
        body.put("New_Password", newPass);

        loginNetworkService.changePassword(body).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response != null) {
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
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
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
    public void referFriend(String refType, String refCode, final IResponseSubcriber iResponseSubcriber) {
        HashMap<String, String> body = new HashMap<>();
        body.put("ref_type", refType);
        body.put("ref_code", refCode);

        loginNetworkService.referFriend(body).enqueue(new Callback<ReferFriendResponse>() {
            @Override
            public void onResponse(Call<ReferFriendResponse> call, Response<ReferFriendResponse> response) {

                if (response != null && response.body() != null) {
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                    /*if (response.body().getStatusNo() == 0) {
                        iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }*/
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Failed to fetch information."));
                }
            }

            @Override
            public void onFailure(Call<ReferFriendResponse> call, Throwable t) {
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
    public void getPospAgentData(String UID, String Name, final IResponseSubcriber iResponseSubcriber) {

        HashMap<String, String> body = new HashMap<>();
        body.put("UID", UID);
        body.put("Name", Name);

        loginNetworkService.getPospAgentData(body).enqueue(new Callback<PospAgentResponse>() {
            @Override
            public void onResponse(Call<PospAgentResponse> call, Response<PospAgentResponse> response) {
                if (response.body() != null) {

                    //callback of data
                    iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                } else {
                    //failure
                    iResponseSubcriber.OnFailure(new RuntimeException("Enable to reach server, Try again later"));
                }
            }

            @Override
            public void onFailure(Call<PospAgentResponse> call, Throwable t) {
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
    public void Getusersignup(final IResponseSubcriber iResponseSubcriber) {

        HashMap<String, String> body = new HashMap<>();
        body.put("app_version", prefManager.getAppVersion());
        body.put("ssid", "");
        body.put("fbaid", "");
        body.put("device_code", prefManager.getDeviceID());

        loginNetworkService.getusersignup(body).enqueue(new Callback<UsersignupResponse>() {
            @Override
            public void onResponse(Call<UsersignupResponse> call, Response<UsersignupResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatusNo() == 0) {

                        iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());

                    } else {
                        iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                      //  iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                } else {
                    iResponseSubcriber.OnSuccess(null, "Failed to fetch information.");
                   // iResponseSubcriber.OnFailure(new RuntimeException("Failed to fetch information."));
                }

            }

            @Override
            public void onFailure(Call<UsersignupResponse> call, Throwable t) {

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
