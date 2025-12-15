package com.zgqf.house.controller;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contrast;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.service.ContrastService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contrast")
public class ContrastController {
    @Autowired
    private ContrastService contrastService;

    /*
     *  查看自己的合同列表
     *  函数名：getContrastsByUser
     *  @param HttpSession session 浏览器中包含"Buyer"或"Seller"属性的session
     *
     *  @return List<Contrast> 搜索出来的合同列表
     */
    @GetMapping("/")
    public List<Contrast> getContrastsByUser(HttpSession session){
        return contrastService.getContrastsByUser(session);
    }

    /*
     *  查看合同详情
     *  函数名：getContrastsById
     *  @param Integer id 合同的c_id
     *
     *  @return Contrast 搜索出来的合同
     */
    @GetMapping("/{id}")
    public Contrast getContrastsById(@PathVariable("id") Integer id){
        return contrastService.getContrastsById(id);
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
        return contrastService.getSellerByContrast(id);
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
        return contrastService.getBuyerByContrast(id);
    }

    /*
     *  买家创建新合同
     *  函数名：creatContrast
     * @param Integer id 房源的h_id,
     *  @param HttpSession session 浏览器中包含"Buyer"属性的session
     *
     *  @return String 返回回执("ok","no")
     */
    @GetMapping("/creat/{id}")
    public String creatContrast(@PathVariable("id") Integer id,HttpSession session){
        return contrastService.creatContrast(id,session);
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
        return contrastService.signContrast(id,sign,session);
    }
}
