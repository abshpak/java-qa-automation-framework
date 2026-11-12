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
    @DisplayName("Значение берётся из файла окружения")
    void shouldReadBaseUrlFromEnvFileWhenEnvIsSet() {
        // Arrange
        var expectedBaseUrl = "https://test.example.com";

        // Act
        var config = ConfigFactory.create(ProjectConfig.class);

        // Assert
        assertThat(config.baseUrl()).isEqualTo(expectedBaseUrl);
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
    @SetSystemProperty(key = "base.url", value = OVERRIDE_BASE_URL)
    void shouldOverrideFileValueWhenSystemPropertyIsSet() {
        // Act
        var config = ConfigFactory.create(ProjectConfig.class);

        // Assert
        assertThat(config.baseUrl()).isEqualTo(OVERRIDE_BASE_URL);
    }
}
