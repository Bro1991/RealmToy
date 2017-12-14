package com.memolease.realmtoy.model;


import com.memolease.realmtoy.network.networkModel.NaverBook;

import io.realm.RealmList;
import io.realm.RealmObject;
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
    int type; //0이면 빈 화면, 1이면 책이 있음
    int readState;

    String createAt;


    int libraryid;
    private String ImagePath;
    int rating;
    int index;

    public RealmList<Memo> memoList;
    public RealmList<PhotoMemo> photoMemoList;


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

    public RealmList<PhotoMemo> getPhotoMemoList() {
        return photoMemoList;
    }

    public void setPhotoMemoList(RealmList<PhotoMemo> photoMemoList) {
        this.photoMemoList = photoMemoList;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLibraryid() {
        return libraryid;
    }

    public void setLibraryid(int libraryid) {
        this.libraryid = libraryid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNaverBook(NaverBook naverBook) {
        this.title = naverBook.getTitle();
        this.sub_title = naverBook.getSub_title();
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
        this.readState = naverBook.getReadState();
        this.type = naverBook.getType();
    }

    public void setBook(Book book) {
        this.id = book.getId();
        this.type = book.getType();
        this.libraryid = book.getLibraryid();
        this.title = book.getTitle();
        this.sub_title = book.getSub_title();
        this.link = book.getLink();
        this.ImageURL = book.getImageURL();
        this.ImagePath = book.getImagePath();
        this.author = book.getAuthor();
        this.discount = book.getDiscount();
        this.price = book.getPrice();
        this.publisher = book.getPublisher();
        this.pubdate = book.getPubdate();
        this.isbn = book.getIsbn();
        this.isbn13 = book.getIsbn13();
        this.description = book.getDescription();
        this.readState = book.getReadState();
    }
}