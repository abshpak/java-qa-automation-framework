package io.github.abshpak.core.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
    "system:properties",
    "classpath:config/${env}.properties"
})
public interface ProjectConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("timeout.seconds")
    @DefaultValue("10")
    int timeoutSeconds();
}
