package io.github.abshpak.api.spec;

import io.github.abshpak.core.config.ProjectConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static java.time.Duration.ofSeconds;

@RequiredArgsConstructor
public class ToolshopSpecProvider {

    private final ProjectConfig config;

    public RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(restAssuredConfig())
                .build();
    }

    private RestAssuredConfig restAssuredConfig() {
        var timeout = ofSeconds(config.timeoutSeconds());
        return RestAssuredConfig.config()
                .httpClient(httpClientConfig()
                                    .connectTimeout(timeout)
                                    .readTimeout(timeout))
                .logConfig(LogConfig.logConfig()
                                   .enableLoggingOfRequestAndResponseIfValidationFails());
    }
}
