package com.zgqf.house.service;

import com.zgqf.house.controller.SellerProfileRequest;
import com.zgqf.house.dto.HouseQueryDTO;
import com.zgqf.house.dto.HouseResultDTO;
import com.zgqf.house.entity.House;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HouseService {
    
    List<House> getHousesBySeller();
    
    House createHouse(House house);
    
    House updateHouse(Integer id, House house);

    House updateHouse(House house);

    boolean deleteHouse(Integer houseId);

    void updateSellerProfile(SellerProfileRequest request);

    /**
     * 获取卖家自己的房源详情（忽略审核状态）
     */
    HouseResultDTO getSellerHouseById(Integer id);

    /**
     * 分页查询房源 - 返回Spring Data Page
     */
    Page<HouseResultDTO> getHouses(HouseQueryDTO queryDTO);

    /**
     * 获取房源详情
     */
    HouseResultDTO getHouseById(Integer id);

    /**
     * 根据标签类型查询
     */
    Page<HouseResultDTO> getHousesByTagType(String tagType, int pageNum, int pageSize);

    /**
     * 根据标签名查询
     */
    Page<HouseResultDTO> getHousesByTagName(String tagName, int pageNum, int pageSize);

    /**
     * 根据标签名查询 (不分页)
     */
    List<HouseResultDTO> getHousesByTagName(String tagName);

    /**
     * 将House实体转换为HouseResultDTO
     */
    HouseResultDTO convertToResultDTO(House house);

    /**
     * 保存房源图片
     */
    void savePictures(Integer houseId, List<String> picturePaths);

    /**
     * 保存房源标签
     */
    void saveTags(Integer houseId, List<Integer> tagIds);

    /**
     * 获取所有标签
     */
    List<com.zgqf.house.entity.Tag> getAllTags();
}
