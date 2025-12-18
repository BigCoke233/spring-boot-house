package com.zgqf.house.service;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.entity.Seller;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface ContractService {
    public List<Contract> getContractsByUser(HttpSession session);
    public Contract getContractsById(Integer id);
    public Seller getSellerByContract(Integer id);
    public Buyer getBuyerByContract(Integer id);
    public String creatContract(Contract contract, HttpSession session);
    public String signContract(Integer id,Integer sign,HttpSession session);
}
