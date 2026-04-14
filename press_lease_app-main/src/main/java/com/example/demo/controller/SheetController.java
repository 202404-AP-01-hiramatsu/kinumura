package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.SaveSlipResponse;
import com.example.demo.dto.SheetSearchResponse;
import com.example.demo.dto.SlipRequest;
import com.example.demo.service.ProductMasterService;
import com.example.demo.service.SlipService;

@RestController
@RequestMapping("/api")
public class SheetController {

    private final ProductMasterService productMasterService;
    private final SlipService slipService;

    public SheetController(ProductMasterService productMasterService, SlipService slipService) {
        this.productMasterService = productMasterService;
        this.slipService = slipService;
    }

    @GetMapping("/search")
    public SheetSearchResponse search(@RequestParam String code) {
        return productMasterService.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No row found for code: " + code));
    }

    @PostMapping("/saveSlip")
    public SaveSlipResponse saveSlip(@RequestBody SlipRequest request) {
        return slipService.saveSlip(request);
    }
}
