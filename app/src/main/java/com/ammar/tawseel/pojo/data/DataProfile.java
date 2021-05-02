package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataProfile {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private Object username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gpsLng")
    @Expose
    private Object gpsLng;
    @SerializedName("gpsLat")
    @Expose
    private Object gpsLat;
    @SerializedName("gpsAddress")
    @Expose
    private Object gpsAddress;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private Object phone;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getGpsLng() {
        return gpsLng;
    }

    public void setGpsLng(Object gpsLng) {
        this.gpsLng = gpsLng;
    }

    public Object getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(Object gpsLat) {
        this.gpsLat = gpsLat;
    }

    public Object getGpsAddress() {
        return gpsAddress;
    }

    public void setGpsAddress(Object gpsAddress) {
        this.gpsAddress = gpsAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
