package io.github.abshpak.api.db.dao;

import io.github.abshpak.api.db.entities.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;

@RequiredArgsConstructor
public class ProductsDao {

    private static final String FIND_BY_ID_SQL = "SELECT id, name, price FROM products WHERE id = ?";

    private static final RowMapper<ProductEntity> ROW_MAPPER = (rs, rowNum) -> ProductEntity.builder()
            .id(rs.getString("id"))
            .name(rs.getString("name"))
            .price(rs.getBigDecimal("price"))
            .build();

    private final JdbcTemplate jdbcTemplate;

    public Optional<ProductEntity> findById(String id) {
        var rows = jdbcTemplate.query(FIND_BY_ID_SQL, ROW_MAPPER, id);
        return Optional.ofNullable(DataAccessUtils.singleResult(rows));
    }
}
