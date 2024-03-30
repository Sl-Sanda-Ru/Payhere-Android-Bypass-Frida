package lk.payhere.androidsdk.model;

/* loaded from: classes7.dex */
public class PaymentInitResult {
    private Data data;
    private String msg;
    private int status;

    /* loaded from: classes7.dex */
    public class Business {
        private String logo;
        private String name;

        public Business() {
        }

        public String getLogo() {
            return this.logo;
        }

        public String getName() {
            return this.name;
        }

        public void setLogo(String str) {
            this.logo = str;
        }

        public void setName(String str) {
            this.name = str;
        }
    }

    /* loaded from: classes7.dex */
    public class Data {
        private Business business;
        private Order order;
        private String[] paymentMethods;
        private Redirection redirection;

        public Data() {
        }

        public Business getBusiness() {
            return this.business;
        }

        public Order getOrder() {
            return this.order;
        }

        public String[] getPaymentMethods() {
            return this.paymentMethods;
        }

        public Redirection getRedirection() {
            return this.redirection;
        }

        public void setBusiness(Business business) {
            this.business = business;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public void setPaymentMethods(String[] strArr) {
            this.paymentMethods = strArr;
        }

        public void setRedirection(Redirection redirection) {
            this.redirection = redirection;
        }
    }

    /* loaded from: classes7.dex */
    public class Order {
        private long amount;
        private String amountFormatted;
        private String currency;
        private String currencyFormatted;
        private String longDescription;
        private String orderKey;
        private String shortDescription;

        public Order() {
        }

        public long getAmount() {
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

        public void setAmount(long j) {
            this.amount = j;
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
    public class Redirection {
        private String redirectType;
        private String url;

        public Redirection() {
        }

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
