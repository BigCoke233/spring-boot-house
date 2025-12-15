package com.zgqf.house.entity;

public class House {
    private Integer h_id;
    private Integer h_seller_id;
    private String h_name;
    private String h_describe;
    private String h_address;
    private String h_detail_address;
    private Double h_price;
    private Double h_longitude;
    private Double h_latitude;
    private Double h_square;
    private Integer h_checked;

    // Getters and Setters
    public Integer getH_id() {
        return h_id;
    }

    public void setH_id(Integer h_id) {
        this.h_id = h_id;
    }

    public Integer getH_seller_id() {
        return h_seller_id;
    }

    public void setH_seller_id(Integer h_seller_id) {
        this.h_seller_id = h_seller_id;
    }

    public String getH_name() {
        return h_name;
    }

    public void setH_name(String h_name) {
        this.h_name = h_name;
    }

    public String getH_describe() {
        return h_describe;
    }

    public void setH_describe(String h_describe) {
        this.h_describe = h_describe;
    }

    public String getH_address() {
        return h_address;
    }

    public void setH_address(String h_address) {
        this.h_address = h_address;
    }

    public String getH_detail_address() {
        return h_detail_address;
    }

    public void setH_detail_address(String h_detail_address) {
        this.h_detail_address = h_detail_address;
    }

    public Double getH_price() {
        return h_price;
    }

    public void setH_price(Double h_price) {
        this.h_price = h_price;
    }

    public Double getH_longitude() {
        return h_longitude;
    }

    public void setH_longitude(Double h_longitude) {
        this.h_longitude = h_longitude;
    }

    public Double getH_latitude() {
        return h_latitude;
    }

    public void setH_latitude(Double h_latitude) {
        this.h_latitude = h_latitude;
    }

    public Double getH_square() {
        return h_square;
    }

    public void setH_square(Double h_square) {
        this.h_square = h_square;
    }

    public Integer getH_checked() {
        return h_checked;
    }

    public void setH_checked(Integer h_checked) {
        this.h_checked = h_checked;
    }
}