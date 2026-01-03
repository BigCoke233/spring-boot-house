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
    private final com.zgqf.house.mapper.HouseMapper houseMapper;
    private final com.zgqf.house.mapper.InstallmentMapper installmentMapper;

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

        // 获取插入后的ID（MyBatis 会回填到 contract 对象中）
        Integer contractId = contract.getC_id();
        if (contractId == null) {
             // 如果 ID 没有回填，尝试使用 getLastInsertContract 获取
             log.warn("Contract ID not returned by insert, trying to fetch...");
             Contract queryContract = new Contract();
             queryContract.setC_buyer_id(createDTO.getBuyerId());
             queryContract.setC_pay_way(createDTO.getPayWay());
             queryContract.setC_house_id(createDTO.getHouseId());
             Contract lastContract = contractMapper.getLastInsertContract(queryContract);
             if (lastContract != null) {
                 contractId = lastContract.getC_id();
                 contract.setC_id(contractId);
             } else {
                 throw new RuntimeException("无法获取新创建的合同ID");
             }
        }

        // 处理分期付款逻辑
        if ("installment".equals(createDTO.getPayWay())) {
            if (createDTO.getTotalPeriods() == null || createDTO.getTotalPeriods() <= 0) {
                // 如果前端没传或者传了0，默认12期
                createDTO.setTotalPeriods(12);
            }

            // 假设首付30%
            double downPayment = createDTO.getTotalPrice() * 0.3;
            // 剩余款项
            double remainPrice = createDTO.getTotalPrice() - downPayment;
            // 每期还款
            double paidPerPeriod = remainPrice / createDTO.getTotalPeriods();

            com.zgqf.house.entity.Installment installment = new com.zgqf.house.entity.Installment();
            installment.setI_contract_id(contractId); // 确保使用正确的ID
            installment.setI_down_payment(downPayment);
            installment.setI_total_periods(createDTO.getTotalPeriods());
            installment.setI_paid_count(0);
            installment.setI_paid_per_period(paidPerPeriod);

            installmentMapper.insertInstallment(installment);
            log.info("创建分期付款信息成功, contractId={}", contractId);
        }

        // 返回完整详情
        log.info("合同创建成功, id={}", contractId);
        return contractMapper.selectContractDetailById(contractId);
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
        
        // 如果卖方同意（agree=1），则下架房源（设置h_checked=0或2，根据业务需求，这里假设0为未审核/下架，或者需要新的状态）
        // 查看House实体和Mapper，h_checked似乎用于审核状态。
        // 如果我们想表示"已售出"或"交易中"，可能需要修改状态。
        // 根据通常逻辑，达成合同后房源不应再展示给其他买家。
        // 这里我们将h_checked设置为2（假设2代表已售出/下架），或者直接用0（未审核/不可见）。
        // 为了安全起见，我们先设为2，如果系统只查询h_checked=1的，那么2也就不可见了。
        if (agree == 1) {
            Integer houseId = existingContract.getC_house_id();
            if (houseId != null) {
                com.zgqf.house.entity.House house = new com.zgqf.house.entity.House();
                house.setH_id(houseId);
                house.setH_checked(2); // 2: 已售出/下架
                houseMapper.update(house);
                log.info("卖方同意合同，房源已下架, houseId={}", houseId);
            }
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
