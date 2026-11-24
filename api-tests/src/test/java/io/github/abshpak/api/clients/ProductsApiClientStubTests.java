package io.github.abshpak.api.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.github.abshpak.api.dto.ProductDto;
import io.github.abshpak.api.dto.ProductsPageDto;
import io.github.abshpak.api.raw.ProductsApiRaw;
import io.github.abshpak.api.spec.ApiSettings;
import io.github.abshpak.api.spec.ToolshopSpecProvider;
import io.github.abshpak.core.json.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.github.abshpak.api.raw.ProductsApiRaw.PRODUCTS_PATH;
import static io.github.abshpak.core.http.HttpStatusCodes.INTERNAL_SERVER_ERROR;
import static io.github.abshpak.core.testing.Tags.STUB;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.testcontainers.shaded.org.awaitility.Durations.ONE_SECOND;

@Tag(STUB)
class ProductsApiClientStubTests {

    private static final Duration CLIENT_TIMEOUT = ONE_SECOND;
    private static final Duration SERVER_DELAY = ofSeconds(3);
    private static final String ACCEPT_HEADER = "Accept";
    private static final String JSON_MEDIA_TYPE = "application/json";

    @RegisterExtension
    static final WireMockExtension WIREMOCK = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    private ProductsApiClient productsClient;

    @BeforeEach
    void createClient() {
        var settings = new ApiSettings(WIREMOCK.baseUrl(), CLIENT_TIMEOUT);
        productsClient = new ProductsApiClient(new ProductsApiRaw(new ToolshopSpecProvider(settings)));
    }

    @Test
    @DisplayName("Ответ сервера превращается в объекты, запрос уходит с нужным заголовком")
    void shouldMapResponseAndSendJsonAcceptHeaderWhenServerRespondsOk() {
        // Arrange
        var expectedProduct = ProductDto.builder()
                .id("stub-id")
                .name("Stub Hammer")
                .price(new BigDecimal("12.50"))
                .inStock(true)
                .build();
        var expectedPage = ProductsPageDto.builder()
                .currentPage(1)
                .data(List.of(expectedProduct))
                .build();
        WIREMOCK.stubFor(get(urlEqualTo(PRODUCTS_PATH))
                                 .willReturn(okJson(JsonUtils.toJson(expectedPage))));

        // Act
        var actualPage = productsClient.getProducts();

        // Assert
        assertThat(actualPage)
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expectedPage);
        WIREMOCK.verify(getRequestedFor(urlEqualTo(PRODUCTS_PATH))
                                .withHeader(ACCEPT_HEADER, containing(JSON_MEDIA_TYPE)));
    }

    @Test
    @DisplayName("Ошибка 500 от сервера даёт понятное сообщение")
    void shouldFailWithStatusCodeWhenServerReturnsError() {
        // Arrange
        WIREMOCK.stubFor(get(urlEqualTo(PRODUCTS_PATH))
                                 .willReturn(serverError()));

        // Act
        var thrown = catchThrowable(() -> productsClient.getProducts());

        // Assert
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining(String.valueOf(INTERNAL_SERVER_ERROR));
    }

    @Test
    @DisplayName("Медленный сервер вызывает таймаут, а не бесконечное ожидание")
    void shouldFailWithTimeoutWhenServerRespondsSlowerThanTimeout() {
        // Arrange
        WIREMOCK.stubFor(get(urlEqualTo(PRODUCTS_PATH))
                                 .willReturn(okJson("{}").withFixedDelay((int) SERVER_DELAY.toMillis())));

        // Act
        var thrown = catchThrowable(() -> productsClient.getProducts());

        // Assert
        assertThat(thrown)
                .isNotNull()
                .hasStackTraceContaining("timed out");
    }
}
