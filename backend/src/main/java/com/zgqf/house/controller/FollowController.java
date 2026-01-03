package com.zgqf.house.controller;

import com.zgqf.house.dto.HouseResultDTO;
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
     * 添加房源到收藏夹
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 操作结果
     */
    @PostMapping("/follows/{houseId}")
    public ResponseEntity<String> followHouse(@RequestHeader("buyerId") Integer buyerId, 
                                             @PathVariable Integer houseId) {
        try {
            // 检查是否已经收藏
            if (followService.isFollowing(buyerId, houseId)) {
                return ResponseEntity.ok("房源已在收藏夹中");
            }
            
            followService.followHouse(buyerId, houseId);
            return ResponseEntity.ok("收藏成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("收藏失败: " + e.getMessage());
        }
    }

    /**
     * 从收藏夹中移除房源
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 操作结果
     */
    @DeleteMapping("/follows/{houseId}")
    public ResponseEntity<String> unfollowHouse(@RequestHeader("buyerId") Integer buyerId,
                                               @PathVariable Integer houseId) {
        try {
            // 检查是否已经收藏
            if (!followService.isFollowing(buyerId, houseId)) {
                return ResponseEntity.ok("房源不在收藏夹中");
            }
            
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
    public ResponseEntity<List<HouseResultDTO>> getFollowHouses(@RequestHeader("buyerId") Integer buyerId) {
        try {
            List<HouseResultDTO> houses = followService.getFollowHouses(buyerId);
            return ResponseEntity.ok(houses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}