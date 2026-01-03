package com.zgqf.house.mapper;

import com.zgqf.house.dto.HouseQueryDTO;
import com.zgqf.house.entity.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HouseMapper {
    
    /**
     * 根据卖家ID查询房源列表
     */
    List<House> selectBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * 插入新房源
     */
    int insert(House house);
    
    /**
     * 更新房源信息
     */
    int update(House house);
    
    /**
     * 根据ID删除房源
     */
    int deleteById(@Param("houseId") Integer houseId);

    /**
     * 查询房源列表（带分页和条件）
     */
    List<House> selectHouseList(HouseQueryDTO queryDTO);

    /**
     * 查询房源总数（用于分页）
     */
    Long countHouseList(HouseQueryDTO queryDTO);

    /**
     * 根据ID查询房源详情
     */
    House selectHouseById(@Param("id") Integer id);

    /**
     * 根据ID查询房源详情 (内部使用，不检查审核状态)
     */
    House selectHouseByIdInternal(@Param("id") Integer id);

    /**
     * 根据房源ID查询标签列表
     */
    List<String> selectTagsByHouseId(@Param("houseId") Integer houseId);

    /**
     * 根据房源ID查询图片列表
     */
    List<String> selectPicturesByHouseId(@Param("houseId") Integer houseId);

    /**
     * 根据标签类型查询房源
     */
    List<House> selectHousesByTagType(@Param("tagType") String tagType);

    /**
     * 根据标签名查询房源
     */
    List<House> selectHousesByTagName(@Param("tagName") String tagName);

    /**
     * 插入房源图片
     */
    int insertPicture(@Param("houseId") Integer houseId, @Param("picturePath") String picturePath, @Param("order") Integer order);

    /**
     * 删除房源的所有图片
     */
    int deletePicturesByHouseId(@Param("houseId") Integer houseId);

    /**
     * 删除房源的所有标签关联
     */
    int deleteTagsByHouseId(@Param("houseId") Integer houseId);
}
