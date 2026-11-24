package io.github.abshpak.api.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
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

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.github.abshpak.api.data.ProductsTestData.firstPageOf;
import static io.github.abshpak.api.data.ProductsTestData.stubProduct;
import static io.github.abshpak.api.raw.ProductsApiRaw.PRODUCTS_PATH;
import static io.github.abshpak.core.testing.Tags.STUB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(STUB)
class ProductsApiClientStubTests {

    private static final Duration CLIENT_TIMEOUT = Duration.ofSeconds(1);
    private static final Duration SERVER_DELAY = Duration.ofSeconds(3);
    private static final int EXPECTED_REQUEST_COUNT = 1;
    private static final String EMPTY_JSON_OBJECT = "{}";
    private static final String TIMEOUT_MESSAGE_FRAGMENT = "timed out";

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
    @DisplayName("Ответ сервера превращается в объекты, запрос уходит один раз с нужным заголовком")
    void shouldMapResponseAndSendSingleJsonRequestWhenServerRespondsOk() {
        // Arrange
        var expectedPage = firstPageOf(stubProduct());
        WIREMOCK.stubFor(get(urlEqualTo(PRODUCTS_PATH))
                                 .willReturn(okJson(JsonUtils.toJson(expectedPage))));

        // Act
        var actualPage = productsClient.getProducts();

        // Assert
        assertThat(actualPage)
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expectedPage);
        WIREMOCK.verify(EXPECTED_REQUEST_COUNT, getRequestedFor(urlEqualTo(PRODUCTS_PATH))
                .withHeader(ACCEPT, containing(APPLICATION_JSON_VALUE)));
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
                .hasMessageContaining(String.valueOf(INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    @DisplayName("Медленный сервер вызывает таймаут, а не бесконечное ожидание")
    void shouldFailWithTimeoutWhenServerRespondsSlowerThanTimeout() {
        // Arrange
        WIREMOCK.stubFor(get(urlEqualTo(PRODUCTS_PATH))
                                 .willReturn(okJson(EMPTY_JSON_OBJECT).withFixedDelay((int) SERVER_DELAY.toMillis())));

        // Act
        var thrown = catchThrowable(() -> productsClient.getProducts());

        // Assert
        assertThat(thrown)
                .isNotNull()
                .hasStackTraceContaining(TIMEOUT_MESSAGE_FRAGMENT);
    }
}
