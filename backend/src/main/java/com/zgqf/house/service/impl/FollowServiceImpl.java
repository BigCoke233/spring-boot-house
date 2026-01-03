package com.zgqf.house.service.impl;

import com.zgqf.house.dto.HouseResultDTO;
import com.zgqf.house.entity.Follow;
import com.zgqf.house.entity.House;
import com.zgqf.house.mapper.FollowMapper;
import com.zgqf.house.service.FollowService;
import com.zgqf.house.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;
    
    @Autowired
    private HouseService houseService;

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
    public List<HouseResultDTO> getFollowHouses(Integer buyerId) {
        List<House> houses = followMapper.getFollowHouses(buyerId);
        return houses.stream()
                .map(houseService::convertToResultDTO)
                .collect(Collectors.toList());
    }
}