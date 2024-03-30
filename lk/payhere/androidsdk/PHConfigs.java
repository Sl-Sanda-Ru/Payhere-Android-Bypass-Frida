package lk.payhere.androidsdk;

import java.util.HashMap;
import lk.payhere.androidsdk.api.PayhereSDK;
import lk.payhere.androidsdk.api.ServiceGenerator;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* loaded from: classes7.dex */
public class PHConfigs {
    public static String BASE_URL = null;
    public static final String CHECKOUT = "checkout";
    public static final String LIVE_URL = "https://www.payhere.lk/pay/";
    public static final String LOCAL_URL = "http://localhost:8080//pay/";
    public static final String SANDBOX_URL = "https://sandbox.payhere.lk/pay/";
    public static final String STATUS = "order_status";
    private static OnMethodReceivedListener listener;

    /* loaded from: classes7.dex */
    public interface OnMethodReceivedListener {
        void onMethodReturned(HashMap<String, PaymentMethodResponse.Data> hashMap);
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void readMethods(PHMainActivity pHMainActivity) {
        ((PayhereSDK) ServiceGenerator.createService(pHMainActivity, PayhereSDK.class)).getPaymentMethods().enqueue(new Callback<PaymentMethodResponse>() { // from class: lk.payhere.androidsdk.PHConfigs.1
            @Override // retrofit2.Callback
            public void onFailure(Call<PaymentMethodResponse> call, Throwable th) {
            }

            @Override // retrofit2.Callback
            public void onResponse(Call<PaymentMethodResponse> call, Response<PaymentMethodResponse> response) {
                PaymentMethodResponse body = response.body();
                if (body != null && body.getStatus() == 1 && PHConfigs.listener != null) {
                    PHConfigs.listener.onMethodReturned(body.getData());
                }
            }
        });
    }

    public static void setBaseUrl(String str) {
        BASE_URL = str;
    }

    public static void setOnMethodReceivedListener(OnMethodReceivedListener onMethodReceivedListener) {
        listener = onMethodReceivedListener;
    }
}
