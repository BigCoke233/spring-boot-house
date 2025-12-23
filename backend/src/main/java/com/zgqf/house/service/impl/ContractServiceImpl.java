package com.zgqf.house.service.impl;

import com.zgqf.house.entity.*;
import com.zgqf.house.mapper.InstallmentMapper;
import com.zgqf.house.service.ContractService;
import com.zgqf.house.mapper.ContractMapper;
import com.zgqf.house.mapper.HouseMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService{
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private InstallmentMapper installmentMapper;
    @Autowired
    private HouseMapper houseMapper;

    /*
     *  查看自己的合同列表
     *  函数名：getContractsByUser
     *  @param HttpSession session 浏览器中包含"Buyer"或"Seller"属性的session
     *
     *  @return List<Contract> 搜索出来的合同列表
     */
    @Override
    public List<Contract> getContractsByUser(HttpSession session) {
        if (session.getAttribute("Buyer") != null){
            Buyer buyer = (Buyer) session.getAttribute("Buyer");
            return contractMapper.getContractsByBuyer(buyer.getB_id());
        }else if (session.getAttribute("Seller") != null){
            Seller seller = (Seller) session.getAttribute("Seller");
            return contractMapper.getContractsBySeller(seller.getS_id());
        }
        return null;
    }

    /*
     *  查看合同详情
     *  函数名：getContractsById
     *  @param Integer id 合同的c_id
     *
     *  @return Contract 搜索出来的合同
     */
    @Override
    public Contract getContractsById(Integer id) {
        return contractMapper.getContractsById(id);
    }

    /*
     *  查看对方的商家资料
     *  函数名：signContract
     *  @param Integer id 合同的c_id,
     *
     *  @return Seller 搜索出来的商家
     */
    @Override
    public Seller getSellerByContract(Integer id) {
        return contractMapper.getSellerByContract(id);
    }

    /*
     *  查看对方的买家资料
     *  函数名：signContract
     *  @param Integer id 合同的c_id,
     *
     *  @return Buyer 搜索出来的商家
     */
    @Override
    public Buyer getBuyerByContract(Integer id) {
        return contractMapper.getBuyerByContract(id);
    }

    /*
     *  买家创建新合同
     *  函数名：creatContract
     *  @param Contract Contract 包含"c_house_id"房源id,
     *                              "c_pay_way"支付方式,
     *                              "c_down_payment"首付金额（若为全款则为0或为空）
     *                              "c_total_periods"总期数（若为全款则为0或为空）
     *                              ,的json字符串,
     *  @param HttpSession session 浏览器中包含"Buyer"属性的session
     *
     *  @return String 返回回执("ok","no")
     */
    @Override
    public String creatContract(Contract Contract, HttpSession session) {
        if (!contractMapper.houseUsable(Contract.getC_house_id()).isEmpty()){
            return "no";
        }

        Buyer buyer = (Buyer) session.getAttribute("Buyer");

        Contract nowContract = new Contract();
        nowContract.setC_buyer_id(buyer.getB_id());
        nowContract.setC_house_id(Contract.getC_house_id());
        //这里缺少房源数据的接口
        //需要有一个根据房源id查找房源的接口
        House house = houseMapper.selectHouseById(Contract.getC_house_id());
        nowContract.setC_total_price(house.getH_price() * house.getH_square());
        if (Contract.getC_pay_way().equals("full")){
            nowContract.setC_pay_way("full");
            contractMapper.insertContract(nowContract);
            return "ok";
        }else if (Contract.getC_pay_way().equals("installment")){
            nowContract.setC_pay_way("installment");
            contractMapper.insertContract(nowContract);

            Contract c = contractMapper.getLastInsertContract(nowContract);

            Installment installment = new Installment();
            installment.setI_contract_id(c.getC_id());
            installment.setI_down_payment(Contract.getC_down_payment());
            installment.setI_total_periods(Contract.getC_total_periods());
            installment.setI_paid_count(0);
            installment.setI_paid_per_period((c.getC_total_price() - installment.getI_down_payment()) / installment.getI_total_periods());

            installmentMapper.insertInstallment(installment);
            return "ok";
        }

        return "no";
    }

    /*
     *  签署或拒绝合同
     *  函数名：signContract
     *  @param Integer id 合同的c_id,
     *  @param Integer sign 同意(1)或拒绝(-1"),
     *  @param HttpSession session 浏览器中包含"Buyer"或"Seller"属性的session
     *
     *  @return String 返回回执("ok","no")
     */
    @Override
    public String signContract(Integer id, Integer sign, HttpSession session) {
        Contract Contract = contractMapper.getContractsById(id);
        if (Contract.getC_seller_agree().equals(-1) || Contract.getC_buyer_agree().equals(-1)){
            return "no";
        }

        Buyer buyer = null;
        Seller seller = null;

        if (session.getAttribute("Buyer") != null){
            Buyer buyerInContract = contractMapper.getBuyerByContract(id);
            buyer = (Buyer) session.getAttribute("Buyer");
            if (buyerInContract.getB_id().equals(buyer.getB_id())){
                return "no";
            }

            Contract.setC_buyer_id(buyer.getB_id());
            Contract.setC_buyer_agree(sign);
        }else if (session.getAttribute("Seller") != null){
            Seller sellerInContract = contractMapper.getSellerByContract(id);
            seller = (Seller) session.getAttribute("Seller");
            if(sellerInContract.getS_id().equals(seller.getS_id())){
                return "no";
            }

            Contract.setC_seller_agree(sign);
        }
        contractMapper.signContract(Contract);

        if (Contract.getC_buyer_agree().equals(1) && Contract.getC_seller_agree().equals(1)){
            Date date = new Date();
            long day = 14;
            date.setTime(date.getTime() + day * 24 * 60 * 60 * 1000);
            Contract.setC_paytime_ending(date);
            contractMapper.addPayTimeEnding(Contract);
        }

        return "ok";
    }
}
