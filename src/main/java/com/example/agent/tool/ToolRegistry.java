package com.example.agent.tool;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Registry of tool objects the agent may be granted access to at runtime.
 *
 * <p>This application ships with no tools registered by default - it is a
 * generic agent with no predefined access. To give it a capability, register
 * a plain object whose methods are annotated with LangChain4j's
 * {@code @Tool} (see {@code dev.langchain4j.agent.tool.Tool}) via
 * {@link #register(String, Object)}, then reference that name in an
 * {@code AgentDefinition}.
 */
@Component
public class ToolRegistry {

    private final Map<String, Object> toolsByName = new LinkedHashMap<>();

    public void register(String name, Object toolObject) {
        toolsByName.put(name, toolObject);
    }

    public void unregister(String name) {
        toolsByName.remove(name);
    }

    public Optional<Object> get(String name) {
        return Optional.ofNullable(toolsByName.get(name));
    }

    public List<Object> resolve(List<String> names) {
        return names.stream()
                .map(name -> toolsByName.get(name))
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    public List<String> availableToolNames() {
        return List.copyOf(toolsByName.keySet());
    }
}
