package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriversSearch {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("gpsLat")
    @Expose
    private Double gpsLat;
    @SerializedName("gpsLng")
    @Expose
    private Double gpsLng;
    @SerializedName("gpsAddress")
    @Expose
    private String gpsAddress;

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

    public Double getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(Double gpsLat) {
        this.gpsLat = gpsLat;
    }

    public Double getGpsLng() {
        return gpsLng;
    }

    public void setGpsLng(Double gpsLng) {
        this.gpsLng = gpsLng;
    }

    public String getGpsAddress() {
        return gpsAddress;
    }

    public void setGpsAddress(String gpsAddress) {
        this.gpsAddress = gpsAddress;
    }


}
