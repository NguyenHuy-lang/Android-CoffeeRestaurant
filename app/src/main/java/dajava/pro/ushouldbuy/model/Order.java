package dajava.pro.ushouldbuy.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {
    private static final long serialVersionUID = 2L;

    private String mCity;
    private String mName;
    private String mPaymentMethod;
    private String mPhone;
    private String mTotalCost;
    private List<Item> mItems;

    public Order(String city, String name, String paymentMethod, String phone, String totalCost, List<Item> items) {
        mCity = city;
        mName = name;
        mPaymentMethod = paymentMethod;
        mPhone = phone;
        mTotalCost = totalCost;
        mItems = items;
    }
    public Order(){

    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPaymentMethod(String mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public void setmTotalCost(String mTotalCost) {
        this.mTotalCost = mTotalCost;
    }

    public void setmItems(List<Item> mItems) {
        this.mItems = mItems;
    }

    public String getCity() {
        return mCity;
    }

    public String getName() {
        return mName;
    }

    public String getPaymentMethod() {
        return mPaymentMethod;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getTotalCost() {
        return mTotalCost;
    }

    public List<Item> getItems() {
        return mItems;
    }
}
