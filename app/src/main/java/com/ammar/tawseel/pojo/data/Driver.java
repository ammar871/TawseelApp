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
    private Double gpsLat;
    @SerializedName("gpsLng")
    @Expose
    private Double gpsLng;
    @SerializedName("gpsAddress")
    @Expose
    private Object gpsAddress;
    @SerializedName("distance")
    @Expose
    private Double distance;

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

    public Object getGpsAddress() {
        return gpsAddress;
    }

    public void setGpsAddress(Object gpsAddress) {
        this.gpsAddress = gpsAddress;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
