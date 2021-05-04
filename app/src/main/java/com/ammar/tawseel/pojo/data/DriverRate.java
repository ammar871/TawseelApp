package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverRate {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("IDName")
    @Expose
    private String iDName;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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