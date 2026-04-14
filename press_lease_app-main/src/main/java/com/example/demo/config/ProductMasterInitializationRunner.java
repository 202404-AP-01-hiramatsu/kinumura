package com.example.demo.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.service.ProductMasterService;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ProductMasterInitializationRunner implements ApplicationRunner {

    private final ProductMasterService productMasterService;

    public ProductMasterInitializationRunner(ProductMasterService productMasterService) {
        this.productMasterService = productMasterService;
    }

    @Override
    public void run(ApplicationArguments args) {
        productMasterService.initializeIfEmpty();
    }
}
