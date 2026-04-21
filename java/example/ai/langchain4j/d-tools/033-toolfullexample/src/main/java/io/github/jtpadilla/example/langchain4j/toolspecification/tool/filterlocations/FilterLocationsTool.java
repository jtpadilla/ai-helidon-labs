package io.github.jtpadilla.example.langchain4j.toolspecification.tool.filterlocations;

import dev.langchain4j.agent.tool.ToolSpecification;

import java.util.List;

public class FilterLocationsTool {

    static public final String NAME = "filter_locations";

    static public final ToolSpecification SPEC = ToolSpecification.builder()
            .name(NAME)
            .description("Proporciona la lista de ciudades en la que estamos interesados")
            .parameters(Parameter.SPEC)
            .build();

    private final List<String> validCities;

    public FilterLocationsTool(List<String> validCities) {
        this.validCities = validCities;
    }

    public String execute(String argumentsJson) {
        Parameter parameter = Parameter.create(argumentsJson);
        List<String> filtered = parameter.poblaciones().stream()
                .filter(this::isValidCity)
                .toList();
        return new Response(filtered).toJson();
    }

    private boolean isValidCity(String required) {
        return validCities.stream()
                .map(String::toUpperCase)
                .anyMatch(query -> query.equals(required.toUpperCase()));
    }

}
