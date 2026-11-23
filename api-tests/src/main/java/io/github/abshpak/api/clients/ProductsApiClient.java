package io.github.abshpak.api.clients;

import io.github.abshpak.api.dto.ProductsPageDto;
import io.github.abshpak.api.raw.ProductsApiRaw;
import io.github.abshpak.core.http.HttpStatusCodes;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductsApiClient {

    private final ProductsApiRaw raw;

    public ProductsPageDto getProducts() {
        return raw.getProductsRequest()
                .then()
                .statusCode(HttpStatusCodes.OK)
                .extract()
                .as(ProductsPageDto.class);
    }
}
