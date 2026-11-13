package io.github.abshpak.api.clients;

import io.github.abshpak.api.dto.ProductsPageDto;
import io.github.abshpak.api.raw.ProductsApiRaw;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductsApiClient {

    private final ProductsApiRaw raw;

    public ProductsPageDto getProducts() {
        return raw.getProductsRequest()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductsPageDto.class);
    }
}
