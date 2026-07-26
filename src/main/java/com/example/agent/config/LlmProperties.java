package com.example.agent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds the {@code agent.llm.*} tree from application.yml, which is itself
 * fully sourced from environment variables. Selecting a different {@link
 * #provider} is the only thing needed to point the agent at a different
 * frontier LLM.
 */
@ConfigurationProperties(prefix = "agent.llm")
public class LlmProperties {

    /** anthropic | openai | google-ai-gemini */
    private String provider = "anthropic";

    private Anthropic anthropic = new Anthropic();
    private OpenAi openai = new OpenAi();
    private GoogleAiGemini googleAiGemini = new GoogleAiGemini();

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Anthropic getAnthropic() {
        return anthropic;
    }

    public void setAnthropic(Anthropic anthropic) {
        this.anthropic = anthropic;
    }

    public OpenAi getOpenai() {
        return openai;
    }

    public void setOpenai(OpenAi openai) {
        this.openai = openai;
    }

    public GoogleAiGemini getGoogleAiGemini() {
        return googleAiGemini;
    }

    public void setGoogleAiGemini(GoogleAiGemini googleAiGemini) {
        this.googleAiGemini = googleAiGemini;
    }

    public static class Anthropic {
        private String apiKey;
        private String modelName;
        private String baseUrl;
        private Integer maxTokens;
        private Double temperature;
        private Integer timeoutSeconds = 60;
        private boolean logRequests;
        private boolean logResponses;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Integer getMaxTokens() {
            return maxTokens;
        }

        public void setMaxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Integer getTimeoutSeconds() {
            return timeoutSeconds;
        }

        public void setTimeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }

        public boolean isLogRequests() {
            return logRequests;
        }

        public void setLogRequests(boolean logRequests) {
            this.logRequests = logRequests;
        }

        public boolean isLogResponses() {
            return logResponses;
        }

        public void setLogResponses(boolean logResponses) {
            this.logResponses = logResponses;
        }
    }

    public static class OpenAi {
        private String apiKey;
        private String modelName;
        private String baseUrl;
        private String organizationId;
        private Integer maxTokens;
        private Double temperature;
        private Integer timeoutSeconds = 60;
        private boolean logRequests;
        private boolean logResponses;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(String organizationId) {
            this.organizationId = organizationId;
        }

        public Integer getMaxTokens() {
            return maxTokens;
        }

        public void setMaxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Integer getTimeoutSeconds() {
            return timeoutSeconds;
        }

        public void setTimeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }

        public boolean isLogRequests() {
            return logRequests;
        }

        public void setLogRequests(boolean logRequests) {
            this.logRequests = logRequests;
        }

        public boolean isLogResponses() {
            return logResponses;
        }

        public void setLogResponses(boolean logResponses) {
            this.logResponses = logResponses;
        }
    }

    public static class GoogleAiGemini {
        private String apiKey;
        private String modelName;
        private Integer maxOutputTokens;
        private Double temperature;
        private Integer timeoutSeconds = 60;
        private boolean logRequestsAndResponses;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public Integer getMaxOutputTokens() {
            return maxOutputTokens;
        }

        public void setMaxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Integer getTimeoutSeconds() {
            return timeoutSeconds;
        }

        public void setTimeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }

        public boolean isLogRequestsAndResponses() {
            return logRequestsAndResponses;
        }

        public void setLogRequestsAndResponses(boolean logRequestsAndResponses) {
            this.logRequestsAndResponses = logRequestsAndResponses;
        }
    }
}
