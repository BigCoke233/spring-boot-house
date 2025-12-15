package com.zgqf.house.service;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Contrast;
import com.zgqf.house.entity.Seller;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface ContrastService {
    public List<Contrast> getContrastsByUser(HttpSession session);
    public Contrast getContrastsById(Integer id);
    public Seller getSellerByContrast(Integer id);
    public Buyer getBuyerByContrast(Integer id);
    public String creatContrast(Integer id, HttpSession session);
    public String signContrast(Integer id,Integer sign,HttpSession session);
}
