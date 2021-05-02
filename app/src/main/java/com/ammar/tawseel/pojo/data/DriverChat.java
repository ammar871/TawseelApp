package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverChat {
    @SerializedName("IDName")
    @Expose
    private String iDName;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stars")
    @Expose
    private Integer stars;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }


}
