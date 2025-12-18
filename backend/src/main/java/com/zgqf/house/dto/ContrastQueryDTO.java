package com.zgqf.house.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ContrastQueryDTO {
    private Integer buyerId;
    private Integer sellerId;
    private Integer houseId;
    private Integer buyerAgree;
    private Integer sellerAgree;
    private Integer paid;
    private Integer delivered;
    private Date startDate;
    private Date endDate;

    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String sortBy = "c_id";
    private String sortOrder = "DESC";

    private Integer offset;

    // 手动计算偏移量的方法
    public Integer getOffset() {
        if (offset == null) {
            offset = (pageNum - 1) * pageSize;
        }
        return offset;
    }

}
