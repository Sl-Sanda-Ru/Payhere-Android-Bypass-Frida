package lk.payhere.androidsdk.fragment;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.inappmessaging.display.FirebaseInAppMessagingDisplay;
import com.google.gson.GsonBuilder;
import com.google.logging.type.LogSeverity;
import java.util.HashMap;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.R;
import lk.payhere.androidsdk.api.PayhereSDK;
import lk.payhere.androidsdk.api.ServiceGenerator;
import lk.payhere.androidsdk.model.InitBaseRequest;
import lk.payhere.androidsdk.model.InitPreapprovalRequest;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentDetails;
import lk.payhere.androidsdk.model.PaymentInitResult;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import lk.payhere.androidsdk.model.StatusResponse;
import lk.payhere.androidsdk.util.NetworkHandler;
import lk.payhere.androidsdk.util.ObservableWebView;
import lk.payhere.androidsdk.util.Utils;
import lk.payhere.androidsdk.util.WebViewBottomSheetbehaviour;
import org.spongycastle.asn1.cmp.PKIFailureInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* loaded from: classes7.dex */
public class PaymentDetailFragment extends Fragment implements PHMainActivity.OnActivityAction {
    private PHMainActivity activity;
    private NewInitResponse.Submission hela_pay;
    private int initHeight;
    private StatusResponse lastResponse;
    private String method;
    private String orderKey;
    private View progressFrame;
    private ImageView progressImage;
    private InitBaseRequest request;
    private ObservableWebView webView;
    private String TAG = "PaymentDetailFragment";
    private Handler handlerForJavascriptInterface = new Handler();
    private boolean dataLoading = false;
    private boolean isCardSave = false;

    /* loaded from: classes7.dex */
    public class MyJavaScriptInterface {
        private Context ctx;

        public MyJavaScriptInterface(Context context) {
            this.ctx = context;
        }

        @JavascriptInterface
        public void showHTML(final String str) {
            PaymentDetailFragment.this.handlerForJavascriptInterface.post(new Runnable() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.MyJavaScriptInterface.1
                @Override // java.lang.Runnable
                public void run() {
                    int indexOf = str.indexOf("reference_id");
                    if (indexOf >= 0) {
                        int indexOf2 = str.indexOf("value=\"", indexOf) + 7;
                        String str2 = str;
                        String substring = str2.substring(indexOf2, str2.indexOf("\"", indexOf2));
                        PaymentDetailFragment.this.orderKey = substring;
                        new StatusChecker(false, false, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, substring);
                    }
                }
            });
        }
    }

    /* loaded from: classes7.dex */
    public class StatusChecker extends AsyncTask<String, Void, StatusResponse> {
        private boolean background;
        private final boolean isHelapayPayment;
        private boolean postActivityFinish;

        public StatusChecker(boolean z, boolean z2, boolean z3) {
            this.background = z;
            this.postActivityFinish = z2;
            this.isHelapayPayment = z3;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            if (!this.background) {
                PaymentDetailFragment.this.displayProgress(true);
            }
        }

        @Override // android.os.AsyncTask
        public StatusResponse doInBackground(String... strArr) {
            PaymentDetailFragment.this.dataLoading = true;
            HashMap hashMap = new HashMap();
            hashMap.put("order_key", strArr[0]);
            try {
                StatusResponse statusResponse = (StatusResponse) new GsonBuilder().create().fromJson(NetworkHandler.sendPost(PHConfigs.BASE_URL + PHConfigs.STATUS, hashMap), (Class<Object>) StatusResponse.class);
                if (statusResponse != null) {
                    System.out.println(statusResponse.toString());
                }
                return statusResponse;
            } catch (Exception e) {
                Log.d(PaymentDetailFragment.this.TAG, "Unable to get order status", e);
                return null;
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(StatusResponse statusResponse) {
            super.onPostExecute((StatusChecker) statusResponse);
            PaymentDetailFragment.this.lastResponse = statusResponse;
            if (statusResponse == null) {
                Toast.makeText(PaymentDetailFragment.this.activity, "Illegal request", 1).show();
                PaymentDetailFragment.this.activity.finish();
            } else if (this.isHelapayPayment && PaymentDetailFragment.this.lastResponse.getStatusState() == StatusResponse.Status.INIT && !PHMainActivity.DISCONNECTED) {
                PaymentDetailFragment.this.runStatusChecker(FirebaseInAppMessagingDisplay.IMPRESSION_THRESHOLD_MILLIS);
            } else {
                if (PaymentDetailFragment.this.lastResponse.getStatusState() == StatusResponse.Status.SUCCESS) {
                    PaymentDetailFragment.this.activity.setPayResultView(new PHResponse<>(PaymentDetailFragment.this.getStatusFromResponse(), "Payment success. Check response data", PaymentDetailFragment.this.lastResponse), PaymentDetailFragment.this.request instanceof InitPreapprovalRequest, false);
                } else if (PaymentDetailFragment.this.lastResponse.getStatusState() == StatusResponse.Status.HOLD) {
                    PaymentDetailFragment.this.activity.setPayResultView(new PHResponse<>(PaymentDetailFragment.this.getStatusFromResponse(), "Payment success. Check response data", PaymentDetailFragment.this.lastResponse), PaymentDetailFragment.this.request instanceof InitPreapprovalRequest, true);
                } else if (PaymentDetailFragment.this.lastResponse.getStatusState() == StatusResponse.Status.FAILED) {
                    PaymentDetailFragment.this.activity.setPayResultView(new PHResponse<>(PaymentDetailFragment.this.getStatusFromResponse(), "Payment failed. Check response data", PaymentDetailFragment.this.lastResponse), PaymentDetailFragment.this.request instanceof InitPreapprovalRequest, true);
                } else if (this.postActivityFinish) {
                    String str = PaymentDetailFragment.this.lastResponse.getStatusState() == StatusResponse.Status.INIT ? "initial" : PaymentDetailFragment.this.lastResponse.getStatusState() == StatusResponse.Status.PAYMENT ? "payment selection" : "unknown";
                    PaymentDetailFragment paymentDetailFragment = PaymentDetailFragment.this;
                    paymentDetailFragment.setError(new PHResponse(paymentDetailFragment.getStatusFromResponse(), e80.c("Payment in ", str, " stage. Check response data"), PaymentDetailFragment.this.lastResponse), true);
                }
                if (!this.background) {
                    PaymentDetailFragment.this.displayProgress(false);
                }
                PaymentDetailFragment.this.dataLoading = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkComplete(boolean z, boolean z2) {
        String str;
        if (this.dataLoading) {
            Toast.makeText(this.activity, "Please wait!!", 1).show();
        } else if (!isStatusUpdated()) {
            new StatusChecker(z, z2, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.orderKey);
        } else {
            int statusFromResponse = getStatusFromResponse();
            if (this.lastResponse.getStatusState() == StatusResponse.Status.SUCCESS) {
                setError(new PHResponse(getStatusFromResponse(), "Payment success. Check response data", this.lastResponse), z2);
            } else if (this.lastResponse.getStatusState() == StatusResponse.Status.FAILED) {
                setError(new PHResponse(getStatusFromResponse(), "Payment failed. Check response data", this.lastResponse), z2);
            } else {
                if (this.lastResponse.getStatusState() == StatusResponse.Status.INIT) {
                    str = "initial";
                } else if (this.lastResponse.getStatusState() == StatusResponse.Status.PAYMENT) {
                    str = "payment selection";
                } else {
                    str = "unknown";
                }
                setError(new PHResponse(statusFromResponse, e80.c("Payment in ", str, " stage. Check response data"), this.lastResponse), z2);
            }
            return true;
        }
        return z2;
    }

    private boolean checkNetworkAvailability() {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.activity.getSystemService("connectivity");
        if (connectivityManager != null && (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayProgress(boolean z) {
        if (z) {
            Glide.with((FragmentActivity) this.activity).asGif().mo14load(Integer.valueOf(R.drawable.spinner_circle)).into(this.progressImage);
            this.progressFrame.setVisibility(0);
            return;
        }
        this.progressFrame.setVisibility(8);
    }

    private int getDeviceSpecHeight(int i) {
        int deviceheight = getDeviceheight();
        int i2 = (int) (deviceheight * 0.9d);
        if (i == 400 && this.isCardSave) {
            i = (i * deviceheight) / LogSeverity.EMERGENCY_VALUE;
        }
        int i3 = (this.initHeight * i) / 400;
        if (i3 <= i2) {
            return i3;
        }
        return i2;
    }

    private int getDeviceheight() {
        return this.activity.getResources().getDisplayMetrics().heightPixels;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getStatusFromResponse() {
        return this.lastResponse.getStatusState() == StatusResponse.Status.SUCCESS ? 1 : -5;
    }

    private void initPayment(Context context) {
        String returnUrl;
        String cancelUrl;
        displayProgress(true);
        PaymentDetails paymentDetails = new PaymentDetails();
        InitBaseRequest initBaseRequest = this.request;
        if (initBaseRequest != null) {
            paymentDetails.setOrderId(initBaseRequest.getOrderId());
            paymentDetails.setMerchantId(this.request.getMerchantId());
            paymentDetails.setCurrency(this.request.getCurrency());
            paymentDetails.setAmount(this.request.getAmount());
            InitBaseRequest initBaseRequest2 = this.request;
            if (initBaseRequest2 instanceof InitRequest) {
                InitRequest initRequest = (InitRequest) initBaseRequest2;
                paymentDetails.setRecurrence(initRequest.getRecurrence());
                paymentDetails.setDuration(initRequest.getDuration());
                paymentDetails.setStartupFee(initRequest.getStartupFee());
            }
            if (this.request.getItems() != null) {
                HashMap<String, String> hashMap = new HashMap<>();
                int i = 1;
                for (Item item : this.request.getItems()) {
                    hashMap.put(p1.c("item_name_", i), item.getName());
                    hashMap.put("item_number_" + i, item.getId());
                    hashMap.put("amount_" + i, String.valueOf(item.getAmount()));
                    hashMap.put("quantity_" + i, String.valueOf(item.getQuantity()));
                    i++;
                }
                paymentDetails.setItemsMap(hashMap);
            } else {
                paymentDetails.setItemsMap(null);
            }
            paymentDetails.setItemsDescription(this.request.getItemsDescription());
            paymentDetails.setFirstName(this.request.getCustomer().getFirstName());
            paymentDetails.setLastName(this.request.getCustomer().getLastName());
            paymentDetails.setEmail(this.request.getCustomer().getEmail());
            paymentDetails.setPhone(this.request.getCustomer().getPhone());
            paymentDetails.setAddress(this.request.getCustomer().getAddress().getAddress());
            paymentDetails.setCity(this.request.getCustomer().getAddress().getCity());
            paymentDetails.setCountry(this.request.getCustomer().getAddress().getCountry());
            paymentDetails.setDeliveryAddress(this.request.getCustomer().getDeliveryAddress().getAddress());
            paymentDetails.setDeliveryCity(this.request.getCustomer().getDeliveryAddress().getCity());
            paymentDetails.setDeliveryCountry(this.request.getCustomer().getDeliveryAddress().getCountry());
            paymentDetails.setCustom1(this.request.getCustom1());
            paymentDetails.setCustom2(this.request.getCustom2());
            String returnUrl2 = this.request.getReturnUrl();
            String str = PHConstants.dummyUrl;
            if (returnUrl2 == null) {
                returnUrl = PHConstants.dummyUrl;
            } else {
                returnUrl = this.request.getReturnUrl();
            }
            paymentDetails.setReturnUrl(returnUrl);
            if (this.request.getCancelUrl() == null) {
                cancelUrl = PHConstants.dummyUrl;
            } else {
                cancelUrl = this.request.getCancelUrl();
            }
            paymentDetails.setCancelUrl(cancelUrl);
            if (this.request.getNotifyUrl() != null) {
                str = this.request.getNotifyUrl();
            }
            paymentDetails.setNotifyUrl(str);
            paymentDetails.setPlatform("android");
            paymentDetails.setReferer(this.activity.getPackageName());
            paymentDetails.setHash("");
            paymentDetails.setMethod(this.method);
            paymentDetails.setAuto(this.request instanceof InitPreapprovalRequest);
            paymentDetails.setAuthorize(this.request.isHoldOnCardEnabled());
        }
        ((PayhereSDK) ServiceGenerator.createService(context, PayhereSDK.class)).initPayment(paymentDetails).enqueue(new Callback<PaymentInitResult>() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.6
            @Override // retrofit2.Callback
            public void onFailure(Call<PaymentInitResult> call, Throwable th) {
                String str2;
                PaymentDetailFragment.this.displayProgress(false);
                if (th != null && th.getMessage() != null && !th.getMessage().equals("")) {
                    str2 = th.getMessage();
                } else {
                    str2 = "Error Occurred";
                }
                PaymentDetailFragment.this.setError(new PHResponse(-1, str2), true);
            }

            @Override // retrofit2.Callback
            public void onResponse(Call<PaymentInitResult> call, Response<PaymentInitResult> response) {
                String msg;
                PaymentInitResult body = response.body();
                if (body != null && body.getStatus() == 1) {
                    PaymentDetailFragment.this.startProcess(body.getData().getRedirection().getUrl());
                    if (body.getData() != null && body.getData().getOrder() != null) {
                        PaymentDetailFragment.this.orderKey = body.getData().getOrder().getOrderKey();
                    }
                } else {
                    PaymentDetailFragment paymentDetailFragment = PaymentDetailFragment.this;
                    if (body == null) {
                        msg = "Error Occurred";
                    } else {
                        msg = body.getMsg();
                    }
                    paymentDetailFragment.setError(new PHResponse(-1, msg), true);
                }
                PaymentDetailFragment.this.displayProgress(false);
            }
        });
    }

    private boolean isStatusUpdated() {
        return false;
    }

    private void openWebBroser() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(this.hela_pay.getUrl()));
        intent.setFlags(PKIFailureInfo.duplicateCertReq);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runStatusChecker(long j) {
        new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.1
            @Override // java.lang.Runnable
            public void run() {
                new StatusChecker(false, false, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, PaymentDetailFragment.this.orderKey);
            }
        }, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setError(PHResponse pHResponse, boolean z) {
        Intent intent = new Intent();
        intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, pHResponse);
        this.activity.setResult(0, intent);
        if (z) {
            this.activity.finish();
        }
    }

    private void setWebView(View view) {
        int i = 1;
        if (this.isCardSave) {
            setWebViewHeight(this.initHeight, true);
        } else {
            setWebViewHeight(this.initHeight, true);
            new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.2
                @Override // java.lang.Runnable
                public void run() {
                    if (PaymentDetailFragment.this.activity.getMethods() != null) {
                        NewInitResponse.PaymentMethod paymentMethod = PaymentDetailFragment.this.activity.getMethods().get(PaymentDetailFragment.this.method);
                        if (paymentMethod != null && paymentMethod.getView().getWindowSize() != null) {
                            PaymentDetailFragment.this.setWebViewHeightAnimated(paymentMethod.getView().getWindowSize().getHeight());
                            return;
                        }
                        return;
                    }
                    PHConfigs.setOnMethodReceivedListener(new PHConfigs.OnMethodReceivedListener() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.2.1
                        @Override // lk.payhere.androidsdk.PHConfigs.OnMethodReceivedListener
                        public void onMethodReturned(HashMap<String, PaymentMethodResponse.Data> hashMap) {
                            PaymentMethodResponse.Data data = hashMap.get(PaymentDetailFragment.this.method);
                            if (data != null && data.getViewSize() != null) {
                                PaymentDetailFragment.this.setWebViewHeightAnimated(data.getViewSize().getHeight());
                            }
                        }
                    });
                }
            }, 250L);
        }
        displayProgress(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setAppCacheEnabled(PHMainActivity.cacheEnabled);
        this.webView.getSettings().setSaveFormData(PHMainActivity.cacheEnabled);
        this.webView.getSettings().setSavePassword(PHMainActivity.cacheEnabled);
        WebSettings settings = this.webView.getSettings();
        if (!PHMainActivity.cacheEnabled) {
            i = 2;
        }
        settings.setCacheMode(i);
        this.webView.addJavascriptInterface(new MyJavaScriptInterface(this.activity), "HtmlViewer");
        this.webView.setWebViewClient(new WebViewClient() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.3
            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                PaymentDetailFragment.this.webView.setVisibility(0);
                webView.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                webView.clearCache(true);
                webView.clearFormData();
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                String str2 = PaymentDetailFragment.this.TAG;
                Log.d(str2, "onPageStarted() :" + str);
                if (str.startsWith(PHConstants.PAYMENT_COMPLETE_URL) || str.startsWith(PHConstants.PAYMENT_COMPLETE_SANDBOX_URL)) {
                    PaymentDetailFragment.this.checkComplete(false, true);
                }
                PaymentDetailFragment.this.webView.setVisibility(0);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                PaymentDetailFragment.this.activity.goBackToApp();
            }

            @Override // android.webkit.WebViewClient
            @TargetApi(21)
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                String str = PaymentDetailFragment.this.TAG;
                StringBuilder b = z5.b("URL Loading res:");
                b.append(webResourceRequest.getUrl().toString());
                Log.d(str, b.toString());
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                String str2 = PaymentDetailFragment.this.TAG;
                Log.d(str2, "URL Loading url:" + str);
                webView.loadUrl(str);
                return true;
            }
        });
        this.webView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.4
            @Override // lk.payhere.androidsdk.util.ObservableWebView.OnScrollChangedCallback
            public void onScrollTopEnd(boolean z) {
                WebViewBottomSheetbehaviour.scrollEnabled = z;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWebViewHeight(int i, boolean z) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.webView.getLayoutParams();
        z5.d("Height of set value - ", i, this.TAG);
        if (!z) {
            i = getDeviceSpecHeight(i);
        }
        ((ViewGroup.MarginLayoutParams) layoutParams).height = i;
        this.webView.setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWebViewHeightAnimated(int i) {
        int deviceSpecHeight = getDeviceSpecHeight(i);
        z5.d("Height of input value - ", i, this.TAG);
        String str = this.TAG;
        StringBuilder b = z5.b("Height of webview value - ");
        b.append(this.webView.getMeasuredHeight());
        Log.d(str, b.toString());
        z5.d("Height of view value - ", deviceSpecHeight, this.TAG);
        ValueAnimator ofInt = ValueAnimator.ofInt(this.webView.getMeasuredHeight(), deviceSpecHeight);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: lk.payhere.androidsdk.fragment.PaymentDetailFragment.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PaymentDetailFragment.this.setWebViewHeight(((Integer) valueAnimator.getAnimatedValue()).intValue(), true);
            }
        });
        ofInt.setDuration(200L);
        ofInt.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startProcess(String str) {
        String validate = validate();
        if (validate == null) {
            if (!checkNetworkAvailability()) {
                setError(new PHResponse(-4, "Unable to connect to the internet"), true);
                return;
            } else {
                this.webView.loadUrl(str);
                return;
            }
        }
        setError(new PHResponse(-3, validate), true);
    }

    private String validate() {
        if (PHConfigs.BASE_URL == null) {
            return "BASE_URL not set";
        }
        InitBaseRequest initBaseRequest = this.request;
        if (initBaseRequest instanceof InitRequest) {
            InitRequest initRequest = (InitRequest) initBaseRequest;
            if (initRequest.getAmount() <= 0.0d) {
                return "Invalid amount";
            }
            if (initRequest.getCurrency() == null || initRequest.getCurrency().length() != 3) {
                return "Invalid currency";
            }
        }
        if (this.request.getMerchantId() != null && this.request.getMerchantId().length() != 0) {
            return null;
        }
        return "Invalid merchant ID";
    }

    @Override // lk.payhere.androidsdk.PHMainActivity.OnActivityAction
    public boolean onActivityBack() {
        return checkComplete(false, true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (PHMainActivity) getActivity();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        try {
            if (getArguments() != null) {
                this.request = (InitBaseRequest) getArguments().getSerializable(PHConstants.INTENT_EXTRA_DATA);
                this.method = getArguments().getString(PHConstants.INTENT_EXTRA_METHOD);
                this.initHeight = getArguments().getInt(PHConstants.INTENT_EXTRA_HEIGHT);
                this.isCardSave = getArguments().getBoolean(PHConstants.INTENT_EXTRA_AUTO);
                this.hela_pay = (NewInitResponse.Submission) getArguments().getSerializable(PHConstants.INTENT_EXTRA_HELA_PAY);
                this.orderKey = getArguments().getString(PHConstants.INTENT_EXTRA_ORDER_KEY);
            }
        } catch (Exception e) {
            Log.e(this.TAG, "Request data not found", e);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Utils.screenHight(this.activity);
        View inflate = layoutInflater.inflate(R.layout.ph_fragment_payment_details, viewGroup, false);
        this.progressFrame = inflate.findViewById(R.id.ph_progress_frame);
        this.webView = (ObservableWebView) inflate.findViewById(R.id.ph_webview);
        this.progressImage = (ImageView) inflate.findViewById(R.id.progress_view);
        this.webView.setBackgroundColor(getResources().getColor(R.color.white));
        this.webView.setVisibility(4);
        ((ProgressBar) inflate.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(inflate.getContext(), R.color.progress_color), PorterDuff.Mode.MULTIPLY);
        if (!checkNetworkAvailability()) {
            setError(new PHResponse(-4, "Unable to connect to the internet"), true);
        } else if (this.method.toUpperCase().equals(PHConstants.SUMBITION_CODE_HELAPAY)) {
            if (this.hela_pay != null && this.orderKey != null) {
                openWebBroser();
            } else {
                Log.d(this.TAG, "order key or helapay paymenthoad null");
                setError(new PHResponse(-1, "Unable to process helapay payment"), true);
            }
        } else {
            setWebView(inflate);
            initPayment(inflate.getContext());
        }
        return inflate;
    }
}
