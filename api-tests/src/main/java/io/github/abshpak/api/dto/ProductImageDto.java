package io.github.abshpak.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductImageDto {

    private String id;
    private String title;

    @JsonProperty("by_name")
    private String byName;

    @JsonProperty("by_url")
    private String byUrl;

    @JsonProperty("source_name")
    private String sourceName;

    @JsonProperty("source_url")
    private String sourceUrl;

    @JsonProperty("file_name")
    private String fileName;
}
