package lk.payhere.androidsdk.model;

/* loaded from: classes7.dex */
public class InitRequest extends InitBaseRequest {
    private String duration;
    private String recurrence;
    private double startupFee;

    public String getDuration() {
        return this.duration;
    }

    public String getRecurrence() {
        return this.recurrence;
    }

    public double getStartupFee() {
        return this.startupFee;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public void setRecurrence(String str) {
        this.recurrence = str;
    }

    public void setStartupFee(double d) {
        this.startupFee = d;
    }
}
