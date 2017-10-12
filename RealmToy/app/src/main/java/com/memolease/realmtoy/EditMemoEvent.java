package com.memolease.realmtoy;

import com.memolease.realmtoy.model.Memo;

/**
 * Created by bro on 2017-09-25.
 */

public class EditMemoEvent {
    String title;
    String content;
    String updatedAt;
    int position;

    public EditMemoEvent() {
    }

    public EditMemoEvent(String title, String content, String updatedAt, int position) {
        this.title = title;
        this.content = content;
        this.updatedAt = updatedAt;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setMemo(Memo memo) {
        this.title = memo.getTitle();
        this.content = memo.getContent();
        this.updatedAt = memo.getUpdatedAt();
    }
}
