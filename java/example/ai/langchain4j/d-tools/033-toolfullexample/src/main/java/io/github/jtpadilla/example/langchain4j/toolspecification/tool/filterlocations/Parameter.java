package io.github.jtpadilla.example.langchain4j.toolspecification.tool.filterlocations;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;

import java.util.ArrayList;
import java.util.List;

class Parameter {

    private static final Gson GSON = new Gson();
    private static final String FIELD = "poblaciones";

    static public JsonObjectSchema SPEC = JsonObjectSchema.builder()
            .description("Lista de poblaciones que se quieren filtrar")
            .addProperty(FIELD, JsonArraySchema.builder()
                    .description("Array con los nombres de poblaciones propuestas")
                    .items(JsonStringSchema.builder().build())
                    .build())
            .required(FIELD)
            .build();

    static public Parameter create(String argumentsJson) {
        JsonObject json = GSON.fromJson(argumentsJson, JsonObject.class);
        List<String> poblaciones = new ArrayList<>();
        if (json.has(FIELD)) {
            JsonArray array = json.getAsJsonArray(FIELD);
            array.forEach(e -> poblaciones.add(e.getAsString()));
        }
        return new Parameter(poblaciones);
    }

    private final List<String> poblaciones;

    private Parameter(List<String> poblaciones) {
        this.poblaciones = poblaciones;
    }

    List<String> poblaciones() {
        return poblaciones;
    }

}
