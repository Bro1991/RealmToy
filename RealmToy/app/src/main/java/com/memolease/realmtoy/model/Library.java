package com.memolease.realmtoy.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bro on 2017-10-13.
 */

public class Library extends RealmObject {
    @PrimaryKey
    private int id;

    private String title;
    private int category;
    public RealmList<Book> bookRealmList;
    private int index;

    //네비게이션 드로어에서 책장과 독서상태 목록을 나누기 위한 type값
    int type;

    int read_state;

    //기본책장 값은 1, 개인이 추가 가능한 책장 값은 0으로 구분..!
    // 0 default = 0, 내 기본 서재 = 1
    int libType;
    Date createAt;
    Date updatedAt;
    int bookCount;


    public Library() {
        type = 0;
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

    public int getRead_state() {
        return read_state;
    }

    public void setRead_state(int read_state) {
        this.read_state = read_state;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLibType() {
        return libType;
    }

    public void setLibType(int libType) {
        this.libType = libType;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
