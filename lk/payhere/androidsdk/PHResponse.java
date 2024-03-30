package lk.payhere.androidsdk;

import java.io.Serializable;

/* loaded from: classes7.dex */
public class PHResponse<T> implements Serializable {
    public static final int STATUS_ERROR_CANCELED = -6;
    public static final int STATUS_ERROR_DATA = -2;
    public static final int STATUS_ERROR_NETWORK = -4;
    public static final int STATUS_ERROR_PAYMENT = -5;
    public static final int STATUS_ERROR_UNKNOWN = -1;
    public static final int STATUS_ERROR_VALIDATION = -3;
    public static final int STATUS_SESSION_TIME_OUT = -7;
    public static final int STATUS_SUCCESS = 1;
    private T data;
    private String message;
    private int status;

    public PHResponse(int i, String str) {
        this.status = i;
        this.message = str;
    }

    public T getData() {
        return this.data;
    }

    public int getStatus() {
        return this.status;
    }

    public boolean isSuccess() {
        return this.status == 1;
    }

    public String toString() {
        StringBuilder b = z5.b("PHResponse{status=");
        b.append(this.status);
        b.append(", message='");
        v60.b(b, this.message, '\'', ", data=");
        b.append(this.data);
        b.append('}');
        return b.toString();
    }

    public PHResponse(int i, String str, T t) {
        this.status = i;
        this.message = str;
        this.data = t;
    }
}
