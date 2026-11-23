package io.github.abshpak.api.db;

import io.github.abshpak.api.db.dao.ProductsDao;
import io.github.abshpak.api.db.support.MariaDbContainers;
import io.github.abshpak.api.db.support.SeedProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mariadb.MariaDBContainer;

import static io.github.abshpak.core.testing.Tags.DB;
import static org.assertj.core.api.Assertions.assertThat;

@Tag(DB)
@Testcontainers(disabledWithoutDocker = true)
class ProductsDaoTests {

    private static final String MISSING_PRODUCT_ID = "no-such-id";

    @Container
    private static final MariaDBContainer MARIADB = MariaDbContainers.productsDb();

    private static ProductsDao productsDao;

    @BeforeAll
    static void prepareDatabase() {
        var jdbcTemplate = MariaDbContainers.jdbcTemplate(MARIADB);
        SeedProduct.insertAll(jdbcTemplate);
        productsDao = new ProductsDao(jdbcTemplate);
    }

    @ParameterizedTest(name = "{0}")
    @EnumSource(SeedProduct.class)
    @DisplayName("Товар из базы превращается в объект с теми же значениями")
    void shouldReturnEntityWhenProductExists(SeedProduct expectedProduct) {
        // Act
        var actualEntity = productsDao.findById(expectedProduct.getId());

        // Assert
        assertThat(actualEntity).hasValueSatisfying(entity -> {
            assertThat(entity.getId()).isEqualTo(expectedProduct.getId());
            assertThat(entity.getName()).isEqualTo(expectedProduct.getProductName());
            assertThat(entity.getPrice()).isEqualByComparingTo(expectedProduct.getPrice());
        });
    }

    @Test
    @DisplayName("Для несуществующего id возвращается пустой результат, а не ошибка")
    void shouldReturnEmptyWhenProductDoesNotExist() {
        // Act
        var actualEntity = productsDao.findById(MISSING_PRODUCT_ID);

        // Assert
        assertThat(actualEntity).isEmpty();
    }
}
