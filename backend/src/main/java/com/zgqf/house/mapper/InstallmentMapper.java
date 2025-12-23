package com.zgqf.house.mapper;

import com.zgqf.house.entity.Installment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InstallmentMapper {
    //插入新分期
    public void insertInstallment(Installment installment);
}
