package com.ammar.tawseel.pojo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayDataMethod {
    @SerializedName("PaymentMethodId")
    @Expose
    private Integer paymentMethodId;
    @SerializedName("PaymentMethodAr")
    @Expose
    private String paymentMethodAr;
    @SerializedName("PaymentMethodEn")
    @Expose
    private String paymentMethodEn;
    @SerializedName("PaymentMethodCode")
    @Expose
    private String paymentMethodCode;
    @SerializedName("IsDirectPayment")
    @Expose
    private Boolean isDirectPayment;
    @SerializedName("ServiceCharge")
    @Expose
    private Double serviceCharge;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("CurrencyIso")
    @Expose
    private Object currencyIso;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodAr() {
        return paymentMethodAr;
    }

    public void setPaymentMethodAr(String paymentMethodAr) {
        this.paymentMethodAr = paymentMethodAr;
    }

    public String getPaymentMethodEn() {
        return paymentMethodEn;
    }

    public void setPaymentMethodEn(String paymentMethodEn) {
        this.paymentMethodEn = paymentMethodEn;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public Boolean getIsDirectPayment() {
        return isDirectPayment;
    }

    public void setIsDirectPayment(Boolean isDirectPayment) {
        this.isDirectPayment = isDirectPayment;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Object getCurrencyIso() {
        return currencyIso;
    }

    public void setCurrencyIso(Object currencyIso) {
        this.currencyIso = currencyIso;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}