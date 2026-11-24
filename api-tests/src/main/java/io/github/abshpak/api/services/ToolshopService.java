package io.github.abshpak.api.services;

import io.github.abshpak.api.clients.ProductsApiClient;
import io.github.abshpak.api.db.repositories.ToolshopRepository;
import io.github.abshpak.api.raw.ProductsApiRaw;
import io.github.abshpak.api.spec.ApiSettings;
import io.github.abshpak.api.spec.ToolshopSpecProvider;
import io.github.abshpak.core.config.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;

public class ToolshopService {

    private final ProjectConfig config;
    private final ProductsApiClient products;
    private ToolshopRepository db;

    public ToolshopService() {
        this.config = ConfigFactory.create(ProjectConfig.class);
        var spec = new ToolshopSpecProvider(ApiSettings.from(config));
        this.products = new ProductsApiClient(new ProductsApiRaw(spec));
    }

    public ProductsApiClient products() {
        return products;
    }

    public ToolshopRepository db() {
        if (db == null) {
            db = new ToolshopRepository(config);
        }
        return db;
    }
}
