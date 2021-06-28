package com.ammar.tawseel.pojo.data;

import android.app.Notification;

import java.util.List;

public class ModelNotifyDate {

  private   String title;
    private List<DataNotification> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DataNotification> getList() {
        return list;
    }

    public void setList(List<DataNotification> list) {
        this.list = list;
    }
}
