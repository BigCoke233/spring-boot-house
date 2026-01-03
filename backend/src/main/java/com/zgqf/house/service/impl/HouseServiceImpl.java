package com.zgqf.house.service.impl;

import com.zgqf.house.controller.SellerProfileRequest;
import com.zgqf.house.dto.HouseQueryDTO;
import com.zgqf.house.dto.HouseResultDTO;
import com.zgqf.house.entity.House;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.mapper.FollowMapper;
import com.zgqf.house.mapper.HouseMapper;
import com.zgqf.house.mapper.SellerMapper;
import com.zgqf.house.service.HouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseServiceImpl implements HouseService {

    private final HouseMapper houseMapper;
    private final SellerMapper sellerMapper;
    private final FollowMapper followMapper;

    @Override
    public List<House> getHousesBySeller() {
        // 假设当前登录用户是卖家，获取卖家ID
        Integer sellerId = getCurrentSellerId(); // 需要从安全上下文中获取
        return houseMapper.selectBySellerId(sellerId);
    }

    @Override
    public void savePictures(Integer houseId, List<String> picturePaths) {
        // First delete existing pictures
        houseMapper.deletePicturesByHouseId(houseId);
        
        // Then insert new ones
        if (picturePaths != null && !picturePaths.isEmpty()) {
            for (int i = 0; i < picturePaths.size(); i++) {
                houseMapper.insertPicture(houseId, picturePaths.get(i), i);
            }
        }
    }

    @Override
    public List<HouseResultDTO> getHousesByTagName(String tagName) {
        List<House> houses = houseMapper.selectHousesByTagName(tagName);
         return houses.stream()
                .map(this::convertToResultDTO)
                .collect(Collectors.toList());
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
        house.setH_id(id);
        return updateHouse(house);
    }

    @Override
    public House updateHouse(House house) {
        // Verify that the house exists and belongs to the current seller
        Integer currentSellerId = getCurrentSellerId();
        Integer houseId = house.getH_id();

        if (houseId == null) {
            throw new IllegalArgumentException("House ID cannot be null");
        }

        // Check if the house exists and get its current data
        // Use internal method that doesn't filter by h_checked, since sellers
        // need to be able to update their own houses even if not yet approved
        House existingHouse = houseMapper.selectHouseByIdInternal(houseId);
        if (existingHouse == null) {
            log.error("Update house failed: House not found with ID: {}", houseId);
            throw new RuntimeException("House not found with ID: " + houseId);
        }

        // Verify ownership - ensure the house belongs to the current seller
        if (existingHouse.getH_seller_id() == null) {
            log.error("Update house failed: House {} has no seller ID", houseId);
            throw new RuntimeException("Data integrity error: House has no seller");
        }
        
        if (!existingHouse.getH_seller_id().equals(currentSellerId)) {
            log.warn("Security warning: User {} tried to update house {} belonging to seller {}", 
                    currentSellerId, houseId, existingHouse.getH_seller_id());
            throw new SecurityException("You can only update your own houses");
        }

        // Update the house
        try {
            int rowsUpdated = houseMapper.update(house);
            if (rowsUpdated == 0) {
                log.warn("Update house {} returned 0 rows updated", houseId);
                throw new RuntimeException("Failed to update house - no changes made");
            }
        } catch (Exception e) {
            log.error("Error updating house {}: {}", houseId, e.getMessage());
            throw e;
        }

        return house;
    }

    @Override
    @Transactional
    public boolean deleteHouse(Integer houseId) {
        // Verify ownership before deletion
        Integer currentSellerId = getCurrentSellerId();

        // Check if the house exists and belongs to the current seller
        House existingHouse = houseMapper.selectBySellerId(currentSellerId).stream()
            .filter(h -> h.getH_id().equals(houseId))
            .findFirst()
            .orElse(null);

        if (existingHouse == null) {
            throw new SecurityException("You can only delete your own houses or house not found");
        }
        
        // 1. Delete pictures
        houseMapper.deletePicturesByHouseId(houseId);
        
        // 2. Delete tags
        houseMapper.deleteTagsByHouseId(houseId);
        
        // 3. Delete favorites (follows)
        followMapper.deleteByHouseId(houseId);

        // 4. Delete house
        try {
            return houseMapper.deleteById(houseId) > 0;
        } catch (Exception e) {
            log.error("Failed to delete house {}", houseId, e);
            throw new RuntimeException("删除失败，可能该房源有关联的合同或订单信息");
        }
    }

    @Override
    public HouseResultDTO getSellerHouseById(Integer id) {
        Integer currentSellerId = getCurrentSellerId();
        
        // Use internal method to ignore checked status
        House house = houseMapper.selectHouseByIdInternal(id);
        
        if (house == null) {
            throw new RuntimeException("房源不存在");
        }
        
        // Check ownership
        if (!house.getH_seller_id().equals(currentSellerId)) {
            throw new SecurityException("只能查看自己的房源信息");
        }
        
        return convertToResultDTO(house);
    }

    @Override
    @Transactional
    public void updateSellerProfile(SellerProfileRequest request) {
        if (request.getS_id() == null) {
            throw new IllegalArgumentException("Seller ID cannot be null");
        }
        
        Seller seller = new Seller();
        seller.setS_id(request.getS_id());
        seller.setS_name(request.getS_name());
        seller.setS_describe(request.getS_describe());
        seller.setS_phone(request.getS_phone());
        seller.setS_email(request.getS_email());
        seller.setS_website(request.getS_website());
        
        sellerMapper.updateSeller(seller);
    }

    // 模拟获取当前卖家ID的方法，实际项目中应该从SecurityContext中获取
    private Integer getCurrentSellerId() {
        // Retrieve the authentication object from the SecurityContext
        org.springframework.security.core.Authentication authentication = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            // Assuming the UserDetails implementation or a custom principal holds the User ID.
            // However, Spring Security UserDetails usually just has username.
            // We might need to look up the user by username or cast to a custom UserDetails class.
            // For this project, let's try to get the user from the HttpSession if we can access it, 
            // but Service layer shouldn't depend on HttpSession directly.
            // A better way is to use a thread-local variable set by an interceptor/filter, 
            // OR use the username to fetch the user ID from the database.
            
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            // Since we don't have UserService injected here, we might need to inject UserMapper or similar.
            // But wait, we are in HouseServiceImpl.
            // Let's assume we can get it from a static context if one exists, or query DB.
            // Actually, for now, to fix the immediate issue without major refactoring:
            // We can check if there is a request context and get the session.
            
            try {
                jakarta.servlet.http.HttpServletRequest request = 
                    ((org.springframework.web.context.request.ServletRequestAttributes) 
                        org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
                jakarta.servlet.http.HttpSession session = request.getSession(false);
                if (session != null) {
                    com.zgqf.house.entity.User user = (com.zgqf.house.entity.User) session.getAttribute("User");
                    if (user != null) {
                        return user.getU_id();
                    }
                }
            } catch (Exception e) {
                // Ignore if not in a web request context
            }
        }

        throw new RuntimeException("Seller not authenticated - cannot obtain seller ID");
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
        // Use selectHouseByIdInternal to skip checked status filtering if audit is not required
        // Or if we want to allow viewing pending houses in some cases.
        // Based on user request: "房屋没有审核机制，但是代码中仍有残留，如果有房屋是否审核相关的判断，请移除。"
        // So we should use the method that doesn't filter by status, or update selectHouseById XML to not filter.
        // Let's use selectHouseByIdInternal which is raw select by ID.
        House house = houseMapper.selectHouseByIdInternal(id);
        if (house == null) {
            throw new RuntimeException("房源不存在");
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
        
        if (house.getH_price() != null) {
            dto.setPrice(BigDecimal.valueOf(house.getH_price()));
        } else {
            dto.setPrice(BigDecimal.ZERO);
        }
        
        dto.setLongitude(house.getH_longitude());
        dto.setLatitude(house.getH_latitude());
        dto.setSquare(house.getH_square());
        dto.setChecked(house.getH_checked());

        // Calculate Total Price
        if (house.getH_price() != null && house.getH_square() != null) {
            dto.setTotalPrice(BigDecimal.valueOf(house.getH_price()).multiply(BigDecimal.valueOf(house.getH_square())));
        } else {
             dto.setTotalPrice(BigDecimal.ZERO);
        }

        // Fetch Seller Info
        if (house.getH_seller_id() != null) {
            dto.setSellerId(house.getH_seller_id());
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
