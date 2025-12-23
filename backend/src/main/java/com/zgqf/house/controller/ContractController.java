package com.zgqf.house.controller;

import com.zgqf.house.dto.ContractQueryDTO;
import com.zgqf.house.dto.ContractCreateDTO;
import com.zgqf.house.dto.ContractUpdateDTO;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ContractController {
    
    private final ContractService contractService;

    // 获取合同列表
    @GetMapping("/contracts")
    public ResponseEntity<Page<Contract>> getContracts(ContractQueryDTO queryDTO) {
        return ResponseEntity.ok(contractService.getContracts(queryDTO));
    }

    // 获取特定合同详情
    @PostMapping("/contract/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable Integer id) {
        // 注意：图片中是 POST 请求获取单个合同，这不太符合 RESTful 规范
        // 但按照图片要求，这里改为 POST
        return ResponseEntity.ok(contractService.getContractDetail(id));
    }

    // 创建合同
    @PostMapping("/contract")
    public ResponseEntity<Contract> createContract(@RequestBody ContractCreateDTO createDTO) {
        return ResponseEntity.ok(contractService.createContract(createDTO));
    }

    // 更新合同（图片中显示是通过 POST /contract/{id} 更新）
    @PostMapping("/contract/{id}/update")
    public ResponseEntity<Contract> updateContract(
            @PathVariable Integer id,
            @RequestBody ContractUpdateDTO updateDTO) {
        return ResponseEntity.ok(contractService.updateContract(id, updateDTO));
    }

    // 删除合同
    @DeleteMapping("/contract/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Integer id) {
        contractService.deleteContract(id);
        return ResponseEntity.ok().build();
    }

    // 买方同意/拒绝
    @PostMapping("/contract/{id}/buyer-agree")
    public ResponseEntity<Contract> updateBuyerAgree(
            @PathVariable Integer id,
            @RequestParam Integer agree) {
        return ResponseEntity.ok(contractService.buyerAgree(id, agree));
    }

    // 卖方同意/拒绝
    @PostMapping("/contract/{id}/seller-agree")
    public ResponseEntity<Contract> updateSellerAgree(
            @PathVariable Integer id,
            @RequestParam Integer agree) {
        return ResponseEntity.ok(contractService.sellerAgree(id, agree));
    }

    // 更新付款状态
    @PostMapping("/contract/{id}/payment")
    public ResponseEntity<Contract> updatePayment(
            @PathVariable Integer id,
            @RequestParam Integer paid) {
        return ResponseEntity.ok(contractService.updatePayment(id, paid));
    }

    // 更新交房状态
    @PostMapping("/contract/{id}/delivery")
    public ResponseEntity<Contract> updateDelivery(
            @PathVariable Integer id,
            @RequestParam Integer delivered) {
        return ResponseEntity.ok(contractService.updateDelivery(id, delivered));
    }
}
