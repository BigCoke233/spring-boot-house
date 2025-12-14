package com.zgqf.house.mapper;

import com.zgqf.house.entity.Follow;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FollowMapper {

    /**
     * 插入收藏记录
     * @param follow 收藏对象
     */
    @Insert("INSERT INTO follow(f_buyer_id, f_house_id) VALUES(#{f_buyer_id}, #{f_house_id})")
    void insertFollow(Follow follow);

    /**
     * 删除收藏记录
     * @param buyerId 买家ID
     * @param houseId 房源ID
     */
    @Delete("DELETE FROM follow WHERE f_buyer_id = #{buyerId} AND f_house_id = #{houseId}")
    void deleteFollow(@Param("buyerId") Integer buyerId, @Param("houseId") Integer houseId);

    /**
     * 查询收藏记录
     * @param buyerId 买家ID
     * @param houseId 房源ID
     * @return 收藏对象
     */
    @Select("SELECT f_buyer_id, f_house_id FROM follow WHERE f_buyer_id = #{buyerId} AND f_house_id = #{houseId}")
    @Results({
        @Result(property = "f_buyer_id", column = "f_buyer_id"),
        @Result(property = "f_house_id", column = "f_house_id")
    })
    Follow getFollow(@Param("buyerId") Integer buyerId, @Param("houseId") Integer houseId);
}