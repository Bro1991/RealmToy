package com.memolease.realmtoy.network.networkModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by bro on 2017-08-17.
 */

public class NaverBook{
    int id;

    @SerializedName("title")
    @Expose
    private String title;

    private String sub_title;

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

    private String isbn13;

    @SerializedName("description")
    @Expose
    private String description;

    private boolean exist_state;
    int type; //0이면 빈 화면, 1이면 책이 있음

    int readState;

    int libraryid;

    public int getLibraryid() {
        return libraryid;
    }

    public void setLibraryid(int libraryid) {
        this.libraryid = libraryid;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public boolean getExist_state() {
        return exist_state;
    }

    public void setExist_state(boolean exist_state) {
        this.exist_state = exist_state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
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

    public String getPubdateWithDot() {
        StringBuilder builder = new StringBuilder(pubdate);
        builder.insert(4, ". ");
        builder.insert(8, ". ");
        return builder.toString();
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

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
