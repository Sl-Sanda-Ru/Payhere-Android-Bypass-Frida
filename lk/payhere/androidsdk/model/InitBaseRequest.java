package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes7.dex */
public class InitBaseRequest implements Serializable {
    private double amount;
    private String cancelUrl;
    private String currency;
    private String custom1;
    private String custom2;
    private Customer customer;
    private boolean internal = false;
    private boolean isHoldOnCardEnabled;
    private List<Item> items;
    private String itemsDescription;
    private String merchantId;
    private String merchantSecret;
    private String notifyUrl;
    private String orderId;
    private String returnUrl;

    public double getAmount() {
        return this.amount;
    }

    public String getCancelUrl() {
        return this.cancelUrl;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getCustom1() {
        return this.custom1;
    }

    public String getCustom2() {
        return this.custom2;
    }

    public Customer getCustomer() {
        if (this.customer == null) {
            this.customer = new Customer();
        }
        return this.customer;
    }

    public List<Item> getItems() {
        if (this.items == null) {
            this.items = new ArrayList();
        }
        return this.items;
    }

    public String getItemsDescription() {
        return this.itemsDescription;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getMerchantSecret() {
        return this.merchantSecret;
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getReturnUrl() {
        return this.returnUrl;
    }

    public boolean isHoldOnCardEnabled() {
        return this.isHoldOnCardEnabled;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public void setAmount(double d) {
        this.amount = d;
    }

    public void setCancelUrl(String str) {
        this.cancelUrl = str;
    }

    public void setCurrency(String str) {
        this.currency = str;
    }

    public void setCustom1(String str) {
        this.custom1 = str;
    }

    public void setCustom2(String str) {
        this.custom2 = str;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setHoldOnCardEnabled(boolean z) {
        this.isHoldOnCardEnabled = z;
    }

    public void setInternal(boolean z) {
        this.internal = z;
    }

    public void setItems(List<Item> list) {
        this.items = list;
    }

    public void setItemsDescription(String str) {
        this.itemsDescription = str;
    }

    public void setMerchantId(String str) {
        this.merchantId = str;
    }

    public void setMerchantSecret(String str) {
        this.merchantSecret = str;
    }

    public void setNotifyUrl(String str) {
        this.notifyUrl = str;
    }

    public void setOrderId(String str) {
        this.orderId = str;
    }

    public void setReturnUrl(String str) {
        this.returnUrl = str;
    }
}
