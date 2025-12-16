package com.zgqf.house.service;

import com.zgqf.house.entity.*;
import com.zgqf.house.mapper.ContrastMapper;
import com.zgqf.house.mapper.InstallmentMapper;
import com.zgqf.house.mapper.HouseMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContrastServiceImpl implements ContrastService{
    @Autowired
    private ContrastMapper contrastMapper;
    @Autowired
    private InstallmentMapper installmentMapper;
    @Autowired
    private HouseMapper houseMapper;

    /*
     *  查看自己的合同列表
     *  函数名：getContrastsByUser
     *  @param HttpSession session 浏览器中包含"Buyer"或"Seller"属性的session
     *
     *  @return List<Contrast> 搜索出来的合同列表
     */
    @Override
    public List<Contrast> getContrastsByUser(HttpSession session) {
        if (session.getAttribute("Buyer") != null){
            Buyer buyer = (Buyer) session.getAttribute("Buyer");
            return contrastMapper.getContrastsByBuyer(buyer.getB_id());
        }else if (session.getAttribute("Seller") != null){
            Seller seller = (Seller) session.getAttribute("Seller");
            return contrastMapper.getContrastsBySeller(seller.getS_id());
        }
        return null;
    }

    /*
     *  查看合同详情
     *  函数名：getContrastsById
     *  @param Integer id 合同的c_id
     *
     *  @return Contrast 搜索出来的合同
     */
    @Override
    public Contrast getContrastsById(Integer id) {
        return contrastMapper.getContrastsById(id);
    }

    /*
     *  查看对方的商家资料
     *  函数名：signContrast
     *  @param Integer id 合同的c_id,
     *
     *  @return Seller 搜索出来的商家
     */
    @Override
    public Seller getSellerByContrast(Integer id) {
        return contrastMapper.getSellerByContrast(id);
    }

    /*
     *  查看对方的买家资料
     *  函数名：signContrast
     *  @param Integer id 合同的c_id,
     *
     *  @return Buyer 搜索出来的商家
     */
    @Override
    public Buyer getBuyerByContrast(Integer id) {
        return contrastMapper.getBuyerByContrast(id);
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
    @Override
    public String creatContrast(Contrast contrast, HttpSession session) {
        if (!contrastMapper.houseUsable(contrast.getC_house_id()).isEmpty()){
            return "no";
        }

        Buyer buyer = (Buyer) session.getAttribute("Buyer");

        Contrast nowContrast = new Contrast();
        nowContrast.setC_buyer_id(buyer.getB_id());
        nowContrast.setC_house_id(contrast.getC_house_id());
        //这里缺少房源数据的接口
        //需要有一个根据房源id查找房源的接口
        House house = houseMapper.getHouseById(contrast.getC_house_id());
        nowContrast.setC_total_price(house.getH_price() * house.getH_square());
        if (contrast.getC_pay_way().equals("full")){
            nowContrast.setC_pay_way("full");
            contrastMapper.insertContrast(nowContrast);
            return "ok";
        }else if (contrast.getC_pay_way().equals("installment")){
            nowContrast.setC_pay_way("installment");
            contrastMapper.insertContrast(nowContrast);

            Contrast c = contrastMapper.getLastInsertContrast(nowContrast);

            Installment installment = new Installment();
            installment.setI_contrast_id(c.getC_id());
            installment.setI_down_payment(contrast.getC_down_payment());
            installment.setI_total_periods(contrast.getC_total_periods());
            installment.setI_paid_count(0);
            installment.setI_paid_per_period((c.getC_total_price() - installment.getI_down_payment()) / installment.getI_total_periods());

            installmentMapper.insertInstallment(installment);
            return "ok";
        }

        return "no";
    }

    /*
     *  签署或拒绝合同
     *  函数名：signContrast
     *  @param Integer id 合同的c_id,
     *  @param Integer sign 同意(1)或拒绝(-1"),
     *  @param HttpSession session 浏览器中包含"Buyer"或"Seller"属性的session
     *
     *  @return String 返回回执("ok","no")
     */
    @Override
    public String signContrast(Integer id, Integer sign, HttpSession session) {
        Contrast contrast = contrastMapper.getContrastsById(id);
        if (contrast.getC_seller_agree().equals(-1) || contrast.getC_buyer_agree().equals(-1)){
            return "no";
        }

        Buyer buyer = null;
        Seller seller = null;

        if (session.getAttribute("Buyer") != null){
            Buyer buyerInContrast = contrastMapper.getBuyerByContrast(id);
            buyer = (Buyer) session.getAttribute("Buyer");
            if (buyerInContrast.getB_id().equals(buyer.getB_id())){
                return "no";
            }

            contrast.setC_buyer_id(buyer.getB_id());
            contrast.setC_buyer_agree(sign);
        }else if (session.getAttribute("Seller") != null){
            Seller sellerInContrast = contrastMapper.getSellerByContrast(id);
            seller = (Seller) session.getAttribute("Seller");
            if(sellerInContrast.getS_id().equals(seller.getS_id())){
                return "no";
            }

            contrast.setC_seller_agree(sign);
        }
        contrastMapper.signContrast(contrast);

        if (contrast.getC_buyer_agree().equals(1) && contrast.getC_seller_agree().equals(1)){
            Date date = new Date();
            long day = 14;
            date.setTime(date.getTime() + day * 24 * 60 * 60 * 1000);
            contrast.setC_paytime_ending(date);
            contrastMapper.addPayTimeEnding(contrast);
        }

        return "ok";
    }
}
