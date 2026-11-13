package io.github.abshpak.api.learning;

import io.github.abshpak.core.config.ProjectConfig;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class ProductsApiLearningTest {

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
}
