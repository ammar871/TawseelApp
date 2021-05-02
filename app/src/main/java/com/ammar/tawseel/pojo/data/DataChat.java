package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataChat {

    @SerializedName("driver")
    @Expose
    private DriverChat driver;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;

    public DriverChat getDriver() {
        return driver;
    }

    public void setDriver(DriverChat driver) {
        this.driver = driver;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
