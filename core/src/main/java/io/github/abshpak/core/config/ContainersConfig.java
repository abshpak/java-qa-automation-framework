package io.github.abshpak.core.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:containers.properties"
})
public interface ContainersConfig extends Config {

    @Key("containers.mariadb.image")
    String mariadbImage();
}
