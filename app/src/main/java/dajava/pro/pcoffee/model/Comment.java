package dajava.pro.pcoffee.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private User user;
    private String content;
    private Integer rating;
    private String dateTime;

    public Comment() {
    }

    public Comment(User user, String content, Integer rating) {
        this.user = user;
        this.content = content;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
