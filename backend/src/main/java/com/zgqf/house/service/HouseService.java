package com.zgqf.house.service;

import com.zgqf.house.dto.HouseQueryDTO;
import com.zgqf.house.dto.HouseResultDTO;
import com.zgqf.house.entity.House;
import org.springframework.data.domain.Page;

public interface HouseService {

    /**
     * 分页查询房源 - 返回Spring Data Page
     */
    public Page<HouseResultDTO> getHouses(HouseQueryDTO queryDTO) ;

    /**
     * 获取房源详情
     */
    public HouseResultDTO getHouseById(Integer id) ;

    /**
     * 根据标签类型查询;
     */
    public Page<HouseResultDTO> getHousesByTagType(String tagType, int pageNum, int pageSize);

    /**
     * 根据标签名查询
     */
    public Page<HouseResultDTO> getHousesByTagName(String tagName, int pageNum, int pageSize);

    /**
     * 将House实体转换为HouseResultDTO
     */
    public HouseResultDTO convertToResultDTO(House house);
}