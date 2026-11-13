package io.github.abshpak.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsPageDto {

    @JsonProperty("current_page")
    private Integer currentPage;

    private List<ProductDto> data;

    private Integer from;
    private Integer to;

    @JsonProperty("last_page")
    private Integer lastPage;

    @JsonProperty("per_page")
    private Integer perPage;

    private Integer total;
}
