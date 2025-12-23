// UserServiceImpl.java
package com.zgqf.house.service.serviceImpl;

import com.zgqf.house.entity.User;
import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.mapper.UserMapper;
import com.zgqf.house.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByType(String type) {
        return userMapper.selectUserList(type);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        log.info("创建用户: username={}, type={}", user.getU_username(), user.getU_type());
        userMapper.insertUser(user);
        log.info("用户创建成功: id={}", user.getU_id());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        log.info("更新用户: id={}", user.getU_id());

        // 1. 先查询用户是否存在
        User existingUser = userMapper.selectUserById(user.getU_id());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在，ID: " + user.getU_id());
        }

        // 2. 只更新允许修改的字段（保护ID和类型）
        existingUser.setU_username(user.getU_username());
        existingUser.setU_password(user.getU_password());

        // 3. 执行更新
        int rows = userMapper.updateUser(existingUser);
        if (rows == 0) {
            throw new RuntimeException("更新用户失败，ID: " + user.getU_id());
        }

        log.info("用户更新成功: id={}", user.getU_id());
        return existingUser;
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        log.info("删除用户: id={}", id);

        User user = userMapper.selectUserById(id);
        if (user == null) {
            log.warn("删除失败: 用户不存在，ID: {}", id);
            throw new RuntimeException("用户不存在，ID: " + id);
        }

        if ("buyer".equals(user.getU_type())) {

            userMapper.deleteBuyer(id);
        } else if ("seller".equals(user.getU_type())) {

            userMapper.deleteSeller(id);
        }

        int rows = userMapper.deleteUser(id);
        if (rows == 0) {
            throw new RuntimeException("删除用户失败，ID: " + id);
        }

        log.info("用户删除成功: id={}", id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public Buyer getBuyerById(Integer id) {
        return userMapper.selectBuyerById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Buyer getBuyerWithUserById(Integer id) {
        return userMapper.selectBuyerWithUserById(id);
    }

    @Override
    @Transactional
    public Buyer createBuyer(Buyer buyer) {
        log.info("创建买方: name={}", buyer.getB_name());

        // 先创建用户
        User user = userMapper.selectUserById(buyer.getB_id());
        if (user == null) {
            throw new RuntimeException("买方用户信息不能为空");
        }
        user.setU_type("buyer");

        // 创建用户记录
        userMapper.insertUser(user);
        buyer.setB_id(user.getU_id());

        // 创建买方记录
        userMapper.insertBuyer(buyer);

        log.info("买方创建成功: id={}, name={}", buyer.getB_id(), buyer.getB_name());
        return buyer;
    }

    @Override
    @Transactional
    public Buyer updateBuyer(Buyer buyer) {
        log.info("更新买方: id={}, name={}", buyer.getB_id(), buyer.getB_name());

        // 1. 检查买方是否存在
        Buyer existingBuyer = userMapper.selectBuyerById(buyer.getB_id());
        if (existingBuyer == null) {
            throw new RuntimeException("买方不存在，ID: " + buyer.getB_id());
        }

        // 2. 更新买方信息
        existingBuyer.setB_name(buyer.getB_name());
        existingBuyer.setB_phone(buyer.getB_phone());
        existingBuyer.setB_email(buyer.getB_email());
        existingBuyer.setB_mobile_assets(buyer.getB_mobile_assets());
        existingBuyer.setB_fixed_assets(buyer.getB_fixed_assets());
        existingBuyer.setB_annual_income(buyer.getB_annual_income());

        // 3. 执行更新
        int rows = userMapper.updateBuyer(existingBuyer);
        if (rows == 0) {
            throw new RuntimeException("更新买方失败，ID: " + buyer.getB_id());
        }

        log.info("买方更新成功: id={}", buyer.getB_id());
        return existingBuyer;
    }

    @Override
    @Transactional(readOnly = true)
    public Seller getSellerById(Integer id) {
        return userMapper.selectSellerById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Seller getSellerWithUserById(Integer id) {
        return userMapper.selectSellerWithUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seller> searchSellers(String name) {
        return userMapper.selectSellerList(name);
    }

    @Override
    @Transactional
    public Seller createSeller(Seller seller) {
        log.info("创建卖方: name={}", seller.getS_name());

        // 先创建用户
        User user = userMapper.selectUserById(seller.getS_id());
        if (user == null) {
            throw new RuntimeException("卖方用户信息不能为空");
        }
        user.setU_type("seller");

        // 创建用户记录
        userMapper.insertUser(user);
        seller.setS_id(user.getU_id());

        // 创建卖方记录
        userMapper.insertSeller(seller);

        log.info("卖方创建成功: id={}, name={}", seller.getS_id(), seller.getS_name());
        return seller;
    }

    @Override
    @Transactional
    public Seller updateSeller(Seller seller) {
        log.info("更新卖方: id={}, name={}", seller.getS_id(), seller.getS_name());

        // 1. 检查卖方是否存在
        Seller existingSeller = userMapper.selectSellerById(seller.getS_id());
        if (existingSeller == null) {
            throw new RuntimeException("卖方不存在，ID: " + seller.getS_id());
        }

        // 2. 更新卖方信息
        existingSeller.setS_name(seller.getS_name());
        existingSeller.setS_describe(seller.getS_describe());
        existingSeller.setS_phone(seller.getS_phone());
        existingSeller.setS_email(seller.getS_email());
        existingSeller.setS_website(seller.getS_website());

        // 3. 执行更新
        int rows = userMapper.updateSeller(existingSeller);
        if (rows == 0) {
            throw new RuntimeException("更新卖方失败，ID: " + seller.getS_id());
        }

        log.info("卖方更新成功: id={}", seller.getS_id());
        return existingSeller;
    }


}
