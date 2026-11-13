package io.github.abshpak.api.learning;

import io.github.abshpak.api.dto.ProductsPageDto;
import io.github.abshpak.core.config.ProjectConfig;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

/**
 * Учебный пример: REST Assured напрямую, без слоёв фреймворка.
 * Показывает "сырой" запрос given/when/then для сравнения с
 * {@link io.github.abshpak.api.products.GetProductsTests},
 * где тот же запрос идёт через Service → Client → Raw.
 * В рабочих тестах REST Assured напрямую не используется.
 */
@Tag("learning")
class ProductsApiLearningTest {

    private final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    @Test
    @DisplayName("API возвращает непустой список товаров")
    void shouldReturnProductsWhenRequestingProductList() {
        given()
                .baseUri(config.apiBaseUrl())
                .log().all()
                .when()
                .get("/products")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data", not(empty()));
    }


    @Test
    @DisplayName("Ответ со списком товаров превращается в объекты")
    void shouldMapProductListToDtoWhenRequestingProducts() {
        // Arrange
        var expectedPage = 1;

        // Act
        var productsPage = given()
                .baseUri(config.apiBaseUrl())
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .extract()
                .as(ProductsPageDto.class);

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
