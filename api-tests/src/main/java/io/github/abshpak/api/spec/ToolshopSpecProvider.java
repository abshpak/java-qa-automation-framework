package io.github.abshpak.api.spec;

import io.github.abshpak.core.config.ProjectConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ToolshopSpecProvider {

    private final ProjectConfig config;

    public ToolshopSpecProvider(ProjectConfig config) {
        this.config = config;
    }

    public RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}
