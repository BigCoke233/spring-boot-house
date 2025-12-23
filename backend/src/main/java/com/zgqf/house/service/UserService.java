package com.zgqf.house.service;

import com.zgqf.house.entity.User;
import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import java.util.List;

public interface UserService {

    // 用户管理
    User getUserById(Integer id);
    User getUserByUsername(String username);
    List<User> getUsersByType(String type);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Integer id);
    List<User>getAllUsers();

    // 买方管理
    Buyer getBuyerById(Integer id);
    Buyer getBuyerWithUserById(Integer id);
    Buyer createBuyer(Buyer buyer);
    Buyer updateBuyer(Buyer buyer);

    // 卖方管理
    Seller getSellerById(Integer id);
    Seller getSellerWithUserById(Integer id);
    List<Seller> searchSellers(String name);
    Seller createSeller(Seller seller);
    Seller updateSeller(Seller seller);

}