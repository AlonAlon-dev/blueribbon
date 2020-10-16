package com.blueribbon.data;

public class Coupon {
    private long couponId;
    private double price;

    public Coupon(long couponId) {
        this.couponId = couponId;
    }

    public long getCouponId() {
        return couponId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
