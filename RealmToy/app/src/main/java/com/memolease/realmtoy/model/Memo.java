package com.memolease.realmtoy.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bro on 2017-09-21.
 */

public class Memo extends RealmObject {
    @PrimaryKey
    private int id;
    private int bookid;
    private String title;
    private String content;
    private String updatedAt;

    public Memo() {
    }

    public Memo(String title, String content, String updatedAt) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
