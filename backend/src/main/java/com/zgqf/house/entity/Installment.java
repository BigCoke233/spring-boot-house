package com.zgqf.house.entity;

public class Installment {
    private Integer i_contract_id;
    private Double i_down_payment;
    private Integer i_total_periods;
    private Double i_paid_per_period;
    private Integer i_paid_count;

    // Getters and Setters
    public Integer getI_contract_id() {
        return i_contract_id;
    }

    public void setI_contract_id(Integer i_contract_id) {
        this.i_contract_id = i_contract_id;
    }

    public Double getI_down_payment() {
        return i_down_payment;
    }

    public void setI_down_payment(Double i_down_payment) {
        this.i_down_payment = i_down_payment;
    }

    public Integer getI_total_periods() {
        return i_total_periods;
    }

    public void setI_total_periods(Integer i_total_periods) {
        this.i_total_periods = i_total_periods;
    }

    public Double getI_paid_per_period() {
        return i_paid_per_period;
    }

    public void setI_paid_per_period(Double i_paid_per_period) {
        this.i_paid_per_period = i_paid_per_period;
    }

    public Integer getI_paid_count() {
        return i_paid_count;
    }

    public void setI_paid_count(Integer i_paid_count) {
        this.i_paid_count = i_paid_count;
    }
}