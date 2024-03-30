package lk.payhere.androidsdk.api;

import android.content.Context;
import com.google.firebase.crashlytics.internal.settings.DefaultSettingsSpiCall;
import java.io.IOException;
import lk.payhere.androidsdk.R;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/* loaded from: classes7.dex */
public abstract class CustomInterceptor implements Interceptor {
    private String accessToken;
    private Context context;

    public CustomInterceptor(Context context) {
        this.context = context;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        if (isInternetAvailable()) {
            try {
                return chain.proceed(request.newBuilder().addHeader("Content-Type", DefaultSettingsSpiCall.ACCEPT_JSON_VALUE).build());
            } catch (Exception e) {
                e.printStackTrace();
                return chain.proceed(request.newBuilder().addHeader("Content-Type", DefaultSettingsSpiCall.ACCEPT_JSON_VALUE).build());
            }
        }
        onInternetUnavailable();
        throw new IOException(this.context.getString(R.string.msg_no_internet));
    }

    public abstract boolean isInternetAvailable();

    public abstract void onInternetUnavailable();

    public CustomInterceptor setAccessToken(String str) {
        this.accessToken = str;
        return this;
    }
}
