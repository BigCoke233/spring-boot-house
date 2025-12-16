package com.zgqf.house.service.impl;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.mapper.BuyerMapper;
import com.zgqf.house.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerServiceImpl implements BuyerService {
    
    @Autowired
    private BuyerMapper buyerMapper;
    
    @Override
    public Buyer getBuyerProfile(Integer buyerId) {
        return buyerMapper.getBuyerById(buyerId);
    }
    
    @Override
    public boolean updateBuyerProfile(Integer buyerId, Buyer buyerInfo) {
        // 确保更新的是当前用户的信息
        buyerInfo.setB_id(buyerId);
        buyerMapper.updateBuyer(buyerInfo);
        return true;
    }
}