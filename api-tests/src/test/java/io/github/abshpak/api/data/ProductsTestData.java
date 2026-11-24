package io.github.abshpak.api.data;

import io.github.abshpak.api.dto.ProductDto;
import io.github.abshpak.api.dto.ProductsPageDto;

import java.math.BigDecimal;
import java.util.List;

public final class ProductsTestData {

    public static final int FIRST_PAGE = 1;

    private static final String STUB_PRODUCT_ID = "stub-id";
    private static final String STUB_PRODUCT_NAME = "Stub Hammer";
    private static final BigDecimal STUB_PRODUCT_PRICE = new BigDecimal("12.50");

    private ProductsTestData() {
    }

    public static ProductDto stubProduct() {
        return ProductDto.builder()
                .id(STUB_PRODUCT_ID)
                .name(STUB_PRODUCT_NAME)
                .price(STUB_PRODUCT_PRICE)
                .inStock(true)
                .build();
    }

    public static ProductsPageDto firstPageOf(ProductDto... products) {
        return ProductsPageDto.builder()
                .currentPage(FIRST_PAGE)
                .data(List.of(products))
                .build();
    }
}
