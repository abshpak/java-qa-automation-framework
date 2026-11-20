package io.github.abshpak.api.db.repositories;

import io.github.abshpak.api.db.dao.ProductsDao;
import io.github.abshpak.core.config.ProjectConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ToolshopRepository {

    private final ProductsDao products;

    public ToolshopRepository(ProjectConfig config) {
        var dataSource = new DriverManagerDataSource(config.dbUrl(), config.dbUsername(), config.dbPassword());
        var jdbcTemplate = new JdbcTemplate(dataSource);
        this.products = new ProductsDao(jdbcTemplate);
    }

    public ProductsDao products() {
        return products;
    }
}
