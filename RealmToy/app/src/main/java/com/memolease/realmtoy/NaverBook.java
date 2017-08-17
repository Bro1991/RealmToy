package com.memolease.realmtoy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bro on 2017-08-17.
 */

public class NaverBook {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("publisher")
    @Expose
    private String publisher;

    @SerializedName("pubdate")
    @Expose
    private String pubdate;

    @SerializedName("isbn")
    @Expose
    private String isbn;

    @SerializedName("description")
    @Expose
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
