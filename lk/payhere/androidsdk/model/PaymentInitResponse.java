package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.List;

/* loaded from: classes7.dex */
public class PaymentInitResponse implements Serializable {
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
        public Business business;
        public Order order;
        public List<PaymentMethod> paymentMethods;
        public String type;

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
        private String discountPercentage;
        private String method;
        private Submission submission;

        public String getDiscountPercentage() {
            return this.discountPercentage;
        }

        public String getMethod() {
            return this.method;
        }

        public Submission getSubmission() {
            return this.submission;
        }

        public void setDiscountPercentage(String str) {
            this.discountPercentage = str;
        }

        public void setMethod(String str) {
            this.method = str;
        }

        public void setSubmission(Submission submission) {
            this.submission = submission;
        }
    }

    /* loaded from: classes7.dex */
    public static class Submission implements Serializable {
        private String redirectType;
        private String url;

        public String getRedirectType() {
            return this.redirectType;
        }

        public String getUrl() {
            return this.url;
        }

        public void setRedirectType(String str) {
            this.redirectType = str;
        }

        public void setUrl(String str) {
            this.url = str;
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
