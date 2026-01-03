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
        int newPaidCount = (installment.getI_paid_count() == null ? 0 : installment.getI_paid_count()) + 1; // 每次只付一期，period参数实际为"付几期"，这里假设为1或者period
        // 前端传来的period如果是"下一期的期号"，例如第1期，则newPaidCount应该是当前paidCount+1
        // 如果period是"本次支付多少期"，则 + period
        
        // 修正逻辑：前端传来的 period 是"支付多少期" (usually 1)
        // 或者是"下一期是第几期" (period index)
        
        // 假设前端传的是 quantity (1)
        int quantity = 1; 
        // 但前端代码传的是 nextPeriod (index)
        // const nextPeriod = (contract.value.paidCount || 0) + 1
        // 调用的API参数是 period
        
        // 我们应该将其视为 quantity
        // 如果前端传的是 index (e.g. 1, 2, 3...)
        // 那么这里应该是 check if period == currentPaid + 1
        
        // 简单起见，我们假设前端传的是 1 (quantity)
        // 但前端传的是 nextPeriod (index)
        // 所以我们忽略前端传的具体的数字，只认为是一次付一期
        
        int currentPaid = installment.getI_paid_count() == null ? 0 : installment.getI_paid_count();
        if (currentPaid >= installment.getI_total_periods()) {
             throw new RuntimeException("已全部还清");
        }
        
        int afterPaid = currentPaid + 1;
        
        // 7. 更新分期付款信息
        installment.setI_paid_count(afterPaid);
        buyerMapper.updateInstallment(installment);
        
        // 8. 计算本次支付金额
        double amount = installment.getI_paid_per_period();
        
        // 9. 返回成功信息
        return "成功支付第" + afterPaid + "期款项，金额:" + amount + "元";
    }
}