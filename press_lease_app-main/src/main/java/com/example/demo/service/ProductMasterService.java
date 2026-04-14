package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SheetSearchResponse;
import com.example.demo.entity.ProductMaster;
import com.example.demo.mapper.ProductMasterMapper;

@Service
public class ProductMasterService {

    private static final Logger log = LoggerFactory.getLogger(ProductMasterService.class);
    private static final String SOURCE_SHEET_NAME = "slip_detail";

    private final ProductMasterMapper productMasterMapper;
    private final Resource seedWorkbook;

    public ProductMasterService(
            ProductMasterMapper productMasterMapper,
            @Value("${product-master.seed-location:classpath:import/press_lease_dummy_data_200.xlsx}") Resource seedWorkbook) {
        this.productMasterMapper = productMasterMapper;
        this.seedWorkbook = seedWorkbook;
    }

    public Optional<SheetSearchResponse> findByCode(String code) {
        ProductMaster productMaster = productMasterMapper.findByCode(code);
        if (productMaster == null) {
            return Optional.empty();
        }
        return Optional.of(new SheetSearchResponse(
                productMaster.getCode(),
                productMaster.getName(),
                productMaster.getTaxPrice() == null ? "" : String.valueOf(productMaster.getTaxPrice())));
    }

    public void initializeIfEmpty() {
        if (productMasterMapper.countAll() > 0) {
            return;
        }
        importFromWorkbook();
    }

    void importFromWorkbook() {
        if (!seedWorkbook.exists()) {
            log.warn("Product master seed workbook was not found: {}", seedWorkbook);
            return;
        }

        Map<String, ProductMaster> uniqueProducts = new LinkedHashMap<>();
        DataFormatter formatter = new DataFormatter();

        try (InputStream inputStream = seedWorkbook.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet(SOURCE_SHEET_NAME);
            if (sheet == null) {
                log.warn("Sheet {} was not found in product master workbook.", SOURCE_SHEET_NAME);
                return;
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String code = getText(row.getCell(2), formatter);
                if (code.isBlank() || uniqueProducts.containsKey(code)) {
                    continue;
                }

                ProductMaster productMaster = new ProductMaster();
                productMaster.setCode(code);
                productMaster.setName(getText(row.getCell(3), formatter));
                productMaster.setTaxPrice(parseInteger(row.getCell(5), formatter));
                uniqueProducts.put(code, productMaster);
            }
        } catch (IOException ex) {
            log.warn("Failed to import product master workbook.", ex);
            return;
        }

        uniqueProducts.values().forEach(productMasterMapper::insert);
        log.info("Imported {} product master rows from workbook.", uniqueProducts.size());
    }

    private String getText(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        return formatter.formatCellValue(cell).trim();
    }

    private Integer parseInteger(Cell cell, DataFormatter formatter) {
        String value = getText(cell, formatter).replace(",", "");
        if (value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            log.debug("Could not parse integer value {} from workbook.", value, ex);
            return null;
        }
    }
}
