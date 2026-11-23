package io.github.abshpak.api.db.support;

import io.github.abshpak.core.config.ContainersConfig;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.mariadb.MariaDBContainer;

public final class MariaDbContainers {

    private static final String SCHEMA_SCRIPT = "db/products-schema.sql";
    private static final String URL_PARAM_SSL_MODE = "sslMode";
    private static final String SSL_MODE_DISABLED = "disable";

    private MariaDbContainers() {
    }

    public static MariaDBContainer productsDb() {
        var config = ConfigFactory.create(ContainersConfig.class);
        return new MariaDBContainer(config.mariadbImage())
                .withInitScript(SCHEMA_SCRIPT)
                .withUrlParam(URL_PARAM_SSL_MODE, SSL_MODE_DISABLED);
    }

    public static JdbcTemplate jdbcTemplate(MariaDBContainer container) {
        var dataSource = new DriverManagerDataSource(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword());
        return new JdbcTemplate(dataSource);
    }
}
