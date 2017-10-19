package com.memolease.realmtoy.event;

/**
 * Created by bro on 2017-10-19.
 */

public class CreateLibraryEvent {
    String title;
    int libType; // 0 default, 1 내 기본 서재


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
}
