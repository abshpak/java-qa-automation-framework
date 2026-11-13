package io.github.abshpak.api.services;

import io.github.abshpak.api.clients.ProductsApiClient;
import io.github.abshpak.api.raw.ProductsApiRaw;
import io.github.abshpak.api.spec.ToolshopSpecProvider;
import io.github.abshpak.core.config.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;

public class ToolshopService {

    private final ProductsApiClient products;

    public ToolshopService() {
        var config = ConfigFactory.create(ProjectConfig.class);
        var spec = new ToolshopSpecProvider(config);
        this.products = new ProductsApiClient(new ProductsApiRaw(spec));
    }

    public ProductsApiClient products() {
        return products;
    }
}
