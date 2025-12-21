package com.zgqf.house.mapper;

import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SellerMapper {
    /**
     * 根据卖家ID获取卖家信息
     * @param sellerId 卖家ID
     * @return 卖家信息
     */
    Seller getSellerById(@Param("sellerId") Integer sellerId);
    
    /**
     * 更新卖家信息
     * @param seller 卖家信息对象
     */
    void updateSeller(Seller seller);
    
    /**
     * 新增卖家信息
     * @param seller 卖家对象
     */
    void insert(Seller seller);
}
