package com.zgqf.house.entity;

import java.util.Date;

public class Contract {
    private Integer c_id;
    private Integer c_buyer_id;
    private Integer c_house_id;
    private Double c_total_price;
    private String c_pay_way;
    private Date c_paytime_ending;
    private Date c_paytime_actually;
    private Date c_delivery_ending;
    private Date c_delivery_actually;
    private Integer c_buyer_agree;
    private Integer c_seller_agree;
    private Integer c_paid;
    private Integer c_delivered;

    // Getters and Setters
    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public Integer getC_buyer_id() {
        return c_buyer_id;
    }

    public void setC_buyer_id(Integer c_buyer_id) {
        this.c_buyer_id = c_buyer_id;
    }

    public Integer getC_house_id() {
        return c_house_id;
    }

    public void setC_house_id(Integer c_house_id) {
        this.c_house_id = c_house_id;
    }

    public Double getC_total_price() {
        return c_total_price;
    }

    public void setC_total_price(Double c_total_price) {
        this.c_total_price = c_total_price;
    }

    public String getC_pay_way() {
        return c_pay_way;
    }

    public void setC_pay_way(String c_pay_way) {
        this.c_pay_way = c_pay_way;
    }

    public Date getC_paytime_ending() {
        return c_paytime_ending;
    }

    public void setC_paytime_ending(Date c_paytime_ending) {
        this.c_paytime_ending = c_paytime_ending;
    }

    public Date getC_paytime_actually() {
        return c_paytime_actually;
    }

    public void setC_paytime_actually(Date c_paytime_actually) {
        this.c_paytime_actually = c_paytime_actually;
    }

    public Date getC_delivery_ending() {
        return c_delivery_ending;
    }

    public void setC_delivery_ending(Date c_delivery_ending) {
        this.c_delivery_ending = c_delivery_ending;
    }

    public Date getC_delivery_actually() {
        return c_delivery_actually;
    }

    public void setC_delivery_actually(Date c_delivery_actually) {
        this.c_delivery_actually = c_delivery_actually;
    }

    public Integer getC_buyer_agree() {
        return c_buyer_agree;
    }

    public void setC_buyer_agree(Integer c_buyer_agree) {
        this.c_buyer_agree = c_buyer_agree;
    }

    public Integer getC_seller_agree() {
        return c_seller_agree;
    }

    public void setC_seller_agree(Integer c_seller_agree) {
        this.c_seller_agree = c_seller_agree;
    }

    public Integer getC_paid() {
        return c_paid;
    }

    public void setC_paid(Integer c_paid) {
        this.c_paid = c_paid;
    }

    public Integer getC_delivered() {
        return c_delivered;
    }

    public void setC_delivered(Integer c_delivered) {
        this.c_delivered = c_delivered;
    }
}