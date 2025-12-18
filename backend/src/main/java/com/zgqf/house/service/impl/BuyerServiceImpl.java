package com.zgqf.house.service.impl;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.entity.Installment;
import com.zgqf.house.mapper.BuyerMapper;
import com.zgqf.house.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Calendar;

@Service
public class BuyerServiceImpl implements BuyerService {
    
    @Autowired
    private BuyerMapper buyerMapper;
    
    @Override
    public Buyer getBuyerProfile(Integer buyerId) {
        return buyerMapper.getBuyerById(buyerId);
    }
    
    @Override
    public boolean updateBuyerProfile(Integer buyerId, Buyer buyerInfo) {
        // 确保更新的是当前用户的信息
        buyerInfo.setB_id(buyerId);
        buyerMapper.updateBuyer(buyerInfo);
        return true;
    }
    
    @Override
    @Transactional
    public String processPayment(Integer buyerId, Integer contractId) {
        // 1. 获取合同信息
        Contract contract = buyerMapper.getContractById(contractId);
        
        // 2. 验证合同是否存在以及是否属于当前买家
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }
        
        if (!contract.getC_buyer_id().equals(buyerId)) {
            throw new RuntimeException("合同不属于当前买家");
        }
        
        // 3. 判断双方是否都已同意
        if (contract.getC_buyer_agree() != 1 || contract.getC_seller_agree() != 1) {
            throw new RuntimeException("双方未都同意，无法付款");
        }
        
        // 4. 更新合同表信息
        contract.setC_paid(1); // 标记为已付款（全款或首付）
        contract.setC_paytime_actually(new Date()); // 设置实际付款时间
        
        // 5. 计算并设置交房截止时间（示例：付款后30天内交房）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        contract.setC_delivery_ending(calendar.getTime());
        
        // 6. 更新数据库
        buyerMapper.updateContract(contract);
        
        // 7. 根据付款方式返回不同消息
        if ("full".equals(contract.getC_pay_way())) {
            return "全款支付成功";
        } else {
            return "首付支付成功";
        }
    }
    
    @Override
    @Transactional
    public String processInstallmentPayment(Integer buyerId, Integer contractId, Integer period) {
        // 1. 获取合同信息
        Contract contract = buyerMapper.getContractById(contractId);
        
        // 2. 验证合同是否存在以及是否属于当前买家
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }
        
        if (!contract.getC_buyer_id().equals(buyerId)) {
            throw new RuntimeException("合同不属于当前买家");
        }
        
        // 3. 判断是否为分期付款方式
        if (!"installment".equals(contract.getC_pay_way())) {
            throw new RuntimeException("该合同不支持分期付款");
        }
        
        // 4. 判断是否已经首付
        if (contract.getC_paid() == null || contract.getC_paid() != 1) {
            throw new RuntimeException("尚未支付首付款，无法进行分期付款");
        }
        
        // 5. 获取分期付款信息
        Installment installment = buyerMapper.getInstallmentByContractId(contractId);
        if (installment == null) {
            throw new RuntimeException("分期付款信息不存在");
        }
        
        // 6. 验证期数是否有效（不能超过总期数）
        int newPaidCount = (installment.getI_paid_count() == null ? 0 : installment.getI_paid_count()) + period;
        if (newPaidCount > installment.getI_total_periods()) {
            throw new RuntimeException("付款期数超过剩余期数");
        }
        
        // 7. 更新分期付款信息（累加已付款期数）
        installment.setI_paid_count(newPaidCount);
        buyerMapper.updateInstallment(installment);
        
        // 8. 计算本次支付金额
        double amount = period * installment.getI_paid_per_period();
        
        // 9. 返回成功信息
        return "成功支付" + period + "期款项，共计:" + amount + "元";
    }
}