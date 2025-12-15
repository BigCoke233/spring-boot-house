package com.zgqf.house.mapper;

import com.zgqf.house.entity.Follow;
import com.zgqf.house.entity.House;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FollowMapper {

    /**
     * 插入收藏记录
     * @param follow 收藏对象
     */
    @Insert("INSERT INTO follow(f_buyer_id, f_house_id) VALUES(#{f_buyer_id}, #{f_house_id})")
    void insertFollow(Follow follow);

    /**
     * 删除收藏记录
     * @param buyerId 买家ID
     * @param houseId 房源ID
     */
    @Delete("DELETE FROM follow WHERE f_buyer_id = #{buyerId} AND f_house_id = #{houseId}")
    void deleteFollow(@Param("buyerId") Integer buyerId, @Param("houseId") Integer houseId);

    /**
     * 查询收藏记录
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 收藏对象
     */
    @Select("SELECT f_buyer_id, f_house_id FROM follow WHERE f_buyer_id = #{buyerId} AND f_house_id = #{houseId}")
    @Results({
        @Result(property = "f_buyer_id", column = "f_buyer_id"),
        @Result(property = "f_house_id", column = "f_house_id")
    })
    Follow getFollow(@Param("buyerId") Integer buyerId, @Param("houseId") Integer houseId);
    
    /**
     * 获取买家收藏的所有房源详细信息
     * @param buyerId 买家ID
     * @return 收藏的房源详细信息列表
     */
    @Select("SELECT h.* FROM house h INNER JOIN follow f ON h.h_id = f.f_house_id WHERE f.f_buyer_id = #{buyerId}")
    @Results({
        @Result(property = "h_id", column = "h_id"),
        @Result(property = "h_seller_id", column = "h_seller_id"),
        @Result(property = "h_name", column = "h_name"),
        @Result(property = "h_describe", column = "h_describe"),
        @Result(property = "h_address", column = "h_address"),
        @Result(property = "h_detail_address", column = "h_detail_address"),
        @Result(property = "h_price", column = "h_price"),
        @Result(property = "h_longitude", column = "h_longitude"),
        @Result(property = "h_latitude", column = "h_latitude"),
        @Result(property = "h_square", column = "h_square"),
        @Result(property = "h_checked", column = "h_checked")
    })
    List<House> getFollowHouses(@Param("buyerId") Integer buyerId);
}