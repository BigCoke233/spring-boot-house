package com.zgqf.house.service;

import com.zgqf.house.dto.HouseQueryDTO;
import com.zgqf.house.dto.HouseResultDTO;
import com.zgqf.house.entity.House;
import com.zgqf.house.mapper.HouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseMapper houseMapper;

    /**
     * 分页查询房源 - 返回Spring Data Page
     */
    public Page<HouseResultDTO> getHouses(HouseQueryDTO queryDTO) {
        // 1. 查询数据列表（SQL中已经有分页）
        List<House> houseList = houseMapper.selectHouseList(queryDTO);

        // 2. 查询总数
        Long total = houseMapper.countHouseList(queryDTO);

        // 3. 转换为DTO
        List<HouseResultDTO> content = houseList.stream()
                .map(this::convertToResultDTO)
                .collect(Collectors.toList());

        // 4. 构建Pageable（Spring Data页码从0开始）
        Pageable pageable = PageRequest.of(
                queryDTO.getPageNum() - 1,  // 转换为0-based
                queryDTO.getPageSize()
        );

        // 5. 返回Page对象
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 获取房源详情
     */
    public HouseResultDTO getHouseById(Integer id) {
        House house = houseMapper.selectHouseById(id);
        if (house == null) {
            throw new RuntimeException("房源不存在或未审核");
        }
        return convertToResultDTO(house);
    }

    /**
     * 根据标签类型查询
     */
    public Page<HouseResultDTO> getHousesByTagType(String tagType, int pageNum, int pageSize) {
        // 构建查询条件
        HouseQueryDTO queryDTO = new HouseQueryDTO();
        queryDTO.setType(tagType);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);

        return getHouses(queryDTO);
    }

    /**
     * 根据标签名查询
     */
    public Page<HouseResultDTO> getHousesByTagName(String tagName, int pageNum, int pageSize) {
        // 构建查询条件
        HouseQueryDTO queryDTO = new HouseQueryDTO();
        queryDTO.setTag(tagName);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);

        return getHouses(queryDTO);
    }

    /**
     * 将House实体转换为HouseResultDTO
     */
    private HouseResultDTO convertToResultDTO(House house) {
        HouseResultDTO dto = new HouseResultDTO();
        dto.setId(house.getId());
        dto.setName(house.getName());
        dto.setDescription(house.getDescription());
        dto.setAddress(house.getAddress());
        dto.setDetailAddress(house.getDetailAddress());
        dto.setPrice(house.getPrice());
        dto.setLongitude(house.getLongitude());
        dto.setLatitude(house.getLatitude());
        dto.setSquare(house.getSquare());
        dto.setTotalPrice(house.getTotalPrice());
        dto.setSellerName(house.getSellerName());
        dto.setSellerPhone(house.getSellerPhone());
        dto.setSellerEmail(house.getSellerEmail());

        // 查询标签
        List<String> tags = houseMapper.selectTagsByHouseId(house.getId());
        dto.setTagNames(tags);

        // 查询图片
        List<String> pictures = houseMapper.selectPicturesByHouseId(house.getId());
        dto.setPicturePaths(pictures);

        return dto;
    }
}