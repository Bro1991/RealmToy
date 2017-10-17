package com.memolease.realmtoy;

import java.io.File;

/**
 * Created by bro on 2017-10-17.
 */

public class AddBookImageEvent {
    String isbn;
    byte[] bytes;
    String imagePath;

    public AddBookImageEvent() {
    }

    public AddBookImageEvent(String isbn, byte[] bytes) {
        this.isbn = isbn;
        this.bytes = bytes;
    }

    public AddBookImageEvent(String isbn, String imagePath) {
        this.isbn = isbn;
        this.imagePath = imagePath;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
