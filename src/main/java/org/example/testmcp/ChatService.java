package org.example.testmcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    ToolCallbackProvider toolCallbackProvider;
    ToolCallback[] toolCallbacks;

    public ChatService(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;

        for (ToolCallback callback : toolCallbackProvider.getToolCallbacks()) {
            ToolDefinition definition = callback.getToolDefinition();
            System.out.println("Tool Name: " + definition.name());
            System.out.println("Description: " + definition.description());
            System.out.println("Input Schema: " + definition.inputSchema());
        }
        this.toolCallbacks = toolCallbackProvider.getToolCallbacks();
        this.chatClient = chatClientBuilder.defaultToolCallbacks(this.toolCallbacks).build();

    }

    public String chat(String userInput) {
        return chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    private static Logger getLogger() {
        return LoggerFactory.getLogger(ChatService.class);
    }
}
