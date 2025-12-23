package com.zgqf.house.mapper;

import com.zgqf.house.entity.House;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HouseMapper {
    
    /**
     * 根据卖家ID查询房源列表
     */
    List<House> selectBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * 插入新房源
     */
    int insert(House house);
    
    /**
     * 更新房源信息
     */
    int update(House house);
    
    /**
     * 根据ID删除房源
     */
    int deleteById(@Param("houseId") Integer houseId);
}
