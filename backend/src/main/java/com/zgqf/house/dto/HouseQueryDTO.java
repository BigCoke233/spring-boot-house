package com.zgqf.house.dto;

import lombok.Data;

@Data
public class HouseQueryDTO {
    private String name;           // 房源名称模糊搜索
    private String address;        // 地址模糊搜索
    private String type;           // 标签类型（如：decorated）
    private String tag;            // 具体标签名
    private Double minPrice;       // 最低单价
    private Double maxPrice;       // 最高单价
    private Double minSquare;      // 最小面积
    private Double maxSquare;      // 最大面积
    private Double minTotalPrice;  // 最低总价
    private Double maxTotalPrice;  // 最高总价
    private String sellerName;     // 卖方公司名
    private Integer sellerId;      // 卖方ID
    private Integer checked = 1;   // 审核状态（默认1：已审核）

    // 分页参数
    private Integer pageNum = 1;   // 页码
    private Integer pageSize = 10; // 每页大小
    private String sortBy = "h_id"; // 排序字段
    private String sortOrder = "DESC"; // 排序方式
}
