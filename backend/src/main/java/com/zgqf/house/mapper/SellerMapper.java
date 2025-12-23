package com.zgqf.house.mapper;

import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SellerMapper {

    /**
     * 根据ID查询卖家信息
     */
    Seller selectById(@Param("sellerId") Integer sellerId);

    /**
     * 更新卖家信息
     */
    int update(Seller seller);
}

