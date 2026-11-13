package io.github.abshpak.api.raw;

import io.github.abshpak.api.spec.ToolshopSpecProvider;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ProductsApiRaw {

    private static final String PRODUCTS_PATH = "/products";

    private final ToolshopSpecProvider spec;

    public ProductsApiRaw(ToolshopSpecProvider spec) {
        this.spec = spec;
    }

    public Response getProductsRequest() {
        return given()
                .spec(spec.request())
                .when()
                .get(PRODUCTS_PATH);
    }
}
