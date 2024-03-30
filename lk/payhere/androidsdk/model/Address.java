package lk.payhere.androidsdk.model;

import java.io.Serializable;

/* loaded from: classes7.dex */
public class Address implements Serializable {
    private String address;
    private String city;
    private String country;

    public String getAddress() {
        return this.address;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCountry(String str) {
        this.country = str;
    }
}
