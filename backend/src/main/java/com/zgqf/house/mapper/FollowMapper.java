package com.zgqf.house.mapper;

import com.zgqf.house.entity.Follow;
import com.zgqf.house.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {

    /**
     * 插入收藏记录
     * @param follow 收藏对象
     */
    void insertFollow(Follow follow);

    /**
     * 删除收藏记录
     * @param buyerId 买家ID
     * @param houseId 房源ID
     */
    void deleteFollow(@Param("buyerId") Integer buyerId, @Param("houseId") Integer houseId);

    /**
     * 查询收藏记录
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 收藏对象
     */
    Follow getFollow(@Param("buyerId") Integer buyerId, @Param("houseId") Integer houseId);
    
    /**
     * 获取买家收藏的所有房源详细信息
     * @param buyerId 买家ID
     * @return 收藏的房源详细信息列表
     */
    List<House> getFollowHouses(@Param("buyerId") Integer buyerId);

    /**
     * 删除指定房源的所有收藏记录
     */
    void deleteByHouseId(@Param("houseId") Integer houseId);
}