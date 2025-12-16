package com.zgqf.house.controller;

import com.zgqf.house.entity.House;
import com.zgqf.house.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    private HouseService houseService;

    /**
     * 查看自己的的房源列表
     * GET /api/seller/houses
     */
    @GetMapping("/houses")
    public ResponseEntity<List<House>> getHousesBySeller() {
        List<House> houses = houseService.getHousesBySeller();
        return ResponseEntity.ok(houses);
    }

    /**
     * 创建新房源
     * POST /api/seller/house/
     */
    @PostMapping("/house")
    public ResponseEntity<House> createHouse(@RequestBody House house) {
        House createdHouse = houseService.createHouse(house);
        return ResponseEntity.ok(createdHouse);
    }

    /**
     * 编辑房源
     * PUT /api/seller/house/{id}
     */
    @PutMapping("/house/{id}")
    public ResponseEntity<House> updateHouse(@PathVariable("id") Integer id, @RequestBody House house) {
        House updatedHouse = houseService.updateHouse(id, house);
        return ResponseEntity.ok(updatedHouse);
    }

    /**
     * 删除房源
     * DELETE /api/seller/house/{id}
     */
    @DeleteMapping("/house/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable("id") Integer id) {
        houseService.deleteHouse(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 编辑完善善商家资料
     * POST /api/seller/profile
     */
    @PostMapping("/profile")
    public ResponseEntity<String> updateSellerProfile(@RequestBody SellerProfileRequest request) {
        houseService.updateSellerProfile(request);
        return ResponseEntity.ok("商家资料更新成功");
    }
}

