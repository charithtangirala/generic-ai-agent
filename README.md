# Generic AI Agent

A Spring Boot application implementing a **generic, purpose-agnostic AI agent** on top of
[LangChain4j](https://docs.langchain4j.dev/). Out of the box it has no job description, context,
skills, or tools - all of that is defined **at runtime**, as Markdown, via a REST API.

## Stack

- Gradle (Groovy DSL)
- Java 26 (LTS)
- Spring Boot 3.4
- LangChain4j 1.0.x

## Design

- **No static annotations.** The agent interface (`GenericAgent`) carries no `@AiService`,
  `@SystemMessage`, or `@Tool` annotations. It is built at runtime with LangChain4j's
  programmatic, fluent `AiServices` builder (see `AgentService`), so the system prompt and the
  set of tools can change between requests without recompiling or restarting.
- **Runtime configuration, not code.** `AgentDefinition` (job description + context + skills +
  tool names, all Markdown) is held in `AgentDefinitionService` and can be replaced via
  `PUT /api/agent/definition`.
- **Provider-agnostic model.** `ChatModelConfig` builds a LangChain4j `ChatModel` bean for
  whichever provider is selected by `AGENT_LLM_PROVIDER` (`anthropic`, `openai`, or
  `google-ai-gemini`). Everything else in the app only depends on the `ChatModel` abstraction.
- **Tools are opt-in and pluggable.** `ToolRegistry` starts empty. Register a plain object whose
  methods use LangChain4j's `@Tool` annotation (`dev.langchain4j.agent.tool.Tool`) via
  `ToolRegistry.register(name, toolObject)`, then reference that name in the agent definition.

## Configuration

All configuration lives in `src/main/resources/application.yml` and is sourced from environment
variables - nothing is hard-coded.

| Env var | Default | Purpose |
|---|---|---|
| `AGENT_LLM_PROVIDER` | `anthropic` | `anthropic` \| `openai` \| `google-ai-gemini` |
| `ANTHROPIC_API_KEY` | - | required when provider is `anthropic` |
| `ANTHROPIC_MODEL_NAME` | `claude-opus-5` | |
| `OPENAI_API_KEY` | - | required when provider is `openai` |
| `OPENAI_MODEL_NAME` | `gpt-4o` | |
| `GOOGLE_AI_GEMINI_API_KEY` | - | required when provider is `google-ai-gemini` |
| `GOOGLE_AI_GEMINI_MODEL_NAME` | `gemini-2.5-pro` | |

See `application.yml` for the full list (base URLs, max tokens, temperature, timeouts, logging).

## Running

```bash
export AGENT_LLM_PROVIDER=anthropic
export ANTHROPIC_API_KEY=sk-ant-...
./gradlew bootRun
```

## API

Configure the agent (job description / context / skills are Markdown; `toolNames` reference tools
registered in `ToolRegistry`):

```bash
curl -X PUT localhost:8080/api/agent/definition \
  -H 'Content-Type: application/json' \
  -d '{
        "jobDescription": "# Job\nYou are a customer support triage assistant.",
        "context": "# Context\nThe product is a SaaS billing platform.",
        "skills": "# Skills\n- Ask clarifying questions before proposing a fix.\n- Cite the relevant doc section when possible.",
        "toolNames": []
      }'
```

Chat with it:

```bash
curl -X POST localhost:8080/api/agent/chat \
  -H 'Content-Type: application/json' \
  -d '{"message": "A customer says their invoice is doubled this month.", "conversationId": "session-1"}'
```

Reset a conversation's memory:

```bash
curl -X DELETE localhost:8080/api/agent/chat/session-1
```

Reset the agent to its blank, no-purpose state:

```bash
curl -X DELETE localhost:8080/api/agent/definition
```
