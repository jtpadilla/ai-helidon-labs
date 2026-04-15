package io.github.jtpadilla.example.langchain4j.conditionalworkflow2;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level1.CategoryRouter;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level1.LegalExpert;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level1.MedicalExpert;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level1.RequestCategory;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level2.CivilEngineer;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level2.EngineeringCategory;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level2.EngineeringRouter;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level2.HardwareEngineer;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level2.MechanicalEngineer;
import io.github.jtpadilla.example.langchain4j.conditionalworkflow2.level2.SoftwareEngineer;

/**
 * Construye el pipeline de agentes con dos niveles de decisión:
 *
 * <pre>
 * ExpertRouterAgent (secuencia)
 *   ├─ level1.CategoryRouter      → escribe "category" (LEGAL | MEDICAL | TECHNICAL | UNKNOWN)
 *   └─ Nivel 1 (condicional)
 *         ├─ MEDICAL   → level1.MedicalExpert
 *         ├─ LEGAL     → level1.LegalExpert
 *         └─ TECHNICAL → secuencia de nivel 2
 *               ├─ level2.EngineeringRouter  → escribe "engineering_category"
 *               └─ Nivel 2 (condicional)
 *                     ├─ SOFTWARE   → level2.SoftwareEngineer
 *                     ├─ HARDWARE   → level2.HardwareEngineer
 *                     ├─ CIVIL      → level2.CivilEngineer
 *                     └─ MECHANICAL → level2.MechanicalEngineer
 * </pre>
 */
public class WorkflowFactory {

    private WorkflowFactory() {}

    public static ExpertRouterAgent build(ChatModel chatModel) {

        // ── Nivel 1: router de categoría principal ──────────────────────────
        CategoryRouter categoryRouter = AgenticServices
                .agentBuilder(CategoryRouter.class)
                .chatModel(chatModel)
                .outputKey("category")
                .build();

        // ── Expertos de nivel 1 ─────────────────────────────────────────────
        MedicalExpert medicalExpert = AgenticServices
                .agentBuilder(MedicalExpert.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();

        LegalExpert legalExpert = AgenticServices
                .agentBuilder(LegalExpert.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();

        // ── Nivel 2: router de disciplina de ingeniería ─────────────────────
        EngineeringRouter engineeringRouter = AgenticServices
                .agentBuilder(EngineeringRouter.class)
                .chatModel(chatModel)
                .outputKey("engineering_category")
                .build();

        // ── Expertos de nivel 2 ─────────────────────────────────────────────
        SoftwareEngineer softwareEngineer = AgenticServices
                .agentBuilder(SoftwareEngineer.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();

        HardwareEngineer hardwareEngineer = AgenticServices
                .agentBuilder(HardwareEngineer.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();

        CivilEngineer civilEngineer = AgenticServices
                .agentBuilder(CivilEngineer.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();

        MechanicalEngineer mechanicalEngineer = AgenticServices
                .agentBuilder(MechanicalEngineer.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();

        // ── Condicional de nivel 2: dispatch por disciplina ─────────────────
        UntypedAgent engineeringDispatcher = AgenticServices.conditionalBuilder()
                .subAgents(
                    scope -> scope.readState("engineering_category", EngineeringCategory.UNKNOWN) == EngineeringCategory.SOFTWARE,
                    softwareEngineer)
                .subAgents(
                    scope -> scope.readState("engineering_category", EngineeringCategory.UNKNOWN) == EngineeringCategory.HARDWARE,
                    hardwareEngineer)
                .subAgents(
                    scope -> scope.readState("engineering_category", EngineeringCategory.UNKNOWN) == EngineeringCategory.CIVIL,
                    civilEngineer)
                .subAgents(
                    scope -> scope.readState("engineering_category", EngineeringCategory.UNKNOWN) == EngineeringCategory.MECHANICAL,
                    mechanicalEngineer)
                .build();

        // ── Sub-flujo técnico: router de ingeniería → dispatch ───────────────
        UntypedAgent technicalSubWorkflow = AgenticServices
                .sequenceBuilder(UntypedAgent.class)
                .subAgents(engineeringRouter, engineeringDispatcher)
                .outputKey("response")
                .build();

        // ── Condicional de nivel 1: dispatch por categoría principal ─────────
        UntypedAgent mainDispatcher = AgenticServices.conditionalBuilder()
                .subAgents(
                    scope -> scope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.MEDICAL,
                    medicalExpert)
                .subAgents(
                    scope -> scope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.LEGAL,
                    legalExpert)
                .subAgents(
                    scope -> scope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.TECHNICAL,
                    technicalSubWorkflow)
                .build();

        // ── Secuencia principal: router de categoría → dispatch ──────────────
        return AgenticServices
                .sequenceBuilder(ExpertRouterAgent.class)
                .subAgents(categoryRouter, mainDispatcher)
                .outputKey("response")
                .build();
    }

}
