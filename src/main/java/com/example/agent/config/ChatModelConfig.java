package com.example.agent.config;

import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Builds the {@link ChatModel} bean for whichever frontier LLM provider is
 * selected via {@code agent.llm.provider} (env var {@code AGENT_LLM_PROVIDER}).
 *
 * <p>This is the only place provider selection happens. Everything downstream
 * (the agent factory, tools, etc.) only ever depends on the {@link ChatModel}
 * abstraction, so swapping Claude for Gemini or OpenAI is a configuration
 * change, not a code change.
 */
@Configuration
@EnableConfigurationProperties(LlmProperties.class)
public class ChatModelConfig {

    @Bean
    public ChatModel chatModel(LlmProperties properties) {
        String provider = properties.getProvider() == null
                ? "anthropic"
                : properties.getProvider().trim().toLowerCase();

        return switch (provider) {
            case "anthropic" -> buildAnthropic(properties.getAnthropic());
            case "openai" -> buildOpenAi(properties.getOpenai());
            case "google-ai-gemini", "google", "gemini" -> buildGoogleAiGemini(properties.getGoogleAiGemini());
            default -> throw new IllegalStateException(
                    "Unsupported agent.llm.provider '" + provider
                            + "'. Supported values: anthropic, openai, google-ai-gemini");
        };
    }

    private ChatModel buildAnthropic(LlmProperties.Anthropic cfg) {
        requireApiKey(cfg.getApiKey(), "ANTHROPIC_API_KEY");

        AnthropicChatModel.AnthropicChatModelBuilder builder = AnthropicChatModel.builder()
                .apiKey(cfg.getApiKey())
                .modelName(cfg.getModelName())
                .timeout(Duration.ofSeconds(cfg.getTimeoutSeconds()))
                .logRequests(cfg.isLogRequests())
                .logResponses(cfg.isLogResponses());

        if (cfg.getMaxTokens() != null) {
            builder.maxTokens(cfg.getMaxTokens());
        }
        if (cfg.getTemperature() != null) {
            builder.temperature(cfg.getTemperature());
        }
        if (hasText(cfg.getBaseUrl())) {
            builder.baseUrl(cfg.getBaseUrl());
        }
        return builder.build();
    }

    private ChatModel buildOpenAi(LlmProperties.OpenAi cfg) {
        requireApiKey(cfg.getApiKey(), "OPENAI_API_KEY");

        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
                .apiKey(cfg.getApiKey())
                .modelName(cfg.getModelName())
                .timeout(Duration.ofSeconds(cfg.getTimeoutSeconds()))
                .logRequests(cfg.isLogRequests())
                .logResponses(cfg.isLogResponses());

        if (cfg.getMaxTokens() != null) {
            builder.maxTokens(cfg.getMaxTokens());
        }
        if (cfg.getTemperature() != null) {
            builder.temperature(cfg.getTemperature());
        }
        if (hasText(cfg.getBaseUrl())) {
            builder.baseUrl(cfg.getBaseUrl());
        }
        if (hasText(cfg.getOrganizationId())) {
            builder.organizationId(cfg.getOrganizationId());
        }
        return builder.build();
    }

    private ChatModel buildGoogleAiGemini(LlmProperties.GoogleAiGemini cfg) {
        requireApiKey(cfg.getApiKey(), "GOOGLE_AI_GEMINI_API_KEY");

        GoogleAiGeminiChatModel.GoogleAiGeminiChatModelBuilder builder = GoogleAiGeminiChatModel.builder()
                .apiKey(cfg.getApiKey())
                .modelName(cfg.getModelName())
                .timeout(Duration.ofSeconds(cfg.getTimeoutSeconds()))
                .logRequestsAndResponses(cfg.isLogRequestsAndResponses());

        if (cfg.getMaxOutputTokens() != null) {
            builder.maxOutputTokens(cfg.getMaxOutputTokens());
        }
        if (cfg.getTemperature() != null) {
            builder.temperature(cfg.getTemperature());
        }
        return builder.build();
    }

    private static void requireApiKey(String apiKey, String envVarName) {
        if (!hasText(apiKey)) {
            throw new IllegalStateException(
                    "Missing API key for the selected LLM provider. Set the " + envVarName + " environment variable.");
        }
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
