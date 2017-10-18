package com.memolease.realmtoy.model;


import com.memolease.realmtoy.NaverBook;

import java.io.File;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bro on 2017-09-21.
 */

public class Book extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String sub_title;
    private String link;
    private String ImageURL;
    private String author;
    private String price;
    private String discount;
    private String publisher;
    private String pubdate;
    private String isbn;
    private String isbn13;
    private String description;
    int readState;

    private String ImagePath;
    public RealmList<Memo> memoList;

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

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
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

    public RealmList<Memo> getMemoList() {
        return memoList;
    }

    public void setMemoList(RealmList<Memo> memoList) {
        this.memoList = memoList;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public void setNaverBook(NaverBook naverBook) {
        this.title = naverBook.getTitle();
        this.link = naverBook.getLink();
        this.ImageURL = naverBook.getImage();
        this.author = naverBook.getAuthor();
        this.discount = naverBook.getDiscount();
        this.price = naverBook.getPrice();
        this.publisher = naverBook.getPublisher();
        this.pubdate = naverBook.getPubdate();
        this.isbn = naverBook.getIsbn();
        this.isbn13 = naverBook.getIsbn13();
        this.description = naverBook.getDescription();
    }
}