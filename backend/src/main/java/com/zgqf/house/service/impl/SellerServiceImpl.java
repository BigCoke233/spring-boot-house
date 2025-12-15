package com.zgqf.house.service.impl;

import com.zgqf.house.entity.Seller;
import com.zgqf.house.mapper.SellerMapper;
import com.zgqf.house.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public Seller getSellerInfo() {
        // 假设当前登录用户是卖家，获取卖家ID
        Integer sellerId = getCurrentSellerId(); // 需要从安全上下文中获取
        return sellerMapper.selectById(sellerId);
    }

    @Override
    public Seller updateSellerInfo(Seller seller) {
        sellerMapper.update(seller);
        return seller;
    }

    // 模拟获取当前卖家ID的方法，实际项目中应该从SecurityContext中获取
    private Integer getCurrentSellerId() {
        // 这里需要从安全上下文中获取当前登录用户的卖家ID
        // 实际实现时需要集成Spring Security
        return 1; // 默认值，实际项目中需要替换
    }
}

