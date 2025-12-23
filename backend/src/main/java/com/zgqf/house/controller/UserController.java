// UserController.java
package com.zgqf.house.controller;

import com.zgqf.house.entity.User;
import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    // 用户管理 - 获取所有用户
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        // 这里需要添加一个获取所有用户的方法
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 用户管理 - 获取特定用户
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // 用户管理 - 创建用户
    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // 用户管理 - 更新用户
    @PostMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setU_id(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    // 用户管理 - 删除用户（需要添加删除方法）
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // 买方管理 - 获取买方
    @GetMapping("/user/buyers/{id}")
    public ResponseEntity<Buyer> getBuyer(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getBuyerWithUserById(id));
    }

    // 买方管理 - 创建买方
    @PostMapping("/user/buyers")
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) {
        return ResponseEntity.ok(userService.createBuyer(buyer));
    }

    // 卖方管理 - 获取特定卖方
    @GetMapping("/user/sellers/{id}")
    public ResponseEntity<Seller> getSeller(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getSellerWithUserById(id));
    }

    // 卖方管理 - 搜索卖方
    @GetMapping("/user/sellers")
    public ResponseEntity<List<Seller>> searchSellers(
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(userService.searchSellers(name));
    }

    // 卖方管理 - 创建卖方
    @PostMapping("/user/sellers")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        return ResponseEntity.ok(userService.createSeller(seller));
    }

}