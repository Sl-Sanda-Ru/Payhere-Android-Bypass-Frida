package lk.payhere.androidsdk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.R;
import lk.payhere.androidsdk.adapter.MethodAdapter;
import lk.payhere.androidsdk.api.PayhereSDK;
import lk.payhere.androidsdk.api.ServiceGenerator;
import lk.payhere.androidsdk.model.InitBaseRequest;
import lk.payhere.androidsdk.model.InitPreapprovalRequest;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.NewInitResponse;
import lk.payhere.androidsdk.model.PaymentInitRequest;
import lk.payhere.androidsdk.model.PaymentMethodResponse;
import lk.payhere.androidsdk.util.ListItemDecore;
import lk.payhere.androidsdk.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* loaded from: classes7.dex */
public class PaymentMethodFragment extends Fragment implements PHMainActivity.OnActivityAction, MethodAdapter.OnPaymentMethodClick {
    private PHMainActivity activity;
    private MethodAdapter cardAdapter;
    private RecyclerView cardPaymentList;
    private NewInitResponse.PaymentMethod helaPayMethod;
    private String orderKey;
    private MethodAdapter otherMethodAdapter;
    private RecyclerView otherPaymentListView;
    private ImageView progressBar;
    private InitBaseRequest request;
    private View view;
    private String TAG = "PaymentMethodFragment";
    private final List<NewInitResponse.PaymentMethod> cardList = new ArrayList();
    private final List<NewInitResponse.PaymentMethod> otherPaymentList = new ArrayList();

    private static float convertDpToPixel(float f, Context context) {
        return (context.getResources().getDisplayMetrics().densityDpi / 160.0f) * f;
    }

    private void createMethodAdapters() {
        this.cardAdapter = new MethodAdapter(this.activity, this.cardList);
        this.otherMethodAdapter = new MethodAdapter(this.activity, this.otherPaymentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.activity, 0, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this.activity, 0, false);
        this.cardPaymentList.setLayoutManager(linearLayoutManager);
        this.otherPaymentListView.setLayoutManager(linearLayoutManager2);
        this.cardPaymentList.addItemDecoration(new ListItemDecore(15));
        this.otherPaymentListView.addItemDecoration(new ListItemDecore(15));
        this.cardPaymentList.setAdapter(this.cardAdapter);
        this.otherPaymentListView.setAdapter(this.otherMethodAdapter);
        this.cardAdapter.setOnPaymentMethodClick(this);
        this.otherMethodAdapter.setOnPaymentMethodClick(this);
    }

    private PaymentInitRequest getPaymentDetails() {
        String returnUrl;
        String cancelUrl;
        PaymentInitRequest paymentInitRequest = new PaymentInitRequest();
        InitBaseRequest initBaseRequest = this.request;
        if (initBaseRequest != null) {
            paymentInitRequest.setOrderId(initBaseRequest.getOrderId());
            paymentInitRequest.setMerchantId(this.request.getMerchantId());
            paymentInitRequest.setCurrency(this.request.getCurrency());
            paymentInitRequest.setAmount(this.request.getAmount());
            InitBaseRequest initBaseRequest2 = this.request;
            if (initBaseRequest2 instanceof InitRequest) {
                InitRequest initRequest = (InitRequest) initBaseRequest2;
                paymentInitRequest.setRecurrence(initRequest.getRecurrence());
                paymentInitRequest.setDuration(initRequest.getDuration());
                paymentInitRequest.setStartupFee(initRequest.getStartupFee());
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
                paymentInitRequest.setItemsMap(hashMap);
            } else {
                paymentInitRequest.setItemsMap(null);
            }
            paymentInitRequest.setItemsDescription(this.request.getItemsDescription());
            paymentInitRequest.setFirstName(this.request.getCustomer().getFirstName());
            paymentInitRequest.setLastName(this.request.getCustomer().getLastName());
            paymentInitRequest.setEmail(this.request.getCustomer().getEmail());
            paymentInitRequest.setPhone(this.request.getCustomer().getPhone());
            paymentInitRequest.setAddress(this.request.getCustomer().getAddress().getAddress());
            paymentInitRequest.setCity(this.request.getCustomer().getAddress().getCity());
            paymentInitRequest.setCountry(this.request.getCustomer().getAddress().getCountry());
            paymentInitRequest.setDeliveryAddress(this.request.getCustomer().getDeliveryAddress().getAddress());
            paymentInitRequest.setDeliveryCity(this.request.getCustomer().getDeliveryAddress().getCity());
            paymentInitRequest.setDeliveryCountry(this.request.getCustomer().getDeliveryAddress().getCountry());
            paymentInitRequest.setCustom1(this.request.getCustom1());
            paymentInitRequest.setCustom2(this.request.getCustom2());
            String returnUrl2 = this.request.getReturnUrl();
            String str = PHConstants.dummyUrl;
            if (returnUrl2 == null) {
                returnUrl = PHConstants.dummyUrl;
            } else {
                returnUrl = this.request.getReturnUrl();
            }
            paymentInitRequest.setReturnUrl(returnUrl);
            if (this.request.getCancelUrl() == null) {
                cancelUrl = PHConstants.dummyUrl;
            } else {
                cancelUrl = this.request.getCancelUrl();
            }
            paymentInitRequest.setCancelUrl(cancelUrl);
            if (this.request.getNotifyUrl() != null) {
                str = this.request.getNotifyUrl();
            }
            paymentInitRequest.setNotifyUrl(str);
            paymentInitRequest.setPlatform("android");
            paymentInitRequest.setReferer(this.activity.getPackageName());
            paymentInitRequest.setHash("");
            paymentInitRequest.setAuto(this.request instanceof InitPreapprovalRequest);
        }
        return paymentInitRequest;
    }

    private void getPaymentmethods() {
        ((PayhereSDK) ServiceGenerator.createService(this.activity, PayhereSDK.class)).getPaymentMethods().enqueue(new Callback<PaymentMethodResponse>() { // from class: lk.payhere.androidsdk.fragment.PaymentMethodFragment.2
            @Override // retrofit2.Callback
            public void onFailure(Call<PaymentMethodResponse> call, Throwable th) {
            }

            @Override // retrofit2.Callback
            public void onResponse(Call<PaymentMethodResponse> call, Response<PaymentMethodResponse> response) {
                response.body();
            }
        });
    }

    private static int getWidth(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readInitResponse(NewInitResponse.Data data) {
        if (data.getPaymentMethods() != null && !data.getPaymentMethods().isEmpty()) {
            this.otherPaymentList.clear();
            this.cardList.clear();
            this.orderKey = data.getOrder().getOrderKey();
            HashMap<String, NewInitResponse.PaymentMethod> hashMap = new HashMap<>();
            for (NewInitResponse.PaymentMethod paymentMethod : data.getPaymentMethods()) {
                hashMap.put(paymentMethod.getMethod(), paymentMethod);
                if (!paymentMethod.getSubmissionCode().toUpperCase().equals("MASTER") && !paymentMethod.getSubmissionCode().toUpperCase().equals("VISA") && !paymentMethod.getSubmissionCode().toUpperCase().equals("AMEX")) {
                    if (paymentMethod.getSubmissionCode().toUpperCase().equals(PHConstants.SUMBITION_CODE_HELAPAY)) {
                        this.helaPayMethod = paymentMethod;
                    } else {
                        this.otherPaymentList.add(paymentMethod);
                    }
                } else {
                    this.cardList.add(paymentMethod);
                }
            }
            this.activity.setMethods(hashMap);
            updateView();
        }
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

    private void setImageSize(ImageView imageView, int i) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).width = i;
        ((ViewGroup.MarginLayoutParams) layoutParams).height = i;
        imageView.setLayoutParams(layoutParams);
    }

    private void updateView() {
        this.progressBar.setVisibility(8);
        if (this.helaPayMethod == null) {
            this.view.findViewById(R.id.helapay_image).setVisibility(8);
            this.view.findViewById(R.id.bank_account_subtitle_txt).setVisibility(8);
        } else {
            this.view.findViewById(R.id.helapay_image).setVisibility(0);
            this.view.findViewById(R.id.bank_account_subtitle_txt).setVisibility(0);
        }
        if (this.cardList.isEmpty()) {
            this.view.findViewById(R.id.card_payment_subtitle_txt).setVisibility(8);
            this.view.findViewById(R.id.card_payment_list).setVisibility(8);
        } else {
            this.view.findViewById(R.id.card_payment_subtitle_txt).setVisibility(0);
            this.view.findViewById(R.id.card_payment_list).setVisibility(0);
            this.cardAdapter.notifyDataSetChanged();
        }
        if (this.otherPaymentList.isEmpty()) {
            this.view.findViewById(R.id.other_payment_subtitle_txt).setVisibility(8);
            this.view.findViewById(R.id.other_payment_list).setVisibility(8);
            return;
        }
        this.view.findViewById(R.id.other_payment_subtitle_txt).setVisibility(0);
        this.view.findViewById(R.id.other_payment_list).setVisibility(0);
        this.otherMethodAdapter.notifyDataSetChanged();
    }

    public void initRequest(InitBaseRequest initBaseRequest) {
        ((PayhereSDK) ServiceGenerator.createService(this.activity, PayhereSDK.class)).initPaymentV2(getPaymentDetails()).enqueue(new Callback<NewInitResponse>() { // from class: lk.payhere.androidsdk.fragment.PaymentMethodFragment.3
            @Override // retrofit2.Callback
            public void onFailure(Call<NewInitResponse> call, Throwable th) {
                String str;
                String str2 = PaymentMethodFragment.this.TAG;
                StringBuilder b = z5.b("Init payment method call throw exception ");
                b.append(th.getMessage());
                Log.d(str2, b.toString());
                if (th.getMessage() != null && !th.getMessage().equals("")) {
                    StringBuilder b2 = z5.b("payment init exception : ");
                    b2.append(th.getMessage());
                    str = b2.toString();
                } else {
                    str = "Init Payment method call failed ";
                }
                PaymentMethodFragment.this.setError(new PHResponse(-1, str, null), true);
            }

            @Override // retrofit2.Callback
            public void onResponse(Call<NewInitResponse> call, Response<NewInitResponse> response) {
                String str;
                if (response.body() != null && response.body().getStatus() == 1) {
                    Log.d(PaymentMethodFragment.this.TAG, "Init Payment method success");
                    PaymentMethodFragment.this.readInitResponse(response.body().getData());
                    return;
                }
                if (response.body() == null || response.body().getMsg() == null || response.body().getMsg().equals("")) {
                    str = "Init Payment method call failed ";
                } else {
                    str = response.body().getMsg();
                }
                Log.d(PaymentMethodFragment.this.TAG, "Init Payment method call failed ");
                PaymentMethodFragment.this.setError(new PHResponse(-1, str, null), true);
            }
        });
    }

    @Override // lk.payhere.androidsdk.PHMainActivity.OnActivityAction
    public boolean onActivityBack() {
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (PHMainActivity) getActivity();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.request = (InitBaseRequest) getArguments().getSerializable(PHConstants.INTENT_EXTRA_DATA);
        } else {
            Log.d(this.TAG, "Argument is null");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int screenHight = Utils.screenHight(this.activity);
        View inflate = layoutInflater.inflate(R.layout.ph_fragment_payment_method, viewGroup, false);
        this.view = inflate;
        NestedScrollView nestedScrollView = (NestedScrollView) inflate.findViewById(R.id.scroller_view);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) nestedScrollView.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).height = (int) (screenHight * 0.5d);
        nestedScrollView.setLayoutParams(layoutParams);
        this.cardPaymentList = (RecyclerView) this.view.findViewById(R.id.card_payment_list);
        this.otherPaymentListView = (RecyclerView) this.view.findViewById(R.id.other_payment_list);
        this.progressBar = (ImageView) this.view.findViewById(R.id.progress_view);
        Glide.with((FragmentActivity) this.activity).asGif().mo14load(Integer.valueOf(R.drawable.spinner_circle)).into(this.progressBar);
        createMethodAdapters();
        initRequest(this.request);
        getPaymentmethods();
        this.view.findViewById(R.id.helapay_image).setOnClickListener(new View.OnClickListener() { // from class: lk.payhere.androidsdk.fragment.PaymentMethodFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PaymentMethodFragment.this.activity.setPayDetailsView(PaymentMethodFragment.this.helaPayMethod, PaymentMethodFragment.this.request, PaymentMethodFragment.this.orderKey);
            }
        });
        return this.view;
    }

    @Override // lk.payhere.androidsdk.adapter.MethodAdapter.OnPaymentMethodClick
    public void onclick(NewInitResponse.PaymentMethod paymentMethod) {
        this.activity.setPayDetailsView(paymentMethod, this.request, this.orderKey);
    }
}
