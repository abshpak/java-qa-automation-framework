package io.github.abshpak.core;

import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ToolchainSanityTest {

    @Value
    @Builder
    static class SampleInfo {
        String name;
        int count;
    }

    @Test
    @DisplayName("Lombok, JUnit и AssertJ подключены и работают")
    void shouldBuildObjectWhenToolchainConfigured() {
        // Arrange
        var expectedName = "framework";
        var expectedCount = 3;

        // Act
        var actual = SampleInfo.builder()
                               .name(expectedName)
                               .count(expectedCount)
                               .build();

        // Assert
        assertThat(actual)
            .returns(expectedName, SampleInfo::getName)
            .returns(expectedCount, SampleInfo::getCount);

    }
}
