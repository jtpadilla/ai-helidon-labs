package io.github.jtpadilla.example.langchain4j.conditionalworkflow2.impl;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;

/**
 * Factoría para construir el agente {@link CategoryRouter}.
 *
 * <p>Registra la salida del agente bajo la clave {@code "category"} del scope,
 * de modo que el dispatcher condicional de nivel 1 pueda leerla.
 */
public class CategoryRouterImpl {

    static public CategoryRouter build(ChatModel chatModel) {
        return AgenticServices
                .agentBuilder(CategoryRouter.class)
                .chatModel(chatModel)
                .outputKey("category")
                .build();
    }

}
