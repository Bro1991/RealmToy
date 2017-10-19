package com.memolease.realmtoy.event;

/**
 * Created by bro on 2017-10-19.
 */

public class EditLibraryEvent {
    int id;
    String title;

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
}
