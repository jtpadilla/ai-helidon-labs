package io.github.jtpadilla.example.langchain4j.goalorientedagenticpattern1;

import dev.langchain4j.agentic.planner.AgentInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoalOrientedSearchGraph {

    private final List<AgentInstance> agents;

    public GoalOrientedSearchGraph(List<AgentInstance> agents) {
        this.agents = agents;
    }

    public List<AgentInstance> search(Set<String> initialKeys, String goal) {
        Set<String> available = new HashSet<>(initialKeys);
        List<AgentInstance> path = new ArrayList<>();

        boolean progress = true;
        while (progress && !available.contains(goal)) {
            progress = false;
            for (var agent : agents) {
                if (available.contains(agent.outputKey())) continue;
                boolean satisfied = agent.arguments().stream()
                        .allMatch(arg -> available.contains(arg.name()) || arg.defaultValue() != null);
                if (satisfied) {
                    path.add(agent);
                    available.add(agent.outputKey());
                    progress = true;
                }
            }
        }

        return available.contains(goal) ? path : Collections.emptyList();
    }
}
