package com.zgqf.house.entity;

import lombok.Data;

@Data
public class Seller {
    private Integer id;         // s_id
    private String name;        // s_name
    private String description; // s_describe
    private String phone;       // s_phone
    private String email;       // s_email
    private String website;     // s_website

    // 关联的用户信息
    private User user;
}
