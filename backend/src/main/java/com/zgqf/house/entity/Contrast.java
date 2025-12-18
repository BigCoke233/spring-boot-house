package com.zgqf.house.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Contrast {
    private Integer id;                // c_id
    private Integer buyerId;           // c_buyer_id
    private Integer houseId;           // c_house_id
    private Double totalPrice;         // c_total_price
    private String payWay;             // c_pay_way (full/installment)
    private Date paytimeEnding;        // c_paytime_ending
    private Date paytimeActually;      // c_paytime_actually
    private Date deliveryEnding;       // c_delivery_ending
    private Date deliveryActually;     // c_delivery_actually
    private Integer buyerAgree;        // c_buyer_agree (-1,0,1)
    private Integer sellerAgree;       // c_seller_agree (-1,0,1)
    private Integer paid;              // c_paid
    private Integer delivered;         // c_delivered

    // 关联信息
    private Buyer buyer;
    private House house;
}
