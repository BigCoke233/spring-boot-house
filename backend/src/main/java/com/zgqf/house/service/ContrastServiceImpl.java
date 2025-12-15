package com.zgqf.house.service;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contrast;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.repository.ContrastRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContrastServiceImpl implements ContrastService{
    @Autowired
    private ContrastRepository contrastRepository;

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
            return contrastRepository.getContrastsByBuyer(buyer.getB_id());
        }else if (session.getAttribute("Seller") != null){
            Seller seller = (Seller) session.getAttribute("Seller");
            return contrastRepository.getContrastsBySeller(seller.getS_id());
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
        return contrastRepository.getContrastsById(id);
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
        return contrastRepository.getSellerByContrast(id);
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
        return contrastRepository.getBuyerByContrast(id);
    }

    /*
     *  买家创建新合同
     *  函数名：creatContrast
     *  @param HttpSession session 浏览器中包含"Buyer"属性的session
     *
     *  @return String 返回回执("ok","no")
     */
    @Override
    public String creatContrast(Integer id, HttpSession session) {
        if (!contrastRepository.houseUsable(id).isEmpty()){
            return "no";
        }

        Buyer buyer = (Buyer) session.getAttribute("Buyer");
        Contrast contrast = new Contrast();
        contrast.setC_buyer_id(buyer.getB_id());
        contrast.setC_house_id(id);
        contrastRepository.insertContrast(contrast);

        return "yes";
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
        Contrast contrast = new Contrast();
        contrast.setC_id(id);
        Buyer buyer = null;
        Seller seller = null;
        if (session.getAttribute("Buyer") != null){
            Buyer buyerInContrast = contrastRepository.getBuyerByContrast(id);
            buyer = (Buyer) session.getAttribute("Buyer");
            if (buyerInContrast.getB_id() != buyer.getB_id()){
                return "no";
            }

            contrast.setC_buyer_id(buyer.getB_id());
            contrast.setC_buyer_agree(sign);
        }else if (session.getAttribute("Seller") != null){
            Seller sellerInContrast = contrastRepository.getSellerByContrast(id);
            seller = (Seller) session.getAttribute("Seller");
            if(sellerInContrast.getS_id() != seller.getS_id()){
                return "no";
            }

            contrast.setC_seller_agree(sign);
        }
        contrastRepository.signContrast(contrast);

        return "ok";
    }
}
