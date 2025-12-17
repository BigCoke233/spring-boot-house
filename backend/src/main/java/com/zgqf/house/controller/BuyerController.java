package com.zgqf.house.controller;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {
    
    @Autowired
    private BuyerService buyerService;
    
    /**
     * 获取买家个人信息
     * @param buyerId 买家ID
     * @return 买家信息
     */
    @GetMapping("/profile")
    public ResponseEntity<Buyer> getProfile(@RequestHeader("buyerId") Integer buyerId) {
        try {
            Buyer buyer = buyerService.getBuyerProfile(buyerId);
            return ResponseEntity.ok(buyer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新买家个人信息
     * @param buyerId 买家ID
     * @param buyerInfo 新的买家信息
     * @return 操作结果
     */
    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestHeader("buyerId") Integer buyerId,
                                               @RequestBody Buyer buyerInfo) {
        try {
            // 防止ID篡改
            buyerInfo.setB_id(buyerId);
            buyerService.updateBuyerProfile(buyerId, buyerInfo);
            return ResponseEntity.ok("个人信息更新成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 买家付款接口（全款/首付）
     * @param buyerId 买家ID
     * @param contractId 合同ID
     * @return 付款结果
     */
    @PostMapping("/pay/{contractId}")
    public ResponseEntity<String> payContract(@RequestHeader("buyerId") Integer buyerId,
                                             @PathVariable Integer contractId) {
        try {
            // 调用服务层处理付款逻辑
            String result = buyerService.processPayment(buyerId, contractId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("付款失败: " + e.getMessage());
        }
    }
}