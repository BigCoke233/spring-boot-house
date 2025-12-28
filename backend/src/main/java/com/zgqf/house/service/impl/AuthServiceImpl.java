package com.zgqf.house.service.impl;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.entity.User;
import com.zgqf.house.mapper.BuyerMapper;
import com.zgqf.house.mapper.SellerMapper;
import com.zgqf.house.mapper.UserMapper;
import com.zgqf.house.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

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
        User user = userMapper.selectUserByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getU_password())) {
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public User register(User user, Map<String, Object> additionalInfo) {
        // Check if username exists
        if (userMapper.selectUserByUsername(user.getU_username()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Encode password
        user.setU_password(passwordEncoder.encode(user.getU_password()));

        // Insert into User table
        userMapper.insertUser(user);
        Integer userId = user.getU_id();

        if ("buyer".equalsIgnoreCase(user.getU_type())) {
            Buyer buyer = new Buyer();
            buyer.setB_id(userId);
            // Try to get from mapped keys first, then fallback to generic keys
            String name = (String) additionalInfo.get("b_name");
            if (name == null) name = (String) additionalInfo.get("name");
            buyer.setB_name(name);

            String phone = (String) additionalInfo.get("b_phone");
            if (phone == null) phone = (String) additionalInfo.get("phone");
            buyer.setB_phone(phone);

            String email = (String) additionalInfo.get("b_email");
            if (email == null) email = (String) additionalInfo.get("email");
            buyer.setB_email(email);

            // Handle optional fields or defaults
            if (additionalInfo.get("mobileAssets") != null) {
                buyer.setB_mobile_assets(Double.valueOf(additionalInfo.get("mobileAssets").toString()));
            } else if (additionalInfo.get("b_mobile_assets") != null) {
                buyer.setB_mobile_assets(Double.valueOf(additionalInfo.get("b_mobile_assets").toString()));
            }
            
            if (additionalInfo.get("fixedAssets") != null) {
                buyer.setB_fixed_assets(Double.valueOf(additionalInfo.get("fixedAssets").toString()));
            } else if (additionalInfo.get("b_fixed_assets") != null) {
                buyer.setB_fixed_assets(Double.valueOf(additionalInfo.get("b_fixed_assets").toString()));
            }

            if (additionalInfo.get("annualIncome") != null) {
                buyer.setB_annual_income(Double.valueOf(additionalInfo.get("annualIncome").toString()));
            } else if (additionalInfo.get("b_annual_income") != null) {
                buyer.setB_annual_income(Double.valueOf(additionalInfo.get("b_annual_income").toString()));
            }
            buyerMapper.insert(buyer);
        } else if ("seller".equalsIgnoreCase(user.getU_type())) {
            Seller seller = new Seller();
            seller.setS_id(userId);
            
            String name = (String) additionalInfo.get("s_name");
            if (name == null) name = (String) additionalInfo.get("name");
            seller.setS_name(name); 

            String phone = (String) additionalInfo.get("s_phone");
            if (phone == null) phone = (String) additionalInfo.get("phone");
            seller.setS_phone(phone);

            String email = (String) additionalInfo.get("s_email");
            if (email == null) email = (String) additionalInfo.get("email");
            seller.setS_email(email);

            String describe = (String) additionalInfo.get("s_describe");
            if (describe == null) describe = (String) additionalInfo.get("describe");
            seller.setS_describe(describe);

            String website = (String) additionalInfo.get("s_website");
            if (website == null) website = (String) additionalInfo.get("website");
            seller.setS_website(website);
            
            sellerMapper.insert(seller);
        } else if ("admin".equalsIgnoreCase(user.getU_type())) {
            // admin 没有对应的表，只有类型字段
        } else {
            throw new RuntimeException("Invalid user type");
        }

        return user;
    }
}
