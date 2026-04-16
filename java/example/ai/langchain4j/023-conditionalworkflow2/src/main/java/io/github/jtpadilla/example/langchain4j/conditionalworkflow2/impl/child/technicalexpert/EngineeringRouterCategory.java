package io.github.jtpadilla.example.langchain4j.conditionalworkflow2.impl.child.technicalexpert;

/**
 * Disciplinas de ingeniería que el {@link EngineeringRouter} asigna a solicitudes técnicas.
 *
 * <p>El dispatcher de nivel 2 en {@link TechnicalWorkflowImpl} lee el estado
 * {@code "engineering_category"} para decidir a qué ingeniero especializado delegar.
 * {@code UNKNOWN} se usa como valor por defecto cuando la categoría no ha sido escrita aún.
 */
public enum EngineeringRouterCategory {
    SOFTWARE, HARDWARE, CIVIL, MECHANICAL, UNKNOWN
}
