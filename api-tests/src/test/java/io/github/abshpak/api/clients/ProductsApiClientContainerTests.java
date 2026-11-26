package io.github.abshpak.api.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.abshpak.api.clients.support.WireMockContainers;
import io.github.abshpak.api.raw.ProductsApiRaw;
import io.github.abshpak.api.spec.ApiSettings;
import io.github.abshpak.api.spec.ToolshopSpecProvider;
import io.github.abshpak.core.json.JsonUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
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
@Testcontainers(disabledWithoutDocker = true)
class ProductsApiClientContainerTests {

    private static final Duration CLIENT_TIMEOUT = Duration.ofSeconds(5);
    private static final int EXPECTED_REQUEST_COUNT = 1;

    @Container
    private static final GenericContainer<?> WIREMOCK = WireMockContainers.wireMock();

    private static WireMock wireMockClient;

    private ProductsApiClient productsClient;

    @BeforeAll
    static void connectToWireMock() {
        wireMockClient = WireMockContainers.client(WIREMOCK);
    }

    @BeforeEach
    void resetStubsAndCreateClient() {
        wireMockClient.resetMappings();
        wireMockClient.resetRequests();
        var settings = new ApiSettings(WireMockContainers.baseUrl(WIREMOCK), CLIENT_TIMEOUT);
        productsClient = new ProductsApiClient(new ProductsApiRaw(new ToolshopSpecProvider(settings)));
    }

    @Test
    @DisplayName("Клиент получает и разбирает ответ WireMock, запущенного в Docker")
    void shouldMapResponseWhenWireMockRunsInContainer() {
        // Arrange
        var expectedPage = firstPageOf(stubProduct());
        wireMockClient.register(get(urlEqualTo(PRODUCTS_PATH))
                                        .willReturn(okJson(JsonUtils.toJson(expectedPage))));

        // Act
        var actualPage = productsClient.getProducts();

        // Assert
        assertThat(actualPage)
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expectedPage);
        wireMockClient.verifyThat(EXPECTED_REQUEST_COUNT, getRequestedFor(urlEqualTo(PRODUCTS_PATH))
                .withHeader(ACCEPT, containing(APPLICATION_JSON_VALUE)));
    }

    @Test
    @DisplayName("Ошибка 500 от WireMock в Docker дает понятное сообщение")
    void shouldFailWithStatusCodeWhenContainerReturnsError() {
        // Arrange
        wireMockClient.register(get(urlEqualTo(PRODUCTS_PATH))
                                        .willReturn(serverError()));

        // Act
        var thrown = catchThrowable(() -> productsClient.getProducts());

        // Assert
        assertThat(thrown)
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining(String.valueOf(INTERNAL_SERVER_ERROR.value()));
    }
}
