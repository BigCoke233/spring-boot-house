package com.zgqf.house.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ContractUpdateDTO {
    private Integer buyerId;
    private Integer houseId;
    private Double totalPrice;
    private String payWay;
    private Date paytimeEnding;
    private Date deliveryEnding;
    
    private Integer buyerAgree;
    private Integer sellerAgree;
    private Date paytimeActually;
    private Date deliveryActually;
    private Integer paid;
    private Integer delivered;
}
