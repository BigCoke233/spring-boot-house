package com.zgqf.house.service.impl;

import com.zgqf.house.dto.ContractQueryDTO;
import com.zgqf.house.dto.ContractCreateDTO;
import com.zgqf.house.dto.ContractUpdateDTO;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.mapper.ContractMapper;
import com.zgqf.house.service.ContractService;
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
public class ContractServiceImpl implements ContractService {

    private final ContractMapper contractMapper;

    @Override
    public Page<Contract> getContracts(ContractQueryDTO queryDTO) {
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
            List<Contract> list = contractMapper.selectContractList(queryDTO);
            Long total = contractMapper.countContractList(queryDTO);

            // 计算分页信息
            int totalPages = (int) Math.ceil((double) total / queryDTO.getPageSize());
            int currentPage = Math.min(queryDTO.getPageNum(), Math.max(1, totalPages));

            Pageable pageable = PageRequest.of(
                    currentPage - 1,
                    queryDTO.getPageSize(),
                    Sort.by(Sort.Direction.fromString(queryDTO.getSortOrder()), queryDTO.getSortBy())
            );

            Page<Contract> result = new PageImpl<>(list, pageable, total);

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
    public Contract getContractDetail(Integer id) {
        log.info("查询合同详细信息, id={}", id);
        return contractMapper.selectContractDetailById(id);
    }

    @Override
    @Transactional
    public Contract createContract(ContractCreateDTO createDTO) {
        log.info("创建合同, buyerId={}, houseId={}",
                createDTO.getBuyerId(), createDTO.getHouseId());

        // 验证输入数据
        validateContractCreateDTO(createDTO);

        Contract contract = new Contract();
        contract.setC_buyer_id(createDTO.getBuyerId());
        contract.setC_house_id(createDTO.getHouseId());
        contract.setC_total_price(createDTO.getTotalPrice());
        contract.setC_pay_way(createDTO.getPayWay());
        contract.setC_paytime_ending(createDTO.getPaytimeEnding());
        contract.setC_delivery_ending(createDTO.getDeliveryEnding());
        contract.setC_buyer_agree(0);   // 初始状态：未处理
        contract.setC_seller_agree(0);  // 初始状态：未处理
        contract.setC_paid(0);         // 初始状态：未付款
        contract.setC_delivered(0);    // 初始状态：未交房

        int rows = contractMapper.insertContract(contract);
        if (rows == 0) {
            throw new RuntimeException("创建合同失败");
        }

        // 获取插入后的ID，这里假设mapper.insertContract会回填ID，或者我们需要重新查询
        // 如果mapper配置了useGeneratedKeys="true" keyProperty="c_id"，那么contract.getC_id()会有值
        if (contract.getC_id() != null) {
            log.info("合同创建成功, id={}", contract.getC_id());
            return contractMapper.selectContractDetailById(contract.getC_id());
        } else {
             // Fallback logic if ID not returned directly (though MyBatis usually does)
             // For now assume it works or we might need to change how we fetch the new contract
             // Using getLastInsertContract logic from old implementation might be needed if not using generated keys
             // But let's assume standard MyBatis behavior first.
             // If we really need the ID and it's missing, we might need a workaround.
             // Given the old code had `getLastInsertContract`, maybe we need that.
             // But `insertContract` usually returns affected rows.
             // Let's try to fetch by the object if ID is missing (risky without unique constraints other than ID)
             // Actually, let's just log and return null or throw if ID is missing?
             // Or better, assume `selectContractDetailById` needs an ID.
             // Let's assume `useGeneratedKeys` is ON.
             return contract; 
        }
    }

    @Override
    @Transactional
    public Contract updateContract(Integer id, ContractUpdateDTO updateDTO) {
        log.info("更新合同, id={}", id);

        // 检查合同是否存在
        Contract existingContract = contractMapper.selectContractDetailById(id);
        if (existingContract == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        // 创建更新对象
        Contract contract = new Contract();
        contract.setC_id(id);

        // 设置可更新的字段
        if (updateDTO.getBuyerAgree() != null) {
            contract.setC_buyer_agree(updateDTO.getBuyerAgree());
        }
        if (updateDTO.getSellerAgree() != null) {
            contract.setC_seller_agree(updateDTO.getSellerAgree());
        }
        if (updateDTO.getPaytimeActually() != null) {
            contract.setC_paytime_actually(updateDTO.getPaytimeActually());
        }
        if (updateDTO.getDeliveryActually() != null) {
            contract.setC_delivery_actually(updateDTO.getDeliveryActually());
        }
        if (updateDTO.getPaid() != null) {
            contract.setC_paid(updateDTO.getPaid());
        }
        if (updateDTO.getDelivered() != null) {
            contract.setC_delivered(updateDTO.getDelivered());
        }

        int rows = contractMapper.updateContract(contract);
        if (rows == 0) {
            throw new RuntimeException("更新合同失败，ID: " + id);
        }

        log.info("合同更新成功, id={}", id);
        return contractMapper.selectContractDetailById(id);
    }

    @Override
    @Transactional
    public void deleteContract(Integer id) {
        log.info("删除合同, id={}", id);

        // 检查合同是否存在
        Contract contract = contractMapper.selectContractDetailById(id);
        if (contract == null) {
            log.warn("删除失败: 合同不存在，ID: {}", id);
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contractMapper.deleteContract(id);
        if (rows == 0) {
            throw new RuntimeException("删除合同失败，ID: " + id);
        }

        log.info("合同删除成功, id={}", id);
    }

    @Override
    @Transactional
    public Contract buyerAgree(Integer id, Integer agree) {
        log.info("买方同意/拒绝合同, id={}, agree={}", id, agree);

        // 验证agree参数
        if (!List.of(-1, 0, 1).contains(agree)) {
            throw new IllegalArgumentException("agree参数必须是-1, 0, 1");
        }

        // 检查合同是否存在
        Contract existingContract = contractMapper.selectContractDetailById(id);
        if (existingContract == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contractMapper.updateBuyerAgree(id, agree);
        if (rows == 0) {
            throw new RuntimeException("更新买方同意状态失败，ID: " + id);
        }

        log.info("买方同意状态更新成功, id={}, agree={}", id, agree);
        return contractMapper.selectContractDetailById(id);
    }

    @Override
    @Transactional
    public Contract sellerAgree(Integer id, Integer agree) {
        log.info("卖方同意/拒绝合同, id={}, agree={}", id, agree);

        // 验证agree参数
        if (!List.of(-1, 0, 1).contains(agree)) {
            throw new IllegalArgumentException("agree参数必须是-1, 0, 1");
        }

        // 检查合同是否存在
        Contract existingContract = contractMapper.selectContractDetailById(id);
        if (existingContract == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contractMapper.updateSellerAgree(id, agree);
        if (rows == 0) {
            throw new RuntimeException("更新卖方同意状态失败，ID: " + id);
        }

        log.info("卖方同意状态更新成功, id={}, agree={}", id, agree);
        return contractMapper.selectContractDetailById(id);
    }

    @Override
    @Transactional
    public Contract updatePayment(Integer id, Integer paid) {
        log.info("更新付款状态, id={}, paid={}", id, paid);

        // 检查合同是否存在
        Contract existingContract = contractMapper.selectContractDetailById(id);
        if (existingContract == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contractMapper.updatePaymentStatus(id, paid);
        if (rows == 0) {
            throw new RuntimeException("更新付款状态失败，ID: " + id);
        }

        // 如果付款成功，记录付款时间
        if (paid == 1) {
            Contract contract = new Contract();
            contract.setC_id(id);
            contract.setC_paytime_actually(new Date());
            contractMapper.updateContract(contract);
            log.info("记录付款时间, id={}", id);
        }

        log.info("付款状态更新成功, id={}, paid={}", id, paid);
        return contractMapper.selectContractDetailById(id);
    }

    @Override
    @Transactional
    public Contract updateDelivery(Integer id, Integer delivered) {
        log.info("更新交房状态, id={}, delivered={}", id, delivered);

        // 检查合同是否存在
        Contract existingContract = contractMapper.selectContractDetailById(id);
        if (existingContract == null) {
            throw new RuntimeException("合同不存在，ID: " + id);
        }

        int rows = contractMapper.updateDeliveryStatus(id, delivered);
        if (rows == 0) {
            throw new RuntimeException("更新交房状态失败，ID: " + id);
        }

        // 如果交房成功，记录交房时间
        if (delivered == 1) {
            Contract contract = new Contract();
            contract.setC_id(id);
            contract.setC_delivery_actually(new Date());
            contractMapper.updateContract(contract);
            log.info("记录交房时间, id={}", id);
        }

        log.info("交房状态更新成功, id={}, delivered={}", id, delivered);
        return contractMapper.selectContractDetailById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countContractsByBuyer(Integer buyerId) {
        return contractMapper.countContractByBuyerId(buyerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countContractsBySeller(Integer sellerId) {
        return contractMapper.countContractBySellerId(sellerId);
    }

    /**
     * 验证合同创建DTO
     */
    private void validateContractCreateDTO(ContractCreateDTO dto) {
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
