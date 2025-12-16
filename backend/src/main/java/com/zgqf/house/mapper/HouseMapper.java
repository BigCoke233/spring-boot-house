package com.zgqf.house.mapper;

import com.zgqf.house.entity.House;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HouseMapper {
    
    /**
     * 根据卖家ID查询房源列表
     */
    @Select("SELECT * FROM house WHERE h_seller_id = #{sellerId}")
    List<House> selectBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * 插入新房源
     */
    @Insert("INSERT INTO house (h_seller_id, h_name, h_describe, h_address, h_detail_address, " +
            "h_price, h_longitude, h_latitude, h_square, h_checked) " +
            "VALUES (#{hSellerId}, #{hName}, #{hDescribe}, #{hAddress}, #{hDetailAddress}, " +
            "#{hPrice}, #{hLongitude}, #{hLatitude}, #{hSquare}, #{hChecked})")
    @Options(useGeneratedKeys = true, keyProperty = "hId")
    int insert(House house);
    
    /**
     * 更新房源信息
     */
    @Update("UPDATE house SET h_name = #{hName}, h_describe = #{hDescribe}, h_address = #{hAddress}, " +
            "h_detail_address = #{hDetailAddress}, h_price = #{hPrice}, h_longitude = #{hLongitude}, " +
            "h_latitude = #{hLatitude}, h_square = #{hSquare}, h_checked = #{hChecked} " +
            "WHERE h_id = #{hId}")
    int update(House house);
    
    /**
     * 根据ID删除房源
     */
    @Delete("DELETE FROM house WHERE h_id = #{houseId}")
    int deleteById(@Param("houseId") Integer houseId);
}
