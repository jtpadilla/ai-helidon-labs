package io.github.jtpadilla.example.langchain4j.toolspecification.tool.filterlocations;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

class Response {

    private static final Gson GSON = new Gson();

    private final List<String> poblaciones;

    Response(List<String> poblaciones) {
        this.poblaciones = poblaciones;
    }

    String toJson() {
        return GSON.toJson(Map.of("poblaciones", poblaciones));
    }

}
