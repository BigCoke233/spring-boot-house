package com.zgqf.house.mapper;

import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SellerMapper {

    /**
     * 根据ID查询卖家信息
     */
    @Select("SELECT * FROM seller WHERE s_id = #{sellerId}")
    Seller selectById(@Param("sellerId") Integer sellerId);

    /**
     * 更新卖家信息
     */
    @Update("UPDATE seller SET s_name = #{sName}, s_describe = #{sDescribe}, " +
            "s_phone = #{sPhone}, s_email = #{sEmail}, s_website = #{sWebsite} " +
            "WHERE s_id = #{sId}")
    int update(Seller seller);
}

