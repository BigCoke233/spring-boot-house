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
        log.info("创建用户: username={}, type={}", user.getUsername(), user.getType());
        userMapper.insertUser(user);
        log.info("用户创建成功: id={}", user.getId());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        log.info("更新用户: id={}", user.getId());

        // 1. 先查询用户是否存在
        User existingUser = userMapper.selectUserById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在，ID: " + user.getId());
        }

        // 2. 只更新允许修改的字段（保护ID和类型）
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());

        // 3. 执行更新
        int rows = userMapper.updateUser(existingUser);
        if (rows == 0) {
            throw new RuntimeException("更新用户失败，ID: " + user.getId());
        }

        log.info("用户更新成功: id={}", user.getId());
        return existingUser;
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        log.info("删除用户: id={}", id);

        // 1. 先检查用户是否存在
        User user = userMapper.selectUserById(id);
        if (user == null) {
            log.warn("删除失败: 用户不存在，ID: {}", id);
            throw new RuntimeException("用户不存在，ID: " + id);
        }

        // 2. 根据用户类型执行不同的删除逻辑
        if ("buyer".equals(user.getType())) {
            // 先删除buyer记录
            userMapper.deleteBuyer(id);
        } else if ("seller".equals(user.getType())) {
            // 先删除seller记录
            userMapper.deleteSeller(id);
        }

        // 3. 删除用户记录
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
        log.info("创建买方: name={}", buyer.getName());

        // 先创建用户
        User user = buyer.getUser();
        if (user == null) {
            throw new RuntimeException("买方用户信息不能为空");
        }
        user.setType("buyer");

        // 创建用户记录
        userMapper.insertUser(user);
        buyer.setId(user.getId());

        // 创建买方记录
        userMapper.insertBuyer(buyer);

        log.info("买方创建成功: id={}, name={}", buyer.getId(), buyer.getName());
        return buyer;
    }

    @Override
    @Transactional
    public Buyer updateBuyer(Buyer buyer) {
        log.info("更新买方: id={}, name={}", buyer.getId(), buyer.getName());

        // 1. 检查买方是否存在
        Buyer existingBuyer = userMapper.selectBuyerById(buyer.getId());
        if (existingBuyer == null) {
            throw new RuntimeException("买方不存在，ID: " + buyer.getId());
        }

        // 2. 更新买方信息
        existingBuyer.setName(buyer.getName());
        existingBuyer.setPhone(buyer.getPhone());
        existingBuyer.setEmail(buyer.getEmail());
        existingBuyer.setMobileAssets(buyer.getMobileAssets());
        existingBuyer.setFixedAssets(buyer.getFixedAssets());
        existingBuyer.setAnnualIncome(buyer.getAnnualIncome());

        // 3. 执行更新
        int rows = userMapper.updateBuyer(existingBuyer);
        if (rows == 0) {
            throw new RuntimeException("更新买方失败，ID: " + buyer.getId());
        }

        log.info("买方更新成功: id={}", buyer.getId());
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
        log.info("创建卖方: name={}", seller.getName());

        // 先创建用户
        User user = seller.getUser();
        if (user == null) {
            throw new RuntimeException("卖方用户信息不能为空");
        }
        user.setType("seller");

        // 创建用户记录
        userMapper.insertUser(user);
        seller.setId(user.getId());

        // 创建卖方记录
        userMapper.insertSeller(seller);

        log.info("卖方创建成功: id={}, name={}", seller.getId(), seller.getName());
        return seller;
    }

    @Override
    @Transactional
    public Seller updateSeller(Seller seller) {
        log.info("更新卖方: id={}, name={}", seller.getId(), seller.getName());

        // 1. 检查卖方是否存在
        Seller existingSeller = userMapper.selectSellerById(seller.getId());
        if (existingSeller == null) {
            throw new RuntimeException("卖方不存在，ID: " + seller.getId());
        }

        // 2. 更新卖方信息
        existingSeller.setName(seller.getName());
        existingSeller.setDescription(seller.getDescription());
        existingSeller.setPhone(seller.getPhone());
        existingSeller.setEmail(seller.getEmail());
        existingSeller.setWebsite(seller.getWebsite());

        // 3. 执行更新
        int rows = userMapper.updateSeller(existingSeller);
        if (rows == 0) {
            throw new RuntimeException("更新卖方失败，ID: " + seller.getId());
        }

        log.info("卖方更新成功: id={}", seller.getId());
        return existingSeller;
    }

    @Override
    @Transactional(readOnly = true)
    public User authenticate(String username, String password) {
        log.info("用户登录尝试: username={}", username);
        User user = userMapper.selectUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            log.info("用户登录成功: username={}, type={}", username, user.getType());
            return user;
        }
        log.warn("用户登录失败: username={}", username);
        return null;
    }
}
