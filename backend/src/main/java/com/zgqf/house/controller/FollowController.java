package com.zgqf.house.controller;

import com.zgqf.house.entity.House;
import com.zgqf.house.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyer")
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * 切换房源收藏状态（用于房源列表）
     * 如果已收藏则取消收藏，如果未收藏则添加收藏
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 操作结果
     */
    @PostMapping("/follow/{houseId}")
    public ResponseEntity<String> toggleFollow(@RequestHeader("buyerId") Integer buyerId, 
                                              @PathVariable Integer houseId) {
        try {
            if (followService.isFollowing(buyerId, houseId)) {
                followService.unfollowHouse(buyerId, houseId);
                return ResponseEntity.ok("已取消收藏");
            } else {
                followService.followHouse(buyerId, houseId);
                return ResponseEntity.ok("收藏成功");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("操作失败: " + e.getMessage());
        }
    }

    /**
     * 买家取消收藏房源（用于收藏列表）
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 操作结果
     */
    @DeleteMapping("/follow/{houseId}")
    public ResponseEntity<String> unfollowHouse(@RequestHeader("buyerId") Integer buyerId,
                                                @PathVariable Integer houseId) {
        try {
            followService.unfollowHouse(buyerId, houseId);
            return ResponseEntity.ok("取消收藏成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("取消收藏失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取买家收藏的房源详细信息列表
     * @param buyerId 买家ID
     * @return 房源详细信息列表
     */
    @GetMapping("/follows")
    public ResponseEntity<List<House>> getFollowHouses(@RequestHeader("buyerId") Integer buyerId) {
        try {
            List<House> houses = followService.getFollowHouses(buyerId);
            return ResponseEntity.ok(houses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}