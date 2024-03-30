package lk.payhere.androidsdk.model;

import java.io.Serializable;

/* loaded from: classes7.dex */
public class Customer implements Serializable {
    private Address address;
    private Address deliveryAddress;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    private void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        if (this.address == null) {
            this.address = new Address();
        }
        return this.address;
    }

    public Address getDeliveryAddress() {
        if (this.deliveryAddress == null) {
            this.deliveryAddress = new Address();
        }
        return this.deliveryAddress;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }
}
