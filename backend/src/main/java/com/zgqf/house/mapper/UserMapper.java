package com.zgqf.house.mapper;

import com.zgqf.house.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    
    int insert(User user);
    
    User findById(Integer id);
}
