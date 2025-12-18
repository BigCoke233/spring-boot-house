package com.zgqf.house.entity;


import lombok.Data;

@Data
public class Buyer {
    private Integer id;             // b_id
    private String name;           // b_name
    private String phone;          // b_phone
    private String email;          // b_email
    private Double mobileAssets;   // b_mobile_assets
    private Double fixedAssets;    // b_fixed_assets
    private Double annualIncome;   // b_annual_income

    // 关联的用户信息
    private User user;

}
