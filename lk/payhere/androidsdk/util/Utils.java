package lk.payhere.androidsdk.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.model.NewInitResponse;

/* loaded from: classes7.dex */
public class Utils {
    public static String getPayMethod(NewInitResponse.PaymentMethod paymentMethod) {
        if (paymentMethod.getMethod().toUpperCase().equals("HELA_PAY")) {
            return PHConstants.METHOD_BANK;
        }
        if (!paymentMethod.getSubmissionCode().toUpperCase().equals("MASTER") && !paymentMethod.getSubmissionCode().toUpperCase().equals("VISA") && !paymentMethod.getSubmissionCode().toUpperCase().equals("AMEX")) {
            return PHConstants.METHOD_OTHER;
        }
        return "CARD";
    }

    public static int screenHight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
