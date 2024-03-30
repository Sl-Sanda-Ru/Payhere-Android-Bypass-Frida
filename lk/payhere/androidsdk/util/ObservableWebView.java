package lk.payhere.androidsdk.util;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/* loaded from: classes7.dex */
public class ObservableWebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    /* loaded from: classes7.dex */
    public interface OnScrollChangedCallback {
        void onScrollTopEnd(boolean z);
    }

    public ObservableWebView(Context context) {
        super(context);
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return this.mOnScrollChangedCallback;
    }

    @Override // android.webkit.WebView, android.view.View
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        boolean z;
        super.onScrollChanged(i, i2, i3, i4);
        z5.d("Scroll location t = ", i2, "ObservableWebView");
        OnScrollChangedCallback onScrollChangedCallback = this.mOnScrollChangedCallback;
        if (onScrollChangedCallback != null) {
            if (i2 == 0) {
                z = true;
            } else {
                z = false;
            }
            onScrollChangedCallback.onScrollTopEnd(z);
        }
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback onScrollChangedCallback) {
        this.mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public ObservableWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ObservableWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
