package com.zgqf.house.service;

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
}