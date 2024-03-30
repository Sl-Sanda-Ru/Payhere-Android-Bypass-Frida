package lk.payhere.androidsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.util.Locale;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.R;

/* loaded from: classes7.dex */
public class PaymentResultFragment extends Fragment implements PHMainActivity.OnActivityAction {
    private PHMainActivity activity;
    private int sec = 5;
    private boolean isAuto = false;

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRetry() {
        this.activity.setCloseBottomSheet(false);
        if (this.isAuto) {
            this.activity.setPayDetailsView("CREDIT / DEBIT CARD", "VISA");
        } else {
            this.activity.setPayMethod();
        }
    }

    @Override // lk.payhere.androidsdk.PHMainActivity.OnActivityAction
    public boolean onActivityBack() {
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (PHMainActivity) getActivity();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.activity = (PHMainActivity) getActivity();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        long j;
        String str;
        boolean z;
        int i;
        String string;
        if (getArguments() != null) {
            j = getArguments().getLong(PHConstants.INTENT_EXTRA_REFERENCE);
            this.isAuto = getArguments().getBoolean(PHConstants.INTENT_EXTRA_AUTO);
            z = getArguments().getBoolean(PHConstants.INTENT_EXTRA_HOLD);
            i = getArguments().getInt(PHConstants.INTENT_EXTRA_STATUS);
            str = getArguments().getString(PHConstants.INTENT_EXTRA_MESSAGE);
        } else {
            j = 0;
            str = null;
            z = false;
            i = 0;
        }
        View inflate = layoutInflater.inflate(R.layout.ph_fragment_payment_result, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.txt_result_id);
        TextView textView2 = (TextView) inflate.findViewById(R.id.txt_result_message);
        TextView textView3 = (TextView) inflate.findViewById(R.id.txt_result_close);
        TextView textView4 = (TextView) inflate.findViewById(R.id.retry_btn);
        TextView textView5 = (TextView) inflate.findViewById(R.id.txt_result_approved);
        if (i != 3 && i != 2) {
            ((ImageView) inflate.findViewById(R.id.img_result_done)).setImageDrawable(ContextCompat.getDrawable(this.activity, R.drawable.payment_declined_image));
            textView2.setText(R.string.decline_description_label);
            textView5.setText(R.string.label_declin_message);
            if (str != null && !str.equals("")) {
                textView.setText(String.format("Reason: %s", str));
                textView.setTextColor(getResources().getColor(R.color.color_red));
            } else {
                textView.setText("Payment Error");
            }
            textView4.setVisibility(0);
            ((TextView) inflate.findViewById(R.id.done_txt)).setText("Cancel");
        } else {
            inflate.findViewById(R.id.img_result_done).setVisibility(0);
            if (this.isAuto) {
                textView5.setText(R.string.label_saved_message);
            } else {
                textView5.setText(R.string.label_payment_approved);
            }
            if (!this.isAuto) {
                string = getString(R.string.payment_reference);
                textView5.setText(R.string.label_payment_approved);
            } else {
                string = getString(R.string.auto_reference);
                textView5.setText(R.string.label_saved_message);
            }
            textView.setText(String.format(Locale.getDefault(), string, String.valueOf(j)));
            if (z) {
                textView2.setText(R.string.hold_card_success_msg);
            } else if (this.isAuto) {
                textView2.setText(R.string.reference_message);
            } else {
                textView2.setText(R.string.payment_message);
            }
            textView.setTextColor(getResources().getColor(R.color.done_text_color));
        }
        Locale locale = Locale.getDefault();
        String string2 = getString(R.string.window_close_msg);
        int i2 = this.sec;
        this.sec = i2 - 1;
        textView3.setText(String.format(locale, string2, Integer.valueOf(i2)));
        inflate.findViewById(R.id.done_txt).setOnClickListener(new View.OnClickListener() { // from class: lk.payhere.androidsdk.fragment.PaymentResultFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PaymentResultFragment.this.activity.goBackToApp();
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() { // from class: lk.payhere.androidsdk.fragment.PaymentResultFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PaymentResultFragment.this.handleRetry();
            }
        });
        return inflate;
    }
}
