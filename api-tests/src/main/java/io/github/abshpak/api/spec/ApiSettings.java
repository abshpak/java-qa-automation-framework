package io.github.abshpak.api.spec;

import io.github.abshpak.core.config.ProjectConfig;

import java.time.Duration;

import static java.time.Duration.ofSeconds;

public record ApiSettings(String baseUrl, Duration timeout) {

    public static ApiSettings from(ProjectConfig config) {
        return new ApiSettings(config.apiBaseUrl(), ofSeconds(config.timeoutSeconds()));
    }
}
