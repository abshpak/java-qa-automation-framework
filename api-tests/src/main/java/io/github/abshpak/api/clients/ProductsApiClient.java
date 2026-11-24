package io.github.abshpak.api.clients;

import io.github.abshpak.api.dto.ProductsPageDto;
import io.github.abshpak.api.raw.ProductsApiRaw;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class ProductsApiClient {

    private final ProductsApiRaw raw;

    public ProductsPageDto getProducts() {
        return raw.getProductsRequest()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProductsPageDto.class);
    }
}
