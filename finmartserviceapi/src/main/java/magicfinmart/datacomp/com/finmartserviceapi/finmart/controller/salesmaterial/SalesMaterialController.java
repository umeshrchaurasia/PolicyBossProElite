package magicfinmart.datacomp.com.finmartserviceapi.finmart.controller.salesmaterial;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import magicfinmart.datacomp.com.finmartserviceapi.PrefManager;
import magicfinmart.datacomp.com.finmartserviceapi.database.DBPersistanceController;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.IResponseSubcriber;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestbuilder.SalesMaterialRequestBuilder;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.SalesMaterialProductResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.SalesPromotionResponse;
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.FestivalCampaignResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nilesh Birhade on 09/02/2018.
 */

public class SalesMaterialController implements ISalesMaterial {

    SalesMaterialRequestBuilder.SalesMaterialNetworkService salesMaterialNetworkService;
    Context mContext;
    DBPersistanceController dbPersistanceController;
    PrefManager prefManager;

    public SalesMaterialController(Context context) {
        salesMaterialNetworkService = new SalesMaterialRequestBuilder().getService();
        mContext = context;
        dbPersistanceController = new DBPersistanceController(mContext);
        prefManager = new PrefManager(mContext);
    }


    @Override
    public void getSalesProducts(final IResponseSubcriber iResponseSubcriber) {
        HashMap<String, String> body = new HashMap<>();
        body.put("app_version", "" + prefManager.getAppVersion());
        body.put("device_code", "" +  prefManager.getDeviceID());
        body.put("ssid", "" + dbPersistanceController.getUserData().getPOSPNo());
        body.put("fbaid", "" + dbPersistanceController.getUserData().getFBAId());
        salesMaterialNetworkService.getSalesProducts(body).enqueue(new Callback<SalesMaterialProductResponse>() {
            @Override
            public void onResponse(Call<SalesMaterialProductResponse> call, Response<SalesMaterialProductResponse> response) {

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
            public void onFailure(Call<SalesMaterialProductResponse> call, Throwable t) {

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
    public void getProductPromotions(int productID, final IResponseSubcriber iResponseSubcriber) {

        HashMap<String, String> body = new HashMap<String, String>();
        body.put("product_id", String.valueOf(productID));
        body.put("app_version", "" + prefManager.getAppVersion());
        body.put("device_code", "" +  prefManager.getDeviceID());
        body.put("ssid", "" + dbPersistanceController.getUserData().getPOSPNo());
        body.put("fbaid", "" + dbPersistanceController.getUserData().getFBAId());

        salesMaterialNetworkService.getProductPromotions(body).enqueue(new Callback<SalesPromotionResponse>() {
            @Override
            public void onResponse(Call<SalesPromotionResponse> call, Response<SalesPromotionResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatusNo() == 0) {
                        dbPersistanceController.storeDocList(response.body().getMasterData().getDocs());
                        iResponseSubcriber.OnSuccess(response.body(), response.body().getMessage());
                    } else {
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException("Failed to fetch information."));
                }

            }

            @Override
            public void onFailure(Call<SalesPromotionResponse> call, Throwable t) {

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
    public void getfestivelink(String FBAID, String LoanId, String Source,final IResponseSubcriber iResponseSubcriber) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("FBAID", FBAID);
        body.put("LoanId",LoanId);
        body.put("Source",Source);

        salesMaterialNetworkService.getfincampaign(body).enqueue(new Callback<FestivalCampaignResponse>() {
            @Override
            public void onResponse(Call<FestivalCampaignResponse> call, Response<FestivalCampaignResponse> response) {

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
            public void onFailure(Call<FestivalCampaignResponse> call, Throwable t) {

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
