package lk.payhere.androidsdk.api;

import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentDetails;
import lk.payhere.androidsdk.model.PaymentInitRequest;
import lk.payhere.androidsdk.model.PaymentInitResult;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/* loaded from: classes7.dex */
public interface PayhereSDK {
    @GET(PHConstants.URL_PAYMENT_METHODS)
    Call<PaymentMethodResponse> getPaymentMethods();

    @GET
    Call<ResponseBody> getUrl(@Url String str);

    @POST(PHConstants.URL_INIT_PAYMENT)
    Call<PaymentInitResult> initPayment(@Body PaymentDetails paymentDetails);

    @POST(PHConstants.URL_INIT_PAYMENT_V2)
    Call<NewInitResponse> initPaymentV2(@Body PaymentInitRequest paymentInitRequest);
}
