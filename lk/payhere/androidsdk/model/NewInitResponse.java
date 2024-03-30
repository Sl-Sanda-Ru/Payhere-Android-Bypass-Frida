package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.List;

/* loaded from: classes7.dex */
public class NewInitResponse implements Serializable {
    private Data data;
    private String msg;
    private int status;

    /* loaded from: classes7.dex */
    public static class Business implements Serializable {
        private String logo;
        private String name;
        private String primaryColor;
        private String textColor;

        public String getLogo() {
            return this.logo;
        }

        public String getName() {
            return this.name;
        }

        public String getPrimaryColor() {
            return this.primaryColor;
        }

        public String getTextColor() {
            return this.textColor;
        }

        public void setLogo(String str) {
            this.logo = str;
        }

        public void setName(String str) {
            this.name = str;
        }

        public void setPrimaryColor(String str) {
            this.primaryColor = str;
        }

        public void setTextColor(String str) {
            this.textColor = str;
        }
    }

    /* loaded from: classes7.dex */
    public static class Data implements Serializable {
        private Business business;
        private Order order;
        private List<PaymentMethod> paymentMethods;
        private String type;

        public Business getBusiness() {
            return this.business;
        }

        public Order getOrder() {
            return this.order;
        }

        public List<PaymentMethod> getPaymentMethods() {
            return this.paymentMethods;
        }

        public String getType() {
            return this.type;
        }

        public void setBusiness(Business business) {
            this.business = business;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public void setPaymentMethods(List<PaymentMethod> list) {
            this.paymentMethods = list;
        }

        public void setType(String str) {
            this.type = str;
        }
    }

    /* loaded from: classes7.dex */
    public static class MobileUrls implements Serializable {
        private String IOS;

        public String getIOS() {
            return this.IOS;
        }

        public void setIOS(String str) {
            this.IOS = str;
        }
    }

    /* loaded from: classes7.dex */
    public static class Order implements Serializable {
        private int amount;
        private String amountFormatted;
        private String currency;
        private String currencyFormatted;
        private String longDescription;
        private String orderKey;
        private String shortDescription;

        public int getAmount() {
            return this.amount;
        }

        public String getAmountFormatted() {
            return this.amountFormatted;
        }

        public String getCurrency() {
            return this.currency;
        }

        public String getCurrencyFormatted() {
            return this.currencyFormatted;
        }

        public String getLongDescription() {
            return this.longDescription;
        }

        public String getOrderKey() {
            return this.orderKey;
        }

        public String getShortDescription() {
            return this.shortDescription;
        }

        public void setAmount(int i) {
            this.amount = i;
        }

        public void setAmountFormatted(String str) {
            this.amountFormatted = str;
        }

        public void setCurrency(String str) {
            this.currency = str;
        }

        public void setCurrencyFormatted(String str) {
            this.currencyFormatted = str;
        }

        public void setLongDescription(String str) {
            this.longDescription = str;
        }

        public void setOrderKey(String str) {
            this.orderKey = str;
        }

        public void setShortDescription(String str) {
            this.shortDescription = str;
        }
    }

    /* loaded from: classes7.dex */
    public static class PaymentMethod implements Serializable {
        private String discount;
        private String method;
        private int orderNo;
        private Submission submission;
        private String submissionCode;
        private View view;

        public String getDiscount() {
            return this.discount;
        }

        public String getMethod() {
            return this.method;
        }

        public int getOrderNo() {
            return this.orderNo;
        }

        public Submission getSubmission() {
            return this.submission;
        }

        public String getSubmissionCode() {
            return this.submissionCode;
        }

        public View getView() {
            return this.view;
        }

        public void setDiscount(String str) {
            this.discount = str;
        }

        public void setMethod(String str) {
            this.method = str;
        }

        public void setOrderNo(int i) {
            this.orderNo = i;
        }

        public void setSubmission(Submission submission) {
            this.submission = submission;
        }

        public void setSubmissionCode(String str) {
            this.submissionCode = str;
        }

        public void setView(View view) {
            this.view = view;
        }
    }

    /* loaded from: classes7.dex */
    public static class Submission implements Serializable {
        private MobileUrls mobileUrls;
        private String redirectType;
        private String url;

        public MobileUrls getMobileUrls() {
            return this.mobileUrls;
        }

        public String getRedirectType() {
            return this.redirectType;
        }

        public String getUrl() {
            return this.url;
        }

        public void setMobileUrls(MobileUrls mobileUrls) {
            this.mobileUrls = mobileUrls;
        }

        public void setRedirectType(String str) {
            this.redirectType = str;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    /* loaded from: classes7.dex */
    public static class View implements Serializable {
        private String imageUrl;
        private WindowSize windowSize;

        public String getImageUrl() {
            return this.imageUrl;
        }

        public WindowSize getWindowSize() {
            return this.windowSize;
        }

        public void setImageUrl(String str) {
            this.imageUrl = str;
        }

        public void setWindowSize(WindowSize windowSize) {
            this.windowSize = windowSize;
        }
    }

    /* loaded from: classes7.dex */
    public static class WindowSize implements Serializable {
        private int height;
        private int width;

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        public void setHeight(int i) {
            this.height = i;
        }

        public void setWidth(int i) {
            this.width = i;
        }
    }

    public Data getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public void setStatus(int i) {
        this.status = i;
    }
}
