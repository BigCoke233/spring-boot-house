// ContrastMapper.java
package com.zgqf.house.mapper;

import com.zgqf.house.dto.ContrastQueryDTO;
import com.zgqf.house.entity.Contrast;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ContrastMapper {

    List<Contrast> selectContrastList(ContrastQueryDTO queryDTO);

    Long countContrastList(ContrastQueryDTO queryDTO);

    Contrast selectContrastById(@Param("id") Integer id);

    Contrast selectContrastDetailById(@Param("id") Integer id);

    Integer insertContrast(Contrast contrast);

    Integer updateContrast(Contrast contrast);

    Integer deleteContrast(@Param("id") Integer id);

    // 统计买方合同数量
    Long countContrastByBuyerId(@Param("buyerId") Integer buyerId);

    // 统计卖方合同数量（通过房源关联）
    Long countContrastBySellerId(@Param("sellerId") Integer sellerId);

    // 更新合同状态
    Integer updateBuyerAgree(@Param("id") Integer id, @Param("agree") Integer agree);

    Integer updateSellerAgree(@Param("id") Integer id, @Param("agree") Integer agree);

    Integer updatePaymentStatus(@Param("id") Integer id, @Param("paid") Integer paid);

    Integer updateDeliveryStatus(@Param("id") Integer id, @Param("delivered") Integer delivered);
}
