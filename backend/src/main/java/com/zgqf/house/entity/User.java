package com.zgqf.house.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;         // u_id
    private String type;        // u_type
    private String username;    // u_username
    private String password;    // u_password
}
