package com.zgqf.house.service.impl;

import com.zgqf.house.controller.SellerProfileRequest;
import com.zgqf.house.entity.House;
import com.zgqf.house.mapper.HouseMapper;
import com.zgqf.house.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {
    
    @Autowired
    private HouseMapper houseMapper;
    
    @Override
    public List<House> getHousesBySeller() {
        // 假设当前登录用户是卖家，获取卖家ID
        Integer sellerId = getCurrentSellerId(); // 需要从安全上下文中获取
        return houseMapper.selectBySellerId(sellerId);
    }
    
    @Override
    public House createHouse(House house) {
        houseMapper.insert(house);
        return house;
    }

    @Override
    public House updateHouse(Integer id, House house) {
        return null;
    }

    @Override
    public House updateHouse(House house) {
        houseMapper.update(house);
        return house;
    }
    
    @Override
    public boolean deleteHouse(Integer houseId) {
        return houseMapper.deleteById(houseId) > 0;
    }

    @Override
    public void updateSellerProfile(SellerProfileRequest request) {

    }

    // 模拟获取当前卖家ID的方法，实际项目中应该从SecurityContext中获取
    private Integer getCurrentSellerId() {
        // 这里需要从安全上下文中获取当前登录用户的卖家ID
        // 实际实现时需要集成Spring Security
        return 1; // 默认值，实际项目中需要替换
    }
}
