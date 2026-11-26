package io.github.abshpak.core.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:containers.properties"
})
public interface ContainersConfig extends Config {

    String MARIADB_IMAGE_KEY = "containers.mariadb.image";
    String WIREMOCK_IMAGE_KEY = "containers.wiremock.image";

    @Key(MARIADB_IMAGE_KEY)
    String mariadbImage();

    @Key(WIREMOCK_IMAGE_KEY)
    String wiremockImage();
}
