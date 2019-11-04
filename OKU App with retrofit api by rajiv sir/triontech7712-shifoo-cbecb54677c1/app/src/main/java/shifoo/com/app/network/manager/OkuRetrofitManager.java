package shifoo.com.app.network.manager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import oku.app.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shifoo.com.app.constants.AppConstants;
import shifoo.com.app.network.services.OkuServices;

public class OkuRetrofitManager {

    private static final int TIME_OUT = 1;
    public static final OkuServices OKU_SERVICE;

    static {

        OkHttpClient.Builder okuCLientBuilder = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(TIME_OUT, TimeUnit.MINUTES)
                .connectTimeout(TIME_OUT, TimeUnit.MINUTES);

        if (BuildConfig.DEBUG) {
            okuCLientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okuCLientBuilder.build()).build();

        OKU_SERVICE = retrofit.create(OkuServices.class);

    }

}
