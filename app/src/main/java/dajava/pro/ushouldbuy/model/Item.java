package dajava.pro.ushouldbuy.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Item implements Parcelable , Serializable , Cloneable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name,description;
    private String picId;
    private int totalInCart;
    private float price;

    public Item(String name, String picId, float price) {

        this.name = name;

        this.picId = picId;
        this.price = price;

    }

    public Item( Parcel in) {

        name=in.readString();
        price= in.readFloat();
        picId=in.readString();
        id = in.readInt();
        totalInCart=in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public int getTotalInCart() {
        return totalInCart;
    }

    public void setTotalInCart(int totalInCart) {
        this.totalInCart = totalInCart;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeString(picId);
        dest.writeInt(totalInCart);
        dest.writeInt(id);
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Item() {
    }
}
