package io.github.abshpak.core.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectConfigTest {

    @Test
    @DisplayName("Настройки читаются из config.properties")
    void shouldReadValuesWhenConfigFileExists() {
        //Arrange
        var expectedBaseUrl = "https://example.com";
        var expectedTimeoutSeconds = 10;

        // ActProjectConfig
        var config = ConfigFactory.create(ProjectConfig.class);

        // Assert
        assertThat(config.baseUrl()).isEqualTo(expectedBaseUrl);
        assertThat(config.timeoutSeconds()).isEqualTo(expectedTimeoutSeconds);
    }
}
