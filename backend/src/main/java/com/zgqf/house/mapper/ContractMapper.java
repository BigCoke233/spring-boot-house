package com.zgqf.house.mapper;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractMapper {
    //查看卖家的合同列表
    public List<Contract> getContractsBySeller(Integer s_id);
    //查看买家的合同列表
    public List<Contract> getContractsByBuyer(Integer b_id);
    //根据合同id查找合同
    public Contract getContractsById(Integer c_id);
    //查看对方的商家资料
    public Seller getSellerByContract(Integer c_id);
    //查看对方的买家资料
    public Buyer getBuyerByContract(Integer c_id);
    //签署或拒绝合同
    public void signContract(Contract contract);
    //查询房子是否可用
    public List<Contract> houseUsable(Integer h_id);
    //创建新合同
    public void insertContract(Contract contract);
    //查找新添加的合同
    public Contract getLastInsertContract(Contract contract);
    //添加预计付款时间
    public void addPayTimeEnding(Contract contract);
}
