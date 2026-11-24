package io.github.abshpak.api.products;

import io.github.abshpak.api.BaseApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.abshpak.api.data.ProductsTestData.FIRST_PAGE;
import static org.assertj.core.api.Assertions.assertThat;

class GetProductsTests extends BaseApiTest {

    @Test
    @DisplayName("Первая страница каталога содержит товары с id и ценой")
    void shouldReturnFirstPageWithProductsWhenRequestingCatalog() {
        // Act
        var productsPage = toolshop.products().getProducts();

        // Assert
        assertThat(productsPage.getCurrentPage()).isEqualTo(FIRST_PAGE);
        assertThat(productsPage.getData())
                .isNotEmpty()
                .allSatisfy(product -> {
                    assertThat(product.getId()).isNotBlank();
                    assertThat(product.getPrice()).isPositive();
                });
    }
}
