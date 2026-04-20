package io.github.jtpadilla.example.langchain4j.toolspecification;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.jtpadilla.example.util.GoogleModels;
import io.helidon.config.Config;

public class ToolDemo {

    private static final String API_KEY = Config.global().get("gemini-api-key").asString().orElseThrow(
            () -> new IllegalStateException("La clave de configuración 'gemini-api-key' es obligatoria"));

    public static void main(String[] args) {

        ChatModel chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(API_KEY)
                .modelName(GoogleModels.geminiFlashLite())
                .build();

    }

    /*
    private static void ask(SupportAgent agent, String sessionId, String question) {
        System.out.printf("%n[%s] %s%n", sessionId, question);
        String solution = agent.handle(sessionId, question);
        System.out.printf("[→%s] %s%n", sessionId, solution);
    }

    private static void sep(String title) {
        System.out.printf("%n────────────── %s ─────────────%n", title);
    }

     */

}
