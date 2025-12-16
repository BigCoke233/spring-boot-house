package com.zgqf.house.repository;

import com.zgqf.house.entity.Installment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InstallmentRepository {
    //插入新分期
    public void insertInstallment(Installment installment);
}
