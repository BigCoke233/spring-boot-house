package com.zgqf.house.entity;

public class Buyer {
    private Integer b_id;
    private String b_name;
    private String b_phone;
    private String b_email;
    private Double b_mobile_assets;
    private Double b_fixed_assets;
    private Double b_annual_income;

    // Getters and Setters
    public Integer getB_id() {
        return b_id;
    }

    public void setB_id(Integer b_id) {
        this.b_id = b_id;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getB_phone() {
        return b_phone;
    }

    public void setB_phone(String b_phone) {
        this.b_phone = b_phone;
    }

    public String getB_email() {
        return b_email;
    }

    public void setB_email(String b_email) {
        this.b_email = b_email;
    }

    public Double getB_mobile_assets() {
        return b_mobile_assets;
    }

    public void setB_mobile_assets(Double b_mobile_assets) {
        this.b_mobile_assets = b_mobile_assets;
    }

    public Double getB_fixed_assets() {
        return b_fixed_assets;
    }

    public void setB_fixed_assets(Double b_fixed_assets) {
        this.b_fixed_assets = b_fixed_assets;
    }

    public Double getB_annual_income() {
        return b_annual_income;
    }

    public void setB_annual_income(Double b_annual_income) {
        this.b_annual_income = b_annual_income;
    }
}