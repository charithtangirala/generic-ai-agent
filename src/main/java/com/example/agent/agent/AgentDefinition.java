package com.example.agent.agent;

import java.util.List;

/**
 * The full runtime definition of what this agent currently is: its job
 * description, background context, skills, and the names of the tools it may
 * use. Everything is plain Markdown so it can be authored, reviewed, and
 * swapped out without touching code or recompiling.
 *
 * @param jobDescription what the agent is for - its role and objectives
 * @param context        background knowledge / domain facts the agent should assume
 * @param skills         Markdown describing how the agent should approach its work
 * @param toolNames      names of tools (registered in the ToolRegistry) the agent may call
 */
public record AgentDefinition(
        String jobDescription,
        String context,
        String skills,
        List<String> toolNames
) {

    public AgentDefinition {
        jobDescription = jobDescription == null ? "" : jobDescription;
        context = context == null ? "" : context;
        skills = skills == null ? "" : skills;
        toolNames = toolNames == null ? List.of() : List.copyOf(toolNames);
    }

    /** The agent with no purpose, context, skills, or tools assigned yet. */
    public static AgentDefinition empty() {
        return new AgentDefinition("", "", "", List.of());
    }

    /**
     * Renders the definition into a single Markdown system prompt. Sections
     * with no content are omitted.
     */
    public String toSystemPrompt() {
        StringBuilder sb = new StringBuilder();
        appendSection(sb, "Job Description", jobDescription);
        appendSection(sb, "Context", context);
        appendSection(sb, "Skills", skills);

        if (sb.isEmpty()) {
            return "You are a helpful AI agent. No specific job description, context, "
                    + "or skills have been configured yet.";
        }
        return sb.toString().stripTrailing();
    }

    private static void appendSection(StringBuilder sb, String heading, String body) {
        if (body != null && !body.isBlank()) {
            sb.append("## ").append(heading).append("\n\n").append(body.strip()).append("\n\n");
        }
    }
}
