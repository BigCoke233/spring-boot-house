package com.zgqf.house.service;

import com.zgqf.house.dto.ContrastQueryDTO;
import com.zgqf.house.dto.ContrastCreateDTO;
import com.zgqf.house.dto.ContrastUpdateDTO;
import com.zgqf.house.entity.Contrast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContrastService {

    Page<Contrast> getContrasts(ContrastQueryDTO queryDTO);

    Contrast getContrastDetail(Integer id);

    Contrast createContrast(ContrastCreateDTO createDTO);

    Contrast updateContrast(Integer id, ContrastUpdateDTO updateDTO);

    void deleteContrast(Integer id);

    // 合同状态操作
    Contrast buyerAgree(Integer id, Integer agree);

    Contrast sellerAgree(Integer id, Integer agree);

    Contrast updatePayment(Integer id, Integer paid);

    Contrast updateDelivery(Integer id, Integer delivered);

    // 统计
    Long countContrastsByBuyer(Integer buyerId);

    Long countContrastsBySeller(Integer sellerId);
}
