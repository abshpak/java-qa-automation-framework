package io.github.abshpak.api.products;

import io.github.abshpak.api.services.ToolshopService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GetProductsTests {

    private final ToolshopService toolshop = new ToolshopService();

    @Test
    @DisplayName("Первая страница каталога содержит товары с id и ценой")
    void shouldReturnFirstPageWithProductsWhenRequestingCatalog() {
        // Arrange
        var expectedPage = 1;

        // Act
        var productsPage = toolshop.products().getProducts();

        // Assert
        assertThat(productsPage.getCurrentPage()).isEqualTo(expectedPage);
        assertThat(productsPage.getData())
                .isNotEmpty()
                .allSatisfy(product -> {
                    assertThat(product.getId()).isNotBlank();
                    assertThat(product.getPrice()).isPositive();
                });
    }
}
