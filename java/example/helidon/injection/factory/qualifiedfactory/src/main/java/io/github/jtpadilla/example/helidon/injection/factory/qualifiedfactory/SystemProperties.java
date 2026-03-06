package io.github.jtpadilla.example.helidon.injection.factory.qualifiedfactory;

import io.helidon.service.registry.Service;

@Service.Singleton
class SystemProperties {

    private final String httpHost;
    private final String httpPort;

    SystemProperties(@SystemProperty("http.host") String httpHost,
                     @SystemProperty("http.port") String httpPort) {
        this.httpHost = httpHost;
        this.httpPort = httpPort;
    }

}