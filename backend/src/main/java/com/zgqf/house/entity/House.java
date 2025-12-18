package com.zgqf.house.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class House {
    private Integer id;              // h_id
    private Integer sellerId;        // h_seller_id
    private String name;            // h_name
    private String description;     // h_describe
    private String address;         // h_address
    private String detailAddress;   // h_detail_address
    private BigDecimal price;       // h_price
    private Double longitude;       // h_longitude
    private Double latitude;        // h_latitude
    private Double square;          // h_square
    private Integer checked;        // h_checked

    // 关联信息（在查询时通过JOIN获取）
    private String sellerName;      // 卖方公司名
    private String sellerPhone;     // 卖方电话
    private String sellerEmail;     // 卖方邮箱

    // 计算字段
    private BigDecimal totalPrice;  // 总价 = price * square
}
