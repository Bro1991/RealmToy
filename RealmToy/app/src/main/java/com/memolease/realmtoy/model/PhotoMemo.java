package com.memolease.realmtoy.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bro on 2017-10-18.
 */

public class PhotoMemo extends RealmObject {
    @PrimaryKey
    private int id;
    private int bookid;
    private String createAt;
    //String updatedAt; 사진메모가 수정된 날자, 필요할까?
    private String ImagePath;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
