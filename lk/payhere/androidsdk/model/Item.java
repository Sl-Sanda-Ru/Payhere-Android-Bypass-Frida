package lk.payhere.androidsdk.model;

import java.io.Serializable;

/* loaded from: classes7.dex */
public class Item implements Serializable {
    private double amount;
    private String id;
    private String name;
    private int quantity;

    public Item() {
    }

    public double getAmount() {
        return this.amount;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setAmount(double d) {
        this.amount = d;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }

    public Item(String str, String str2, int i, double d) {
        this.id = str;
        this.name = str2;
        this.quantity = i;
        this.amount = d;
    }
}
