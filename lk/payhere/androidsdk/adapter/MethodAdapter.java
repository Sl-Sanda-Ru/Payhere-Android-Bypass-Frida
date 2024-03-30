package lk.payhere.androidsdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import lk.payhere.androidsdk.R;
import lk.payhere.androidsdk.model.NewInitResponse;

/* loaded from: classes7.dex */
public class MethodAdapter extends RecyclerView.Adapter<MethoadViewHolder> {
    private final Context context;
    private final List<NewInitResponse.PaymentMethod> data;
    private OnPaymentMethodClick onPaymentMethodClick;

    /* loaded from: classes7.dex */
    public static class MethoadViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ConstraintLayout parent;

        public MethoadViewHolder(View view) {
            super(view);
            this.parent = (ConstraintLayout) view.findViewById(R.id.item_parent);
            this.imageView = (ImageView) view.findViewById(R.id.method_icon_image);
        }
    }

    /* loaded from: classes7.dex */
    public interface OnPaymentMethodClick {
        void onclick(NewInitResponse.PaymentMethod paymentMethod);
    }

    public MethodAdapter(Context context, List<NewInitResponse.PaymentMethod> list) {
        this.context = context;
        this.data = list;
    }

    private void mapMethodIcons(String str, ImageView imageView) {
        str.getClass();
        char c = 65535;
        switch (str.hashCode()) {
            case -2027938206:
                if (str.equals("MASTER")) {
                    c = 0;
                    break;
                }
                break;
            case -1762898670:
                if (str.equals("VISHWA")) {
                    c = 1;
                    break;
                }
                break;
            case 2012639:
                if (str.equals("AMEX")) {
                    c = 2;
                    break;
                }
                break;
            case 2634817:
                if (str.equals("VISA")) {
                    c = 3;
                    break;
                }
                break;
            case 67161945:
                if (str.equals("FRIMI")) {
                    c = 4;
                    break;
                }
                break;
            case 67702860:
                if (str.equals("GENIE")) {
                    c = 5;
                    break;
                }
                break;
            case 73172224:
                if (str.equals("MCASH")) {
                    c = 6;
                    break;
                }
                break;
            case 77264235:
                if (str.equals("QPLUS")) {
                    c = 7;
                    break;
                }
                break;
            case 1054421636:
                if (str.equals("LOLC_IPAY")) {
                    c = '\b';
                    break;
                }
                break;
            case 2060589416:
                if (str.equals("EZCASH")) {
                    c = '\t';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.master));
                return;
            case 1:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.vishwa));
                return;
            case 2:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.amex));
                return;
            case 3:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.visa));
                return;
            case 4:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.frimi));
                return;
            case 5:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.genie));
                return;
            case 6:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.mcash));
                return;
            case 7:
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.q_pay));
                return;
            case '\b':
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.methoad_background));
                return;
            case '\t':
                imageView.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ezcash));
                return;
            default:
                return;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.data.size();
    }

    public void setOnPaymentMethodClick(OnPaymentMethodClick onPaymentMethodClick) {
        this.onPaymentMethodClick = onPaymentMethodClick;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(MethoadViewHolder methoadViewHolder, int i) {
        final NewInitResponse.PaymentMethod paymentMethod = this.data.get(i);
        if (paymentMethod != null) {
            Glide.with(this.context).mo25load(paymentMethod.getView().getImageUrl()).into(methoadViewHolder.imageView);
        }
        methoadViewHolder.parent.setOnClickListener(new View.OnClickListener() { // from class: lk.payhere.androidsdk.adapter.MethodAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MethodAdapter.this.onPaymentMethodClick != null) {
                    MethodAdapter.this.onPaymentMethodClick.onclick(paymentMethod);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public MethoadViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MethoadViewHolder(LayoutInflater.from(this.context).inflate(R.layout.component_payment_method_item, viewGroup, false));
    }
}
