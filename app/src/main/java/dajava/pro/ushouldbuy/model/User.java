package dajava.pro.ushouldbuy.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id,email,phone,fullname, image, password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String id, String email, String phone, String fullname) {
        this.email = email;
        this.phone = phone;
        this.fullname = fullname;
        this.id = id;
    }

    public User(String email, String phone, String fullname) {
        this.email = email;
        this.phone = phone;
        this.fullname = fullname;
    }

    public User(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User(){

    }
}
