package com.zgqf.house.mapper;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.entity.Installment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface BuyerMapper {
    /**
     * 根据买家ID获取买家信息
     * @param buyerId 买家ID
     * @return 买家信息
     */
    Buyer getBuyerById(@Param("buyerId") Integer buyerId);
    
    /**
     * 更新买家信息
     * @param buyer 买家信息对象
     */
    void updateBuyer(Buyer buyer);
    
    /**
     * 根据合同ID获取合同信息
     * @param contractId 合同ID
     * @return 合同信息
     */
    Contract getContractById(@Param("contractId") Integer contractId);
    
    /**
     * 更新合同信息
     * @param contract 合同对象
     */
    void updateContract(Contract contract);
    
    /**
     * 根据合同ID获取分期付款信息
     * @param contractId 合同ID
     * @return 分期付款信息
     */
    Installment getInstallmentByContractId(@Param("contractId") Integer contractId);
    
    /**
     * 更新分期付款信息
     * @param installment 分期付款对象
     */
    void updateInstallment(Installment installment);
    
    /**
     * 获取分期付款计算详情
     * @param contractId 合同ID
     * @return 分期付款计算详情Map
     */
    Map<String, Object> getInstallmentCalculationDetails(@Param("contractId") Integer contractId);
}