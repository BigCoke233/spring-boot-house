package com.zgqf.house.service.impl;

import com.zgqf.house.controller.SellerProfileRequest;
import com.zgqf.house.dto.HouseQueryDTO;
import com.zgqf.house.dto.HouseResultDTO;
import com.zgqf.house.entity.House;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.mapper.HouseMapper;
import com.zgqf.house.mapper.SellerMapper;
import com.zgqf.house.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseMapper houseMapper;
    private final SellerMapper sellerMapper;

    @Override
    public List<House> getHousesBySeller() {
        // 假设当前登录用户是卖家，获取卖家ID
        Integer sellerId = getCurrentSellerId(); // 需要从安全上下文中获取
        return houseMapper.selectBySellerId(sellerId);
    }

    @Override
    public House createHouse(House house) {
        if (house.getH_seller_id() == null) {
            house.setH_seller_id(getCurrentSellerId());
        }
        houseMapper.insert(house);
        return house;
    }

    @Override
    public House updateHouse(Integer id, House house) {
        // Implementation pending or consolidated with the other updateHouse
        return null;
    }

    @Override
    public House updateHouse(House house) {
        houseMapper.update(house);
        return house;
    }

    @Override
    public boolean deleteHouse(Integer houseId) {
        return houseMapper.deleteById(houseId) > 0;
    }

    @Override
    public void updateSellerProfile(SellerProfileRequest request) {
        // Implementation pending
    }

    // 模拟获取当前卖家ID的方法，实际项目中应该从SecurityContext中获取
    private Integer getCurrentSellerId() {
        // 这里需要从安全上下文中获取当前登录用户的卖家ID
        // 实际实现时需要集成Spring Security
        return 1; // 默认值，实际项目中需要替换
    }

    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public HouseResultDTO convertToResultDTO(House house) {
        HouseResultDTO dto = new HouseResultDTO();
        dto.setId(house.getH_id());
        dto.setName(house.getH_name());
        dto.setDescription(house.getH_describe());
        dto.setAddress(house.getH_address());
        dto.setDetailAddress(house.getH_detail_address());
        dto.setPrice(BigDecimal.valueOf(house.getH_price()));
        dto.setLongitude(house.getH_longitude());
        dto.setLatitude(house.getH_latitude());
        dto.setSquare(house.getH_square());

        // Calculate Total Price
        if (house.getH_price() != null && house.getH_square() != null) {
            dto.setTotalPrice(BigDecimal.valueOf(house.getH_price()).multiply(BigDecimal.valueOf(house.getH_square())));
        }

        // Fetch Seller Info
        if (house.getH_seller_id() != null) {
            Seller seller = sellerMapper.getSellerById(house.getH_seller_id());
            if (seller != null) {
                dto.setSellerName(seller.getS_name());
                dto.setSellerPhone(seller.getS_phone());
                dto.setSellerEmail(seller.getS_email());
            }
        }

        // 查询标签
        List<String> tags = houseMapper.selectTagsByHouseId(house.getH_id());
        dto.setTagNames(tags);

        // 查询图片
        List<String> pictures = houseMapper.selectPicturesByHouseId(house.getH_id());
        dto.setPicturePaths(pictures);

        return dto;
    }
}
