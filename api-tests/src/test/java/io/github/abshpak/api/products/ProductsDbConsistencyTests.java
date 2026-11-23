package io.github.abshpak.api.products;

import io.github.abshpak.api.services.ToolshopService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static io.github.abshpak.core.testing.Tags.DB;
import static org.assertj.core.api.Assertions.assertThat;

@Tag(DB)
@EnabledIfSystemProperty(
        named = "env",
        matches = "local",
        disabledReason = "Проверки в БД доступны только на локальном стенде: -Denv=local")
class ProductsDbConsistencyTests {

    private final ToolshopService toolshop = new ToolshopService();

    @Test
    @DisplayName("Имя и цена товара из API совпадают с записью в базе")
    void shouldStoreSameNameAndPriceInDbWhenProductReturnedByApi() {
        // Arrange
        var expectedProduct = toolshop.products().getProducts().getData().get(0);

        // Act
        var actualEntity = toolshop.db().products().findById(expectedProduct.getId());

        // Assert
        assertThat(actualEntity).hasValueSatisfying(entity -> {
            assertThat(entity.getName()).isEqualTo(expectedProduct.getName());
            assertThat(entity.getPrice()).isEqualByComparingTo(expectedProduct.getPrice());
        });
    }
}
