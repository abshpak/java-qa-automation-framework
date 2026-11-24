package io.github.abshpak.core.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/${env}.properties"
})
public interface ProjectConfig extends Config {

    String API_BASE_URL_KEY = "api.base.url";
    String UI_BASE_URL_KEY = "ui.base.url";
    String TIMEOUT_SECONDS_KEY = "timeout.seconds";
    String DEFAULT_TIMEOUT_SECONDS = "10";
    String DB_URL_KEY = "db.url";
    String DB_USERNAME_KEY = "db.username";
    String DB_PASSWORD_KEY = "db.password";

    @Key(API_BASE_URL_KEY)
    String apiBaseUrl();

    @Key(UI_BASE_URL_KEY)
    String uiBaseUrl();

    @Key(TIMEOUT_SECONDS_KEY)
    @DefaultValue(DEFAULT_TIMEOUT_SECONDS)
    int timeoutSeconds();

    @Key(DB_URL_KEY)
    String dbUrl();

    @Key(DB_USERNAME_KEY)
    String dbUsername();

    @Key(DB_PASSWORD_KEY)
    String dbPassword();
}
