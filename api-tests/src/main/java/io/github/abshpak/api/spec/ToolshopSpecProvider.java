package io.github.abshpak.api.spec;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

import static io.restassured.config.HttpClientConfig.httpClientConfig;

@RequiredArgsConstructor
public class ToolshopSpecProvider {

    private final ApiSettings settings;

    public RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(settings.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(restAssuredConfig())
                .addFilter(new AllureRestAssured())
                .build();
    }

    private RestAssuredConfig restAssuredConfig() {
        return RestAssuredConfig.config()
                .httpClient(httpClientConfig()
                                    .connectTimeout(settings.timeout())
                                    .readTimeout(settings.timeout()))
                .logConfig(LogConfig.logConfig()
                                   .enableLoggingOfRequestAndResponseIfValidationFails());
    }
}
