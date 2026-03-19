package io.github.jtpadilla.example.helidon.builder.validation;

import io.helidon.builder.api.Option;
import io.helidon.builder.api.Prototype;

import java.util.Optional;

// Convención: nombre termina en "Blueprint", package-private
@Prototype.Blueprint
interface DatabaseConfigBlueprint {

    String jdbcUrl();

    Optional<String> username();

    /**
     * Define el entorno de ejecución del servidor.
     * Solo se permiten los valores especificados.
     */
    @Option.AllowedValues({
            @Option.AllowedValue(value = "DEV", description = "Entorno de desarrollo local"),
            @Option.AllowedValue(value = "TEST", description = "Entorno de pruebas/QA"),
            @Option.AllowedValue(value = "PROD", description = "Entorno de producción")
    })
    @Option.Default("DEV") // Valor por defecto si no se especifica
    String entorno();

}