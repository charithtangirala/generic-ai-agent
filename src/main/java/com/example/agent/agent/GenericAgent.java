package com.example.agent.agent;

/**
 * The agent's chat contract. Deliberately annotation-free (no
 * {@code @AiService}, {@code @SystemMessage}, {@code @Tool}) - its job
 * description, context, skills and tools are all assembled at runtime by
 * {@link AgentService} using LangChain4j's programmatic {@code AiServices}
 * builder instead of compile-time annotations.
 */
public interface GenericAgent {

    String chat(String userMessage);
}
