package io.github.jtpadilla.example.langchain4j.toolspecification.tool.currenttime;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.Map;

class Response {

    private static final Gson GSON = new Gson();

    private final String currentTime;

    Response(LocalDateTime currentTime) {
        this.currentTime = currentTime.toString();
    }

    String toJson() {
        return GSON.toJson(Map.of("current_time", currentTime));
    }

}
