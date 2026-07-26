package com.example.agent.agent;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Holds the agent's current {@link AgentDefinition}. Starts out empty (no
 * purpose, context, skills, or tools) and can be replaced at runtime, e.g.
 * via {@code AgentController}, without restarting the application.
 */
@Service
public class AgentDefinitionService {

    private final AtomicReference<AgentDefinition> current = new AtomicReference<>(AgentDefinition.empty());

    public AgentDefinition get() {
        return current.get();
    }

    public AgentDefinition set(AgentDefinition definition) {
        AgentDefinition next = definition == null ? AgentDefinition.empty() : definition;
        current.set(next);
        return next;
    }

    public AgentDefinition reset() {
        return set(AgentDefinition.empty());
    }
}
