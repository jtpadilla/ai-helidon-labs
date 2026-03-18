package io.github.jtpadilla.example.helidon.builder.simple;

import io.helidon.builder.api.Option;
import io.helidon.builder.api.Prototype;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// Convención: nombre termina en "Blueprint", package-private
@Prototype.Blueprint
interface ServiceConfigBlueprint {

    // Propiedad requerida (sin @Option.Default → lanza excepción si no se setea)
    String name();

    // Propiedad opcional con default
    @Option.Default("localhost")
    String host();

    // Default numérico (usa @Option.DefaultInt, DefaultLong, DefaultDouble, DefaultBoolean)
    @Option.DefaultInt(8080)
    int port();

    // Optional de Java → la propiedad es completamente opcional
    Optional<String> description();

    // Lista → genera addXxx() y clearXxx() en el builder
    List<String> tags();

    // Mapa
    Map<String, String> properties();

}