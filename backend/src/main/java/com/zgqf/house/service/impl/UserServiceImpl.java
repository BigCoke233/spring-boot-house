package com.zgqf.house.service.impl;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.entity.User;
import com.zgqf.house.mapper.BuyerMapper;
import com.zgqf.house.mapper.SellerMapper;
import com.zgqf.house.mapper.UserMapper;
import com.zgqf.house.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BuyerMapper buyerMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getU_password())) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public User register(User user, Map<String, Object> additionalInfo) {
        // Check if username exists
        if (userMapper.findByUsername(user.getU_username()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Encode password
        user.setU_password(passwordEncoder.encode(user.getU_password()));

        // Insert into User table
        userMapper.insert(user);
        Integer userId = user.getU_id();

        if ("buyer".equalsIgnoreCase(user.getU_type())) {
            Buyer buyer = new Buyer();
            buyer.setB_id(userId);
            buyer.setB_name((String) additionalInfo.get("name"));
            buyer.setB_phone((String) additionalInfo.get("phone"));
            buyer.setB_email((String) additionalInfo.get("email"));
            // Handle optional fields or defaults
            if (additionalInfo.get("mobileAssets") != null) {
                buyer.setB_mobile_assets(Double.valueOf(additionalInfo.get("mobileAssets").toString()));
            }
            if (additionalInfo.get("fixedAssets") != null) {
                buyer.setB_fixed_assets(Double.valueOf(additionalInfo.get("fixedAssets").toString()));
            }
            if (additionalInfo.get("annualIncome") != null) {
                buyer.setB_annual_income(Double.valueOf(additionalInfo.get("annualIncome").toString()));
            }
            buyerMapper.insert(buyer);
        } else if ("seller".equalsIgnoreCase(user.getU_type())) {
            Seller seller = new Seller();
            seller.setS_id(userId);
            seller.setS_name((String) additionalInfo.get("name")); // Company name
            seller.setS_phone((String) additionalInfo.get("phone"));
            seller.setS_email((String) additionalInfo.get("email"));
            seller.setS_describe((String) additionalInfo.get("describe"));
            seller.setS_website((String) additionalInfo.get("website"));
            sellerMapper.insert(seller);
        } else if ("admin".equalsIgnoreCase(user.getU_type())) {
            // admin 没有对应的表，只有类型字段
        } else {
            throw new RuntimeException("Invalid user type");
        }

        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
