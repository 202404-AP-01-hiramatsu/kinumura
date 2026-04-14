package com.example.demo.mapper;

import java.util.List;

import com.example.demo.entity.ProductMaster;

public interface ProductMasterMapper {

    ProductMaster findByCode(String code);

    int countAll();

    void insert(ProductMaster productMaster);

    List<ProductMaster> findAll();
}
