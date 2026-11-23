package io.github.abshpak.api.db.support;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
public enum SeedProduct {

    HAMMER("hammer-id", "Test Hammer", "12.50"),
    PLIERS("pliers-id", "Test Pliers", "9.99");

    private static final String INSERT_SQL = "INSERT INTO products (id, name, price) VALUES (?, ?, ?)";

    private final String id;
    private final String productName;
    private final BigDecimal price;

    SeedProduct(String id, String productName, String price) {
        this.id = id;
        this.productName = productName;
        this.price = new BigDecimal(price);
    }

    public static void insertAll(JdbcTemplate jdbcTemplate) {
        Arrays.stream(values())
                .forEach(product -> jdbcTemplate.update(INSERT_SQL, product.id, product.productName, product.price));
    }
}
