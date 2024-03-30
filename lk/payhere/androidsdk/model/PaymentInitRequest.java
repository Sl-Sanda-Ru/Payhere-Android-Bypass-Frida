package lk.payhere.androidsdk.model;

import java.io.Serializable;
import java.util.HashMap;

/* loaded from: classes7.dex */
public class PaymentInitRequest implements Serializable {
    private String address;
    private double amount;
    private boolean auto;
    private String cancelUrl;
    private String city;
    private String country;
    private String currency;
    private String custom1;
    private String custom2;
    private String deliveryAddress;
    private String deliveryCity;
    private String deliveryCountry;
    private String duration;
    private String email;
    private String firstName;
    private String hash;
    private String itemsDescription;
    private HashMap<String, String> itemsMap;
    private String lastName;
    private String merchantId;
    private String notifyUrl;
    private String orderId;
    private String phone;
    private String platform;
    private String recurrence;
    private String referer;
    private String returnUrl;
    private double startupFee;

    public String getAddress() {
        return this.address;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getCancelUrl() {
        return this.cancelUrl;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
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

    public String getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public String getDeliveryCity() {
        return this.deliveryCity;
    }

    public String getDeliveryCountry() {
        return this.deliveryCountry;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getHash() {
        return this.hash;
    }

    public String getItemsDescription() {
        return this.itemsDescription;
    }

    public HashMap<String, String> getItemsMap() {
        return this.itemsMap;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPlatform() {
        return this.platform;
    }

    public String getRecurrence() {
        return this.recurrence;
    }

    public String getReferer() {
        return this.referer;
    }

    public String getReturnUrl() {
        return this.returnUrl;
    }

    public double getStartupFee() {
        return this.startupFee;
    }

    public boolean isAuto() {
        return this.auto;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public void setAmount(double d) {
        this.amount = d;
    }

    public void setAuto(boolean z) {
        this.auto = z;
    }

    public void setCancelUrl(String str) {
        this.cancelUrl = str;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCountry(String str) {
        this.country = str;
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

    public void setDeliveryAddress(String str) {
        this.deliveryAddress = str;
    }

    public void setDeliveryCity(String str) {
        this.deliveryCity = str;
    }

    public void setDeliveryCountry(String str) {
        this.deliveryCountry = str;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public void setHash(String str) {
        this.hash = str;
    }

    public void setItemsDescription(String str) {
        this.itemsDescription = str;
    }

    public void setItemsMap(HashMap<String, String> hashMap) {
        this.itemsMap = hashMap;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public void setMerchantId(String str) {
        this.merchantId = str;
    }

    public void setNotifyUrl(String str) {
        this.notifyUrl = str;
    }

    public void setOrderId(String str) {
        this.orderId = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public void setPlatform(String str) {
        this.platform = str;
    }

    public void setRecurrence(String str) {
        this.recurrence = str;
    }

    public void setReferer(String str) {
        this.referer = str;
    }

    public void setReturnUrl(String str) {
        this.returnUrl = str;
    }

    public void setStartupFee(double d) {
        this.startupFee = d;
    }
}
