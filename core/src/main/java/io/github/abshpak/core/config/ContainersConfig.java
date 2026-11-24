package io.github.abshpak.core.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:containers.properties"
})
public interface ContainersConfig extends Config {

    String MARIADB_IMAGE_KEY = "containers.mariadb.image";

    @Key(MARIADB_IMAGE_KEY)
    String mariadbImage();
}
