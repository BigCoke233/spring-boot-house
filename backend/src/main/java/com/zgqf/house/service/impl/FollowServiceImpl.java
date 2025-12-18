package com.zgqf.house.service.impl;

import com.zgqf.house.entity.Follow;
import com.zgqf.house.entity.House;
import com.zgqf.house.mapper.FollowMapper;
import com.zgqf.house.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Override
    public void followHouse(Integer buyerId, Integer houseId) {
        // 检查是否已经收藏过
        if (!isFollowing(buyerId, houseId)) {
            // 创建新的收藏记录
            Follow follow = new Follow();
            follow.setF_buyer_id(buyerId);
            follow.setF_house_id(houseId);
            followMapper.insertFollow(follow);
        }
    }

    @Override
    public void unfollowHouse(Integer buyerId, Integer houseId) {
        followMapper.deleteFollow(buyerId, houseId);
    }
    
    @Override
    public boolean isFollowing(Integer buyerId, Integer houseId) {
        Follow follow = followMapper.getFollow(buyerId, houseId);
        return follow != null;
    }
    
    @Override
    public List<House> getFollowHouses(Integer buyerId) {
        return followMapper.getFollowHouses(buyerId);
    }
}