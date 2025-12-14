package com.zgqf.house.service;

import com.zgqf.house.entity.House;
import com.zgqf.house.entity.Follow;

import java.util.List;

public interface FollowService {
    /**
     * 收藏房源
     * @param buyerId 买家ID
     * @param houseId 房源ID
     */
    void followHouse(Integer buyerId, Integer houseId);

    /**
     * 取消收藏房源
     * @param buyerId 买家ID
     * @param houseId 房源ID
     */
    void unfollowHouse(Integer buyerId, Integer houseId);
    
    /**
     * 检查是否已收藏指定房源
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 是否已收藏
     */
    boolean isFollowing(Integer buyerId, Integer houseId);
    
    /**
     * 获取买家收藏的所有房源ID
     * @param buyerId 买家ID
     * @return 房源ID列表
     */
    List<Integer> getFollowHouseIds(Integer buyerId);
}