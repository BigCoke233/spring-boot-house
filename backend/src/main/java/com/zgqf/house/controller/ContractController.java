package com.zgqf.house.controller;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contract;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.service.BuyerService;
import com.zgqf.house.service.ContractService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract")
public class ContractController {
    @Autowired
    private ContractService contractService;

    /*
     *  查看自己的合同列表
     *  函数名：getContrastsByUser
     *  @param HttpSession session 浏览器中包含"Buyer"或"Seller"属性的session
     *
     *  @return List<Contrast> 搜索出来的合同列表
     */
    @GetMapping("/")
    public List<Contract> getContrastsByUser(HttpSession session){
        return contractService.getContractsByUser(session);
    }

    /*
     *  查看合同详情
     *  函数名：getContrastsById
     *  @param Integer id 合同的c_id
     *
     *  @return Contrast 搜索出来的合同
     */
    @GetMapping("/{id}")
    public Contract getContrastsById(@PathVariable("id") Integer id){
        return contractService.getContractsById(id);
    }

    /*
     *  查看对方的商家资料
     *  函数名：signContrast
     *  @param Integer id 合同的c_id,
     *
     *  @return Seller 搜索出来的商家
     */
    @GetMapping("/seller_profile/{id}")
    public Seller getSellerByContrast(@PathVariable("id") Integer id){
        return contractService.getSellerByContract(id);
    }

    /*
     *  查看对方的买家资料
     *  函数名：signContrast
     *  @param Integer id 合同的c_id,
     *
     *  @return Buyer 搜索出来的商家
     */
    @GetMapping("/buyer_profile/{id}")
    public Buyer getBuyerByContrast(@PathVariable("id") Integer id){
        return contractService.getBuyerByContract(id);
    }

    /*
     *  买家创建新合同
     *  函数名：creatContrast
     *  @param Contrast contrast 包含"c_house_id"房源id,
     *                              "c_pay_way"支付方式,
     *                              "c_down_payment"首付金额（若为全款则为0或为空）
     *                              "c_total_periods"总期数（若为全款则为0或为空）
     *                              ,的json字符串,
     *  @param HttpSession session 浏览器中包含"Buyer"属性的session
     *
     *  @return String 返回回执("ok","no")
     */
    @PostMapping("/")
    public String creatContrast(@RequestBody Contract contract,HttpSession session){
        return contractService.creatContract(contract,session);
    }

    /*
     *  签署或拒绝合同
     *  函数名：signContrast
     *  @param Integer id 合同的c_id,
     *  @param Integer sign 同意(1)或拒绝(-1),
     *  @param HttpSession session 浏览器中包含"Buyer"或"Seller"属性的session
     *
     *  @return String 返回回执("ok","no")
     */
    @PostMapping("/sign/{id}/{sign}")
    public String signContrast(@PathVariable("id") Integer id,@PathVariable("sign") Integer sign,HttpSession session){
        return contractService.signContract(id,sign,session);
    }

    @Autowired
    private BuyerService buyerService;

    /**
     * 买家付款接口（全款/首付）
     * @param buyerId 买家ID
     * @param contractId 合同ID
     * @return 付款结果
     */
    @PostMapping("/pay/{contractId}")
    public ResponseEntity<String> payContract(@RequestHeader("buyerId") Integer buyerId,
                                             @PathVariable Integer contractId) {
        try {
            // 调用服务层处理付款逻辑
            String result = buyerService.processPayment(buyerId, contractId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("付款失败: " + e.getMessage());
        }
    }

    /**
     * 买家分期付款接口
     * @param buyerId 买家ID
     * @param contractId 合同ID
     * @param period 付款期数
     * @return 付款结果
     */
    @PostMapping("/installment/{contractId}")
    public ResponseEntity<String> payInstallment(@RequestHeader("buyerId") Integer buyerId,
                                               @PathVariable Integer contractId,
                                               @RequestParam Integer period) {
        try {
            // 调用服务层处理分期付款逻辑
            String result = buyerService.processInstallmentPayment(buyerId, contractId, period);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("分期付款失败: " + e.getMessage());
        }
    }
}
