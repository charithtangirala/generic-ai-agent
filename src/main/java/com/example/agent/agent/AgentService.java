package com.example.agent.agent;

import com.example.agent.tool.ToolRegistry;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds and drives the agent using LangChain4j's programmatic
 * {@code AiServices} fluent builder - deliberately not the
 * {@code @AiService}/{@code @SystemMessage}/{@code @Tool} annotation style.
 *
 * <p>The agent's job description, context, skills (all Markdown, held in
 * {@link AgentDefinitionService}) and its granted tools (looked up by name in
 * {@link ToolRegistry}) can change between calls, so a fresh {@code
 * GenericAgent} proxy is assembled per conversation turn from whatever the
 * current definition is.
 */
@Service
public class AgentService {

    private static final int CHAT_MEMORY_MESSAGES = 50;
    private static final String DEFAULT_CONVERSATION_ID = "default";

    private final ChatModel chatModel;
    private final AgentDefinitionService agentDefinitionService;
    private final ToolRegistry toolRegistry;
    private final Map<String, ChatMemory> chatMemories = new ConcurrentHashMap<>();

    public AgentService(ChatModel chatModel,
                         AgentDefinitionService agentDefinitionService,
                         ToolRegistry toolRegistry) {
        this.chatModel = chatModel;
        this.agentDefinitionService = agentDefinitionService;
        this.toolRegistry = toolRegistry;
    }

    public String chat(String userMessage) {
        return chat(DEFAULT_CONVERSATION_ID, userMessage);
    }

    public String chat(String conversationId, String userMessage) {
        GenericAgent agent = buildAgent(conversationId);
        return agent.chat(userMessage);
    }

    public void resetConversation(String conversationId) {
        chatMemories.remove(conversationId);
    }

    private GenericAgent buildAgent(String conversationId) {
        AgentDefinition definition = agentDefinitionService.get();
        List<Object> tools = toolRegistry.resolve(definition.toolNames());
        ChatMemory chatMemory = chatMemories.computeIfAbsent(
                conversationId,
                id -> MessageWindowChatMemory.withMaxMessages(CHAT_MEMORY_MESSAGES));

        AiServices<GenericAgent> builder = AiServices.builder(GenericAgent.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .systemMessageProvider(memoryId -> definition.toSystemPrompt());

        if (!tools.isEmpty()) {
            builder = builder.tools(tools);
        }

        return builder.build();
    }
}
