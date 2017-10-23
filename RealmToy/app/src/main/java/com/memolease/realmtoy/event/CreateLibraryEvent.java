package com.memolease.realmtoy.event;

/**
 * Created by bro on 2017-10-19.
 */

public class CreateLibraryEvent {
    String title;
    int libType; // 0 default, 1 내 기본 서재
    int type;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLibType() {
        return libType;
    }

    public void setLibType(int libType) {
        this.libType = libType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
