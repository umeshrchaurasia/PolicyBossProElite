package magicfinmart.datacomp.com.finmartserviceapi.finmart.retrobuilder


import android.content.SyncContext
import com.google.gson.GsonBuilder
import magicfinmart.datacomp.com.finmartserviceapi.BuildConfig
import magicfinmart.datacomp.com.finmartserviceapi.finmart.requestbuilder.Syncontact.SyncContactInterface
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object   RetroHelper {

    private val retrofit by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val gson = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .create()

        val okHttpClient = okhttp3.OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)

            .build()


        Retrofit.Builder()
            .baseUrl(BuildConfig.FINMART_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    val api : SyncContactInterface by lazy {

        retrofit.create(SyncContactInterface::class.java )
    }


}