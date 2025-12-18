// ContrastServiceImpl.java
package com.zgqf.house.service.serviceImpl;

import com.zgqf.house.dto.ContrastQueryDTO;
import com.zgqf.house.dto.ContrastCreateDTO;
import com.zgqf.house.dto.ContrastUpdateDTO;
import com.zgqf.house.entity.Contrast;
import com.zgqf.house.mapper.ContrastMapper;
import com.zgqf.house.service.ContrastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContrastServiceImpl implements ContrastService {

    private final ContrastMapper contrastMapper;

    @Override
    public Page<Contrast> getContrasts(ContrastQueryDTO queryDTO) {
        log.info("查询合同列表, buyerId={}, sellerId={}, pageNum={}, pageSize={}",
                queryDTO.getBuyerId(), queryDTO.getSellerId(),
                queryDTO.getPageNum(), queryDTO.getPageSize());

        try {
            // 验证分页参数
            if (queryDTO.getPageNum() == null || queryDTO.getPageNum() <= 0) {
                queryDTO.setPageNum(1);
            }
            if (queryDTO.getPageSize() == null || queryDTO.getPageSize() <= 0) {
                queryDTO.setPageSize(10);
            }

            // 设置最大分页限制（防止恶意请求）
            if (queryDTO.getPageSize() > 100) {
                queryDTO.setPageSize(100);
            }

            // 执行查询
            List<Contrast> list = contrastMapper.selectContrastList(queryDTO);
            Long total = contrastMapper.countContrastList(queryDTO);

            // 计算分页信息
            int totalPages = (int) Math.ceil((double) total / queryDTO.getPageSize());
            int currentPage = Math.min(queryDTO.getPageNum(), Math.max(1, totalPages));

            Pageable pageable = PageRequest.of(
                    currentPage - 1,
                    queryDTO.getPageSize(),
                    Sort.by(Sort.Direction.fromString(queryDTO.getSortOrder()), queryDTO.getSortBy())
            );

            Page<Contrast> result = new PageImpl<>(list, pageable, total);

            log.info("查询合同列表完成, 共{}条记录, {}页, 当前第{}页",
                    total, totalPages, currentPage);
            return result;

        } catch (Exception e) {
            log.error("查询合同列表失败, 参数: {}", queryDTO, e);
            throw new RuntimeException("查询合同列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Contrast getContrastById(Integer id) {
        log.info("查询合同详情, id={}", id);
        return contrastMapper.selectContrastById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Contrast getContrastDetail(Integer id) {
        log.info("查询合同详细信息, id={}", id);
        return contrastMapper.selectContrastDetailById(id);
    }

    @Override
    @Transactional
    public Contrast createContrast(ContrastCreateDTO createDTO) {
        log.info("创建合同, buyerId={}, houseId={}",
                createDTO.getBuyerId(), createDTO.getHouseId());

        // 验证输入数据
        validateContrastCreateDTO(createDTO);

        Contrast contrast = new Contrast();
        contrast.setBuyerId(createDTO.getBuyerId());
        contrast.setHouseId(createDTO.getHouseId());
        contrast.setTotalPrice(createDTO.getTotalPrice());
        contrast.setPayWay(createDTO.getPayWay());
        contrast.setPaytimeEnding(createDTO.getPaytimeEnding());
        contrast.setDeliveryEnding(createDTO.getDeliveryEnding());
        contrast.setBuyerAgree(0);   // 初始状态：未处理
        contrast.setSellerAgree(0);  // 初始状态：未处理
        contrast.setPaid(0);         // 初始状态：未付款
        contrast.setDelivered(0);    // 初始状态：未交房

        int rows = contrastMapper.insertContrast(contrast);
        if (rows == 0) {
            throw new RuntimeException("创建合同失败");
        }

        log.info("合同创建成功, id={}", contrast.getId());
        return contrastMapper.selectContrastDetailById(contrast.getId());
    }

    @Override
    @Transactional
    public Contrast updateContrast(Integer id, ContrastUpdateDTO updateDTO) {
        log.info("更新合同, id={}", id);

        // 检查合同是否存在
        Contrast existingContrast = contrastMapper.selectContrastById(id);
        if (existingContrast == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        // 创建更新对象
        Contrast contrast = new Contrast();
        contrast.setId(id);

        // 设置可更新的字段
        if (updateDTO.getBuyerAgree() != null) {
            contrast.setBuyerAgree(updateDTO.getBuyerAgree());
        }
        if (updateDTO.getSellerAgree() != null) {
            contrast.setSellerAgree(updateDTO.getSellerAgree());
        }
        if (updateDTO.getPaytimeActually() != null) {
            contrast.setPaytimeActually(updateDTO.getPaytimeActually());
        }
        if (updateDTO.getDeliveryActually() != null) {
            contrast.setDeliveryActually(updateDTO.getDeliveryActually());
        }
        if (updateDTO.getPaid() != null) {
            contrast.setPaid(updateDTO.getPaid());
        }
        if (updateDTO.getDelivered() != null) {
            contrast.setDelivered(updateDTO.getDelivered());
        }

        int rows = contrastMapper.updateContrast(contrast);
        if (rows == 0) {
            throw new RuntimeException("更新合同失败，ID: " + id);
        }

        log.info("合同更新成功, id={}", id);
        return contrastMapper.selectContrastDetailById(id);
    }

    @Override
    @Transactional
    public void deleteContrast(Integer id) {
        log.info("删除合同, id={}", id);

        // 检查合同是否存在
        Contrast contrast = contrastMapper.selectContrastById(id);
        if (contrast == null) {
            log.warn("删除失败: 合同不存在，ID: {}", id);
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contrastMapper.deleteContrast(id);
        if (rows == 0) {
            throw new RuntimeException("删除合同失败，ID: " + id);
        }

        log.info("合同删除成功, id={}", id);
    }

    @Override
    @Transactional
    public Contrast buyerAgree(Integer id, Integer agree) {
        log.info("买方同意/拒绝合同, id={}, agree={}", id, agree);

        // 验证agree参数
        if (!List.of(-1, 0, 1).contains(agree)) {
            throw new IllegalArgumentException("agree参数必须是-1, 0, 1");
        }

        // 检查合同是否存在
        Contrast existingContrast = contrastMapper.selectContrastById(id);
        if (existingContrast == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contrastMapper.updateBuyerAgree(id, agree);
        if (rows == 0) {
            throw new RuntimeException("更新买方同意状态失败，ID: " + id);
        }

        log.info("买方同意状态更新成功, id={}, agree={}", id, agree);
        return contrastMapper.selectContrastDetailById(id);
    }

    @Override
    @Transactional
    public Contrast sellerAgree(Integer id, Integer agree) {
        log.info("卖方同意/拒绝合同, id={}, agree={}", id, agree);

        // 验证agree参数
        if (!List.of(-1, 0, 1).contains(agree)) {
            throw new IllegalArgumentException("agree参数必须是-1, 0, 1");
        }

        // 检查合同是否存在
        Contrast existingContrast = contrastMapper.selectContrastById(id);
        if (existingContrast == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contrastMapper.updateSellerAgree(id, agree);
        if (rows == 0) {
            throw new RuntimeException("更新卖方同意状态失败，ID: " + id);
        }

        log.info("卖方同意状态更新成功, id={}, agree={}", id, agree);
        return contrastMapper.selectContrastDetailById(id);
    }

    @Override
    @Transactional
    public Contrast updatePayment(Integer id, Integer paid) {
        log.info("更新付款状态, id={}, paid={}", id, paid);

        // 检查合同是否存在
        Contrast existingContrast = contrastMapper.selectContrastById(id);
        if (existingContrast == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contrastMapper.updatePaymentStatus(id, paid);
        if (rows == 0) {
            throw new RuntimeException("更新付款状态失败，ID: " + id);
        }

        // 如果付款成功，记录付款时间
        if (paid == 1) {
            Contrast contrast = new Contrast();
            contrast.setId(id);
            contrast.setPaytimeActually(new Date());
            contrastMapper.updateContrast(contrast);
            log.info("记录付款时间, id={}", id);
        }

        log.info("付款状态更新成功, id={}, paid={}", id, paid);
        return contrastMapper.selectContrastDetailById(id);
    }

    @Override
    @Transactional
    public Contrast updateDelivery(Integer id, Integer delivered) {
        log.info("更新交房状态, id={}, delivered={}", id, delivered);

        // 检查合同是否存在
        Contrast existingContrast = contrastMapper.selectContrastById(id);
        if (existingContrast == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contrastMapper.updateDeliveryStatus(id, delivered);
        if (rows == 0) {
            throw new RuntimeException("更新交房状态失败，ID: " + id);
        }

        // 如果交房成功，记录交房时间
        if (delivered == 1) {
            Contrast contrast = new Contrast();
            contrast.setId(id);
            contrast.setDeliveryActually(new Date());
            contrastMapper.updateContrast(contrast);
            log.info("记录交房时间, id={}", id);
        }

        log.info("交房状态更新成功, id={}, delivered={}", id, delivered);
        return contrastMapper.selectContrastDetailById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countContrastsByBuyer(Integer buyerId) {
        return contrastMapper.countContrastByBuyerId(buyerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countContrastsBySeller(Integer sellerId) {
        return contrastMapper.countContrastBySellerId(sellerId);
    }

    /**
     * 验证合同创建DTO
     */
    private void validateContrastCreateDTO(ContrastCreateDTO dto) {
        if (dto.getBuyerId() == null) {
            throw new IllegalArgumentException("买方ID不能为空");
        }
        if (dto.getHouseId() == null) {
            throw new IllegalArgumentException("房源ID不能为空");
        }
        if (dto.getTotalPrice() == null || dto.getTotalPrice() <= 0) {
            throw new IllegalArgumentException("总价必须大于0");
        }
        if (dto.getPayWay() == null ||
                (!"full".equals(dto.getPayWay()) && !"installment".equals(dto.getPayWay()))) {
            throw new IllegalArgumentException("付款方式必须是'full'或'installment'");
        }
        if (dto.getPaytimeEnding() == null) {
            throw new IllegalArgumentException("付款截止日期不能为空");
        }
        if (dto.getDeliveryEnding() == null) {
            throw new IllegalArgumentException("交房截止日期不能为空");
        }
    }
}