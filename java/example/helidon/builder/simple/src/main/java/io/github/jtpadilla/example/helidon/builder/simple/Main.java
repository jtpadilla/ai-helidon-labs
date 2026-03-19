package io.github.jtpadilla.example.helidon.builder.simple;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Construcción fluente
        ServiceConfig config = ServiceConfig.builder()
                .name("auth-service")
                .host("api.example.com")
                .port(443)
                .addTags(List.of("prod", "auth"))
                .addTag("popo")
                .addTag("lolo")
                .build();
        System.out.println(config);

        // Copy-builder (inmutabilidad con cambios puntuales)
        ServiceConfig updated = ServiceConfig.builder(config)
                .port(8443)
                .build();
        System.out.println(updated);

    }

}
