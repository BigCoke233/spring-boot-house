
package com.zgqf.house.mapper;

import com.zgqf.house.entity.User;
import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {

    // 用户操作
    User selectUserById(@Param("id") Integer id);
    User selectUserByUsername(@Param("username") String username);
    List<User> selectUserList(@Param("type") String type);
    Integer insertUser(User user);
    Integer updateUser(User user);  // 确保这个方法存在
    Integer deleteUser(@Param("id") Integer id);
    List<User> selectAllUsers();

    // 买方操作
    Buyer selectBuyerById(@Param("id") Integer id);
    Buyer selectBuyerWithUserById(@Param("id") Integer id);
    Integer insertBuyer(Buyer buyer);
    Integer updateBuyer(Buyer buyer);  // 确保这个方法存在
    Integer deleteBuyer(@Param("id") Integer id);  // 添加这个方法

    // 卖方操作
    Seller selectSellerById(@Param("id") Integer id);
    Seller selectSellerWithUserById(@Param("id") Integer id);
    List<Seller> selectSellerList(@Param("name") String name);
    Integer insertSeller(Seller seller);
    Integer updateSeller(Seller seller);  // 确保这个方法存在
    Integer deleteSeller(@Param("id") Integer id);  // 添加这个方法
}