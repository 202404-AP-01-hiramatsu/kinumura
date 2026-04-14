package com.example.demo.config;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializationRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializationRunner.class);

    private final DataSource dataSource;
    private final String databasePlatform;

    public DatabaseInitializationRunner(
            DataSource dataSource,
            @Value("${app.database.platform:mysql}") String databasePlatform) {
        this.dataSource = dataSource;
        this.databasePlatform = databasePlatform;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Resource> scripts = switch (databasePlatform.toLowerCase()) {
            case "postgres", "postgresql", "supabase" -> List.of(
                    new ClassPathResource("db/postgres/todo-init.sql"),
                    new ClassPathResource("db/postgres/product-master-init.sql"),
                    new ClassPathResource("db/postgres/slip-init.sql"),
                    new ClassPathResource("db/postgres/slip-media-init.sql"),
                    new ClassPathResource("db/postgres/slip-media-project-init.sql"),
                    new ClassPathResource("db/postgres/slip-header-extra-init.sql"),
                    new ClassPathResource("db/postgres/slip-return-init.sql"),
                    new ClassPathResource("db/postgres/master-setting-init.sql"));
            default -> List.of(
                    new ClassPathResource("db/mysql-init.sql"),
                    new ClassPathResource("db/product-master-init.sql"),
                    new ClassPathResource("db/slip-init.sql"),
                    new ClassPathResource("db/slip-media-init.sql"),
                    new ClassPathResource("db/slip-media-project-init.sql"),
                    new ClassPathResource("db/slip-header-extra-init.sql"),
                    new ClassPathResource("db/slip-return-init.sql"),
                    new ClassPathResource("db/master-setting-init.sql"));
        };

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSqlScriptEncoding("UTF-8");
        scripts.forEach(populator::addScript);

        try {
            populator.execute(dataSource);
            log.info("Database initialization scripts completed successfully for platform {}.", databasePlatform);
        } catch (Exception ex) {
            log.warn("Database initialization was skipped for platform {} because the database is not ready yet.",
                    databasePlatform, ex);
        }
    }
}
