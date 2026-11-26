package io.github.abshpak.api.clients.support;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.abshpak.core.config.ContainersConfig;
import org.aeonbits.owner.ConfigFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.http.HttpStatus.OK;

public final class WireMockContainers {

    private static final int WIREMOCK_PORT = 8080;
    private static final String READINESS_PATH = "/__admin/mappings";
    private static final String BASE_URL_TEMPLATE = "http://%s:%d";

    private WireMockContainers() {
    }

    public static GenericContainer<?> wireMock() {
        var config = ConfigFactory.create(ContainersConfig.class);
        return new GenericContainer<>(DockerImageName.parse(config.wiremockImage()))
                .withExposedPorts(WIREMOCK_PORT)
                .waitingFor(Wait.forHttp(READINESS_PATH).forStatusCode(OK.value()));
    }

    public static String baseUrl(GenericContainer<?> container) {
        return BASE_URL_TEMPLATE.formatted(container.getHost(), container.getMappedPort(WIREMOCK_PORT));
    }

    public static WireMock client(GenericContainer<?> container) {
        return new WireMock(container.getHost(), container.getMappedPort(WIREMOCK_PORT));
    }
}
