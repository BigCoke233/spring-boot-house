package com.zgqf.house.controller;

import com.zgqf.house.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract")
public class ContractController {
    
    @Autowired
    private BuyerService buyerService;
    
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