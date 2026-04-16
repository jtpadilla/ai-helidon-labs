package io.github.jtpadilla.example.langchain4j.conditionalworkflow2.impl;

/**
 * Categorías de alto nivel que el {@link CategoryRouter} asigna a cada solicitud de usuario.
 *
 * <p>El dispatcher de nivel 1 en {@link ExpertRouterAgentImpl} lee el estado {@code "category"}
 * para decidir a qué experto delegar: médico, jurídico o técnico.
 * {@code UNKNOWN} se usa como valor por defecto cuando la categoría no ha sido escrita aún.
 */
public enum CategoryRouterRequest {
    LEGAL, MEDICAL, TECHNICAL, UNKNOWN
}
