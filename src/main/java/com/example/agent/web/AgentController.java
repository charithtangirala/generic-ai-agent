package com.example.agent.web;

import com.example.agent.agent.AgentDefinition;
import com.example.agent.agent.AgentDefinitionService;
import com.example.agent.agent.AgentService;
import com.example.agent.tool.ToolRegistry;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Runtime control surface for the generic agent:
 * <ul>
 *   <li>{@code PUT /api/agent/definition} - (re)configure job description, context, skills, and tools via Markdown</li>
 *   <li>{@code GET /api/agent/definition} - inspect the current configuration</li>
 *   <li>{@code GET /api/agent/tools} - list tool names available to grant</li>
 *   <li>{@code POST /api/agent/chat} - talk to the agent</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentDefinitionService agentDefinitionService;
    private final AgentService agentService;
    private final ToolRegistry toolRegistry;

    public AgentController(AgentDefinitionService agentDefinitionService,
                            AgentService agentService,
                            ToolRegistry toolRegistry) {
        this.agentDefinitionService = agentDefinitionService;
        this.agentService = agentService;
        this.toolRegistry = toolRegistry;
    }

    @GetMapping("/definition")
    public AgentDefinition getDefinition() {
        return agentDefinitionService.get();
    }

    @PutMapping("/definition")
    public AgentDefinition setDefinition(@RequestBody DefinitionRequest request) {
        AgentDefinition definition = new AgentDefinition(
                request.jobDescription(),
                request.context(),
                request.skills(),
                request.toolNames());
        return agentDefinitionService.set(definition);
    }

    @DeleteMapping("/definition")
    public AgentDefinition resetDefinition() {
        return agentDefinitionService.reset();
    }

    @GetMapping("/tools")
    public List<String> availableTools() {
        return toolRegistry.availableToolNames();
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String conversationId = request.conversationId() == null || request.conversationId().isBlank()
                ? "default"
                : request.conversationId();
        String reply = agentService.chat(conversationId, request.message());
        return new ChatResponse(reply);
    }

    @DeleteMapping("/chat/{conversationId}")
    public void resetConversation(@PathVariable String conversationId) {
        agentService.resetConversation(conversationId);
    }

    public record DefinitionRequest(
            String jobDescription,
            String context,
            String skills,
            List<String> toolNames) {
    }

    public record ChatRequest(
            @NotBlank String message,
            String conversationId) {
    }

    public record ChatResponse(String reply) {
    }
}
