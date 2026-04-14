package com.example.demo.service;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import com.example.demo.entity.MasterSetting;
import com.example.demo.mapper.MasterSettingMapper;

@Service
public class MasterSettingService {

    private static final Logger log = LoggerFactory.getLogger(MasterSettingService.class);
    private static final String DEFAULT_MASTER_TEXT = "ここにExcelに表示する固定文言を入力してください";

    private final MasterSettingMapper masterSettingMapper;
    private final DataSource dataSource;
    private final String databasePlatform;

    public MasterSettingService(
            MasterSettingMapper masterSettingMapper,
            DataSource dataSource,
            @Value("${app.database.platform:mysql}") String databasePlatform) {
        this.masterSettingMapper = masterSettingMapper;
        this.dataSource = dataSource;
        this.databasePlatform = databasePlatform;
    }

    public MasterSetting getOrCreateSetting() {
        ensureMasterSettingReady();

        MasterSetting setting = masterSettingMapper.find();
        if (setting != null) {
            return setting;
        }

        MasterSetting initial = new MasterSetting();
        initial.setMasterText(DEFAULT_MASTER_TEXT);
        masterSettingMapper.insert(initial);
        return masterSettingMapper.find();
    }

    public void updateMasterText(String masterText) {
        MasterSetting current = getOrCreateSetting();
        current.setMasterText(masterText);
        masterSettingMapper.update(current);
    }

    public String getMasterText() {
        MasterSetting setting = getOrCreateSetting();
        return setting != null ? setting.getMasterText() : DEFAULT_MASTER_TEXT;
    }

    private synchronized void ensureMasterSettingReady() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSqlScriptEncoding("UTF-8");
        populator.addScript(resolveMasterSettingScript());
        populator.execute(dataSource);
    }

    private Resource resolveMasterSettingScript() {
        String path = switch (databasePlatform.toLowerCase()) {
            case "postgres", "postgresql", "supabase" -> "db/postgres/master-setting-init.sql";
            default -> "db/master-setting-init.sql";
        };
        log.debug("Ensuring master setting schema with script {}", path);
        return new ClassPathResource(path);
    }
}
