package com.zgqf.house.service;

import com.zgqf.house.entity.Buyer;

public interface BuyerService {
    /**
     * 获取买家个人信息
     * @param buyerId 买家ID
     * @return 买家信息
     */
    Buyer getBuyerProfile(Integer buyerId);
    
    /**
     * 更新买家个人信息
     * @param buyerId 买家ID
     * @param buyerInfo 新的买家信息
     * @return 更新结果
     */
    boolean updateBuyerProfile(Integer buyerId, Buyer buyerInfo);
    
    /**
     * 处理买家付款（全款/首付）
     * @param buyerId 买家ID
     * @param contractId 合同ID
     * @return 处理结果消息
     */
    String processPayment(Integer buyerId, Integer contractId);
}