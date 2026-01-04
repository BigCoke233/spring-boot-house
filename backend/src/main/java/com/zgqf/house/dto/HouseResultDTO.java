package com.zgqf.house.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class HouseResultDTO {
    private Integer id;
    private String name;
    private String description;
    private String address;
    private String detailAddress;
    private BigDecimal price;
    private Double longitude;
    private Double latitude;
    private Double square;
    private BigDecimal totalPrice;
    private Integer sellerId;
    private String sellerName;
    private String sellerPhone;
    private String sellerEmail;
    private Integer checked; // 审核状态：0-审核中, 1-已审核, 2-已下架
    private List<String> picturePaths;  // 图片路径列表
    private List<String> tagNames;      // 标签名称列表
    private List<Integer> tagIds;       // 标签ID列表
}
