package io.github.abshpak.api.products;

import io.github.abshpak.api.BaseApiTest;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.abshpak.api.data.ProductsTestData.FIRST_PAGE;
import static io.github.abshpak.core.testing.Features.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;

@Feature(PRODUCTS)
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
