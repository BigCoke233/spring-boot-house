package com.zgqf.house.controller;

import com.zgqf.house.dto.HouseQueryDTO;
import com.zgqf.house.dto.HouseResultDTO;
import com.zgqf.house.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/houses")
@RequiredArgsConstructor
public class HouseController {
    @Autowired
    private  HouseService houseService;

    /**
     * GET /api/public/houses
     * 查询房源列表（支持多种筛选条件）
     * 示例：/api/public/houses?type=decorated&minSquare=100
     */
    @GetMapping
    public ResponseEntity<Page<HouseResultDTO>> getHouses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minSquare,
            @RequestParam(required = false) Double maxSquare,
            @RequestParam(required = false) Double minTotalPrice,
            @RequestParam(required = false) Double maxTotalPrice,
            @RequestParam(required = false) String sellerName,
            @RequestParam(required = false) Integer sellerId,
            @RequestParam(required = false) Integer checked,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "h_id") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder) {

        HouseQueryDTO queryDTO = new HouseQueryDTO();
        queryDTO.setName(name);
        queryDTO.setAddress(address);
        queryDTO.setType(type);
        queryDTO.setTag(tag);
        queryDTO.setMinPrice(minPrice);
        queryDTO.setMaxPrice(maxPrice);
        queryDTO.setMinSquare(minSquare);
        queryDTO.setMaxSquare(maxSquare);
        queryDTO.setMinTotalPrice(minTotalPrice);
        queryDTO.setMaxTotalPrice(maxTotalPrice);
        queryDTO.setSellerName(sellerName);
        queryDTO.setSellerId(sellerId);
        queryDTO.setChecked(checked);
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setSortBy(sortBy);
        queryDTO.setSortOrder(sortOrder);

        Page<HouseResultDTO> result = houseService.getHouses(queryDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/public/houses/{id}
     * 获取房源详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<HouseResultDTO> getHouseById(@PathVariable Integer id) {
        HouseResultDTO house = houseService.getHouseById(id);
        return ResponseEntity.ok(house);
    }

    /**
     * GET /api/public/houses/by-tag-type
     * 根据标签类型查询（如：decorated）
     * 示例：/api/public/houses/by-tag-type?type=decorated&pageNum=1&pageSize=10
     */
    @GetMapping("/by-tag-type")
    public ResponseEntity<Page<HouseResultDTO>> getHousesByTagType(
            @RequestParam String type,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<HouseResultDTO> houses = houseService.getHousesByTagType(type, pageNum, pageSize);
        return ResponseEntity.ok(houses);
    }

    /**
     * GET /api/public/houses/by-tag-name
     * 根据标签名查询
     * 示例：/api/public/houses/by-tag-name?tag=精装修&pageNum=1&pageSize=10
     */
    @GetMapping("/by-tag-name")
    public ResponseEntity<Page<HouseResultDTO>> getHousesByTagName(
            @RequestParam String tag,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Page<HouseResultDTO> houses = houseService.getHousesByTagName(tag, pageNum, pageSize);
        return ResponseEntity.ok(houses);
    }
}
