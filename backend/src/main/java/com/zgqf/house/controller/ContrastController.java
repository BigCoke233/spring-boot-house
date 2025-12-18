package com.zgqf.house.controller;

import com.zgqf.house.dto.ContrastQueryDTO;
import com.zgqf.house.dto.ContrastCreateDTO;
import com.zgqf.house.dto.ContrastUpdateDTO;
import com.zgqf.house.entity.Contrast;
import com.zgqf.house.service.ContrastService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ContrastController {
    @Autowired
    private ContrastService contrastService;

    // 获取合同列表
    @GetMapping("/contracts")
    public ResponseEntity<Page<Contrast>> getContrasts(ContrastQueryDTO queryDTO) {
        return ResponseEntity.ok(contrastService.getContrasts(queryDTO));
    }

    // 获取特定合同详情
    @PostMapping("/contract/{id}")
    public ResponseEntity<Contrast> getContrast(@PathVariable Integer id) {
        // 注意：图片中是 POST 请求获取单个合同，这不太符合 RESTful 规范
        // 但按照图片要求，这里改为 POST
        return ResponseEntity.ok(contrastService.getContrastDetail(id));
    }

    // 创建合同
    @PostMapping("/contract")
    public ResponseEntity<Contrast> createContrast(@RequestBody ContrastCreateDTO createDTO) {
        return ResponseEntity.ok(contrastService.createContrast(createDTO));
    }

    // 更新合同（图片中显示是通过 POST /contract/{id} 更新）
    @PostMapping("/contract/{id}/update")
    public ResponseEntity<Contrast> updateContrast(
            @PathVariable Integer id,
            @RequestBody ContrastUpdateDTO updateDTO) {
        return ResponseEntity.ok(contrastService.updateContrast(id, updateDTO));
    }

    // 删除合同
    @DeleteMapping("/contract/{id}")
    public ResponseEntity<Void> deleteContrast(@PathVariable Integer id) {
        contrastService.deleteContrast(id);
        return ResponseEntity.ok().build();
    }

    // 买方同意/拒绝
    @PostMapping("/contract/{id}/buyer-agree")
    public ResponseEntity<Contrast> updateBuyerAgree(
            @PathVariable Integer id,
            @RequestParam Integer agree) {
        return ResponseEntity.ok(contrastService.buyerAgree(id, agree));
    }

    // 卖方同意/拒绝
    @PostMapping("/contract/{id}/seller-agree")
    public ResponseEntity<Contrast> updateSellerAgree(
            @PathVariable Integer id,
            @RequestParam Integer agree) {
        return ResponseEntity.ok(contrastService.sellerAgree(id, agree));
    }

    // 更新付款状态
    @PostMapping("/contract/{id}/payment")
    public ResponseEntity<Contrast> updatePayment(
            @PathVariable Integer id,
            @RequestParam Integer paid) {
        return ResponseEntity.ok(contrastService.updatePayment(id, paid));
    }

    // 更新交房状态
    @PostMapping("/contract/{id}/delivery")
    public ResponseEntity<Contrast> updateDelivery(
            @PathVariable Integer id,
            @RequestParam Integer delivered) {
        return ResponseEntity.ok(contrastService.updateDelivery(id, delivered));
    }
}
