package com.ammar.tawseel.pojo.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataOrder implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("from_lat")
    @Expose
    private String fromLat;
    @SerializedName("from_lng")
    @Expose
    private String fromLng;
    @SerializedName("from_address")
    @Expose
    private String fromAddress;
    @SerializedName("to_lat")
    @Expose
    private String toLat;
    @SerializedName("to_lng")
    @Expose
    private String toLng;
    @SerializedName("to_address")
    @Expose
    private String toAddress;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("IDName")
    @Expose
    private String iDName;
    @SerializedName("current_lng")
    @Expose
    private String currentLng;
    @SerializedName("current_lat")
    @Expose
    private String currentLat;
    @SerializedName("bill_id")
    @Expose
    private String billId;

    protected DataOrder(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        userId = in.readString();
        driverId = in.readString();
        status = in.readString();
        price = in.readString();
        fromLat = in.readString();
        fromLng = in.readString();
        fromAddress = in.readString();
        toLat = in.readString();
        toLng = in.readString();
        toAddress = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        iDName = in.readString();
        currentLng = in.readString();
        currentLat = in.readString();
        billId = in.readString();
    }

    public static final Creator<DataOrder> CREATOR = new Creator<DataOrder>() {
        @Override
        public DataOrder createFromParcel(Parcel in) {
            return new DataOrder(in);
        }

        @Override
        public DataOrder[] newArray(int size) {
            return new DataOrder[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFromLat() {
        return fromLat;
    }

    public void setFromLat(String fromLat) {
        this.fromLat = fromLat;
    }

    public String getFromLng() {
        return fromLng;
    }

    public void setFromLng(String fromLng) {
        this.fromLng = fromLng;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToLat() {
        return toLat;
    }

    public void setToLat(String toLat) {
        this.toLat = toLat;
    }

    public String getToLng() {
        return toLng;
    }

    public void setToLng(String toLng) {
        this.toLng = toLng;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
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

    public String getIDName() {
        return iDName;
    }

    public void setIDName(String iDName) {
        this.iDName = iDName;
    }

    public String getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(String currentLng) {
        this.currentLng = currentLng;
    }

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(userId);
        dest.writeString(driverId);
        dest.writeString(status);
        dest.writeString(price);
        dest.writeString(fromLat);
        dest.writeString(fromLng);
        dest.writeString(fromAddress);
        dest.writeString(toLat);
        dest.writeString(toLng);
        dest.writeString(toAddress);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(iDName);
        dest.writeString(currentLng);
        dest.writeString(currentLat);
        dest.writeString(billId);
    }
}
