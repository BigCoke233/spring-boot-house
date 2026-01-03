package com.zgqf.house.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ContractCreateDTO {
    private Integer buyerId;
    private Integer houseId;
    private Double totalPrice;
    private String payWay;
    private Integer totalPeriods;
    private Date paytimeEnding;
    private Date deliveryEnding;
}
