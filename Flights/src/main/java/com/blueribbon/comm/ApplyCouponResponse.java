package com.blueribbon.comm;

public class ApplyCouponResponse {
    public enum Status {
        OK,
        COUPON_ID_NOT_FOUND,
        WRONG_PRICE
    }

    private ApplyCouponResponse.Status status;
    private double priceAfterDiscount;

    public ApplyCouponResponse.Status getStatus() {
        return status;
    }

    public void setStatus(ApplyCouponResponse.Status status) {
        this.status = status;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }
}
