package com.zgqf.house.service;

import com.zgqf.house.entity.Seller;

public interface SellerService {

    /**
     * 获取当前卖家信息
     */
    Seller getSellerInfo();

    /**
     * 更新卖家信息
     */
    Seller updateSellerInfo(Seller seller);
}

