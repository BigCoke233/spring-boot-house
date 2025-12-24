package com.zgqf.house.controller;

import com.zgqf.house.entity.Seller;
import com.zgqf.house.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/sellers")
@RequiredArgsConstructor
public class PublicSellerController {

    private final UserService userService;

    /**
     * GET /api/public/sellers
     * 获取所有卖家（地产商）列表
     */
    @GetMapping
    public ResponseEntity<List<Seller>> getSellers(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(userService.searchSellers(name));
    }
}
