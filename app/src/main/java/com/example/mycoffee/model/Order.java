package com.example.mycoffee.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 2L;

    private String mCity;
    private String mName;
    private String mPaymentMethod;
    private String mPhone;
    private String mTotalCost;
    private String dateCreate;
    private List<Item> mItems;
    private String status;
    private Long id;
    private boolean notify;


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

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
