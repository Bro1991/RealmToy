package com.memolease.realmtoy.event;

/**
 * Created by bro on 2017-08-18.
 */

public class NextSearchBookEvent {
    String query;
    int start;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
