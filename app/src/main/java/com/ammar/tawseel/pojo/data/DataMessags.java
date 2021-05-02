package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMessags {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("unreaded")
    @Expose
    private String unreaded;
    @SerializedName("IDName")
    @Expose
    private String iDName;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUnreaded() {
        return unreaded;
    }

    public void setUnreaded(String unreaded) {
        this.unreaded = unreaded;
    }

    public String getIDName() {
        return iDName;
    }

    public void setIDName(String iDName) {
        this.iDName = iDName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
