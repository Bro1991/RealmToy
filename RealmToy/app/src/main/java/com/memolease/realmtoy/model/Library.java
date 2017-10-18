package com.memolease.realmtoy.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bro on 2017-10-13.
 */

public class Library extends RealmObject {
    @PrimaryKey
    private int id;

    private String name;
    private int category;
    public RealmList<Book> bookRealmList;
    private int index;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public RealmList<Book> getBookRealmList() {
        return bookRealmList;
    }

    public void setBookRealmList(RealmList<Book> bookRealmList) {
        this.bookRealmList = bookRealmList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
