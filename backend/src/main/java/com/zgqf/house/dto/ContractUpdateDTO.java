package com.zgqf.house.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ContractUpdateDTO {
    private Integer buyerAgree;
    private Integer sellerAgree;
    private Date paytimeActually;
    private Date deliveryActually;
    private Integer paid;
    private Integer delivered;
}
