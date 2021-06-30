package com.ammar.tawseel.netWorke.RX;

import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClintRX {

    APIInterFace apiInterFace;
    private static Retrofit retrofit = null;
    final static String BASE_URL = "https://tawseel.pal-dev.com/";

    public static APIClintRX INSTANCE;

    public APIClintRX() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .connectionSpecs(
                        Arrays.asList(
                                ConnectionSpec.MODERN_TLS,
                                ConnectionSpec.COMPATIBLE_TLS,
                                ConnectionSpec.CLEARTEXT
                        )
                )
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();


        apiInterFace = retrofit.create(APIInterFace.class);


    }

    public static APIClintRX getINSTANCE() {
        if (null == INSTANCE) {
            INSTANCE = new APIClintRX();
        }
        return INSTANCE;
    }

    public static void setINSTANCE(APIClintRX INSTANCE) {
        APIClintRX.INSTANCE = INSTANCE;
    }

    public Observable<APIResponse.ResponseDrivers> getHomeData(String token, String lang, String currency) {
        return apiInterFace.getRXDrivers(token, lang, currency);
    }

//    public Observable<APIResponses.ProductsAllResponse> getViewProducts(String idCato, String token, String lang, String currency, String cunttry) {
//        return apiInterFace.getViewProducts(idCato, token, lang, currency, cunttry);
//    }
}
