package magicfinmart.datacomp.com.finmartserviceapi.finmart.requestbuilder.Syncontact

import com.utility.finmartcontact.core.requestentity.CallLogRequestEntity
import com.utility.finmartcontact.core.requestentity.ContactLeadRequestEntity
import com.utility.finmartcontact.core.response.ContactLogResponse
import magicfinmart.datacomp.com.finmartserviceapi.dynamic_urls.response.HorizonEmpDetailResponse
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.AuthToken.OauthTokenResponse
import magicfinmart.datacomp.com.finmartserviceapi.finmart.response.ContactLeadResponse
import magicfinmart.datacomp.com.finmartserviceapi.finmart.retrobuilder.FinmartRetroRequestBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Rahul on 20/06/2022.
 */
interface SyncContactInterface {

    @POST()
   suspend fun saveContactLead(@Url url: String, @Body body : ContactLeadRequestEntity): Response<ContactLeadResponse>


    @POST()
    suspend fun saveCallLog(@Url url: String, @Body body : CallLogRequestEntity): Response<ContactLogResponse>

    @GET()
    suspend fun getHorizonDetails(@Url url: String): Response<HorizonEmpDetailResponse>


    @POST()
    suspend fun saveCallLogOld(@Url url: String, @Body body : CallLogRequestEntity): Call<ContactLogResponse>



    @POST()
    fun saveContactLeadOld(@Url url: String, @Body body : ContactLeadRequestEntity): Call<ContactLeadResponse>


    @Headers("token:" + FinmartRetroRequestBuilder.token)
    @POST()
    suspend fun saveDeviceDetails1(@Url url: String, @Body body : HashMap<String,String> ): Response<ContactLogResponse>

    @Headers("token:" + FinmartRetroRequestBuilder.token)
    @POST("/app_visitor/save_device_details")
    suspend fun saveDeviceDetails( @Body body : HashMap<String,String> ): Response<ContactLogResponse>


    @Headers("token:" + FinmartRetroRequestBuilder.token)
    @POST("/auth_tokens/generate_web_auth_token")
    suspend fun getOauthToken( @Body body : HashMap<String,String> ): Response<OauthTokenResponse>


}