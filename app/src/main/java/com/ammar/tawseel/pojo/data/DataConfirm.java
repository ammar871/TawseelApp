package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataConfirm {

    @SerializedName("driver_id")
    @Expose
    private Integer driverId;

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }
}
