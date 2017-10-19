package com.memolease.realmtoy.event;

public class PutChangeStateBookEvent {
    int bookid;
    int readState;

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }
}
