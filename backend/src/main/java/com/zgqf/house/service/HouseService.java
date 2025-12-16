package com.zgqf.house.service;

import com.zgqf.house.controller.SellerProfileRequest;
import com.zgqf.house.entity.House;
import java.util.List;

public interface HouseService {
    
    List<House> getHousesBySeller();
    
    House createHouse(House house);
    
    House updateHouse(Integer id, House house);

    House updateHouse(House house);


    boolean deleteHouse(Integer houseId);

    void updateSellerProfile(SellerProfileRequest request);
}
