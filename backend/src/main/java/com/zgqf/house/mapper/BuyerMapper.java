package com.zgqf.house.mapper;

import com.zgqf.house.entity.Buyer;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BuyerMapper {
    /**
     * 根据买家ID获取买家信息
     * @param buyerId 买家ID
     * @return 买家信息
     */
    @Select("SELECT * FROM buyer WHERE b_id = #{buyerId}")
    Buyer getBuyerById(@Param("buyerId") Integer buyerId);
    
    /**
     * 更新买家信息
     * @param buyer 买家信息对象
     */
    @Update("UPDATE buyer SET b_name=#{b_name}, b_phone=#{b_phone}, b_email=#{b_email}, " +
            "b_mobile_assets=#{b_mobile_assets}, b_fixed_assets=#{b_fixed_assets}, " +
            "b_annual_income=#{b_annual_income} WHERE b_id=#{b_id}")
    void updateBuyer(Buyer buyer);
}