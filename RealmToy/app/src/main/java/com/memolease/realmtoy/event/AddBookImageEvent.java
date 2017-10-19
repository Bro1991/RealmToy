package com.memolease.realmtoy.event;

/**
 * Created by bro on 2017-10-17.
 */

public class AddBookImageEvent {
    String isbn;
    String imagePath;
    int bookid;

    public AddBookImageEvent() {
    }

    public AddBookImageEvent(int bookid, String imagePath) {
        this.imagePath = imagePath;
        this.bookid = bookid;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }
}
