package io.github.abshpak.core.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ProjectConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("timeout.seconds")
    @DefaultValue("10")
    int timeoutSeconds();
}
