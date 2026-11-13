package io.github.abshpak.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonUtilsTest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ProductInfo {
        private String name;
        private int quantity;
        @JsonProperty("in_stock")
        private boolean inStock;
    }

    @Test
    @DisplayName("JSON превращается в объект, незнакомые поля пропускаются")
    void shouldConvertJsonToObjectWhenJsonHasUnknownFields() {
        // Arrange
        var json = """
            {
              "name": "Hammer",
              "quantity": 3,
              "in_stock": true,
              "brand": "ForgeFlex"
            }
            """;
        var expected = ProductInfo.builder()
                                  .name("Hammer")
                                  .quantity(3)
                                  .inStock(true)
                                  .build();

        // Act
        var actual = JsonUtils.fromJson(json, ProductInfo.class);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Объект превращается в JSON с именами полей как в API")
    void shouldUseApiFieldNameWhenFieldIsAnnotated() {
        // Arrange
        var product = ProductInfo.builder()
                                 .name("Hammer")
                                 .quantity(3)
                                 .inStock(true)
                                 .build();

        // Act
        var actualJson = JsonUtils.toJson(product);

        // Assert
        assertThat(actualJson)
            .contains("\"in_stock\":true")
            .doesNotContain("inStock");
    }

    @Test
    @DisplayName("Битый JSON даёт понятную ошибку")
    void shouldThrowClearErrorWhenJsonIsInvalid() {
        // Arrange
        var invalidJson = "not a json";

        // Act + Assert
        assertThatThrownBy(() -> JsonUtils.fromJson(invalidJson, ProductInfo.class))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("ProductInfo");
    }
}
