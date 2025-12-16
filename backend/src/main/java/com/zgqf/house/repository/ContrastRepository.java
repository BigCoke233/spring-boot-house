package com.zgqf.house.repository;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contrast;
import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContrastRepository {
    //查看卖家的合同列表
    public List<Contrast> getContrastsBySeller(Integer s_id);
    //查看买家的合同列表
    public List<Contrast> getContrastsByBuyer(Integer b_id);
    //根据合同id查找合同
    public Contrast getContrastsById(Integer c_id);
    //查看对方的商家资料
    public Seller getSellerByContrast(Integer c_id);
    //查看对方的买家资料
    public Buyer getBuyerByContrast(Integer c_id);
    //签署或拒绝合同
    public void signContrast(Contrast contrast);
    //查询房子是否可用
    public List<Contrast> houseUsable(Integer h_id);
    //创建新合同
    public void insertContrast(Contrast contrast);
    //查找新添加的合同
    public Contrast getLastInsertContrast(Contrast contrast);
    //添加预计付款时间
    public void addPayTimeEnding(Contrast contrast);
}
