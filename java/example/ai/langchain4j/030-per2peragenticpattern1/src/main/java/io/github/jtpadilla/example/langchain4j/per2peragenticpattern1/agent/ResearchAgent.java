package io.github.jtpadilla.example.langchain4j.per2peragenticpattern1.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface ResearchAgent {

    @Agent("Conduct iterative research on a given topic: gather literature, form a hypothesis, critique it, validate it and score it until the score is satisfactory.")
    String research(@V("topic") String topic);

}
