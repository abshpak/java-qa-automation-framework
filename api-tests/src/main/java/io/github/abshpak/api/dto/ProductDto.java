package io.github.abshpak.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;

    @JsonProperty("is_location_offer")
    private Boolean locationOffer;

    @JsonProperty("is_rental")
    private Boolean rental;

    @JsonProperty("co2_rating")
    private String co2Rating;

    @JsonProperty("in_stock")
    private Boolean inStock;

    @JsonProperty("is_eco_friendly")
    private Boolean ecoFriendly;

    @JsonProperty("product_image")
    private ProductImageDto productImage;

    private CategoryDto category;
    private BrandDto brand;
}
