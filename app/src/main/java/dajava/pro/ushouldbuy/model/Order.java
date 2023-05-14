package dajava.pro.ushouldbuy.model;

import java.util.Map;

public class Order {
    private String mCity;
    private String mName;
    private String mPaymentMethod;
    private String mPhone;
    private String mTotalCost;
    private Map<String, Integer> mItems;

    public Order(String city, String name, String paymentMethod, String phone, String totalCost, Map<String, Integer> items) {
        mCity = city;
        mName = name;
        mPaymentMethod = paymentMethod;
        mPhone = phone;
        mTotalCost = totalCost;
        mItems = items;
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

    public Map<String, Integer> getItems() {
        return mItems;
    }
}
