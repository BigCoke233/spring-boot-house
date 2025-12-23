package com.zgqf.house.service;

import com.zgqf.house.dto.ContractQueryDTO;
import com.zgqf.house.dto.ContractCreateDTO;
import com.zgqf.house.dto.ContractUpdateDTO;
import com.zgqf.house.entity.Contract;
import org.springframework.data.domain.Page;

public interface ContractService {

    Page<Contract> getContracts(ContractQueryDTO queryDTO);

    Contract getContractDetail(Integer id);

    Contract createContract(ContractCreateDTO createDTO);

    Contract updateContract(Integer id, ContractUpdateDTO updateDTO);

    void deleteContract(Integer id);

    // 合同状态操作
    Contract buyerAgree(Integer id, Integer agree);

    Contract sellerAgree(Integer id, Integer agree);

    Contract updatePayment(Integer id, Integer paid);

    Contract updateDelivery(Integer id, Integer delivered);

    // 统计
    Long countContractsByBuyer(Integer buyerId);

    Long countContractsBySeller(Integer sellerId);
}
