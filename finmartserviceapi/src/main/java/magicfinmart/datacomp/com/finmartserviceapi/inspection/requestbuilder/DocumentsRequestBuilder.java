package magicfinmart.datacomp.com.finmartserviceapi.inspection.requestbuilder;

import java.util.HashMap;

import magicfinmart.datacomp.com.finmartserviceapi.inspection.InspectionRetroRequestBuilder;
import magicfinmart.datacomp.com.finmartserviceapi.inspection.entity.VehSelfDeclarationEntity;
import magicfinmart.datacomp.com.finmartserviceapi.inspection.response.DocumentResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by Nilesh Birhade on 14-12-2017.
 */

public class DocumentsRequestBuilder extends InspectionRetroRequestBuilder {
    //sub url
    public static final String sub_url = "/api/";


    public DocumentsNetworkService getService() {

        return super.build().create(DocumentsNetworkService.class);
    }

    public interface DocumentsNetworkService {

        @Multipart
        @POST(SUB_URL + "/vehicle-documents")
        Call<DocumentResponse> uploadDocument(@Part() MultipartBody.Part doc, @QueryMap HashMap<String, String> body);

        @Multipart
        @POST(SUB_URL + "/vehicle-video-documents")
        Call<DocumentResponse> uploadVideo(@Part() MultipartBody.Part doc, @QueryMap HashMap<String, String> body);

        @POST(SUB_URL + "/vehicle-inspection-details")
        Call<DocumentResponse> selfDeclaration(@Body VehSelfDeclarationEntity selfDeclarationEntity);

    }
}
