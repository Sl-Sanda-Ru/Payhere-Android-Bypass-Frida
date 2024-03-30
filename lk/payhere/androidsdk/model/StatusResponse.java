package lk.payhere.androidsdk.model;

import java.io.Serializable;

/* loaded from: classes7.dex */
public class StatusResponse implements Serializable {
    private String currency;
    private String message;
    private long paymentNo;
    private long price;
    private String sign;
    private int status;

    /* loaded from: classes7.dex */
    public enum Status {
        INIT(0),
        PAYMENT(1),
        SUCCESS(2),
        HOLD(3),
        FAILED(-2),
        CANCELED(-6);
        
        private int value;

        Status(int i) {
            this.value = i;
        }

        public static Status getStatus(int i) {
            Status[] values;
            for (Status status : values()) {
                if (status.value() == i) {
                    return status;
                }
            }
            return null;
        }

        public int value() {
            return this.value;
        }
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getMessage() {
        return this.message;
    }

    public long getPaymentNo() {
        return this.paymentNo;
    }

    public long getPrice() {
        return this.price;
    }

    public String getSign() {
        return this.sign;
    }

    public int getStatus() {
        return this.status;
    }

    public Status getStatusState() {
        return Status.getStatus(this.status);
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public void setPaymentNo(long j) {
        this.paymentNo = j;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String toString() {
        StringBuilder b = z5.b("StatusResponse{status=");
        b.append(this.status);
        b.append(", paymentNo=");
        b.append(this.paymentNo);
        b.append('}');
        return b.toString();
    }
}
