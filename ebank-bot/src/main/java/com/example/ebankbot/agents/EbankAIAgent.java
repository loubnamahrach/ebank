
package com.example.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class EbankAIAgent {

    private final ChatClient chatClient;

    public EbankAIAgent(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            ToolCallbackProvider tools) {

        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        Vous êtes un assistant bancaire pour eBank.
                        Vous répondez aux questions en utilisant les outils MCP disponibles.
                        Important pour les appels d'outils :
                        - Les identifiants numériques (comme 'id') doivent toujours être passés comme un nombre entier (ex: 1, 2, 3), jamais sous forme de chaîne de caractères entre guillemets.
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(tools)
                .build();
    }

    public String chat(String query, String conversationId) {
        return chatClient.prompt()
                .user(query)
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId
                ))
                .call()
                .content();
    }
}
