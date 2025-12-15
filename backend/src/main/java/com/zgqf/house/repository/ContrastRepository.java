package com.zgqf.house.repository;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contrast;
import com.zgqf.house.entity.Seller;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContrastRepository {
    public List<Contrast> getContrastsBySeller(Integer s_id);
    public List<Contrast> getContrastsByBuyer(Integer b_id);
    public Contrast getContrastsById(Integer c_id);
    public Seller getSellerByContrast(Integer c_id);
    public Buyer getBuyerByContrast(Integer c_id);
    public void signContrast(Contrast contrast);
    public List<Contrast> houseUsable(Integer h_id);
    public void insertContrast(Contrast contrast);
}
