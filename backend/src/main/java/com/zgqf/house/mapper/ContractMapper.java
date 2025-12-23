package com.zgqf.house.mapper;

import com.zgqf.house.dto.ContractQueryDTO;
import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {

    // === 原始方法保留，避免影响旧逻辑 ===
    //查看卖家的合同列表
    List<Contract> getContractsBySeller(Integer s_id);
    //查看买家的合同列表
    List<Contract> getContractsByBuyer(Integer b_id);
    //根据合同id查找合同
    Contract getContractsById(Integer c_id);
    //查看对方的商家资料
    Seller getSellerByContract(Integer c_id);
    //查看对方的买家资料
    Buyer getBuyerByContract(Integer c_id);
    //签署或拒绝合同
    void signContract(Contract contract);
    //查询房子是否可用
    List<Contract> houseUsable(Integer h_id);
    //创建新合同 (修改返回值以支持 Mybatis 返回受影响行数)
    int insertContract(Contract contract);
    //查找新添加的合同
    Contract getLastInsertContract(Contract contract);
    //添加预计付款时间
    void addPayTimeEnding(Contract contract);

    // === 新增方法以支持合并后的 Service 逻辑 ===
    // 条件查询合同列表
    List<Contract> selectContractList(ContractQueryDTO queryDTO);
    // 统计合同数量
    Long countContractList(ContractQueryDTO queryDTO);
    // 根据ID查询详情（包含关联信息）
    Contract selectContractDetailById(Integer id);
    // 更新合同
    int updateContract(Contract contract);
    // 删除合同
    int deleteContract(Integer id);
    
    // 状态更新快捷方法
    int updateBuyerAgree(@Param("id") Integer id, @Param("agree") Integer agree);
    int updateSellerAgree(@Param("id") Integer id, @Param("agree") Integer agree);
    int updatePaymentStatus(@Param("id") Integer id, @Param("paid") Integer paid);
    int updateDeliveryStatus(@Param("id") Integer id, @Param("delivered") Integer delivered);
    
    // 统计方法
    Long countContractByBuyerId(Integer buyerId);
    Long countContractBySellerId(Integer sellerId);
}
