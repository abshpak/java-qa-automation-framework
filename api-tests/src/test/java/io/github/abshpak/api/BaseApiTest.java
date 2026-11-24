package io.github.abshpak.api;

import io.github.abshpak.api.services.ToolshopService;

public abstract class BaseApiTest {

    protected final ToolshopService toolshop = new ToolshopService();
}
