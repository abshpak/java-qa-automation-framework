package io.github.abshpak.core.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.SetSystemProperty;

import static org.assertj.core.api.Assertions.assertThat;

@SetSystemProperty(key = "env", value = "test")
class ProjectConfigTest {

    private static final String OVERRIDE_BASE_URL = "https://override.example.com";

    @Test
    @DisplayName("Адреса берутся из файла окружения")
    void shouldReadBaseUrlsFromEnvFileWhenEnvIsSet() {
        // Arrange
        var expectedApiBaseUrl = "https://api.test.example.com";
        var expectedUiBaseUrl = "https://ui.test.example.com";

        // Act
        var config = ConfigFactory.create(ProjectConfig.class);

        // Assert
        assertThat(config.apiBaseUrl()).isEqualTo(expectedApiBaseUrl);
        assertThat(config.uiBaseUrl()).isEqualTo(expectedUiBaseUrl);
    }

    @Test
    @DisplayName("Значение по умолчанию, если ключа нет в файле")
    void shouldUseDefaultTimeoutWhenKeyIsMissing() {
        // Arrange
        var expectedTimeoutSeconds = 10;

        // Act
        var config = ConfigFactory.create(ProjectConfig.class);

        // Assert
        assertThat(config.timeoutSeconds()).isEqualTo(expectedTimeoutSeconds);
    }

    @Test
    @DisplayName("Параметр запуска важнее файла окружения")
    @SetSystemProperty(key = "api.base.url", value = OVERRIDE_BASE_URL)
    void shouldOverrideFileValueWhenSystemPropertyIsSet() {
        // Act
        var config = ConfigFactory.create(ProjectConfig.class);

        // Assert
        assertThat(config.apiBaseUrl()).isEqualTo(OVERRIDE_BASE_URL);
    }
}
