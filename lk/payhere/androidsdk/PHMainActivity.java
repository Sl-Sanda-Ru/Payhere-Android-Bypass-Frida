package lk.payhere.androidsdk;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.WorkRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.inappmessaging.display.FirebaseInAppMessagingDisplay;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import lk.payhere.androidsdk.fragment.PaymentDetailFragment;
import lk.payhere.androidsdk.fragment.PaymentMethodFragment;
import lk.payhere.androidsdk.fragment.PaymentResultFragment;
import lk.payhere.androidsdk.model.InitBaseRequest;
import lk.payhere.androidsdk.model.InitPreapprovalRequest;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.StatusResponse;
import lk.payhere.androidsdk.util.NetworkHandler;
import lk.payhere.androidsdk.util.Utils;
import lk.payhere.androidsdk.util.WebViewBottomSheetbehaviour;
import org.spongycastle.asn1.cmp.PKIFailureInfo;

/* loaded from: classes7.dex */
public class PHMainActivity extends PayHereBaseActivity {
    public static boolean cacheEnabled = false;
    private View bottomSheet;
    private Bundle bundle;
    private OnActivityAction listener;
    private HashMap<String, NewInitResponse.PaymentMethod> methods;
    private BottomSheetBehavior sheetBehavior;
    private PHResponse<StatusResponse> statusResponse;
    private TextView txtTitle;
    private int viewheight;
    private final int VIEW_METHOD = 1;
    private final int VIEW_DETAILS = 2;
    private final int VIEW_RESULT = 3;
    private int view = 1;
    private boolean isSaveCard = false;
    private int peekHeight = 0;
    private int prevState = 4;
    private boolean skipResult = false;
    private boolean closeBottomSheet = true;
    private boolean isHelapayPayment = false;

    /* loaded from: classes7.dex */
    public interface OnActivityAction {
        boolean onActivityBack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkHelapayStatus(final String str, final InitBaseRequest initBaseRequest) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.13
            @Override // java.lang.Runnable
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("order_key", str);
                try {
                    StatusResponse statusResponse = (StatusResponse) new GsonBuilder().create().fromJson(NetworkHandler.sendPost(PHConfigs.BASE_URL + PHConfigs.STATUS, hashMap), (Class<Object>) StatusResponse.class);
                    if (statusResponse != null) {
                        System.out.println(statusResponse.toString());
                        PHMainActivity.this.handlePaymentResponse(statusResponse, initBaseRequest, str, false);
                    }
                } catch (Exception e) {
                    Log.d(PayHereBaseActivity.TAG, "Unable to get order status", e);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void colorFadeAnimation(final View view, int i, int i2, final boolean z) {
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), Integer.valueOf(i), Integer.valueOf(i2));
        ofObject.setDuration(250L);
        ofObject.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: lk.payhere.androidsdk.PHMainActivity.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        ofObject.addListener(new Animator.AnimatorListener() { // from class: lk.payhere.androidsdk.PHMainActivity.7
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (z) {
                    PHMainActivity.this.setUserCanceledError();
                    PHMainActivity.this.overridePendingTransition(0, 0);
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }
        });
        ofObject.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getStatusFromResponse(StatusResponse statusResponse) {
        return statusResponse.getStatusState() == StatusResponse.Status.SUCCESS ? 1 : -5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePaymentResponse(final StatusResponse statusResponse, final InitBaseRequest initBaseRequest, final String str, final boolean z) {
        new Timer().schedule(new TimerTask() { // from class: lk.payhere.androidsdk.PHMainActivity.14
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                StatusResponse statusResponse2;
                if (!z && ((statusResponse2 = statusResponse) == null || statusResponse2.getStatusState() != StatusResponse.Status.INIT || PayHereBaseActivity.DISCONNECTED)) {
                    if (statusResponse.getStatusState() == StatusResponse.Status.SUCCESS) {
                        PHMainActivity pHMainActivity = PHMainActivity.this;
                        pHMainActivity.setPayResultView(new PHResponse<>(pHMainActivity.getStatusFromResponse(statusResponse), "Payment success. Check response data", statusResponse), initBaseRequest instanceof InitPreapprovalRequest, false);
                        return;
                    } else if (statusResponse.getStatusState() == StatusResponse.Status.HOLD) {
                        PHMainActivity pHMainActivity2 = PHMainActivity.this;
                        pHMainActivity2.setPayResultView(new PHResponse<>(pHMainActivity2.getStatusFromResponse(statusResponse), "Payment success. Check response data", statusResponse), initBaseRequest instanceof InitPreapprovalRequest, true);
                        return;
                    } else if (statusResponse.getStatusState() == StatusResponse.Status.FAILED) {
                        PHMainActivity pHMainActivity3 = PHMainActivity.this;
                        pHMainActivity3.setError(new PHResponse(pHMainActivity3.getStatusFromResponse(statusResponse), "Payment failed. Check response data", statusResponse), true);
                        return;
                    } else {
                        return;
                    }
                }
                PHMainActivity.this.checkHelapayStatus(str, initBaseRequest);
            }
        }, FirebaseInAppMessagingDisplay.IMPRESSION_THRESHOLD_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackClicked() {
        int i = this.view;
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    return;
                }
            } else if (this.isSaveCard) {
                this.sheetBehavior.setState(4);
                return;
            } else {
                setPayMethod();
                return;
            }
        }
        this.sheetBehavior.setState(4);
    }

    private void openHelakuruIntent(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setFlags(PKIFailureInfo.duplicateCertReq);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setError(final PHResponse pHResponse, boolean z) {
        runOnUiThread(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.15
            @Override // java.lang.Runnable
            public void run() {
                PHMainActivity.this.setPayResultView(pHResponse, false, false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserCanceledError() {
        Intent intent = new Intent();
        PHResponse<StatusResponse> pHResponse = this.statusResponse;
        if (pHResponse != null && pHResponse.getData() != null && (this.statusResponse.getData().getStatusState() == StatusResponse.Status.SUCCESS || this.statusResponse.getData().getStatusState() == StatusResponse.Status.HOLD || this.statusResponse.getData().getStatusState() == StatusResponse.Status.FAILED)) {
            intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, this.statusResponse);
            setResult(-1, intent);
        } else {
            intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, new PHResponse(-6, "User canceled the request"));
            setResult(0, intent);
        }
        finish();
    }

    @Override // lk.payhere.androidsdk.PayHereBaseActivity
    public /* bridge */ /* synthetic */ void enableTimer() {
        super.enableTimer();
    }

    public HashMap<String, NewInitResponse.PaymentMethod> getMethods() {
        return this.methods;
    }

    public void goBackToApp() {
        this.sheetBehavior.setState(4);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        OnActivityAction onActivityAction = this.listener;
        if (onActivityAction != null && onActivityAction.onActivityBack()) {
            this.sheetBehavior.setState(4);
        }
    }

    @Override // lk.payhere.androidsdk.PayHereBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Utils.screenHight(this);
        if (!getIntent().hasExtra(PHConstants.INTENT_EXTRA_DATA)) {
            PHResponse pHResponse = new PHResponse(-2, "INTENT_EXTRA_DATA not found");
            Intent intent = new Intent();
            intent.putExtra(PHConstants.INTENT_EXTRA_RESULT, pHResponse);
            setResult(0, intent);
            this.sheetBehavior.setState(4);
        }
        if (getIntent().hasExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT)) {
            this.skipResult = getIntent().getBooleanExtra(PHConstants.INTENT_EXTRA_SKIP_RESULT, false);
        }
        if (getIntent().hasExtra(PHConstants.INTENT_EXTRA_CACHE_ENABLE)) {
            cacheEnabled = getIntent().getBooleanExtra(PHConstants.INTENT_EXTRA_CACHE_ENABLE, false);
        }
        try {
            setContentView(R.layout.ph_activity_phmain);
        } catch (IllegalStateException unused) {
            setTheme(R.style.phtransparent_windowTitle_fix);
            setContentView(R.layout.ph_activity_phmain);
        }
        View findViewById = findViewById(R.id.bottom_sheet);
        this.bottomSheet = findViewById;
        BottomSheetBehavior from = BottomSheetBehavior.from(findViewById);
        this.sheetBehavior = from;
        from.setState(5);
        this.sheetBehavior.setPeekHeight(this.peekHeight);
        boolean z = true;
        WebViewBottomSheetbehaviour.scrollEnabled = true;
        Serializable serializableExtra = getIntent().getSerializableExtra(PHConstants.INTENT_EXTRA_DATA);
        Bundle bundle2 = new Bundle();
        this.bundle = bundle2;
        bundle2.putSerializable(PHConstants.INTENT_EXTRA_DATA, serializableExtra);
        View findViewById2 = this.bottomSheet.findViewById(R.id.bottom_sheet_hedder);
        this.txtTitle = (TextView) findViewById(R.id.pay_with_text_title);
        findViewById2.setOnTouchListener(new View.OnTouchListener() { // from class: lk.payhere.androidsdk.PHMainActivity.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 2 || motionEvent.getAction() == 1) {
                    if (PHMainActivity.this.sheetBehavior.getState() == 4) {
                        PHMainActivity.this.sheetBehavior.setState(3);
                    } else if (PHMainActivity.this.isSaveCard) {
                        PHMainActivity.this.sheetBehavior.setState(4);
                    } else {
                        PHMainActivity.this.onBackClicked();
                    }
                }
                return true;
            }
        });
        InitBaseRequest initBaseRequest = (InitBaseRequest) serializableExtra;
        if (initBaseRequest instanceof InitRequest) {
            InitRequest initRequest = (InitRequest) initBaseRequest;
            this.isSaveCard = (initRequest.getDuration() == null || initRequest.getDuration().equals("") || initRequest.getRecurrence().equals("")) ? false : false;
        } else {
            if (initBaseRequest instanceof InitPreapprovalRequest) {
                initBaseRequest.setHoldOnCardEnabled(false);
                Log.d(PayHereBaseActivity.TAG, "hold on card not effect for pre approval request,If request is InitPreapprovalRequest always hold on card disabled");
            }
            this.isSaveCard = true;
        }
        String str = PHConfigs.LIVE_URL;
        if (initBaseRequest != null && initBaseRequest.getMerchantId() != null && !initBaseRequest.getMerchantId().equals("")) {
            if (initBaseRequest.getMerchantId().toCharArray()[0] == '1') {
                this.bottomSheet.findViewById(R.id.debug_value).setVisibility(0);
                str = PHConfigs.SANDBOX_URL;
            } else if (initBaseRequest.getMerchantId().toCharArray()[0] == '2') {
                this.bottomSheet.findViewById(R.id.debug_value).setVisibility(8);
            } else if (initBaseRequest.getMerchantId().toCharArray()[0] == '0') {
                str = PHConfigs.LOCAL_URL;
            }
        }
        PHConfigs.setBaseUrl(str);
        if (this.isSaveCard) {
            setPayDetailsView("CREDIT / DEBIT CARD", "VISA");
        } else {
            setPayMethod();
        }
        final View findViewById3 = findViewById(R.id.main_back);
        findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() { // from class: lk.payhere.androidsdk.PHMainActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PHMainActivity.this.view != 1 && PHMainActivity.this.view != 2) {
                    if (PHMainActivity.this.view == 3) {
                        Intent intent2 = new Intent();
                        intent2.putExtra(PHConstants.INTENT_EXTRA_RESULT, PHMainActivity.this.statusResponse);
                        PHMainActivity.this.setResult(0, intent2);
                        PHMainActivity.this.sheetBehavior.setState(4);
                        return;
                    } else if (PHMainActivity.this.view != 2) {
                        PHMainActivity.this.setUserCanceledError();
                        return;
                    } else {
                        return;
                    }
                }
                PHMainActivity.this.sheetBehavior.setState(4);
            }
        });
        this.sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() { // from class: lk.payhere.androidsdk.PHMainActivity.3
            @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
            public void onSlide(@NonNull View view, float f) {
            }

            @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
            public void onStateChanged(@NonNull View view, int i) {
                if (PHMainActivity.this.isSaveCard && PHMainActivity.this.view == 2 && i == 4) {
                    PHMainActivity.this.setUserCanceledError();
                } else if (PHMainActivity.this.isHelapayPayment && PHMainActivity.this.view == 2 && i == 4) {
                    PHMainActivity.this.setUserCanceledError();
                } else if (PHMainActivity.this.view == 2 && i == 4) {
                    PHMainActivity.this.setUserCanceledError();
                }
                if (i == 2 && (PHMainActivity.this.view != 2 || PHMainActivity.this.isSaveCard)) {
                    boolean z2 = false;
                    if (PHMainActivity.this.prevState != 4) {
                        int color = PHMainActivity.this.getResources().getColor(R.color.bottom_sheet_back);
                        int color2 = PHMainActivity.this.getResources().getColor(17170445);
                        PHMainActivity pHMainActivity = PHMainActivity.this;
                        pHMainActivity.colorFadeAnimation(findViewById3, color, color2, (pHMainActivity.view == 1 || PHMainActivity.this.view == 3) ? true : true);
                    } else {
                        PHMainActivity.this.colorFadeAnimation(findViewById3, PHMainActivity.this.getResources().getColor(17170445), PHMainActivity.this.getResources().getColor(R.color.bottom_sheet_back), false);
                    }
                }
                PHMainActivity.this.prevState = i;
                Log.d("PHMainActivity", "Bottom Sheet state : " + i);
            }
        });
        findViewById2.post(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.4
            @Override // java.lang.Runnable
            public void run() {
                PHMainActivity.this.peekHeight = 0;
            }
        });
        findViewById(R.id.bottom_sheet_back_icon).setOnClickListener(new View.OnClickListener() { // from class: lk.payhere.androidsdk.PHMainActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PHMainActivity.this.onBackClicked();
            }
        });
        PHConfigs.readMethods(this);
    }

    @Override // lk.payhere.androidsdk.PayHereBaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    @Override // lk.payhere.androidsdk.PayHereBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
    }

    @Override // lk.payhere.androidsdk.PayHereBaseActivity, android.app.Activity
    public /* bridge */ /* synthetic */ void onUserInteraction() {
        super.onUserInteraction();
    }

    @Override // lk.payhere.androidsdk.PayHereBaseActivity
    public /* bridge */ /* synthetic */ void resetDisconnectTimer() {
        super.resetDisconnectTimer();
    }

    public void setCloseBottomSheet(boolean z) {
        this.closeBottomSheet = z;
    }

    public void setMethods(HashMap<String, NewInitResponse.PaymentMethod> hashMap) {
        this.methods = hashMap;
    }

    public void setPayDetailsView(String str, String str2) {
        if (this.isSaveCard) {
            this.txtTitle.setText(getString(R.string.pay_with_card_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(0);
            this.sheetBehavior.setPeekHeight(this.peekHeight);
            this.viewheight = (int) (Utils.screenHight(this) * 0.6d);
        }
        new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.9
            @Override // java.lang.Runnable
            public void run() {
                if (PHMainActivity.this.sheetBehavior.getState() != 3) {
                    PHMainActivity.this.sheetBehavior.setState(3);
                }
            }
        }, 250L);
        this.view = 2;
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        PaymentDetailFragment paymentDetailFragment = new PaymentDetailFragment();
        this.bundle.putString(PHConstants.INTENT_EXTRA_METHOD, str2);
        this.bundle.putInt(PHConstants.INTENT_EXTRA_HEIGHT, this.viewheight);
        this.bundle.putBoolean(PHConstants.INTENT_EXTRA_AUTO, this.isSaveCard);
        paymentDetailFragment.setArguments(this.bundle);
        this.listener = paymentDetailFragment;
        beginTransaction.replace(R.id.frame_main_fragment_container, paymentDetailFragment);
        beginTransaction.commit();
    }

    public void setPayMethod() {
        new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.8
            @Override // java.lang.Runnable
            public void run() {
                PHMainActivity pHMainActivity = PHMainActivity.this;
                pHMainActivity.viewheight = pHMainActivity.bottomSheet.findViewById(R.id.frame_main_fragment_container).getMeasuredHeight();
                PHMainActivity.this.sheetBehavior.setPeekHeight(PHMainActivity.this.peekHeight);
                if (PHMainActivity.this.sheetBehavior.getState() != 3) {
                    PHMainActivity.this.sheetBehavior.setState(3);
                }
            }
        }, 250L);
        this.txtTitle.setText(getString(R.string.pay_with_text));
        findViewById(R.id.bottom_sheet_back_icon).setVisibility(0);
        this.view = 1;
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        PaymentMethodFragment paymentMethodFragment = new PaymentMethodFragment();
        paymentMethodFragment.setArguments(this.bundle);
        this.listener = paymentMethodFragment;
        beginTransaction.replace(R.id.frame_main_fragment_container, paymentMethodFragment);
        beginTransaction.commit();
    }

    public void setPayResultView(final PHResponse<StatusResponse> pHResponse, final boolean z, final boolean z2) {
        this.closeBottomSheet = true;
        runOnUiThread(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.12
            @Override // java.lang.Runnable
            public void run() {
                long j;
                int i;
                String str;
                PHMainActivity.this.findViewById(R.id.bottom_sheet_back_icon).setVisibility(8);
                PHMainActivity.this.statusResponse = pHResponse;
                PHMainActivity.this.view = 3;
                PHMainActivity.this.sheetBehavior.setSkipCollapsed(true);
                if (!PHMainActivity.this.skipResult) {
                    new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.12.1
                        @Override // java.lang.Runnable
                        public void run() {
                            PHMainActivity pHMainActivity = PHMainActivity.this;
                            pHMainActivity.viewheight = pHMainActivity.bottomSheet.findViewById(R.id.frame_main_fragment_container).getMeasuredHeight();
                            if (PHMainActivity.this.sheetBehavior.getState() != 3) {
                                PHMainActivity.this.sheetBehavior.setState(3);
                            }
                        }
                    }, 250L);
                    if (((StatusResponse) pHResponse.getData()).getStatus() != 3 && ((StatusResponse) pHResponse.getData()).getStatus() != 2) {
                        PHMainActivity.this.txtTitle.setText(PHMainActivity.this.getString(R.string.label_declined));
                    } else if (z) {
                        PHMainActivity.this.txtTitle.setText(PHMainActivity.this.getString(R.string.label_save));
                    } else {
                        PHMainActivity.this.txtTitle.setText(PHMainActivity.this.getString(R.string.label_paid));
                    }
                    FragmentTransaction beginTransaction = PHMainActivity.this.getSupportFragmentManager().beginTransaction();
                    PaymentResultFragment paymentResultFragment = new PaymentResultFragment();
                    Bundle bundle = PHMainActivity.this.bundle;
                    PHResponse pHResponse2 = pHResponse;
                    if (pHResponse2 != null) {
                        j = ((StatusResponse) pHResponse2.getData()).getPaymentNo();
                    } else {
                        j = 0;
                    }
                    bundle.putLong(PHConstants.INTENT_EXTRA_REFERENCE, j);
                    PHMainActivity.this.bundle.putBoolean(PHConstants.INTENT_EXTRA_AUTO, z);
                    PHMainActivity.this.bundle.putBoolean(PHConstants.INTENT_EXTRA_HOLD, z2);
                    Bundle bundle2 = PHMainActivity.this.bundle;
                    PHResponse pHResponse3 = pHResponse;
                    if (pHResponse3 != null) {
                        i = ((StatusResponse) pHResponse3.getData()).getStatus();
                    } else {
                        i = -2;
                    }
                    bundle2.putInt(PHConstants.INTENT_EXTRA_STATUS, i);
                    Bundle bundle3 = PHMainActivity.this.bundle;
                    PHResponse pHResponse4 = pHResponse;
                    if (pHResponse4 != null) {
                        str = ((StatusResponse) pHResponse4.getData()).getMessage();
                    } else {
                        str = "";
                    }
                    bundle3.putString(PHConstants.INTENT_EXTRA_MESSAGE, str);
                    paymentResultFragment.setArguments(PHMainActivity.this.bundle);
                    PHMainActivity.this.listener = paymentResultFragment;
                    beginTransaction.replace(R.id.frame_main_fragment_container, paymentResultFragment);
                    beginTransaction.commitAllowingStateLoss();
                    new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.12.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (PHMainActivity.this.closeBottomSheet) {
                                PHMainActivity.this.goBackToApp();
                            }
                        }
                    }, WorkRequest.MIN_BACKOFF_MILLIS);
                    return;
                }
                PHMainActivity.this.goBackToApp();
            }
        });
    }

    @Override // lk.payhere.androidsdk.PayHereBaseActivity
    public /* bridge */ /* synthetic */ void stopDisconnectTimer() {
        super.stopDisconnectTimer();
    }

    public void setPayDetailsView(NewInitResponse.PaymentMethod paymentMethod, final InitBaseRequest initBaseRequest, final String str) {
        this.isHelapayPayment = false;
        this.view = 2;
        if (paymentMethod.getSubmissionCode().equals(PHConstants.SUMBITION_CODE_HELAPAY)) {
            this.isHelapayPayment = true;
            PayHereBaseActivity.DISCONNECT_TIMEOUT = PayHereBaseActivity.DISCONNECT_TIMEOUT_HELAPAY;
            resetDisconnectTimer();
            openHelakuruIntent(paymentMethod.getSubmission().getUrl());
            new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.10
                @Override // java.lang.Runnable
                public void run() {
                    PHMainActivity.this.handlePaymentResponse(null, initBaseRequest, str, true);
                }
            }, FirebaseInAppMessagingDisplay.IMPRESSION_THRESHOLD_MILLIS);
            return;
        }
        this.viewheight = (int) (Utils.screenHight(this) * 0.6d);
        if (this.isSaveCard) {
            this.sheetBehavior.setPeekHeight(this.peekHeight);
            String str2 = PayHereBaseActivity.TAG;
            StringBuilder b = z5.b("view height ");
            b.append(this.viewheight);
            Log.d(str2, b.toString());
            this.txtTitle.setText(getString(R.string.pay_with_card_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(0);
        }
        new Handler().postDelayed(new Runnable() { // from class: lk.payhere.androidsdk.PHMainActivity.11
            @Override // java.lang.Runnable
            public void run() {
                if (PHMainActivity.this.sheetBehavior.getState() != 3) {
                    PHMainActivity.this.sheetBehavior.setState(3);
                }
            }
        }, 250L);
        if (Utils.getPayMethod(paymentMethod).equals("CARD")) {
            this.txtTitle.setText(getString(R.string.pay_with_card_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(0);
        } else if (Utils.getPayMethod(paymentMethod).equals(PHConstants.METHOD_OTHER)) {
            this.txtTitle.setText(getString(R.string.pay_with_other_text));
            findViewById(R.id.bottom_sheet_back_icon).setVisibility(0);
        }
        this.view = 2;
        resetDisconnectTimer();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        PaymentDetailFragment paymentDetailFragment = new PaymentDetailFragment();
        this.bundle.putString(PHConstants.INTENT_EXTRA_METHOD, paymentMethod.getSubmissionCode());
        this.bundle.putInt(PHConstants.INTENT_EXTRA_HEIGHT, this.viewheight);
        this.bundle.putBoolean(PHConstants.INTENT_EXTRA_AUTO, this.isSaveCard);
        this.bundle.putSerializable(PHConstants.INTENT_EXTRA_HELA_PAY, paymentMethod.getSubmission());
        this.bundle.putString(PHConstants.INTENT_EXTRA_ORDER_KEY, str);
        paymentDetailFragment.setArguments(this.bundle);
        this.listener = paymentDetailFragment;
        beginTransaction.replace(R.id.frame_main_fragment_container, paymentDetailFragment);
        beginTransaction.commit();
    }
}
