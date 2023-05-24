package dajava.pro.pcoffee.model;
import android.os.Parcel;
import java.io.Serializable;

public class Item implements Serializable , Cloneable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name,description;
    private String picId;
    private int totalInCart;
    private Integer price;

    public Item(String name, String picId, Integer price) {

        this.name = name;

        this.picId = picId;
        this.price = price;

    }

    public Item( Parcel in) {

        name=in.readString();
        price= in.readInt();
        picId=in.readString();
        id = in.readInt();
        totalInCart=in.readInt();
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Item() {
    }
}
