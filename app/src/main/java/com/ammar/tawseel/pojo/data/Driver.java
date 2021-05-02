package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("gpsLat")
    @Expose
    private Object gpsLat;
    @SerializedName("gpsLng")
    @Expose
    private Object gpsLng;
    @SerializedName("gpsAddress")
    @Expose
    private Object gpsAddress;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(Object gpsLat) {
        this.gpsLat = gpsLat;
    }

    public Object getGpsLng() {
        return gpsLng;
    }

    public void setGpsLng(Object gpsLng) {
        this.gpsLng = gpsLng;
    }

    public Object getGpsAddress() {
        return gpsAddress;
    }

    public void setGpsAddress(Object gpsAddress) {
        this.gpsAddress = gpsAddress;
    }
}
