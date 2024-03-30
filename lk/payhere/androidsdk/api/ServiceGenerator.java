package lk.payhere.androidsdk.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lk.payhere.androidsdk.PHConfigs;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/* loaded from: classes7.dex */
public class ServiceGenerator {
    private static String BASE_URL = "https://www.payhere.lk/pay/";
    private static OkHttpClient okHttpClient;
    private static Map<String, Object> clients = new HashMap();
    private static String accessToken = null;

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean checkNetworkAvailability(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static <T> T createService(Context context, Class<T> cls) {
        T t = (T) clients.get(getKey(cls));
        if (t == null) {
            T t2 = (T) initService(context, cls, null);
            clients.put(getKey(cls), t2);
            return t2;
        }
        return t;
    }

    private static <T> String getKey(Class<T> cls) {
        return cls.getSimpleName();
    }

    private static void initHttpClient(final Context context, String str) {
        if (okHttpClient != null) {
            if (str != null) {
                String str2 = accessToken;
                if (str2 != null && str2.equals(str)) {
                    return;
                }
            } else {
                return;
            }
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        CustomInterceptor customInterceptor = new CustomInterceptor(context) { // from class: lk.payhere.androidsdk.api.ServiceGenerator.1
            @Override // lk.payhere.androidsdk.api.CustomInterceptor
            public boolean isInternetAvailable() {
                return ServiceGenerator.checkNetworkAvailability(context);
            }

            @Override // lk.payhere.androidsdk.api.CustomInterceptor
            public void onInternetUnavailable() {
            }
        };
        accessToken = str;
        if (str == null) {
            builder.addInterceptor(customInterceptor);
        } else {
            builder.addInterceptor(customInterceptor.setAccessToken(str));
        }
        TimeUnit timeUnit = TimeUnit.SECONDS;
        builder.connectTimeout(30L, timeUnit);
        builder.readTimeout(30L, timeUnit);
        okHttpClient = builder.build();
    }

    private static <T> T initService(Context context, Class<T> cls, String str) {
        String str2 = PHConfigs.BASE_URL;
        if (str2 != null && !str2.equals("")) {
            BASE_URL = PHConfigs.BASE_URL;
        }
        initHttpClient(context, str);
        Gson create = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        Retrofit.Builder baseUrl = new Retrofit.Builder().baseUrl(BASE_URL);
        baseUrl.addConverterFactory(ScalarsConverterFactory.create());
        baseUrl.addConverterFactory(GsonConverterFactory.create(create));
        T t = (T) baseUrl.client(okHttpClient).build().create(cls);
        clients.put(getKey(cls), t);
        return t;
    }

    public static <T> T createService(Context context, Class<T> cls, String str) {
        T t = (T) initService(context, cls, str);
        clients.put(getKey(cls), t);
        return t;
    }
}
